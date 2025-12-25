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
import models.Listing;

import java.io.IOException;

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

        loadAmenities(listing.getAmenities());
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
    private void bookListing(ActionEvent event) {
        System.out.println("Попытка забронировать: " + currentListing.getTitle());

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Бронирование");
        alert.setHeaderText("Бронирование объявления");
        alert.setContentText("Вы собираетесь забронировать:\n\n" +
                currentListing.getTitle() + "\n" +
                "Цена: " + String.format("%,d ₸", currentListing.getPrice()) + " за ночь\n\n" +
                "Функция бронирования находится в разработке! 🚀");
        alert.showAndWait();

        /*
         * Здесь позже можно добавить переход на экран бронирования:
         *
         * try {
         *     FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Booking.fxml"));
         *     Scene scene = new Scene(loader.load(), 1000, 700);
         *
         *     BookingController controller = loader.getController();
         *     controller.setListing(currentListing);
         *
         *     Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         *     stage.setTitle("Mini Airbnb - Бронирование");
         *     stage.setScene(scene);
         *     stage.show();
         * } catch (IOException e) {
         *     e.printStackTrace();
         * }
         */
    }

    @FXML
    private void backToCatalog(ActionEvent event) {
        try {
            System.out.println("Возврат к каталогу...");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Catalog.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Mini Airbnb - Каталог жилья");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка при возврате к каталогу!");
            e.printStackTrace();
        }
    }
}