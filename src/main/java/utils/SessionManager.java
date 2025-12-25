package utils;

import models.User;

public class SessionManager {

    private static User currentUser = null;

    public static void setCurrentUser(User user) {
        currentUser = user;
        System.out.println("✅ Пользователь вошёл в систему: " + user.getName());
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        if (currentUser != null) {
            System.out.println("👋 Пользователь вышел: " + currentUser.getName());
            currentUser = null;
        }
    }

    public static String getCurrentUserName() {
        return isLoggedIn() ? currentUser.getName() : "Гость";
    }

    public static int getCurrentUserId() {
        return isLoggedIn() ? currentUser.getId() : -1;
    }
}