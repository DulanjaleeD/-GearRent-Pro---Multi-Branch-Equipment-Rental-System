-- GearRent Pro Database Schema

DROP DATABASE IF EXISTS gearrentpro;
CREATE DATABASE gearrentpro;
USE gearrentpro;

-- 1. User Roles Table
CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

-- 2. Branches Table
CREATE TABLE branches (
    branch_id INT PRIMARY KEY AUTO_INCREMENT,
    branch_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    contact_number VARCHAR(20),
    role_id INT NOT NULL,
    branch_id INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id)
);

-- 4. Membership Levels Table
CREATE TABLE membership_levels (
    level_id INT PRIMARY KEY AUTO_INCREMENT,
    level_name VARCHAR(50) UNIQUE NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

-- 5. Customers Table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    nic_passport VARCHAR(50) UNIQUE NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    membership_level_id INT DEFAULT 1,
    total_deposit_held DECIMAL(10,2) DEFAULT 0,
    max_deposit_limit DECIMAL(10,2) DEFAULT 500000,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (membership_level_id) REFERENCES membership_levels(level_id)
);

-- 6. Equipment Categories Table
CREATE TABLE equipment_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    base_price_factor DECIMAL(5,2) DEFAULT 1.0,
    weekend_multiplier DECIMAL(5,2) DEFAULT 1.2,
    default_late_fee_per_day DECIMAL(10,2) DEFAULT 100.0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Equipment Table
CREATE TABLE equipment (
    equipment_id INT PRIMARY KEY AUTO_INCREMENT,
    equipment_code VARCHAR(30) UNIQUE NOT NULL,
    category_id INT NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    purchase_year INT NOT NULL,
    base_daily_price DECIMAL(10,2) NOT NULL,
    security_deposit DECIMAL(10,2) NOT NULL,
    status ENUM('Available', 'Reserved', 'Rented', 'Under Maintenance') DEFAULT 'Available',
    branch_id INT NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES equipment_categories(category_id),
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id)
);

-- 8. Reservations Table
CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_code VARCHAR(30) UNIQUE NOT NULL,
    equipment_id INT NOT NULL,
    customer_id INT NOT NULL,
    branch_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('Active', 'Cancelled', 'Converted') DEFAULT 'Active',
    notes TEXT,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 9. Rentals Table
CREATE TABLE rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    rental_code VARCHAR(30) UNIQUE NOT NULL,
    equipment_id INT NOT NULL,
    customer_id INT NOT NULL,
    branch_id INT NOT NULL,
    reservation_id INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    actual_return_date DATE,
    rental_days INT NOT NULL,
    base_rental_amount DECIMAL(10,2) NOT NULL,
    long_rental_discount DECIMAL(10,2) DEFAULT 0,
    membership_discount DECIMAL(10,2) DEFAULT 0,
    final_rental_amount DECIMAL(10,2) NOT NULL,
    security_deposit DECIMAL(10,2) NOT NULL,
    late_fee DECIMAL(10,2) DEFAULT 0,
    damage_charge DECIMAL(10,2) DEFAULT 0,
    damage_description TEXT,
    total_charges DECIMAL(10,2) NOT NULL,
    deposit_refund DECIMAL(10,2) DEFAULT 0,
    additional_payment DECIMAL(10,2) DEFAULT 0,
    payment_status ENUM('Paid', 'Unpaid', 'Partial') DEFAULT 'Unpaid',
    rental_status ENUM('Active', 'Returned', 'Overdue', 'Cancelled') DEFAULT 'Active',
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    returned_by INT,
    returned_at TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id),
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id),
    FOREIGN KEY (returned_by) REFERENCES users(user_id)
);

-- 10. Pricing Configuration Table
CREATE TABLE pricing_config (
    config_id INT PRIMARY KEY AUTO_INCREMENT,
    long_rental_days_threshold INT DEFAULT 7,
    long_rental_discount_percentage DECIMAL(5,2) DEFAULT 10.0,
    max_rental_days INT DEFAULT 30,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX idx_equipment_status ON equipment(status);
CREATE INDEX idx_equipment_branch ON equipment(branch_id);
CREATE INDEX idx_equipment_category ON equipment(category_id);
CREATE INDEX idx_reservations_dates ON reservations(start_date, end_date);
CREATE INDEX idx_reservations_equipment ON reservations(equipment_id);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_rentals_dates ON rentals(start_date, end_date);
CREATE INDEX idx_rentals_equipment ON rentals(equipment_id);
CREATE INDEX idx_rentals_status ON rentals(rental_status);
CREATE INDEX idx_rentals_customer ON rentals(customer_id);
CREATE INDEX idx_customers_membership ON customers(membership_level_id);
CREATE INDEX idx_users_role ON users(role_id);
CREATE INDEX idx_users_branch ON users(branch_id);
