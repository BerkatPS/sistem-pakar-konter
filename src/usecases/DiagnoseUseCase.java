package usecases;

import entities.Diagnosis;
import entities.Gejala;
import entities.Solusi;
import frameworks.DatabaseConnector;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiagnoseUseCase {
    private DatabaseConnector databaseConnector;

    public DiagnoseUseCase(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // Method untuk melakukan diagnosa berdasarkan keluhan
    public Diagnosis diagnose(String keluhan) {
        double totalCF = 0;
        StringBuilder hasilDiagnosa = new StringBuilder("Hasil Diagnosa:\n");
        StringBuilder solusi = new StringBuilder("Solusi:\n");

        List<Gejala> gejalas = new ArrayList<>();
        try {
            // Mendapatkan semua gejala dari database
            gejalas = databaseConnector.getAllGejala();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Diagnosis("Error mengakses data gejala dari database.", "Tidak dapat mengakses solusi.");
        }

        boolean gejalaDitemukan = false;

        // Memeriksa setiap gejala untuk menemukan kecocokan dengan keluhan pengguna
        for (Gejala gejala : gejalas) {
            if (keluhan != null && keluhan.toLowerCase().contains(gejala.getNama().toLowerCase())) {
                gejalaDitemukan = true;
                totalCF += gejala.getCertaintyFactor();
                hasilDiagnosa.append("Kerusakan pada: " + gejala.getNama() + " dengan CF: " + gejala.getCertaintyFactor() * 100 + "%\n");

                // Mendapatkan solusi berdasarkan ID gejala
                Solusi sol = null;
                try {
                    sol = databaseConnector.getSolusiByGejalaId(gejala.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (sol != null) {
                    solusi.append(sol.tampilkanSolusi() + "\n");
                }
            }
        }

        if (!gejalaDitemukan) {
            hasilDiagnosa.append("Tidak ada gejala yang sesuai dengan keluhan Anda. Silakan coba lagi dengan keluhan yang lebih jelas.\n");
            solusi.append("Tidak dapat memberikan solusi karena gejala tidak dikenali.\n");
        }

        return new Diagnosis(hasilDiagnosa.toString(), solusi.toString());
    }

    // Method untuk mendapatkan gejala-gejala berdasarkan keluhan
    public List<Gejala> getGejalasForDiagnosis(String keluhan) {
        List<Gejala> gejalas = new ArrayList<>();  // Gunakan java.util.List<Gejala>
        try {
            // Mendapatkan semua gejala dari database
            List<Gejala> allGejalas = databaseConnector.getAllGejala();

            // Memeriksa setiap gejala untuk menemukan yang cocok dengan keluhan
            for (Gejala gejala : allGejalas) {
                if (keluhan != null && keluhan.toLowerCase().contains(gejala.getNama().toLowerCase())) {
                    gejalas.add(gejala);  // Menambahkan gejala yang ditemukan
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gejalas;  // Mengembalikan List<Gejala> yang benar
    }


}
