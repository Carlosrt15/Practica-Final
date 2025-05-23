CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    licensePlate VARCHAR(20),
    year INT
);

CREATE TABLE user_car (
    user_id CHAR(36),
    car_id INT,
    PRIMARY KEY (user_id, car_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT,
    type ENUM('Gasolina', 'Revisión', 'ITV', 'Cambio de aceite', 'Otros'),
    kilometers INT,
    date DATE,
    amount DECIMAL(10, 2),
    description TEXT,
    FOREIGN KEY (car_id) REFERENCES cars(id)
);
