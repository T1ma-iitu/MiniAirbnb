package models;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int listingId;
    private int userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guestsCount;
    private int totalPrice;
    private String status;

    public Booking() {}

    public Booking(int id, int listingId, int userId, LocalDate checkInDate,
                   LocalDate checkOutDate, int guestsCount, int totalPrice, String status) {
        this.id = id;
        this.listingId = listingId;
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestsCount = guestsCount;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getListingId() { return listingId; }
    public void setListingId(int listingId) { this.listingId = listingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getGuestsCount() { return guestsCount; }
    public void setGuestsCount(int guestsCount) { this.guestsCount = guestsCount; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getNights() {
        return java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
}