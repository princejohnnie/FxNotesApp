package com.example.crudapp.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    private static final String DATABASE_URL =
            "jdbc:sqlite:C:\\Users\\princejohnnie\\IdeaProjects\\CRUDApp\\src\\main\\java\\com\\example\\crudapp\\database\\notes_db.db";

    public Connection connection;

    public Connection getDbConnection(){
        try{
            if (connection == null){
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Connection created successfully: " + connection.toString());
            }

        } catch (Exception e){
            System.out.println(e.toString());
        }
        return connection;
    }
}
