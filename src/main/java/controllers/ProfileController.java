package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import models.Booking;
import models.Listing;
import models.User;
import utils.BookingDAO;
import utils.DataStore;
import utils.SessionManager;

import java.io.IOException;
import java.util.List;

public class ProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private VBox bookingsContainer;

    @FXML
    private Label totalBookingsLabel;

    @FXML
    private Label activeBookingsLabel;

    @FXML
    private Label totalSpentLabel;

    @FXML
    public void initialize() {
        System.out.println("Личный кабинет загружен!");

        if (!SessionManager.isLoggedIn()) {
            System.err.println("Пользователь не авторизован!");
            return;
        }

        loadProfile();
        loadBookings();
    }

    private void loadProfile() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getName());
            emailLabel.setText(user.getEmail());
            System.out.println("✅ Профиль загружен: " + user.getName());
        }
    }

    @FXML
    private void loadBookings() {
        bookingsContainer.getChildren().clear();

        int userId = SessionManager.getCurrentUserId();
        List<Booking> bookings = BookingDAO.getUserBookings(userId);

        if (bookings.isEmpty()) {
            Label noBookings = new Label("У вас пока нет бронирований 📭");
            noBookings.setStyle("-fx-font-size: 16px; -fx-text-fill: #717171; -fx-padding: 20;");
            bookingsContainer.getChildren().add(noBookings);

            updateStatistics(bookings);
            return;
        }

        for (Booking booking : bookings) {
            VBox bookingCard = createBookingCard(booking);
            bookingsContainer.getChildren().add(bookingCard);
        }

        updateStatistics(bookings);

        System.out.println("✅ Загружено " + bookings.size() + " бронирований");
    }

    private VBox createBookingCard(Booking booking) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #EBEBEB; " +
                "-fx-border-radius: 12; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Listing listing = DataStore.getListingById(booking.getListingId());
        String listingTitle = listing != null ? listing.getTitle() : "Объявление #" + booking.getListingId();

        Label title = new Label(listingTitle);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox datesBox = new HBox(20);
        datesBox.setAlignment(Pos.CENTER_LEFT);

        VBox checkInBox = new VBox(5);
        Label checkInLabel = new Label("Заезд:");
        checkInLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #717171;");
        Label checkInDate = new Label(booking.getCheckInDate().toString());
        checkInDate.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        checkInBox.getChildren().addAll(checkInLabel, checkInDate);

        VBox checkOutBox = new VBox(5);
        Label checkOutLabel = new Label("Выезд:");
        checkOutLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #717171;");
        Label checkOutDate = new Label(booking.getCheckOutDate().toString());
        checkOutDate.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        checkOutBox.getChildren().addAll(checkOutLabel, checkOutDate);

        VBox nightsBox = new VBox(5);
        Label nightsLabel = new Label("Ночей:");
        nightsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #717171;");
        Label nights = new Label(String.valueOf(booking.getNights()));
        nights.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        nightsBox.getChildren().addAll(nightsLabel, nights);

        datesBox.getChildren().addAll(checkInBox, checkOutBox, nightsBox);

        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER_LEFT);

        Label price = new Label(String.format("%,d ₸", booking.getTotalPrice()));
        price.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FF385C;");

        Label status = new Label(getStatusText(booking.getStatus()));
        status.setStyle(getStatusStyle(booking.getStatus()));
        status.setPadding(new Insets(5, 15, 5, 15));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if ("pending".equals(booking.getStatus()) || "confirmed".equals(booking.getStatus())) {
            Button cancelBtn = new Button("❌ Отменить");
            cancelBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; " +
                    "-fx-font-size: 14px; -fx-padding: 8 20; -fx-background-radius: 8; -fx-cursor: hand;");
            cancelBtn.setOnAction(e -> cancelBooking(booking.getId()));

            bottomBox.getChildren().addAll(price, spacer, status, cancelBtn);
        } else {
            bottomBox.getChildren().addAll(price, spacer, status);
        }

        card.getChildren().addAll(title, datesBox, new Separator(), bottomBox);

        return card;
    }

    private void cancelBooking(int bookingId) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Отмена бронирования");
        confirm.setHeaderText("Вы уверены?");
        confirm.setContentText("Отменить это бронирование?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = BookingDAO.cancelBooking(bookingId);

                if (success) {
                    showSuccess("Успешно!", "Бронирование отменено");
                    loadBookings(); // Перезагружаем список
                } else {
                    showError("Ошибка", "Не удалось отменить бронирование");
                }
            }
        });
    }

    private void updateStatistics(List<Booking> bookings) {
        int total = bookings.size();
        int active = 0;
        int totalSpent = 0;

        for (Booking b : bookings) {
            if ("pending".equals(b.getStatus()) || "confirmed".equals(b.getStatus())) {
                active++;
            }
            if (!"cancelled".equals(b.getStatus())) {
                totalSpent += b.getTotalPrice();
            }
        }

        totalBookingsLabel.setText(String.valueOf(total));
        activeBookingsLabel.setText(String.valueOf(active));
        totalSpentLabel.setText(String.format("%,d ₸", totalSpent));
    }

    @FXML
    private void logout(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Выход");
        confirm.setHeaderText("Выйти из аккаунта?");
        confirm.setContentText("Вы уверены?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SessionManager.logout();
                backToMainMenu(event);
            }
        });
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MainMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Mini Airbnb");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка при возврате на главное меню!");
            e.printStackTrace();
        }
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return " Ожидает";
            case "confirmed": return " Подтверждено";
            case "cancelled": return " Отменено";
            default: return status;
        }
    }

    private String getStatusStyle(String status) {
        String baseStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 15;";
        switch (status) {
            case "pending":
                return baseStyle + " -fx-background-color: #FFF3CD; -fx-text-fill: #856404;";
            case "confirmed":
                return baseStyle + " -fx-background-color: #D4EDDA; -fx-text-fill: #155724;";
            case "cancelled":
                return baseStyle + " -fx-background-color: #F8D7DA; -fx-text-fill: #721C24;";
            default:
                return baseStyle + " -fx-background-color: #E0E0E0; -fx-text-fill: #222222;";
        }
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}