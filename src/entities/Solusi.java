package entities;

public class Solusi {
    private int id;
    private int gejalaId;
    private String deskripsiSolusi;
    private String jenisSolusi;

    // Constructor dengan semua parameter
    public Solusi(int id, int gejalaId, String deskripsiSolusi, String jenisSolusi) {
        this.id = id;
        this.gejalaId = gejalaId;
        this.deskripsiSolusi = deskripsiSolusi;
        this.jenisSolusi = jenisSolusi;
    }

    // Getter untuk id
    public int getId() {
        return id;
    }

    // Getter untuk gejalaId
    public int getGejalaId() {
        return gejalaId;
    }

    // Getter untuk deskripsiSolusi
    public String getDeskripsiSolusi() {
        return deskripsiSolusi;
    }

    // Getter untuk jenisSolusi
    public String getJenisSolusi() {
        return jenisSolusi;
    }

    // Method untuk menampilkan solusi
    public String tampilkanSolusi() {
        return "Solusi: " + deskripsiSolusi + "\nJenis Solusi: " + jenisSolusi;
    }
}
