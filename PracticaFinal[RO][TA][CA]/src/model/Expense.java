package model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private int carId;
    private ExpenseType type;
    private int kilometers;
    private LocalDate date;
    private double amount;
    private String description;

    public Expense(int carId, ExpenseType type, int kilometers, LocalDate date, double amount, String description) {
        this.carId = carId;
        this.type = type;
        this.kilometers = kilometers;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public Expense(int id, int carId, ExpenseType type, int kilometers, LocalDate date, double amount, String description) {
        this(carId, type, kilometers, date, amount, description);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getCarId() {
        return this.carId;
    }

    public ExpenseType getType() {
        return this.type;
    }

    public int getKilometers() {
        return this.kilometers;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
