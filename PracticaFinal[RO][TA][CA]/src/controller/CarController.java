package controller;

import model.Car;
import model.DBConnection;
import model.User;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarController {

    private UserController userController = new UserController();

    // Registrar un coche y vincularlo a un usuario
    public boolean registerCar(Car car, String userId) {
        String insertCarSql = "INSERT INTO cars (brand, model, licensePlate, year) VALUES (?, ?, ?, ?)";
        String insertUserCarSql = "INSERT INTO user_car (user_id, car_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement carStmt = conn.prepareStatement(insertCarSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

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

    // Listar coches de un usuario
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

    // Añadir un propietario a un coche dado el nombre del usuario
    public boolean addOwnerToCar(int carId, String userName) {
     
        User user = userController.getUserByName(userName);

        if (user == null) {
            System.out.println("No existe usuario con nombre: " + userName);
            return false;
        }

        String sql = "INSERT INTO user_car (user_id, car_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getId());
            stmt.setInt(2, carId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al añadir propietario. Puede que ya sea propietario o haya otro problema.");
            e.printStackTrace();
        }

        return false;
    }



    // Modificar un coche (solo si el usuario es propietario)
public boolean updateCar(Car car, String userId) {
    // Verificar si el usuario es propietario
    String checkOwnershipSql = "SELECT * FROM user_car WHERE car_id = ? AND user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkOwnershipSql)) {

        checkStmt.setInt(1, car.getId());
        checkStmt.setString(2, userId);
        ResultSet rs = checkStmt.executeQuery();

        if (!rs.next()) {
            return false; // No es propietario
        }

        String updateSql = "UPDATE cars SET brand = ?, model = ?, licensePlate = ?, year = ? WHERE id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, car.getBrand());
            updateStmt.setString(2, car.getModel());
            updateStmt.setString(3, car.getLicensePlate());
            updateStmt.setInt(4, car.getYear());
            updateStmt.setInt(5, car.getId());
            updateStmt.executeUpdate();
            return true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

// Eliminar un coche (solo si el usuario es el único propietario)
public boolean deleteCar(int carId, String userId) {
    String ownershipSql = "SELECT COUNT(*) AS total FROM user_car WHERE car_id = ?";
    String checkUserSql = "SELECT * FROM user_car WHERE car_id = ? AND user_id = ?";
    String deleteCarSql = "DELETE FROM cars WHERE id = ?";
    String deleteRelationsSql = "DELETE FROM user_car WHERE car_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql);
         PreparedStatement countOwnersStmt = conn.prepareStatement(ownershipSql)) {

        checkUserStmt.setInt(1, carId);
        checkUserStmt.setString(2, userId);
        ResultSet checkRs = checkUserStmt.executeQuery();

        if (!checkRs.next()) {
            return false; // No es propietario
        }

        countOwnersStmt.setInt(1, carId);
        ResultSet countRs = countOwnersStmt.executeQuery();
        if (countRs.next() && countRs.getInt("total") > 1) {
            return false; // Hay más de un propietario, no puede eliminar
        }

        // Eliminar relaciones y coche (solo si 1 propietario 2 no )
        try (PreparedStatement deleteRelationsStmt = conn.prepareStatement(deleteRelationsSql);
             PreparedStatement deleteCarStmt = conn.prepareStatement(deleteCarSql)) {

            deleteRelationsStmt.setInt(1, carId);
            deleteRelationsStmt.executeUpdate();

            deleteCarStmt.setInt(1, carId);
            deleteCarStmt.executeUpdate();

            return true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

}
