package view;

import controller.CarController;
import controller.UserController;
import model.Car;
import model.User;

import java.util.List;
import java.util.Scanner;

public class MainView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserController userController = new UserController();
    private static final CarController carController = new CarController();
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
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opción no válida.");
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
            System.out.println("Usuario registrado correctamente.");
        } else {
            System.out.println("El nombre de usuario ya existe.");
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
            System.out.println("Sesión iniciada. ¡Bienvenido, " + user.getName() + "!");
            showUserMenu();
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }
    }

    private static void showUserMenu() {
        while (true) {
            System.out.println("===== MENÚ DE USUARIO =====");
            System.out.println("1. Ver mis coches");
            System.out.println("2. Añadir coche");
            System.out.println("3. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
    List<Car> cars = carController.getCarsByUserId(currentUser.getId());
    if (cars.isEmpty()) {
        System.out.println("No tienes coches registrados.");
    } else              {
        
        System.out.println("=== Tus coches ===");
        for (Car car : cars) {
            System.out.println("Marca: " + car.getBrand() +
                               " | Modelo: " + car.getModel() +
                               " | Matrícula: " + car.getLicensePlate() +
                               " | Año: " + car.getYear());
        }
    }
    break;

                case "2":
                    addCar();
                    break;
                case "3":
                    currentUser = null;
                    System.out.println("Sesión cerrada.");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void addCar() {
                System.out.println("=== Añadir nuevo coche ===");

        System.out.print("Marca: ");
             String brand = scanner.nextLine();

        System.out.print("Modelo: ");
        String model = scanner.nextLine();

        System.out.print("Matrícula: ");
        String licensePlate = scanner.nextLine();

        System.out.print("Año: ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Año no válido.");
            return;
        }

        Car car = new Car(brand, model, licensePlate, year);

        boolean success = carController.registerCar(car, currentUser.getId());

        if (success) {
            System.out.println("Coche registrado correctamente.");
        } else {
            System.out.println("Error al registrar el coche.");
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
