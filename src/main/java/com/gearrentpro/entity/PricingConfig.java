package com.gearrentpro.entity;

import java.math.BigDecimal;

public class PricingConfig {
    private int configId;
    private int longRentalDaysThreshold;
    private BigDecimal longRentalDiscountPercentage;
    private int maxRentalDays;

    public PricingConfig() {}

    public PricingConfig(int configId, int longRentalDaysThreshold, BigDecimal longRentalDiscountPercentage, int maxRentalDays) {
        this.configId = configId;
        this.longRentalDaysThreshold = longRentalDaysThreshold;
        this.longRentalDiscountPercentage = longRentalDiscountPercentage;
        this.maxRentalDays = maxRentalDays;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getLongRentalDaysThreshold() {
        return longRentalDaysThreshold;
    }

    public void setLongRentalDaysThreshold(int longRentalDaysThreshold) {
        this.longRentalDaysThreshold = longRentalDaysThreshold;
    }

    public BigDecimal getLongRentalDiscountPercentage() {
        return longRentalDiscountPercentage;
    }

    public void setLongRentalDiscountPercentage(BigDecimal longRentalDiscountPercentage) {
        this.longRentalDiscountPercentage = longRentalDiscountPercentage;
    }

    public int getMaxRentalDays() {
        return maxRentalDays;
    }

    public void setMaxRentalDays(int maxRentalDays) {
        this.maxRentalDays = maxRentalDays;
    }
}
