package com.gearrentpro.dao;

import com.gearrentpro.entity.PricingConfig;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;

public class PricingConfigDAO {

    public PricingConfig getConfig() throws SQLException {
        String sql = "SELECT * FROM pricing_config LIMIT 1";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                PricingConfig config = new PricingConfig();
                config.setConfigId(rs.getInt("config_id"));
                config.setLongRentalDaysThreshold(rs.getInt("long_rental_days_threshold"));
                config.setLongRentalDiscountPercentage(rs.getBigDecimal("long_rental_discount_percentage"));
                config.setMaxRentalDays(rs.getInt("max_rental_days"));
                return config;
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error retrieving pricing config", e);
        }
    }

    public void update(PricingConfig config) throws SQLException {
        String sql = "UPDATE pricing_config SET long_rental_days_threshold = ?, " +
                    "long_rental_discount_percentage = ?, max_rental_days = ? WHERE config_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, config.getLongRentalDaysThreshold());
            stmt.setBigDecimal(2, config.getLongRentalDiscountPercentage());
            stmt.setInt(3, config.getMaxRentalDays());
            stmt.setInt(4, config.getConfigId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating pricing config", e);
        }
    }
}
