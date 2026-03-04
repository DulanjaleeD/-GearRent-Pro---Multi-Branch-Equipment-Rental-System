package com.gearrentpro.dao;

import com.gearrentpro.entity.Equipment;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    public List<Equipment> findAll() throws SQLException {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT e.*, c.name as category_name, b.name as branch_name " +
                    "FROM equipment e " +
                    "JOIN equipment_categories c ON e.category_id = c.category_id " +
                    "JOIN branches b ON e.branch_id = b.branch_id " +
                    "WHERE e.is_active = TRUE ORDER BY e.equipment_code";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                equipmentList.add(extractEquipmentFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving equipment", e);
        }
        return equipmentList;
    }

    public List<Equipment> findByBranch(int branchId) throws SQLException {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT e.*, c.name as category_name, b.name as branch_name " +
                    "FROM equipment e " +
                    "JOIN equipment_categories c ON e.category_id = c.category_id " +
                    "JOIN branches b ON e.branch_id = b.branch_id " +
                    "WHERE e.branch_id = ? AND e.is_active = TRUE ORDER BY e.equipment_code";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                equipmentList.add(extractEquipmentFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving equipment by branch", e);
        }
        return equipmentList;
    }

    public Equipment findById(int equipmentId) throws SQLException {
        String sql = "SELECT e.*, c.name as category_name, b.name as branch_name " +
                    "FROM equipment e " +
                    "JOIN equipment_categories c ON e.category_id = c.category_id " +
                    "JOIN branches b ON e.branch_id = b.branch_id " +
                    "WHERE e.equipment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, equipmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractEquipmentFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding equipment", e);
        }
    }

    public List<Equipment> findAvailableByBranch(int branchId) throws SQLException {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT e.*, c.name as category_name, b.name as branch_name " +
                    "FROM equipment e " +
                    "JOIN equipment_categories c ON e.category_id = c.category_id " +
                    "JOIN branches b ON e.branch_id = b.branch_id " +
                    "WHERE e.branch_id = ? AND e.status = 'Available' AND e.is_active = TRUE " +
                    "ORDER BY e.equipment_code";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                equipmentList.add(extractEquipmentFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving available equipment", e);
        }
        return equipmentList;
    }

    public void create(Equipment equipment) throws SQLException {
        String sql = "INSERT INTO equipment (equipment_code, category_id, brand, model, purchase_year, " +
                    "base_daily_price, security_deposit, status, branch_id, description, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, equipment.getEquipmentCode());
            stmt.setInt(2, equipment.getCategoryId());
            stmt.setString(3, equipment.getBrand());
            stmt.setString(4, equipment.getModel());
            stmt.setInt(5, equipment.getPurchaseYear());
            stmt.setBigDecimal(6, equipment.getBaseDailyPrice());
            stmt.setBigDecimal(7, equipment.getSecurityDeposit());
            stmt.setString(8, equipment.getStatus().name().replace("_", " "));
            stmt.setInt(9, equipment.getBranchId());
            stmt.setString(10, equipment.getDescription());
            stmt.setBoolean(11, equipment.isActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                equipment.setEquipmentId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating equipment", e);
        }
    }

    public void update(Equipment equipment) throws SQLException {
        String sql = "UPDATE equipment SET category_id = ?, brand = ?, model = ?, purchase_year = ?, " +
                    "base_daily_price = ?, security_deposit = ?, status = ?, description = ?, is_active = ? " +
                    "WHERE equipment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, equipment.getCategoryId());
            stmt.setString(2, equipment.getBrand());
            stmt.setString(3, equipment.getModel());
            stmt.setInt(4, equipment.getPurchaseYear());
            stmt.setBigDecimal(5, equipment.getBaseDailyPrice());
            stmt.setBigDecimal(6, equipment.getSecurityDeposit());
            stmt.setString(7, equipment.getStatus().name().replace("_", " "));
            stmt.setString(8, equipment.getDescription());
            stmt.setBoolean(9, equipment.isActive());
            stmt.setInt(10, equipment.getEquipmentId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating equipment", e);
        }
    }

    public void updateStatus(int equipmentId, Equipment.EquipmentStatus status) throws SQLException {
        String sql = "UPDATE equipment SET status = ? WHERE equipment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name().replace("_", " "));
            stmt.setInt(2, equipmentId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating equipment status", e);
        }
    }

    private Equipment extractEquipmentFromResultSet(ResultSet rs) throws SQLException {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(rs.getInt("equipment_id"));
        equipment.setEquipmentCode(rs.getString("equipment_code"));
        equipment.setCategoryId(rs.getInt("category_id"));
        equipment.setCategoryName(rs.getString("category_name"));
        equipment.setBrand(rs.getString("brand"));
        equipment.setModel(rs.getString("model"));
        equipment.setPurchaseYear(rs.getInt("purchase_year"));
        equipment.setBaseDailyPrice(rs.getBigDecimal("base_daily_price"));
        equipment.setSecurityDeposit(rs.getBigDecimal("security_deposit"));
        equipment.setStatus(Equipment.EquipmentStatus.valueOf(rs.getString("status").replace(" ", "_")));
        equipment.setBranchId(rs.getInt("branch_id"));
        equipment.setBranchName(rs.getString("branch_name"));
        equipment.setDescription(rs.getString("description"));
        equipment.setActive(rs.getBoolean("is_active"));
        equipment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return equipment;
    }
}
