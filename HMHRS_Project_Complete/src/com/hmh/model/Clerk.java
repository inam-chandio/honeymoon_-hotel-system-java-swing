package com.hmh.model;

public class Clerk {
    private int frontDeskId;
    private String frontDeskName;
    private String frontDeskPhoneNumber;
    private String frontDeskEmail;

    // Constructor
    public Clerk(int frontDeskId, String frontDeskName, String frontDeskPhoneNumber, String frontDeskEmail) {
        this.frontDeskId = frontDeskId;
        this.frontDeskName = frontDeskName;
        this.frontDeskPhoneNumber = frontDeskPhoneNumber;
        this.frontDeskEmail = frontDeskEmail;
    }

    // Getters and Setters
    public int getFrontDeskId() {
        return frontDeskId;
    }

    public void setFrontDeskId(int frontDeskId) {
        this.frontDeskId = frontDeskId;
    }

    public String getFrontDeskName() {
        return frontDeskName;
    }

    public void setFrontDeskName(String frontDeskName) {
        this.frontDeskName = frontDeskName;
    }

    public String getFrontDeskPhoneNumber() {
        return frontDeskPhoneNumber;
    }

    public void setFrontDeskPhoneNumber(String frontDeskPhoneNumber) {
        this.frontDeskPhoneNumber = frontDeskPhoneNumber;
    }

    public String getFrontDeskEmail() {
        return frontDeskEmail;
    }

    public void setFrontDeskEmail(String frontDeskEmail) {
        this.frontDeskEmail = frontDeskEmail;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "frontDeskId=" + frontDeskId +
                ", frontDeskName='" + frontDeskName + '\'' +
                ", frontDeskPhoneNumber='" + frontDeskPhoneNumber + '\'' +
                ", frontDeskEmail='" + frontDeskEmail + '\'' +
                '}';
    }
}
