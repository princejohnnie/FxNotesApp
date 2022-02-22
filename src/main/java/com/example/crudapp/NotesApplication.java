package com.example.crudapp;

import com.example.crudapp.database.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NotesApplication.class.getResource("notes-view.fxml"));;
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Notes Application");
        stage.setScene(scene);
        stage.setMaxWidth(750);
        stage.setMaxHeight(650);
        stage.setMinHeight(650);
        stage.setMinWidth(750);
        stage.show();
    }



    public static void main(String[] args) {

        launch(args);
    }
}