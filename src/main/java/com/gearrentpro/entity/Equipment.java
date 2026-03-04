package com.gearrentpro.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Equipment {
    private int equipmentId;
    private String equipmentCode;
    private int categoryId;
    private String categoryName;
    private String brand;
    private String model;
    private int purchaseYear;
    private BigDecimal baseDailyPrice;
    private BigDecimal securityDeposit;
    private EquipmentStatus status;
    private int branchId;
    private String branchName;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;

    public enum EquipmentStatus {
        Available, Reserved, Rented, Under_Maintenance
    }

    public Equipment() {}

    public Equipment(int equipmentId, String equipmentCode, int categoryId, String categoryName, String brand,
                     String model, int purchaseYear, BigDecimal baseDailyPrice, BigDecimal securityDeposit,
                     EquipmentStatus status, int branchId, String branchName, String description, boolean isActive,
                     LocalDateTime createdAt) {
        this.equipmentId = equipmentId;
        this.equipmentCode = equipmentCode;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.brand = brand;
        this.model = model;
        this.purchaseYear = purchaseYear;
        this.baseDailyPrice = baseDailyPrice;
        this.securityDeposit = securityDeposit;
        this.status = status;
        this.branchId = branchId;
        this.branchName = branchName;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPurchaseYear() {
        return purchaseYear;
    }

    public void setPurchaseYear(int purchaseYear) {
        this.purchaseYear = purchaseYear;
    }

    public BigDecimal getBaseDailyPrice() {
        return baseDailyPrice;
    }

    public void setBaseDailyPrice(BigDecimal baseDailyPrice) {
        this.baseDailyPrice = baseDailyPrice;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return brand + " " + model + " (" + equipmentCode + ")";
    }
}
