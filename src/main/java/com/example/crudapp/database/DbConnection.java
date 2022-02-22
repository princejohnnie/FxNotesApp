package com.example.crudapp.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/notes_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Yungjohn4619#";

    public Connection connection;

    public Connection getDbConnection(){
        try{
            if (connection == null){
                connection = DriverManager.getConnection(DATABASE_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connection created successfully: " + connection.toString());
            }

        } catch (Exception e){
            System.out.println(e.toString());
        }
        return connection;
    }
}
