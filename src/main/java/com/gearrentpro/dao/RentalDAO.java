package com.gearrentpro.dao;

import com.gearrentpro.entity.Rental;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    public List<Rental> findAll() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = buildSelectQuery() + " ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving rentals", e);
        }
        return rentals;
    }

    public List<Rental> findByBranch(int branchId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = buildSelectQuery() + " WHERE r.branch_id = ? ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving rentals by branch", e);
        }
        return rentals;
    }

    public List<Rental> findOverdue() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = buildSelectQuery() + " WHERE r.rental_status = 'Overdue' ORDER BY r.end_date";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving overdue rentals", e);
        }
        return rentals;
    }

    public List<Rental> findOverdueByBranch(int branchId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = buildSelectQuery() + " WHERE r.branch_id = ? AND r.rental_status = 'Overdue' ORDER BY r.end_date";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving overdue rentals by branch", e);
        }
        return rentals;
    }

    public Rental findById(int rentalId) throws SQLException {
        String sql = buildSelectQuery() + " WHERE r.rental_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractRentalFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding rental", e);
        }
    }

    public boolean hasOverlap(int equipmentId, LocalDate startDate, LocalDate endDate, Integer excludeRentalId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rentals " +
                    "WHERE equipment_id = ? AND rental_status IN ('Active', 'Overdue') " +
                    "AND ((start_date BETWEEN ? AND ?) OR (end_date BETWEEN ? AND ?) " +
                    "OR (start_date <= ? AND end_date >= ?))";

        if (excludeRentalId != null) {
            sql += " AND rental_id != ?";
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, equipmentId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            stmt.setDate(4, Date.valueOf(startDate));
            stmt.setDate(5, Date.valueOf(endDate));
            stmt.setDate(6, Date.valueOf(startDate));
            stmt.setDate(7, Date.valueOf(endDate));

            if (excludeRentalId != null) {
                stmt.setInt(8, excludeRentalId);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (Exception e) {
            throw new SQLException("Error checking rental overlap", e);
        }
    }

    public void create(Rental rental, Connection conn) throws SQLException {
        String sql = "INSERT INTO rentals (rental_code, equipment_id, customer_id, branch_id, reservation_id, " +
                    "start_date, end_date, rental_days, base_rental_amount, long_rental_discount, " +
                    "membership_discount, final_rental_amount, security_deposit, total_charges, " +
                    "payment_status, rental_status, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, rental.getRentalCode());
            stmt.setInt(2, rental.getEquipmentId());
            stmt.setInt(3, rental.getCustomerId());
            stmt.setInt(4, rental.getBranchId());
            if (rental.getReservationId() != null) {
                stmt.setInt(5, rental.getReservationId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setDate(6, Date.valueOf(rental.getStartDate()));
            stmt.setDate(7, Date.valueOf(rental.getEndDate()));
            stmt.setInt(8, rental.getRentalDays());
            stmt.setBigDecimal(9, rental.getBaseRentalAmount());
            stmt.setBigDecimal(10, rental.getLongRentalDiscount());
            stmt.setBigDecimal(11, rental.getMembershipDiscount());
            stmt.setBigDecimal(12, rental.getFinalRentalAmount());
            stmt.setBigDecimal(13, rental.getSecurityDeposit());
            stmt.setBigDecimal(14, rental.getTotalCharges());
            stmt.setString(15, rental.getPaymentStatus().name());
            stmt.setString(16, rental.getRentalStatus().name());
            stmt.setInt(17, rental.getCreatedBy());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                rental.setRentalId(rs.getInt(1));
            }
        }
    }

    public void processReturn(int rentalId, LocalDate returnDate, java.math.BigDecimal lateFee,
                            java.math.BigDecimal damageCharge, String damageDescription,
                            java.math.BigDecimal depositRefund, java.math.BigDecimal additionalPayment,
                            int returnedBy, Connection conn) throws SQLException {
        String sql = "UPDATE rentals SET actual_return_date = ?, late_fee = ?, damage_charge = ?, " +
                    "damage_description = ?, total_charges = total_charges + ? + ?, " +
                    "deposit_refund = ?, additional_payment = ?, rental_status = 'Returned', " +
                    "returned_by = ?, returned_at = NOW() " +
                    "WHERE rental_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setBigDecimal(2, lateFee);
            stmt.setBigDecimal(3, damageCharge);
            stmt.setString(4, damageDescription);
            stmt.setBigDecimal(5, lateFee);
            stmt.setBigDecimal(6, damageCharge);
            stmt.setBigDecimal(7, depositRefund);
            stmt.setBigDecimal(8, additionalPayment);
            stmt.setInt(9, returnedBy);
            stmt.setInt(10, rentalId);

            stmt.executeUpdate();
        }
    }

    public void updateStatusToOverdue(int rentalId) throws SQLException {
        String sql = "UPDATE rentals SET rental_status = 'Overdue' WHERE rental_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating rental status to overdue", e);
        }
    }

    public List<Rental> findActiveRentals() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = buildSelectQuery() + " WHERE r.rental_status = 'Active' AND r.end_date < CURDATE()";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error finding active rentals past due date", e);
        }
        return rentals;
    }

    private String buildSelectQuery() {
        return "SELECT r.*, " +
               "CONCAT(e.brand, ' ', e.model) as equipment_name, " +
               "c.name as customer_name, " +
               "c.contact_number as customer_contact, " +
               "b.name as branch_name, " +
               "u1.full_name as created_by_name, " +
               "u2.full_name as returned_by_name " +
               "FROM rentals r " +
               "JOIN equipment e ON r.equipment_id = e.equipment_id " +
               "JOIN customers c ON r.customer_id = c.customer_id " +
               "JOIN branches b ON r.branch_id = b.branch_id " +
               "JOIN users u1 ON r.created_by = u1.user_id " +
               "LEFT JOIN users u2 ON r.returned_by = u2.user_id";
    }

    private Rental extractRentalFromResultSet(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setRentalId(rs.getInt("rental_id"));
        rental.setRentalCode(rs.getString("rental_code"));
        rental.setEquipmentId(rs.getInt("equipment_id"));
        rental.setEquipmentName(rs.getString("equipment_name"));
        rental.setCustomerId(rs.getInt("customer_id"));
        rental.setCustomerName(rs.getString("customer_name"));
        rental.setCustomerContact(rs.getString("customer_contact"));
        rental.setBranchId(rs.getInt("branch_id"));
        rental.setBranchName(rs.getString("branch_name"));

        int reservationId = rs.getInt("reservation_id");
        rental.setReservationId(rs.wasNull() ? null : reservationId);

        rental.setStartDate(rs.getDate("start_date").toLocalDate());
        rental.setEndDate(rs.getDate("end_date").toLocalDate());

        Date actualReturn = rs.getDate("actual_return_date");
        rental.setActualReturnDate(actualReturn != null ? actualReturn.toLocalDate() : null);

        rental.setRentalDays(rs.getInt("rental_days"));
        rental.setBaseRentalAmount(rs.getBigDecimal("base_rental_amount"));
        rental.setLongRentalDiscount(rs.getBigDecimal("long_rental_discount"));
        rental.setMembershipDiscount(rs.getBigDecimal("membership_discount"));
        rental.setFinalRentalAmount(rs.getBigDecimal("final_rental_amount"));
        rental.setSecurityDeposit(rs.getBigDecimal("security_deposit"));
        rental.setLateFee(rs.getBigDecimal("late_fee"));
        rental.setDamageCharge(rs.getBigDecimal("damage_charge"));
        rental.setDamageDescription(rs.getString("damage_description"));
        rental.setTotalCharges(rs.getBigDecimal("total_charges"));
        rental.setDepositRefund(rs.getBigDecimal("deposit_refund"));
        rental.setAdditionalPayment(rs.getBigDecimal("additional_payment"));
        rental.setPaymentStatus(Rental.PaymentStatus.valueOf(rs.getString("payment_status")));
        rental.setRentalStatus(Rental.RentalStatus.valueOf(rs.getString("rental_status")));
        rental.setCreatedBy(rs.getInt("created_by"));
        rental.setCreatedByName(rs.getString("created_by_name"));
        rental.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        int returnedBy = rs.getInt("returned_by");
        rental.setReturnedBy(rs.wasNull() ? null : returnedBy);
        rental.setReturnedByName(rs.getString("returned_by_name"));

        Timestamp returnedAt = rs.getTimestamp("returned_at");
        rental.setReturnedAt(returnedAt != null ? returnedAt.toLocalDateTime() : null);

        return rental;
    }
}
