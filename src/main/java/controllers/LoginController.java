package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import models.User;
import utils.DataStore;
import utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginTabButton;

    @FXML
    private Button registerTabButton;

    @FXML
    private VBox loginForm;

    @FXML
    private VBox registerForm;

    @FXML
    private TextField loginEmailField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private TextField registerNameField;

    @FXML
    private TextField registerEmailField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private PasswordField registerConfirmPasswordField;

    @FXML
    public void initialize() {
        System.out.println("Экран входа/регистрации загружен!");

        switchToLogin(null);
    }

    @FXML
    private void switchToLogin(ActionEvent event) {
        loginForm.setVisible(true);
        loginForm.setManaged(true);
        registerForm.setVisible(false);
        registerForm.setManaged(false);

        loginTabButton.setStyle("-fx-background-color: #FF385C; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 40; " +
                "-fx-background-radius: 8 0 0 8; -fx-cursor: hand;");
        registerTabButton.setStyle("-fx-background-color: #F7F7F7; -fx-text-fill: #222222; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 40; " +
                "-fx-background-radius: 0 8 8 0; -fx-cursor: hand;");
    }

    @FXML
    private void switchToRegister(ActionEvent event) {
        loginForm.setVisible(false);
        loginForm.setManaged(false);
        registerForm.setVisible(true);
        registerForm.setManaged(true);

        loginTabButton.setStyle("-fx-background-color: #F7F7F7; -fx-text-fill: #222222; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 40; " +
                "-fx-background-radius: 8 0 0 8; -fx-cursor: hand;");
        registerTabButton.setStyle("-fx-background-color: #FF385C; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 40; " +
                "-fx-background-radius: 0 8 8 0; -fx-cursor: hand;");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Ошибка входа", "Пожалуйста, заполните все поля");
            return;
        }

        User user = DataStore.getUserByEmail(email);

        if (user == null) {
            showError("Ошибка входа", "Пользователь с таким email не найден");
            return;
        }

        if (!user.getPassword().equals(password)) {
            showError("Ошибка входа", "Неверный пароль");
            return;
        }

        System.out.println("✅ Успешный вход: " + user.getName());

        SessionManager.setCurrentUser(user);

        showSuccess("Добро пожаловать!",
                "Вы успешно вошли в систему",
                "Привет, " + user.getName() + "! 👋");

        backToMainMenu(event);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = registerNameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Ошибка регистрации", "Пожалуйста, заполните все поля");
            return;
        }

        if (!email.contains("@")) {
            showError("Ошибка регистрации", "Введите корректный email");
            return;
        }

        if (password.length() < 6) {
            showError("Ошибка регистрации", "Пароль должен быть минимум 6 символов");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Ошибка регистрации", "Пароли не совпадают");
            return;
        }

        if (DataStore.getUserByEmail(email) != null) {
            showError("Ошибка регистрации", "Пользователь с таким email уже существует");
            return;
        }

        int newId = DataStore.users.size() + 1;
        User newUser = new User(newId, name, email, password);
        DataStore.users.add(newUser);

        System.out.println("✅ Новый пользователь зарегистрирован: " + name);

        SessionManager.setCurrentUser(newUser);

        showSuccess("Регистрация успешна!",
                "Добро пожаловать в Mini Airbnb!",
                "Ваш аккаунт успешно создан, " + name + "! 🎉");

        backToMainMenu(event);
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}