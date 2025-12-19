package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Listing;
import utils.DataStore;

import java.io.IOException;

public class CatalogController {

    @FXML
    private VBox listingsContainer;

    @FXML
    public void initialize() {
        loadTestData();
        renderListings();
    }

    private void renderListings() {
        listingsContainer.getChildren().clear();

        for (Listing listing : DataStore.listings) {

            // Карточка
            HBox card = new HBox(10);
            card.setStyle(
                    "-fx-padding: 10;" +
                            "-fx-background-color: #ffffff;" +
                            "-fx-border-color: #dddddd;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;"
            );

            ImageView imageView = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/house.png"))
            );
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            VBox infoBox = new VBox(5);

            Label title = new Label(listing.getTitle());
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label city = new Label("Город: " + listing.getCity());
            Label price = new Label("Цена: $" + listing.getPrice());

            Button viewBtn = new Button("Посмотреть");

            // 🔹 ПЕРЕХОД НА ДЕТАЛИ
            viewBtn.setOnAction(e -> openDetails(listing, e));

            infoBox.getChildren().addAll(title, city, price, viewBtn);
            card.getChildren().addAll(imageView, infoBox);

            listingsContainer.getChildren().add(card);
        }
    }

    private void openDetails(Listing listing, javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/View/ListingDetails.fxml")
            );
            Scene scene = new Scene(loader.load(), 800, 600);

            ListingDetailsController controller = loader.getController();
            controller.setListing(listing);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTestData() {
        if (!DataStore.listings.isEmpty()) return;

        DataStore.listings.add(new Listing(
                1,
                "Уютная квартира в центре",
                "Алматы",
                2,
                500
        ));

        DataStore.listings.add(new Listing(
                2,
                "Апартаменты с видом на горы",
                "Астана",
                3,
                800
        ));

        DataStore.listings.add(new Listing(
                3,
                "Студия рядом с метро",
                "Алматы",
                1,
                300
        ));
    }
}
