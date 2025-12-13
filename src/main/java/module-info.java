module com.example.miniairbnb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens app to javafx.fxml, javafx.graphics;
    opens controllers to javafx.fxml;
    opens models to javafx.fxml;

    exports app;
}
