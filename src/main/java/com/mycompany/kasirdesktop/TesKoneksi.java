package com.mycompany.kasirdesktop;

import java.sql.Connection;
import java.sql.SQLException;

public class TesKoneksi {

    public static void main(String[] args) {
        try (Connection conn = koneksi.getKoneksi()) {
            System.out.println("Koneksi database berhasil!");
        } catch (SQLException e) {
            System.out.println("Koneksi database gagal!");
            e.printStackTrace();
        }
    }
}