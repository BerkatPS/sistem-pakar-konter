package entities;

public class Gejala {
    private int id;
    private String nama;
    private double certaintyFactor;

    public Gejala(int id, String nama, double certaintyFactor) {
        this.id = id;
        this.nama = nama;
        this.certaintyFactor = certaintyFactor;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public double getCertaintyFactor() {
        return certaintyFactor;
    }
}
