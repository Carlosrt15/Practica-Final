package controller;

import model.DBConnection;
import model.User;

import java.sql.*;
import java.util.UUID;

public class UserController {

    public boolean registerUser(String name, String password) {
        if (getUserByName(name) != null) {
            return false;
        }

        String sql = "INSERT INTO users (uuid, nombre, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public User loginUser(String name, String password) {
        User user = getUserByName(name);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    private User getUserByName(String name) {
    String sql = "SELECT * FROM users WHERE name = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("DEBUG: Usuario ya existe en BD -> " + rs.getString("name"));
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
        } else {
            System.out.println("DEBUG: Usuario no encontrado");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}
}
