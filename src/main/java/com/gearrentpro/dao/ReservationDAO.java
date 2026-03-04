package com.gearrentpro.dao;

import com.gearrentpro.entity.Reservation;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, " +
                    "CONCAT(e.brand, ' ', e.model) as equipment_name, " +
                    "c.name as customer_name, " +
                    "b.name as branch_name, " +
                    "u.full_name as created_by_name " +
                    "FROM reservations r " +
                    "JOIN equipment e ON r.equipment_id = e.equipment_id " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "JOIN branches b ON r.branch_id = b.branch_id " +
                    "JOIN users u ON r.created_by = u.user_id " +
                    "ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving reservations", e);
        }
        return reservations;
    }

    public List<Reservation> findByBranch(int branchId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, " +
                    "CONCAT(e.brand, ' ', e.model) as equipment_name, " +
                    "c.name as customer_name, " +
                    "b.name as branch_name, " +
                    "u.full_name as created_by_name " +
                    "FROM reservations r " +
                    "JOIN equipment e ON r.equipment_id = e.equipment_id " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "JOIN branches b ON r.branch_id = b.branch_id " +
                    "JOIN users u ON r.created_by = u.user_id " +
                    "WHERE r.branch_id = ? " +
                    "ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving reservations by branch", e);
        }
        return reservations;
    }

    public Reservation findById(int reservationId) throws SQLException {
        String sql = "SELECT r.*, " +
                    "CONCAT(e.brand, ' ', e.model) as equipment_name, " +
                    "c.name as customer_name, " +
                    "b.name as branch_name, " +
                    "u.full_name as created_by_name " +
                    "FROM reservations r " +
                    "JOIN equipment e ON r.equipment_id = e.equipment_id " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "JOIN branches b ON r.branch_id = b.branch_id " +
                    "JOIN users u ON r.created_by = u.user_id " +
                    "WHERE r.reservation_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractReservationFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding reservation", e);
        }
    }

    public boolean hasOverlap(int equipmentId, LocalDate startDate, LocalDate endDate, Integer excludeReservationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                    "WHERE equipment_id = ? AND status = 'Active' " +
                    "AND ((start_date BETWEEN ? AND ?) OR (end_date BETWEEN ? AND ?) " +
                    "OR (start_date <= ? AND end_date >= ?))";

        if (excludeReservationId != null) {
            sql += " AND reservation_id != ?";
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

            if (excludeReservationId != null) {
                stmt.setInt(8, excludeReservationId);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (Exception e) {
            throw new SQLException("Error checking reservation overlap", e);
        }
    }

    public void create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (reservation_code, equipment_id, customer_id, branch_id, " +
                    "start_date, end_date, status, notes, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, reservation.getReservationCode());
            stmt.setInt(2, reservation.getEquipmentId());
            stmt.setInt(3, reservation.getCustomerId());
            stmt.setInt(4, reservation.getBranchId());
            stmt.setDate(5, Date.valueOf(reservation.getStartDate()));
            stmt.setDate(6, Date.valueOf(reservation.getEndDate()));
            stmt.setString(7, reservation.getStatus().name());
            stmt.setString(8, reservation.getNotes());
            stmt.setInt(9, reservation.getCreatedBy());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reservation.setReservationId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating reservation", e);
        }
    }

    public void updateStatus(int reservationId, Reservation.ReservationStatus status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, reservationId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating reservation status", e);
        }
    }

    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setReservationCode(rs.getString("reservation_code"));
        reservation.setEquipmentId(rs.getInt("equipment_id"));
        reservation.setEquipmentName(rs.getString("equipment_name"));
        reservation.setCustomerId(rs.getInt("customer_id"));
        reservation.setCustomerName(rs.getString("customer_name"));
        reservation.setBranchId(rs.getInt("branch_id"));
        reservation.setBranchName(rs.getString("branch_name"));
        reservation.setStartDate(rs.getDate("start_date").toLocalDate());
        reservation.setEndDate(rs.getDate("end_date").toLocalDate());
        reservation.setStatus(Reservation.ReservationStatus.valueOf(rs.getString("status")));
        reservation.setNotes(rs.getString("notes"));
        reservation.setCreatedBy(rs.getInt("created_by"));
        reservation.setCreatedByName(rs.getString("created_by_name"));
        reservation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return reservation;
    }
}
