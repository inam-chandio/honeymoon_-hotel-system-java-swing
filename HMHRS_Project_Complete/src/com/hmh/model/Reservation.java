package com.hmh.model;

public class Reservation {
    private int reservationId;
    private String reservationDate;
    private String reservationTime;
    private String checkInDate;
    private String checkOutDate;
    private String reservationStatus;
    private int allocatedRoom;

    // Constructor
    public Reservation(int reservationId, String reservationDate, String reservationTime, String checkInDate, String checkOutDate, String reservationStatus, int allocatedRoom) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationStatus = reservationStatus;
        this.allocatedRoom = allocatedRoom;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getAllocatedRoom() {
        return allocatedRoom;
    }

    public void setAllocatedRoom(int allocatedRoom) {
        this.allocatedRoom = allocatedRoom;
    }

    // Methods
    public void makeReservation() {
        // Logic for making a reservation
    }

    public void cancelReservation() {
        // Logic for canceling a reservation
    }

    public int allocateRoom() {
        // Logic for allocating a room
        return this.allocatedRoom;
    }

    public double calculateCancellationFee() {
        // Logic for calculating cancellation fee
        return 0.0; // Placeholder
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", reservationDate='" + reservationDate + '\'' +
                ", reservationTime='" + reservationTime + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", allocatedRoom=" + allocatedRoom +
                '}';
    }
}
