package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AboutController {

    @FXML private Label name1;
    @FXML private Label role1;
    @FXML private Label task1;
    @FXML private Label email1;

    @FXML private Label name2;
    @FXML private Label role2;
    @FXML private Label task2;
    @FXML private Label email2;

    @FXML private Label university;
    @FXML private Label course;
    @FXML private Label year;

    @FXML
    public void initialize() {

        name1.setText(" Әбдіхалық Темірлан");
        role1.setText("Full-Stack Developer");
        task1.setText("Backend, база данных PostgreSQL, CRUD операции");
        email1.setText("TIMA@mail.kz");

        name2.setText("Бакдаулет Нурлан");
        role2.setText("Frontend Developer");
        task2.setText("Дизайн интерфейса, JavaFX, работа с FXML");
        email2.setText("BAGA@mail.kz");

        university.setText("🎓 IITU");
        course.setText("Курс: Java Programming to OOP");
        year.setText("Год: 2025");

        System.out.println("Страница 'О нас' загружена!");
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
            e.printStackTrace();
        }
    }
}