package com.gearrentpro.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Customer {
    private int customerId;
    private String customerCode;
    private String name;
    private String nicPassport;
    private String contactNumber;
    private String email;
    private String address;
    private int membershipLevelId;
    private String membershipLevelName;
    private BigDecimal totalDepositHeld;
    private BigDecimal maxDepositLimit;
    private boolean isActive;
    private LocalDateTime createdAt;

    public Customer() {}

    public Customer(int customerId, String customerCode, String name, String nicPassport, String contactNumber,
                    String email, String address, int membershipLevelId, String membershipLevelName,
                    BigDecimal totalDepositHeld, BigDecimal maxDepositLimit, boolean isActive, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.name = name;
        this.nicPassport = nicPassport;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.membershipLevelId = membershipLevelId;
        this.membershipLevelName = membershipLevelName;
        this.totalDepositHeld = totalDepositHeld;
        this.maxDepositLimit = maxDepositLimit;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicPassport() {
        return nicPassport;
    }

    public void setNicPassport(String nicPassport) {
        this.nicPassport = nicPassport;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMembershipLevelId() {
        return membershipLevelId;
    }

    public void setMembershipLevelId(int membershipLevelId) {
        this.membershipLevelId = membershipLevelId;
    }

    public String getMembershipLevelName() {
        return membershipLevelName;
    }

    public void setMembershipLevelName(String membershipLevelName) {
        this.membershipLevelName = membershipLevelName;
    }

    public BigDecimal getTotalDepositHeld() {
        return totalDepositHeld;
    }

    public void setTotalDepositHeld(BigDecimal totalDepositHeld) {
        this.totalDepositHeld = totalDepositHeld;
    }

    public BigDecimal getMaxDepositLimit() {
        return maxDepositLimit;
    }

    public void setMaxDepositLimit(BigDecimal maxDepositLimit) {
        this.maxDepositLimit = maxDepositLimit;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name + " (" + customerCode + ")";
    }
}
