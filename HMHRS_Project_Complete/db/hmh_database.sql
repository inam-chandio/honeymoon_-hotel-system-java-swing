
CREATE DATABASE IF NOT EXISTS hmh_database;
USE hmh_database;

-- Table: Rooms
CREATE TABLE Rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(50) NOT NULL,
    bed_type VARCHAR(50) NOT NULL,
    bed_count INT NOT NULL,
    quality_level VARCHAR(50) NOT NULL,
    smoking_allowed BOOLEAN NOT NULL,
    daily_rate DOUBLE NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

-- Table: Guests
CREATE TABLE Guests (
    guest_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255) NOT NULL,
    credit_card VARCHAR(16) NOT NULL
);

-- Table: Reservations
CREATE TABLE Reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_price DOUBLE,
    FOREIGN KEY (guest_id) REFERENCES Guests(guest_id),
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
);

-- Table: Payments
CREATE TABLE Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount_paid DOUBLE NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)
);

-- Table: Clerks
CREATE TABLE Clerks (
    clerk_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Table: Managers
CREATE TABLE Managers (
    manager_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL
);
