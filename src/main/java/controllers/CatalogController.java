package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import models.Listing;
import utils.DataStore;

import java.io.IOException;

public class CatalogController {

    @FXML
    private GridPane listingsGrid;

    @FXML
    public void initialize() {
        System.out.println("Каталог загружен!");
        loadListings();
    }

    private void loadListings() {
        System.out.println("Загрузка объявлений из DataStore...");

        if (listingsGrid == null) {
            System.err.println("ОШИБКА: listingsGrid не инициализирован!");
            return;
        }

        listingsGrid.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Listing listing : DataStore.listings) {
            VBox card = createListingCard(listing);
            listingsGrid.add(card, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }

        System.out.println("Загружено " + DataStore.listings.size() + " объявлений");
    }

    private VBox createListingCard(Listing listing) {
        VBox card = new VBox(10);
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: white; -fx-border-color: #EBEBEB; " +
                "-fx-border-radius: 12; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                "-fx-cursor: hand;");

        StackPane imagePane = new StackPane();
        imagePane.setPrefHeight(200);
        imagePane.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 12 12 0 0;");
        Label emoji = new Label(listing.getEmoji());
        emoji.setStyle("-fx-font-size: 60px;");
        imagePane.getChildren().add(emoji);

        VBox info = new VBox(5);
        info.setPadding(new Insets(15));

        HBox ratingBox = new HBox(5);
        ratingBox.setAlignment(Pos.CENTER_LEFT);
        Label rating = new Label("⭐ " + listing.getRating());
        rating.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Label reviews = new Label("(" + listing.getReviewsCount() + " отзывов)");
        reviews.setStyle("-fx-font-size: 12px; -fx-text-fill: #717171;");
        ratingBox.getChildren().addAll(rating, reviews);

        Label title = new Label(listing.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        title.setWrapText(true);

        Label city = new Label(listing.getCity());
        city.setStyle("-fx-font-size: 14px; -fx-text-fill: #717171;");

        Label guestInfo = new Label(listing.getGuestInfo());
        guestInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #717171;");
        guestInfo.setWrapText(true);

        HBox priceBox = new HBox(5);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        priceBox.setPadding(new Insets(10, 0, 0, 0));
        Label price = new Label(String.format("%,d ₸", listing.getPrice()));
        price.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label perNight = new Label("/ ночь");
        perNight.setStyle("-fx-font-size: 14px; -fx-text-fill: #717171;");
        priceBox.getChildren().addAll(price, perNight);

        info.getChildren().addAll(ratingBox, title, city, guestInfo, priceBox);

        card.getChildren().addAll(imagePane, info);
        card.setOnMouseClicked(event -> openListingDetails(listing));

        return card;
    }

    private void openListingDetails(Listing listing) {
        try {
            System.out.println("Открытие деталей объявления: " + listing.getTitle());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ListingDetails.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);

            ListingDetailsController controller = loader.getController();
            controller.setListing(listing);

            Stage stage = (Stage) listingsGrid.getScene().getWindow();
            stage.setTitle("Mini Airbnb - " + listing.getTitle());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка при открытии деталей объявления!");
            e.printStackTrace();
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        try {
            System.out.println("Возврат на главное меню...");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MainMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Mini Airbnb");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка при возврате на главное меню!");
            e.printStackTrace();
        }
    }
}