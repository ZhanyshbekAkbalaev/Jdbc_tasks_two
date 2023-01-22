package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Configuration {
    public static Connection getGonnection (){
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres" ,
                    "postgres",
                    "1234"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
