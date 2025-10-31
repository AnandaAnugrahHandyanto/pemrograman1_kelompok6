/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pemrograman1_kelompok5;

/**
 *
 * @author vynix-linux
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class KoneksiDB {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/reservalink_db?useSSL=false";
                String user = "root";
                String password = "1";

                // Daftarkan driver JDBC
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                
                // Buat koneksi
                koneksi = DriverManager.getConnection(url, user, password);
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal terhubung ke database: " + e.getMessage());
                System.err.println("Koneksi Gagal: " + e.getMessage());
            }
        }
        return koneksi;
    }
}
