package com.hmh.model;

public class QualityLevel {
    private int qualityId;
    private String qualityName; // New field
    private double dailyRate;

    // Constructor
    public QualityLevel(int qualityId, String qualityName, double dailyRate) {
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.dailyRate = dailyRate;
    }

    // Getters and Setters
    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }
}
