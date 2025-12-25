package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public void initialize() {
        System.out.println("Главное меню загружено!");
        System.out.println("Mini Airbnb готов к работе!");
    }

    @FXML
    private void openCatalog(ActionEvent event) {
        try {
            System.out.println("Открытие каталога...");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Catalog.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Mini Airbnb - Каталог жилья");
            stage.setScene(scene);
            stage.show();

            System.out.println("Каталог успешно открыт!");

        } catch (IOException e) {
            System.err.println("Ошибка при открытии каталога!");
            e.printStackTrace();

            showError("Ошибка", "Не удалось открыть каталог",
                    "Проверьте, что файл Catalog.fxml существует в папке resources/View/");
        }
    }

    @FXML
    private void openLogin(ActionEvent event) {
        try {
            System.out.println("Открытие экрана входа/регистрации");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Mini Airbnb - Вход");
            stage.setScene(scene);
            stage.show();

            System.out.println("Экран входа успешно открыт!");

        } catch (IOException e) {
            System.err.println("Ошибка при открытии экрана входа!");
            e.printStackTrace();
            showError("Ошибка", "Не удалось открыть форму входа",
                    "Проверьте, что файл Login.fxml существует в папке resources/View/");
        }
    }

    private void showError(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}