package models;

public class Listing {

    private int id;
    private String title;
    private String city;
    private int rooms;
    private int price;
    private String description;
    private String type;
    private int guests;
    private int bedrooms;
    private int beds;
    private int bathrooms;
    private double rating;
    private int reviewsCount;
    private String emoji;
    private String amenities;

    public Listing(int id, String title, String city, int rooms, int price) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.rooms = rooms;
        this.price = price;
        this.description = "Прекрасное место для отдыха";
        this.type = "Квартира";
        this.guests = 2;
        this.bedrooms = 1;
        this.beds = 1;
        this.bathrooms = 1;
        this.rating = 4.5;
        this.reviewsCount = 50;
        this.emoji = "🏠";
        this.amenities = "Wi-Fi, Кухня, Парковка";
    }

    public Listing(int id, String title, String city, int rooms, int price,
                   String description, String type, int guests, int bedrooms,
                   int beds, int bathrooms, double rating, int reviewsCount,
                   String emoji, String amenities) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.rooms = rooms;
        this.price = price;
        this.description = description;
        this.type = type;
        this.guests = guests;
        this.bedrooms = bedrooms;
        this.beds = beds;
        this.bathrooms = bathrooms;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.emoji = emoji;
        this.amenities = amenities;
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

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getGuests() {
        return guests;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBeds() {
        return beds;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getGuestInfo() {
        return guests + " гостя • " + bedrooms + " спальня • " + beds + " кровать • " + bathrooms + " ванная";
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }
}