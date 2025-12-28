package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Booking;
import models.Listing;
import utils.BookingDAO;
import utils.FavoriteDAO;
import utils.SessionManager;

import java.io.IOException;
import java.time.LocalDate;

public class ListingDetailsController {

    @FXML
    private Label emojiLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label reviewsLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label guestsLabel;

    @FXML
    private Label bedroomsLabel;

    @FXML
    private Label bedsLabel;

    @FXML
    private Label bathroomsLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private FlowPane amenitiesPane;

    private Listing currentListing;

    @FXML
    public void initialize() {
        System.out.println("Страница деталей объявления загружена!");
    }

    public void setListing(Listing listing) {
        this.currentListing = listing;

        System.out.println("Загрузка деталей для: " + listing.getTitle());

        emojiLabel.setText(listing.getEmoji());
        titleLabel.setText(listing.getTitle());
        ratingLabel.setText("⭐ " + listing.getRating());
        reviewsLabel.setText("(" + listing.getReviewsCount() + " отзывов)");
        cityLabel.setText(listing.getCity());
        typeLabel.setText("Тип: " + listing.getType());
        guestsLabel.setText(String.valueOf(listing.getGuests()));
        bedroomsLabel.setText(String.valueOf(listing.getBedrooms()));
        bedsLabel.setText(String.valueOf(listing.getBeds()));
        bathroomsLabel.setText(String.valueOf(listing.getBathrooms()));
        priceLabel.setText(String.format("%,d ₸", listing.getPrice()));
        descriptionLabel.setText(listing.getDescription());

        FavoriteDAO.incrementViews(listing.getId());

        if (SessionManager.isLoggedIn()) {
            boolean isFav = FavoriteDAO.isFavorite(SessionManager.getCurrentUserId(), listing.getId());
        }

        loadAmenities(listing.getAmenities());
    }

    @FXML
    private void bookListing(ActionEvent event) {
        if (!SessionManager.isLoggedIn()) {
            showError("Ошибка", "Войдите в систему", "Для бронирования нужно войти");
            return;
        }

        System.out.println("Создание бронирования...");

        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);
        int guests = 2;
        int pricePerNight = currentListing.getPrice();
        int totalPrice = pricePerNight * 2;

        Booking booking = BookingDAO.createBooking(
                currentListing.getId(),
                SessionManager.getCurrentUserId(),
                checkIn,
                checkOut,
                guests,
                totalPrice
        );

        if (booking != null) {
            showSuccess("Успешно!",
                    "Бронирование создано!",
                    "Даты: " + checkIn + " - " + checkOut + "\nСумма: " + totalPrice + " ₸");
        } else {
            showError("Ошибка", "Не удалось создать бронирование", "Попробуйте позже");
        }
    }

    @FXML
    private void toggleFavorite(ActionEvent event) {
        if (!SessionManager.isLoggedIn()) {
            showError("Ошибка", "Войдите в систему", "Чтобы добавить в избранное, нужно войти");
            return;
        }

        int userId = SessionManager.getCurrentUserId();
        int listingId = currentListing.getId();

        boolean isFav = FavoriteDAO.isFavorite(userId, listingId);

        if (isFav) {
            FavoriteDAO.removeFromFavorites(userId, listingId);
            showInfo("Избранное", "Удалено из избранного", "💔");
        } else {
            FavoriteDAO.addToFavorites(userId, listingId);
            showInfo("Избранное", "Добавлено в избранное!", "❤️");
        }
    }

    private void loadAmenities(String amenitiesString) {
        amenitiesPane.getChildren().clear();

        if (amenitiesString == null || amenitiesString.isEmpty()) {
            return;
        }

        String[] amenities = amenitiesString.split(",");

        for (String amenity : amenities) {
            VBox amenityCard = new VBox(5);
            amenityCard.setStyle("-fx-background-color: #F7F7F7; -fx-padding: 15 20; " +
                    "-fx-background-radius: 8; -fx-alignment: center-left;");
            amenityCard.setPrefWidth(200);

            Label amenityLabel = new Label("✓ " + amenity.trim());
            amenityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #222222;");
            amenityLabel.setWrapText(true);

            amenityCard.getChildren().add(amenityLabel);
            amenitiesPane.getChildren().add(amenityCard);
        }
    }

    @FXML
    private void backToCatalog(ActionEvent event) {
        try {
            System.out.println("Возврат к каталогу...");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Catalog.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Mini Airbnb - Каталог жилья");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка при возврате к каталогу!");
            e.printStackTrace();
        }
    }

    private void showSuccess(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}