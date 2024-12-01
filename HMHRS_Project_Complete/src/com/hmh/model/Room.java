package com.hmh.model;

public class Room {
    private int roomId;
    private String roomNumber;
    private String bedType;
    private int bedCount;
    private boolean smokingAllowed;
    private boolean available;
    private int qualityId;

    // Constructor
    public Room(int roomId, String roomNumber, String bedType, int bedCount,
                boolean smokingAllowed, boolean available, int qualityId) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.bedType = bedType;
        this.bedCount = bedCount;
        this.smokingAllowed = smokingAllowed;
        this.available = available;
        this.qualityId = qualityId;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", bedType='" + bedType + '\'' +
                ", bedCount=" + bedCount +
                ", smokingAllowed=" + smokingAllowed +
                ", available=" + available +
                ", qualityId=" + qualityId +
                '}';
    }
}
