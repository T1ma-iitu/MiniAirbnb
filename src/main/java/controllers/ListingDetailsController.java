package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Listing;

public class ListingDetailsController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label roomsLabel;
    @FXML
    private Label priceLabel;

    private Listing listing;

    public void setListing(Listing listing) {
        this.listing = listing;

        titleLabel.setText(listing.getTitle());
        cityLabel.setText("Город: " + listing.getCity());
        roomsLabel.setText("Комнаты: " + listing.getRooms());
        priceLabel.setText("Цена: $" + listing.getPrice());
    }
}
