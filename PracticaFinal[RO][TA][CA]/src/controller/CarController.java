package controller;

import model.Car;
import  model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
            import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarController {

    public boolean registerCar(Car car, String userId) {
        String insertCarSql = "INSERT INTO cars (brand, model, licensePlate, year) VALUES (?, ?, ?, ?)";
        String insertUserCarSql = "INSERT INTO user_car (user_id, car_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement carStmt = conn.prepareStatement(insertCarSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // 1. Insertar coche
            carStmt.setString(1, car.getBrand());
            carStmt.setString(2, car.getModel());
            carStmt.setString(3, car.getLicensePlate());
            carStmt.setInt(4, car.getYear());
            carStmt.executeUpdate();

           
            ResultSet generatedKeys = carStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int carId = generatedKeys.getInt(1);

                
                try (PreparedStatement userCarStmt = conn.prepareStatement(insertUserCarSql)) {
                    userCarStmt.setString(1, userId);
                         userCarStmt.setInt(2, carId);
                         userCarStmt.executeUpdate();
                    return true;
                     }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;



        


    }
        // Lstar coches usuarios
    public List<Car> getCarsByUserId(String userId) {
    List<Car> cars = new ArrayList<>();
    String sql = "SELECT c.* FROM cars c " +
                 "JOIN user_car uc ON c.id = uc.car_id " +
                 "WHERE uc.user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Car car = new Car(
                rs.getInt("id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("licensePlate"),
                rs.getInt("year")
            );
            cars.add(car);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return cars;
}

    
}
