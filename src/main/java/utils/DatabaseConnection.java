package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/miniairbnb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Sulpak2023";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);

                System.out.println("✅ Подключение к базе данных PostgreSQL успешно!");
            }
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ ОШИБКА: PostgreSQL драйвер не найден!");
            System.err.println("Добавь зависимость в pom.xml:");
            System.err.println("<dependency>");
            System.err.println("    <groupId>org.postgresql</groupId>");
            System.err.println("    <artifactId>postgresql</artifactId>");
            System.err.println("    <version>42.7.1</version>");
            System.err.println("</dependency>");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.err.println("❌ ОШИБКА подключения к базе данных!");
            System.err.println("Проверь:");
            System.err.println("1. Запущен ли PostgreSQL сервер");
            System.err.println("2. Правильные ли данные для подключения (URL, USER, PASSWORD)");
            System.err.println("3. Существует ли база данных 'miniairbnb'");
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Подключение к базе данных закрыто");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при закрытии подключения");
            e.printStackTrace();
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Тест подключения успешен!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Тест подключения провален!");
            e.printStackTrace();
        }
        return false;
    }
}