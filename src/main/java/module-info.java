module com.example.crudapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.crudapp to javafx.fxml;
    exports com.example.crudapp;

    exports com.example.crudapp.model to javafx.fxml;
}