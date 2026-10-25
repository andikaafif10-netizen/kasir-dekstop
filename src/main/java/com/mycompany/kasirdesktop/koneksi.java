
package com.mycompany.kasirdesktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {

    private static final String URL =
            "jdbc:mysql://localhost:3306/db_kasir";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getKoneksi() throws SQLException {
        return DriverManager.getConnection(
                URL, USER, PASSWORD
        );
    }
}