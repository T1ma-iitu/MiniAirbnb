package models;

public class Listing {

    private int id;
    private String title;
    private String city;
    private int rooms;
    private int price;

    public Listing(int id, String title, String city, int rooms, int price) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.rooms = rooms;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public int getRooms() {
        return rooms;
    }

    public int getPrice() {
        return price;
    }
}
