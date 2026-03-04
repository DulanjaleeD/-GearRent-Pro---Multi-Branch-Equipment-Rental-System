package com.gearrentpro.dao;

import com.gearrentpro.entity.Branch;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    public List<Branch> findAll() throws SQLException {
        List<Branch> branches = new ArrayList<>();
        String sql = "SELECT * FROM branches WHERE is_active = TRUE ORDER BY name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                branches.add(extractBranchFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving branches", e);
        }
        return branches;
    }

    public Branch findById(int branchId) throws SQLException {
        String sql = "SELECT * FROM branches WHERE branch_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractBranchFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding branch", e);
        }
    }

    public void create(Branch branch) throws SQLException {
        String sql = "INSERT INTO branches (branch_code, name, address, contact_number, is_active) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, branch.getBranchCode());
            stmt.setString(2, branch.getName());
            stmt.setString(3, branch.getAddress());
            stmt.setString(4, branch.getContactNumber());
            stmt.setBoolean(5, branch.isActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                branch.setBranchId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating branch", e);
        }
    }

    public void update(Branch branch) throws SQLException {
        String sql = "UPDATE branches SET name = ?, address = ?, contact_number = ?, is_active = ? " +
                    "WHERE branch_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, branch.getName());
            stmt.setString(2, branch.getAddress());
            stmt.setString(3, branch.getContactNumber());
            stmt.setBoolean(4, branch.isActive());
            stmt.setInt(5, branch.getBranchId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating branch", e);
        }
    }

    private Branch extractBranchFromResultSet(ResultSet rs) throws SQLException {
        Branch branch = new Branch();
        branch.setBranchId(rs.getInt("branch_id"));
        branch.setBranchCode(rs.getString("branch_code"));
        branch.setName(rs.getString("name"));
        branch.setAddress(rs.getString("address"));
        branch.setContactNumber(rs.getString("contact_number"));
        branch.setActive(rs.getBoolean("is_active"));
        branch.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return branch;
    }
}
