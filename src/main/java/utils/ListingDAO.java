package utils;

import models.Listing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListingDAO {

    public static List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        String sql = "SELECT * FROM listings ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Listing listing = createListingFromResultSet(rs);
                listings.add(listing);
            }

            System.out.println(" Загружено " + listings.size() + " объявлений из БД");

        } catch (SQLException e) {
            System.err.println(" Ошибка при загрузке объявлений");
            e.printStackTrace();
        }

        return listings;
    }

    public static Listing getListingById(int id) {
        String sql = "SELECT * FROM listings WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createListingFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println(" Ошибка при поиске объявления");
            e.printStackTrace();
        }

        return null;
    }

    public static List<Listing> getListingsByCity(String city) {
        List<Listing> listings = new ArrayList<>();
        String sql = "SELECT * FROM listings WHERE LOWER(city) LIKE LOWER(?) ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + city + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listings.add(createListingFromResultSet(rs));
            }

            System.out.println(" Найдено " + listings.size() + " объявлений в городе: " + city);

        } catch (SQLException e) {
            System.err.println(" Ошибка при поиске по городу");
            e.printStackTrace();
        }

        return listings;
    }

    public static List<Listing> getListingsByPriceRange(int minPrice, int maxPrice) {
        List<Listing> listings = new ArrayList<>();
        String sql = "SELECT * FROM listings WHERE price BETWEEN ? AND ? ORDER BY price";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, minPrice);
            pstmt.setInt(2, maxPrice);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listings.add(createListingFromResultSet(rs));
            }

            System.out.println(" Найдено " + listings.size() + " объявлений в диапазоне цен");

        } catch (SQLException e) {
            System.err.println(" Ошибка при фильтрации по цене");
            e.printStackTrace();
        }

        return listings;
    }

    public static List<Listing> getListingsByType(String type) {
        List<Listing> listings = new ArrayList<>();
        String sql = "SELECT * FROM listings WHERE type = ? ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listings.add(createListingFromResultSet(rs));
            }

            System.out.println(" Найдено " + listings.size() + " объявлений типа: " + type);

        } catch (SQLException e) {
            System.err.println(" Ошибка при фильтрации по типу");
            e.printStackTrace();
        }

        return listings;
    }

    public static Listing createListing(Listing listing) {
        String sql = "INSERT INTO listings (title, description, city, type, rooms, guests, " +
                "bedrooms, beds, bathrooms, price, rating, reviews_count, emoji, amenities, owner_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, listing.getTitle());
            pstmt.setString(2, listing.getDescription());
            pstmt.setString(3, listing.getCity());
            pstmt.setString(4, listing.getType());
            pstmt.setInt(5, listing.getRooms());
            pstmt.setInt(6, listing.getGuests());
            pstmt.setInt(7, listing.getBedrooms());
            pstmt.setInt(8, listing.getBeds());
            pstmt.setInt(9, listing.getBathrooms());
            pstmt.setInt(10, listing.getPrice());
            pstmt.setDouble(11, listing.getRating());
            pstmt.setInt(12, listing.getReviewsCount());
            pstmt.setString(13, listing.getEmoji());
            pstmt.setString(14, listing.getAmenities());
            pstmt.setInt(15, SessionManager.getCurrentUserId());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int newId = rs.getInt("id");
                listing.setId(newId);
                System.out.println(" Объявление создано с ID: " + newId);
                return listing;
            }

        } catch (SQLException e) {
            System.err.println(" Ошибка при создании объявления");
            e.printStackTrace();
        }

        return null;
    }

    public static boolean updateListing(Listing listing) {
        String sql = "UPDATE listings SET title = ?, description = ?, city = ?, type = ?, " +
                "rooms = ?, guests = ?, bedrooms = ?, beds = ?, bathrooms = ?, price = ?, " +
                "emoji = ?, amenities = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, listing.getTitle());
            pstmt.setString(2, listing.getDescription());
            pstmt.setString(3, listing.getCity());
            pstmt.setString(4, listing.getType());
            pstmt.setInt(5, listing.getRooms());
            pstmt.setInt(6, listing.getGuests());
            pstmt.setInt(7, listing.getBedrooms());
            pstmt.setInt(8, listing.getBeds());
            pstmt.setInt(9, listing.getBathrooms());
            pstmt.setInt(10, listing.getPrice());
            pstmt.setString(11, listing.getEmoji());
            pstmt.setString(12, listing.getAmenities());
            pstmt.setInt(13, listing.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" Объявление обновлено");
                return true;
            }

        } catch (SQLException e) {
            System.err.println(" Ошибка при обновлении объявления");
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteListing(int id) {
        String sql = "DELETE FROM listings WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" Объявление удалено");
                return true;
            }

        } catch (SQLException e) {
            System.err.println(" Ошибка при удалении объявления");
            e.printStackTrace();
        }

        return false;
    }

    private static Listing createListingFromResultSet(ResultSet rs) throws SQLException {
        return new Listing(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("city"),
                rs.getInt("rooms"),
                rs.getInt("price"),
                rs.getString("description"),
                rs.getString("type"),
                rs.getInt("guests"),
                rs.getInt("bedrooms"),
                rs.getInt("beds"),
                rs.getInt("bathrooms"),
                rs.getDouble("rating"),
                rs.getInt("reviews_count"),
                rs.getString("emoji"),
                rs.getString("amenities")
        );
    }
}