package model;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String licensePlate;
    private int year;

    public Car(String brand, String model, String licensePlate, int year) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
    }

    public Car(int id, String brand, String model, String licensePlate, int year) {
        this(brand, model, licensePlate, year);
        this.id = id;
    }

    public int getId() {

        return this.id;
    }

    public String getBrand()
    {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public int getYear() {
        return this.year;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
