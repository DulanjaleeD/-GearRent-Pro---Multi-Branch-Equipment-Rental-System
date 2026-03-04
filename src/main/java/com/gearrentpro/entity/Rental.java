package com.gearrentpro.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rental {
    private int rentalId;
    private String rentalCode;
    private int equipmentId;
    private String equipmentName;
    private int customerId;
    private String customerName;
    private String customerContact;
    private int branchId;
    private String branchName;
    private Integer reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private int rentalDays;
    private BigDecimal baseRentalAmount;
    private BigDecimal longRentalDiscount;
    private BigDecimal membershipDiscount;
    private BigDecimal finalRentalAmount;
    private BigDecimal securityDeposit;
    private BigDecimal lateFee;
    private BigDecimal damageCharge;
    private String damageDescription;
    private BigDecimal totalCharges;
    private BigDecimal depositRefund;
    private BigDecimal additionalPayment;
    private PaymentStatus paymentStatus;
    private RentalStatus rentalStatus;
    private int createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private Integer returnedBy;
    private String returnedByName;
    private LocalDateTime returnedAt;

    public enum PaymentStatus {
        Paid, Unpaid, Partial
    }

    public enum RentalStatus {
        Active, Returned, Overdue, Cancelled
    }

    public Rental() {}

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getRentalCode() {
        return rentalCode;
    }

    public void setRentalCode(String rentalCode) {
        this.rentalCode = rentalCode;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
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

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public BigDecimal getBaseRentalAmount() {
        return baseRentalAmount;
    }

    public void setBaseRentalAmount(BigDecimal baseRentalAmount) {
        this.baseRentalAmount = baseRentalAmount;
    }

    public BigDecimal getLongRentalDiscount() {
        return longRentalDiscount;
    }

    public void setLongRentalDiscount(BigDecimal longRentalDiscount) {
        this.longRentalDiscount = longRentalDiscount;
    }

    public BigDecimal getMembershipDiscount() {
        return membershipDiscount;
    }

    public void setMembershipDiscount(BigDecimal membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
    }

    public BigDecimal getFinalRentalAmount() {
        return finalRentalAmount;
    }

    public void setFinalRentalAmount(BigDecimal finalRentalAmount) {
        this.finalRentalAmount = finalRentalAmount;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getDamageCharge() {
        return damageCharge;
    }

    public void setDamageCharge(BigDecimal damageCharge) {
        this.damageCharge = damageCharge;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public BigDecimal getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(BigDecimal totalCharges) {
        this.totalCharges = totalCharges;
    }

    public BigDecimal getDepositRefund() {
        return depositRefund;
    }

    public void setDepositRefund(BigDecimal depositRefund) {
        this.depositRefund = depositRefund;
    }

    public BigDecimal getAdditionalPayment() {
        return additionalPayment;
    }

    public void setAdditionalPayment(BigDecimal additionalPayment) {
        this.additionalPayment = additionalPayment;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReturnedBy() {
        return returnedBy;
    }

    public void setReturnedBy(Integer returnedBy) {
        this.returnedBy = returnedBy;
    }

    public String getReturnedByName() {
        return returnedByName;
    }

    public void setReturnedByName(String returnedByName) {
        this.returnedByName = returnedByName;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
