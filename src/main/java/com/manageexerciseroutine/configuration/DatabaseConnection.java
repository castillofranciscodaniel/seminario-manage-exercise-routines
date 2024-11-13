package com.manageexerciseroutine.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/exercise_management"; // Cambia el puerto y el nombre de la BD si es necesario
    private static final String USER = "root"; // Usuario de tu MySQL
    private static final String PASSWORD = "Student123@"; // Sin contraseña

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
