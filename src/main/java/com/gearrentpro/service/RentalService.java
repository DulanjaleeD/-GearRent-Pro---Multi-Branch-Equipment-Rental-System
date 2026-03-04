package com.gearrentpro.service;

import com.gearrentpro.dao.*;
import com.gearrentpro.entity.*;
import com.gearrentpro.util.DatabaseConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {
    private final RentalDAO rentalDAO;
    private final EquipmentDAO equipmentDAO;
    private final CustomerDAO customerDAO;
    private final MembershipLevelDAO membershipDAO;
    private final PricingConfigDAO pricingConfigDAO;
    private final ReservationDAO reservationDAO;

    public RentalService() {
        this.rentalDAO = new RentalDAO();
        this.equipmentDAO = new EquipmentDAO();
        this.customerDAO = new CustomerDAO();
        this.membershipDAO = new MembershipLevelDAO();
        this.pricingConfigDAO = new PricingConfigDAO();
        this.reservationDAO = new ReservationDAO();
    }

    public List<Rental> getAllRentals() throws SQLException {
        return rentalDAO.findAll();
    }

    public List<Rental> getRentalsByBranch(int branchId) throws SQLException {
        return rentalDAO.findByBranch(branchId);
    }

    public List<Rental> getOverdueRentals() throws SQLException {
        return rentalDAO.findOverdue();
    }

    public List<Rental> getOverdueRentalsByBranch(int branchId) throws SQLException {
        return rentalDAO.findOverdueByBranch(branchId);
    }

    public Rental calculateRentalCost(int equipmentId, int customerId, LocalDate startDate, LocalDate endDate)
            throws SQLException {
        Equipment equipment = equipmentDAO.findById(equipmentId);
        Customer customer = customerDAO.findById(customerId);
        MembershipLevel membership = membershipDAO.findById(customer.getMembershipLevelId());
        PricingConfig config = pricingConfigDAO.getConfig();

        if (equipment == null || customer == null || membership == null || config == null) {
            throw new IllegalArgumentException("Invalid equipment or customer");
        }

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (rentalDays > config.getMaxRentalDays()) {
            throw new IllegalArgumentException("Rental period exceeds maximum of " + config.getMaxRentalDays() + " days");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            BigDecimal dailyPrice = equipment.getBaseDailyPrice();

            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                dailyPrice = dailyPrice.multiply(BigDecimal.valueOf(1.2));
            }

            totalAmount = totalAmount.add(dailyPrice);
            currentDate = currentDate.plusDays(1);
        }

        BigDecimal baseAmount = totalAmount;
        BigDecimal longRentalDiscount = BigDecimal.ZERO;
        BigDecimal membershipDiscount = BigDecimal.ZERO;

        if (rentalDays >= config.getLongRentalDaysThreshold()) {
            longRentalDiscount = baseAmount.multiply(config.getLongRentalDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            totalAmount = totalAmount.subtract(longRentalDiscount);
        }

        if (membership.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
            membershipDiscount = totalAmount.multiply(membership.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            totalAmount = totalAmount.subtract(membershipDiscount);
        }

        Rental rental = new Rental();
        rental.setEquipmentId(equipmentId);
        rental.setCustomerId(customerId);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setRentalDays((int) rentalDays);
        rental.setBaseRentalAmount(baseAmount);
        rental.setLongRentalDiscount(longRentalDiscount);
        rental.setMembershipDiscount(membershipDiscount);
        rental.setFinalRentalAmount(totalAmount);
        rental.setSecurityDeposit(equipment.getSecurityDeposit());
        rental.setTotalCharges(totalAmount);

        return rental;
    }

    public void createRental(Rental rental) throws SQLException {
        Equipment equipment = equipmentDAO.findById(rental.getEquipmentId());
        Customer customer = customerDAO.findById(rental.getCustomerId());

        if (equipment == null || customer == null) {
            throw new IllegalArgumentException("Invalid equipment or customer");
        }

        BigDecimal newDepositTotal = customer.getTotalDepositHeld().add(rental.getSecurityDeposit());
        if (newDepositTotal.compareTo(customer.getMaxDepositLimit()) > 0) {
            throw new IllegalArgumentException("Customer deposit limit exceeded. " +
                    "Current: " + customer.getTotalDepositHeld() + ", " +
                    "New deposit: " + rental.getSecurityDeposit() + ", " +
                    "Limit: " + customer.getMaxDepositLimit());
        }

        if (rentalDAO.hasOverlap(rental.getEquipmentId(), rental.getStartDate(), rental.getEndDate(), null)) {
            throw new IllegalArgumentException("Equipment is already rented or reserved for this period");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            rental.setRentalCode(generateRentalCode());
            rental.setPaymentStatus(Rental.PaymentStatus.Paid);
            rental.setRentalStatus(Rental.RentalStatus.Active);

            rentalDAO.create(rental, conn);

            equipmentDAO.updateStatus(rental.getEquipmentId(), Equipment.EquipmentStatus.Rented);

            customerDAO.updateDepositHeld(rental.getCustomerId(), newDepositTotal);

            if (rental.getReservationId() != null) {
                reservationDAO.updateStatus(rental.getReservationId(), Reservation.ReservationStatus.Converted);
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error creating rental", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void processReturn(int rentalId, LocalDate returnDate, boolean hasDamage,
                            BigDecimal damageCharge, String damageDescription, int returnedBy) throws SQLException {
        Rental rental = rentalDAO.findById(rentalId);
        if (rental == null) {
            throw new IllegalArgumentException("Rental not found");
        }

        if (rental.getRentalStatus() != Rental.RentalStatus.Active &&
            rental.getRentalStatus() != Rental.RentalStatus.Overdue) {
            throw new IllegalArgumentException("Rental is not active");
        }

        Equipment equipment = equipmentDAO.findById(rental.getEquipmentId());
        Customer customer = customerDAO.findById(rental.getCustomerId());
        EquipmentCategory category = new EquipmentCategoryDAO().findById(equipment.getCategoryId());

        long daysLate = ChronoUnit.DAYS.between(rental.getEndDate(), returnDate);
        BigDecimal lateFee = BigDecimal.ZERO;

        if (daysLate > 0) {
            lateFee = category.getDefaultLateFeePerDay().multiply(BigDecimal.valueOf(daysLate));
        }

        BigDecimal totalNewCharges = lateFee.add(damageCharge != null ? damageCharge : BigDecimal.ZERO);
        BigDecimal depositRefund;
        BigDecimal additionalPayment;

        if (totalNewCharges.compareTo(rental.getSecurityDeposit()) <= 0) {
            depositRefund = rental.getSecurityDeposit().subtract(totalNewCharges);
            additionalPayment = BigDecimal.ZERO;
        } else {
            depositRefund = BigDecimal.ZERO;
            additionalPayment = totalNewCharges.subtract(rental.getSecurityDeposit());
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            rentalDAO.processReturn(rentalId, returnDate, lateFee,
                    damageCharge != null ? damageCharge : BigDecimal.ZERO,
                    damageDescription, depositRefund, additionalPayment, returnedBy, conn);

            Equipment.EquipmentStatus newStatus = hasDamage ?
                    Equipment.EquipmentStatus.Under_Maintenance : Equipment.EquipmentStatus.Available;
            equipmentDAO.updateStatus(rental.getEquipmentId(), newStatus);

            BigDecimal newDepositTotal = customer.getTotalDepositHeld().subtract(rental.getSecurityDeposit());
            customerDAO.updateDepositHeld(rental.getCustomerId(), newDepositTotal);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error processing return", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateOverdueRentals() throws SQLException {
        List<Rental> activeRentals = rentalDAO.findActiveRentals();
        for (Rental rental : activeRentals) {
            rentalDAO.updateStatusToOverdue(rental.getRentalId());
        }
    }

    private String generateRentalCode() {
        return "RNT-" + System.currentTimeMillis();
    }
}
