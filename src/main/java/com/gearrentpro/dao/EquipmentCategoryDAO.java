package com.gearrentpro.dao;

import com.gearrentpro.entity.EquipmentCategory;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentCategoryDAO {

    public List<EquipmentCategory> findAll() throws SQLException {
        List<EquipmentCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM equipment_categories WHERE is_active = TRUE ORDER BY name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving categories", e);
        }
        return categories;
    }

    public EquipmentCategory findById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM equipment_categories WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding category", e);
        }
    }

    public void create(EquipmentCategory category) throws SQLException {
        String sql = "INSERT INTO equipment_categories (name, description, base_price_factor, weekend_multiplier, " +
                    "default_late_fee_per_day, is_active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBigDecimal(3, category.getBasePriceFactor());
            stmt.setBigDecimal(4, category.getWeekendMultiplier());
            stmt.setBigDecimal(5, category.getDefaultLateFeePerDay());
            stmt.setBoolean(6, category.isActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                category.setCategoryId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating category", e);
        }
    }

    public void update(EquipmentCategory category) throws SQLException {
        String sql = "UPDATE equipment_categories SET name = ?, description = ?, base_price_factor = ?, " +
                    "weekend_multiplier = ?, default_late_fee_per_day = ?, is_active = ? WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBigDecimal(3, category.getBasePriceFactor());
            stmt.setBigDecimal(4, category.getWeekendMultiplier());
            stmt.setBigDecimal(5, category.getDefaultLateFeePerDay());
            stmt.setBoolean(6, category.isActive());
            stmt.setInt(7, category.getCategoryId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating category", e);
        }
    }

    private EquipmentCategory extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        EquipmentCategory category = new EquipmentCategory();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setBasePriceFactor(rs.getBigDecimal("base_price_factor"));
        category.setWeekendMultiplier(rs.getBigDecimal("weekend_multiplier"));
        category.setDefaultLateFeePerDay(rs.getBigDecimal("default_late_fee_per_day"));
        category.setActive(rs.getBoolean("is_active"));
        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return category;
    }
}
