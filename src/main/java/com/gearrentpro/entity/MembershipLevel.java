package com.gearrentpro.entity;

import java.math.BigDecimal;

public class MembershipLevel {
    private int levelId;
    private String levelName;
    private BigDecimal discountPercentage;
    private String description;
    private boolean isActive;

    public MembershipLevel() {}

    public MembershipLevel(int levelId, String levelName, BigDecimal discountPercentage, String description, boolean isActive) {
        this.levelId = levelId;
        this.levelName = levelName;
        this.discountPercentage = discountPercentage;
        this.description = description;
        this.isActive = isActive;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
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

    @Override
    public String toString() {
        return levelName;
    }
}
