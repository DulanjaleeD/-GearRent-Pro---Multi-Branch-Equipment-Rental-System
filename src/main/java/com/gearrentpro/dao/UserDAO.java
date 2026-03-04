package com.gearrentpro.dao;

import com.gearrentpro.entity.User;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT u.*, r.role_name, b.name as branch_name " +
                    "FROM users u " +
                    "JOIN roles r ON u.role_id = r.role_id " +
                    "LEFT JOIN branches b ON u.branch_id = b.branch_id " +
                    "WHERE u.username = ? AND u.is_active = TRUE";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding user by username", e);
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name, b.name as branch_name " +
                    "FROM users u " +
                    "JOIN roles r ON u.role_id = r.role_id " +
                    "LEFT JOIN branches b ON u.branch_id = b.branch_id " +
                    "WHERE u.is_active = TRUE";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving all users", e);
        }
        return users;
    }

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, contact_number, role_id, branch_id, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getContactNumber());
            stmt.setInt(6, user.getRoleId());
            if (user.getBranchId() != null) {
                stmt.setInt(7, user.getBranchId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setBoolean(8, user.isActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating user", e);
        }
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ?, contact_number = ?, " +
                    "role_id = ?, branch_id = ?, is_active = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getContactNumber());
            stmt.setInt(4, user.getRoleId());
            if (user.getBranchId() != null) {
                stmt.setInt(5, user.getBranchId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setBoolean(6, user.isActive());
            stmt.setInt(7, user.getUserId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating user", e);
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setContactNumber(rs.getString("contact_number"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));

        int branchId = rs.getInt("branch_id");
        user.setBranchId(rs.wasNull() ? null : branchId);
        user.setBranchName(rs.getString("branch_name"));

        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}
