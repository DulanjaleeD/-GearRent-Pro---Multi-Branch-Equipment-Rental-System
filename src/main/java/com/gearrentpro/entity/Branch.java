package com.gearrentpro.entity;

import java.time.LocalDateTime;

public class Branch {
    private int branchId;
    private String branchCode;
    private String name;
    private String address;
    private String contactNumber;
    private boolean isActive;
    private LocalDateTime createdAt;

    public Branch() {}

    public Branch(int branchId, String branchCode, String name, String address, String contactNumber, boolean isActive, LocalDateTime createdAt) {
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
        return name;
    }
}
