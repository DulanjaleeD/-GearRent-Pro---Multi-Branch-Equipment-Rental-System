package com.gearrentpro.dao;

import com.gearrentpro.entity.Customer;
import com.gearrentpro.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT c.*, m.level_name " +
                    "FROM customers c " +
                    "JOIN membership_levels m ON c.membership_level_id = m.level_id " +
                    "WHERE c.is_active = TRUE ORDER BY c.name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving customers", e);
        }
        return customers;
    }

    public Customer findById(int customerId) throws SQLException {
        String sql = "SELECT c.*, m.level_name " +
                    "FROM customers c " +
                    "JOIN membership_levels m ON c.membership_level_id = m.level_id " +
                    "WHERE c.customer_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error finding customer", e);
        }
    }

    public List<Customer> search(String keyword) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT c.*, m.level_name " +
                    "FROM customers c " +
                    "JOIN membership_levels m ON c.membership_level_id = m.level_id " +
                    "WHERE c.is_active = TRUE AND (c.name LIKE ? OR c.customer_code LIKE ? OR c.contact_number LIKE ?) " +
                    "ORDER BY c.name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Error searching customers", e);
        }
        return customers;
    }

    public void create(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_code, name, nic_passport, contact_number, email, address, " +
                    "membership_level_id, total_deposit_held, max_deposit_limit, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getCustomerCode());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getNicPassport());
            stmt.setString(4, customer.getContactNumber());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getAddress());
            stmt.setInt(7, customer.getMembershipLevelId());
            stmt.setBigDecimal(8, customer.getTotalDepositHeld());
            stmt.setBigDecimal(9, customer.getMaxDepositLimit());
            stmt.setBoolean(10, customer.isActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setCustomerId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Error creating customer", e);
        }
    }

    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, nic_passport = ?, contact_number = ?, email = ?, " +
                    "address = ?, membership_level_id = ?, max_deposit_limit = ?, is_active = ? " +
                    "WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getNicPassport());
            stmt.setString(3, customer.getContactNumber());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getMembershipLevelId());
            stmt.setBigDecimal(7, customer.getMaxDepositLimit());
            stmt.setBoolean(8, customer.isActive());
            stmt.setInt(9, customer.getCustomerId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating customer", e);
        }
    }

    public void updateDepositHeld(int customerId, java.math.BigDecimal amount) throws SQLException {
        String sql = "UPDATE customers SET total_deposit_held = ? WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error updating customer deposit", e);
        }
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setCustomerCode(rs.getString("customer_code"));
        customer.setName(rs.getString("name"));
        customer.setNicPassport(rs.getString("nic_passport"));
        customer.setContactNumber(rs.getString("contact_number"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setMembershipLevelId(rs.getInt("membership_level_id"));
        customer.setMembershipLevelName(rs.getString("level_name"));
        customer.setTotalDepositHeld(rs.getBigDecimal("total_deposit_held"));
        customer.setMaxDepositLimit(rs.getBigDecimal("max_deposit_limit"));
        customer.setActive(rs.getBoolean("is_active"));
        customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return customer;
    }
}
