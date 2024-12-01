package frameworks;// frameworks/DatabaseConnector.java
import entities.Gejala;
import entities.Solusi;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// frameworks/DatabaseConnector.java
public class DatabaseConnector {
    private Connection connection;

    public DatabaseConnector() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_pakar_konter", "root", "");
    }

    // Mendapatkan semua gejala dari database
    public List<Gejala> getAllGejala() throws SQLException {
        List<Gejala> gejalas = new ArrayList<>();
        String query = "SELECT * FROM gejala";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                double certaintyFactor = rs.getDouble("certainty_factor");
                gejalas.add(new Gejala(id, nama, certaintyFactor));
            }
        }
        return gejalas;
    }

    public boolean registerUser(String username, String password) throws SQLException {
        // Mengecek apakah username sudah ada di database
        String checkQuery = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            // Jika username sudah terdaftar, kembalikan false
            if (rs.next()) {
                return false;  // Username sudah ada di database
            }
        }

        // Jika username belum terdaftar, simpan user baru ke database
        String insertQuery = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);  // Pastikan password di-encrypt di aplikasi nyata
            insertStmt.executeUpdate();  // Eksekusi query untuk menambah user baru
        }

        return true;  // Pendaftaran berhasil
    }

    public User verifikasiLogin(String username, String password) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                User user = new User(id, username, password, role);  // Membuat objek User
                SessionManager.addLoggedInUser(user);  // Menambahkan pengguna ke daftar login
                return user;  // Mengembalikan objek User
            }
        }
        return null;  // Jika username atau password salah
    }



    // Mendapatkan solusi berdasarkan ID gejala
    public Solusi getSolusiByGejalaId(int gejalaId) throws SQLException {
        String query = "SELECT * FROM solusi WHERE gejala_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gejalaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Membuat objek Solusi dan mengisinya dengan data dari ResultSet
                int id = rs.getInt("id");
                String deskripsiSolusi = rs.getString("deskripsi_solusi");
                String jenisSolusi = rs.getString("jenis_solusi");

                // Mengembalikan objek Solusi yang sudah diisi
                return new Solusi(id, gejalaId, deskripsiSolusi, jenisSolusi);
            }
        }
        return null;  // Jika tidak ditemukan solusi
    }
}



