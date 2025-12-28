package utils;

import models.Booking;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public static Booking createBooking(int listingId, int userId, LocalDate checkIn,
                                        LocalDate checkOut, int guests, int price) {
        String sql = "INSERT INTO bookings (listing_id, user_id, check_in_date, check_out_date, " +
                "guests_count, total_price, status) VALUES (?, ?, ?, ?, ?, ?, 'pending') RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listingId);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, Date.valueOf(checkIn));
            pstmt.setDate(4, Date.valueOf(checkOut));
            pstmt.setInt(5, guests);
            pstmt.setInt(6, price);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("✅ Бронирование создано с ID: " + id);
                return new Booking(id, listingId, userId, checkIn, checkOut, guests, price, "pending");
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка создания бронирования");
            e.printStackTrace();
        }
        return null;
    }

    public static List<Booking> getUserBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY check_in_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("id"),
                        rs.getInt("listing_id"),
                        rs.getInt("user_id"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getInt("guests_count"),
                        rs.getInt("total_price"),
                        rs.getString("status")
                );
                bookings.add(booking);
            }

            System.out.println("✅ Загружено " + bookings.size() + " бронирований");

        } catch (SQLException e) {
            System.err.println("❌ Ошибка загрузки бронирований");
            e.printStackTrace();
        }
        return bookings;
    }

    public static boolean updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bookingId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Статус обновлён на: " + newStatus);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка обновления статуса");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean cancelBooking(int bookingId) {
        return updateBookingStatus(bookingId, "cancelled");
    }

    public static Booking getBookingById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Booking(
                        rs.getInt("id"),
                        rs.getInt("listing_id"),
                        rs.getInt("user_id"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getInt("guests_count"),
                        rs.getInt("total_price"),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}