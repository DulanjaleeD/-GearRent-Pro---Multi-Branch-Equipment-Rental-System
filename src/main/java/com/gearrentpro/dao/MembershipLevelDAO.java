package com.gearrentpro.dao;

import com.gearrentpro.entity.MembershipLevel;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipLevelDAO {

    public List<MembershipLevel> findAll() throws SQLException {
        List<MembershipLevel> levels = new ArrayList<>();
        String sql = "SELECT * FROM membership_levels WHERE is_active = TRUE ORDER BY discount_percentage";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                levels.add(extractLevelFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving membership levels", e);
        }
        return levels;
    }

    public MembershipLevel findById(int levelId) throws SQLException {
        String sql = "SELECT * FROM membership_levels WHERE level_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, levelId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractLevelFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding membership level", e);
        }
    }

    public void create(MembershipLevel level) throws SQLException {
        String sql = "INSERT INTO membership_levels (level_name, discount_percentage, description, is_active) " +
                    "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, level.getLevelName());
            stmt.setBigDecimal(2, level.getDiscountPercentage());
            stmt.setString(3, level.getDescription());
            stmt.setBoolean(4, level.isActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                level.setLevelId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating membership level", e);
        }
    }

    public void update(MembershipLevel level) throws SQLException {
        String sql = "UPDATE membership_levels SET level_name = ?, discount_percentage = ?, description = ?, " +
                    "is_active = ? WHERE level_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, level.getLevelName());
            stmt.setBigDecimal(2, level.getDiscountPercentage());
            stmt.setString(3, level.getDescription());
            stmt.setBoolean(4, level.isActive());
            stmt.setInt(5, level.getLevelId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating membership level", e);
        }
    }

    private MembershipLevel extractLevelFromResultSet(ResultSet rs) throws SQLException {
        MembershipLevel level = new MembershipLevel();
        level.setLevelId(rs.getInt("level_id"));
        level.setLevelName(rs.getString("level_name"));
        level.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
        level.setDescription(rs.getString("description"));
        level.setActive(rs.getBoolean("is_active"));
        return level;
    }
}
