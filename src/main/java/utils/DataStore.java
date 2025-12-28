package utils;

import models.User;
import models.Listing;
import models.Booking;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static List<User> users = new ArrayList<>();
    public static List<Listing> listings = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();

    static {
        loadDataFromDatabase();
    }

    private static void loadDataFromDatabase() {
        System.out.println(" Загрузка данных из базы данных PostgreSQL...");

        try {
            if (DatabaseConnection.testConnection()) {
                users = UserDAO.getAllUsers();

                listings = ListingDAO.getAllListings();

                System.out.println(" Данные успешно загружены из БД!");
                System.out.println("   Пользователей: " + users.size());
                System.out.println("   Объявлений: " + listings.size());
            } else {
                System.err.println(" Не удалось подключиться к базе данных!");
                System.err.println("  Используются тестовые данные в памяти...");
                initializeTestData();
            }

        } catch (Exception e) {
            System.err.println(" Ошибка при загрузке данных из БД!");
            e.printStackTrace();
            System.err.println("  Используются тестовые данные в памяти...");
            initializeTestData();
        }
    }

    private static void initializeTestData() {
        users.add(new User(1, "Айжан Касымова", "aizhan@mail.kz", "password123"));
        users.add(new User(2, "Нурлан Абдуллин", "nurlan@mail.kz", "password123"));
        users.add(new User(3, "Дина Сарсенова", "dina@mail.kz", "password123"));

        listings.add(new Listing(
                1,
                "Уютная квартира у моря",
                "Алматы, Казахстан",
                1,
                15000,
                "Прекрасная квартира с видом на горы",
                "Квартира",
                2, 1, 1, 1,
                4.95, 128,
                "🏖️",
                "Wi-Fi, Кондиционер, Кухня"
        ));

        listings.add(new Listing(
                2,
                "Домик в горах",
                "Алматы, Казахстан",
                2,
                25000,
                "Уютный домик с потрясающим видом",
                "Дом",
                4, 2, 3, 1,
                4.87, 96,
                "🏔️",
                "Wi-Fi, Камин, Кухня"
        ));

        System.out.println("✅ Загружены тестовые данные в память");
    }

    public static void reload() {
        users.clear();
        listings.clear();
        bookings.clear();
        loadDataFromDatabase();
    }

    public static User getUserByEmail(String email) {
        User user = UserDAO.getUserByEmail(email);
        if (user != null) {
            return user;
        }

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public static Listing getListingById(int id) {
        Listing listing = ListingDAO.getListingById(id);
        if (listing != null) {
            return listing;
        }

        for (Listing l : listings) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }
}