package controller;

import model.Expense;
import model.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {

    public boolean registerExpense(Expense expense) {
        String sql = "INSERT INTO expenses (car_id, type, kilometers, date, amount, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expense.getCarId());
            stmt.setString(2, expense.getType().name());
            stmt.setInt(3, expense.getKilometers());
            stmt.setDate(4, Date.valueOf(expense.getDate()));
            stmt.setDouble(5, expense.getAmount());
            stmt.setString(6, expense.getDescription());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Expense> getExpensesByCarId(int carId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE car_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            // Mirar da error
            while (rs.next()) {
                    Expense expense = new Expense(
                        rs.getInt("id"),
                        carId,
                        model.ExpenseType.valueOf(rs.getString("type")),
                                rs.getInt("kilometers"),
                                 rs.getDate("date").toLocalDate(),
                            rs.getDouble("amount"),
                        rs.getString("description")
                );
                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }
}
