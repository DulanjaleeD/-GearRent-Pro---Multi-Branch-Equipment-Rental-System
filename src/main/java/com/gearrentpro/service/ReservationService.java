package com.gearrentpro.service;

import com.gearrentpro.dao.CustomerDAO;
import com.gearrentpro.dao.EquipmentDAO;
import com.gearrentpro.dao.ReservationDAO;
import com.gearrentpro.entity.Customer;
import com.gearrentpro.entity.Equipment;
import com.gearrentpro.entity.Reservation;
import com.gearrentpro.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final EquipmentDAO equipmentDAO;
    private final CustomerDAO customerDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.equipmentDAO = new EquipmentDAO();
        this.customerDAO = new CustomerDAO();
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.findAll();
    }

    public List<Reservation> getReservationsByBranch(int branchId) throws SQLException {
        return reservationDAO.findByBranch(branchId);
    }

    public void createReservation(Reservation reservation) throws SQLException {
        Equipment equipment = equipmentDAO.findById(reservation.getEquipmentId());
        Customer customer = customerDAO.findById(reservation.getCustomerId());

        if (equipment == null || customer == null) {
            throw new IllegalArgumentException("Invalid equipment or customer");
        }

        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()) + 1;
        if (days > 30) {
            throw new IllegalArgumentException("Reservation period cannot exceed 30 days");
        }

        BigDecimal depositNeeded = equipment.getSecurityDeposit();
        BigDecimal newTotal = customer.getTotalDepositHeld().add(depositNeeded);

        if (newTotal.compareTo(customer.getMaxDepositLimit()) > 0) {
            throw new IllegalArgumentException("Customer deposit limit would be exceeded");
        }

        if (reservationDAO.hasOverlap(reservation.getEquipmentId(), reservation.getStartDate(),
                                     reservation.getEndDate(), null)) {
            throw new IllegalArgumentException("Equipment is already reserved or rented for this period");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            reservation.setReservationCode(generateReservationCode());
            reservation.setStatus(Reservation.ReservationStatus.Active);

            reservationDAO.create(reservation);

            equipmentDAO.updateStatus(reservation.getEquipmentId(), Equipment.EquipmentStatus.Reserved);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error creating reservation", e);
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

    public void cancelReservation(int reservationId) throws SQLException {
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            reservationDAO.updateStatus(reservationId, Reservation.ReservationStatus.Cancelled);

            equipmentDAO.updateStatus(reservation.getEquipmentId(), Equipment.EquipmentStatus.Available);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error cancelling reservation", e);
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

    private String generateReservationCode() {
        return "RES-" + System.currentTimeMillis();
    }
}
