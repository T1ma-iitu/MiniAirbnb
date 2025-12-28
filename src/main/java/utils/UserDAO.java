package utils;

import models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                users.add(user);
            }

            System.out.println("✅ Загружено " + users.size() + " пользователей из БД");

        } catch (SQLException e) {
            System.err.println("❌ Ошибка при загрузке пользователей");
            e.printStackTrace();
        }

        return users;
    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка при поиске пользователя по ID");
            e.printStackTrace();
        }

        return null;
    }

    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка при поиске пользователя по email");
            e.printStackTrace();
        }

        return null;
    }

    public static User createUser(String name, String email, String password) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int newId = rs.getInt("id");
                System.out.println("✅ Пользователь создан с ID: " + newId);

                return new User(newId, name, email, password);
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка при создании пользователя");
            e.printStackTrace();
        }

        return null;
    }

    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Пользователь обновлён");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка при обновлении пользователя");
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Пользователь удалён");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка при удалении пользователя");
            e.printStackTrace();
        }

        return false;
    }

    public static boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }
}