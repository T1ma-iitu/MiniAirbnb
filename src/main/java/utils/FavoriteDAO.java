package utils;

import models.Listing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    public static boolean addToFavorites(int userId, int listingId) {
        String sql = "INSERT INTO favorites (user_id, listing_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, listingId);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Добавлено в избранное!");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean removeFromFavorites(int userId, int listingId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND listing_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, listingId);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Удалено из избранного");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isFavorite(int userId, int listingId) {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND listing_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, listingId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Listing> getUserFavorites(int userId) {
        List<Listing> favorites = new ArrayList<>();
        String sql = "SELECT l.* FROM listings l " +
                "JOIN favorites f ON l.id = f.listing_id " +
                "WHERE f.user_id = ? ORDER BY f.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Listing listing = new Listing(
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
                favorites.add(listing);
            }

            System.out.println("✅ Загружено " + favorites.size() + " избранных");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return favorites;
    }

    public static void incrementViews(int listingId) {
        String sql = "UPDATE listings SET views_count = views_count + 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listingId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}