package view;

import controller.UserController;
import model.User;

import java.util.Scanner;

public class MainView {
    private static Scanner scanner = new Scanner(System.in);
    private static UserController userController = new UserController();
    private static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("===== CONTROL DE GASTOS DE COCHES =====");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    loginUser();
                    break;
                case "3":
                    System.out.println("👋 Saliendo del sistema...");
                    return;
                default:
                    System.out.println("❌ Opción no válida.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Ingrese nombre de usuario: ");
        String name = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String password = scanner.nextLine();

        boolean registered = userController.registerUser(name, password);
        if (registered) {
            System.out.println(" Usuario registrado correctamente.");
        } else {
            System.out.println(" El nombre de usuario ya existe.");
        }
    }

    private static void loginUser() {
        System.out.print("Nombre de usuario: ");
        String name = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        User user = userController.loginUser(name, password);
        if (user != null) {
            currentUser = user;
            System.out.println(" Sesión iniciada. ¡Bienvenido, " + user.getName() + "!");
            showUserMenu();
        } else {
            System.out.println(" Usuario o contraseña incorrectos.");
        }
    }

    private static void showUserMenu() {
        while (true) {
            System.out.println("\n===== MENÚ DE USUARIO =====");
            System.out.println("1. Ver mis coches");
            System.out.println("2. Añadir coche");
            System.out.println("3. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    currentUser = null;
                    System.out.println(" Sesión cerrada.");
                    return;
                default:
                    System.out.println(" Opción no válida.");
            }
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
