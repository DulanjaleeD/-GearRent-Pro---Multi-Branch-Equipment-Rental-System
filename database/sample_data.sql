-- GearRent Pro Sample Data
USE gearrentpro;

-- Insert Roles
INSERT INTO roles (role_name, description) VALUES
('Admin', 'System administrator with full access'),
('Branch Manager', 'Manager of a specific branch'),
('Staff', 'Branch staff for daily operations');

-- Insert Branches
INSERT INTO branches (branch_code, name, address, contact_number) VALUES
('PAN001', 'Panadura Branch', 'No. 123, Galle Road, Panadura', '0382234567'),
('GAL001', 'Galle Branch', 'No. 45, Main Street, Galle Fort', '0912234567'),
('COL001', 'Colombo Branch', 'No. 78, Duplication Road, Colombo 04', '0112234567');

-- Insert Users (password is 'password123' for all)
-- Password hash generated using BCrypt
INSERT INTO users (username, password_hash, full_name, email, contact_number, role_id, branch_id) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'admin@gearrentpro.com', '0771234567', 1, NULL),
('manager_pan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kamal Silva', 'kamal@gearrentpro.com', '0771234568', 2, 1),
('manager_gal', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nimal Perera', 'nimal@gearrentpro.com', '0771234569', 2, 2),
('staff_pan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sunil Fernando', 'sunil@gearrentpro.com', '0771234570', 3, 1),
('staff_gal', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Amal Jayasinghe', 'amal@gearrentpro.com', '0771234571', 3, 2),
('staff_col', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Chamara Dias', 'chamara@gearrentpro.com', '0771234572', 3, 3);

-- Insert Membership Levels
INSERT INTO membership_levels (level_name, discount_percentage, description) VALUES
('Regular', 0.00, 'Standard membership with no discount'),
('Silver', 5.00, 'Silver membership with 5% discount'),
('Gold', 10.00, 'Gold membership with 10% discount');

-- Insert Customers
INSERT INTO customers (customer_code, name, nic_passport, contact_number, email, address, membership_level_id) VALUES
('CUST001', 'Saman Kumara', '199012345678', '0771111111', 'saman@email.com', 'Panadura', 1),
('CUST002', 'Nimal Rodrigo', '198523456789', '0772222222', 'nimal@email.com', 'Galle', 2),
('CUST003', 'Kasun Perera', '199234567890', '0773333333', 'kasun@email.com', 'Colombo', 3),
('CUST004', 'Ruwan Silva', '198845678901', '0774444444', 'ruwan@email.com', 'Panadura', 1),
('CUST005', 'Dilshan Fernando', '199556789012', '0775555555', 'dilshan@email.com', 'Galle', 2),
('CUST006', 'Chaminda Jayawardena', '198667890123', '0776666666', 'chaminda@email.com', 'Colombo', 1),
('CUST007', 'Mahela Dissanayake', '199778901234', '0777777777', 'mahela@email.com', 'Panadura', 3),
('CUST008', 'Kumar Sangakkara', '198889012345', '0778888888', 'kumar@email.com', 'Galle', 2),
('CUST009', 'Lasith Malinga', '199990123456', '0779999999', 'lasith@email.com', 'Colombo', 1),
('CUST010', 'Angelo Mathews', '199101234567', '0770000000', 'angelo@email.com', 'Panadura', 3);

-- Insert Equipment Categories
INSERT INTO equipment_categories (name, description, base_price_factor, weekend_multiplier, default_late_fee_per_day) VALUES
('Power Tools', 'Drills, saws, grinders and other power tools', 1.0, 1.3, 150.00),
('Garden Equipment', 'Lawn mowers, trimmers, leaf blowers', 1.0, 1.5, 200.00),
('Construction Equipment', 'Heavy machinery for construction', 1.5, 1.2, 500.00),
('Party & Event', 'Tables, chairs, tents, sound systems', 0.8, 2.0, 100.00),
('Camping Gear', 'Tents, sleeping bags, camping equipment', 0.9, 1.8, 80.00);

-- Insert Equipment
INSERT INTO equipment (equipment_code, category_id, brand, model, purchase_year, base_daily_price, security_deposit, status, branch_id, description) VALUES
-- Panadura Branch Equipment
('EQ-PAN-001', 1, 'Bosch', 'GSB 550 Power Drill', 2022, 500.00, 2000.00, 'Available', 1, '550W professional drill'),
('EQ-PAN-002', 1, 'Makita', 'Circular Saw 5704R', 2023, 800.00, 3000.00, 'Available', 1, '1200W circular saw'),
('EQ-PAN-003', 2, 'Honda', 'HRX Lawn Mower', 2022, 1200.00, 5000.00, 'Available', 1, 'Self-propelled lawn mower'),
('EQ-PAN-004', 2, 'Stihl', 'FS 55 Trimmer', 2023, 600.00, 2500.00, 'Available', 1, 'Professional grass trimmer'),
('EQ-PAN-005', 4, 'Sony', 'MHC-V73D Sound System', 2021, 2500.00, 10000.00, 'Rented', 1, 'High-power audio system'),
('EQ-PAN-006', 5, 'Coleman', 'Sundome Tent 6-Person', 2022, 800.00, 3000.00, 'Available', 1, 'Family camping tent'),
('EQ-PAN-007', 1, 'DeWalt', 'DWE7491RS Table Saw', 2023, 1500.00, 6000.00, 'Available', 1, 'Portable table saw'),
-- Galle Branch Equipment
('EQ-GAL-001', 1, 'Bosch', 'GBH 2-26 DRE Rotary Hammer', 2022, 900.00, 3500.00, 'Available', 2, 'Professional rotary hammer'),
('EQ-GAL-002', 2, 'Husqvarna', '125B Leaf Blower', 2023, 700.00, 2800.00, 'Available', 2, 'Handheld leaf blower'),
('EQ-GAL-003', 3, 'JCB', 'Mini Excavator 8008', 2021, 15000.00, 50000.00, 'Available', 2, '800kg micro excavator'),
('EQ-GAL-004', 3, 'Caterpillar', 'Skid Steer 226D', 2020, 12000.00, 45000.00, 'Under Maintenance', 2, 'Compact loader'),
('EQ-GAL-005', 4, 'Generic', 'Party Tent 20x20', 2022, 3000.00, 8000.00, 'Available', 2, 'Large event tent'),
('EQ-GAL-006', 5, 'MSR', 'Elixir 2 Backpacking Tent', 2023, 600.00, 2500.00, 'Reserved', 2, 'Lightweight 2-person tent'),
('EQ-GAL-007', 1, 'Hilti', 'TE 7-C Rotary Hammer', 2022, 1000.00, 4000.00, 'Available', 2, 'Cordless rotary hammer'),
-- Colombo Branch Equipment
('EQ-COL-001', 1, 'Milwaukee', 'M18 Impact Driver', 2023, 700.00, 2800.00, 'Available', 3, 'Compact impact driver'),
('EQ-COL-002', 2, 'Craftsman', 'CMXGMAM121302 Riding Mower', 2022, 2500.00, 10000.00, 'Available', 3, '30-inch riding lawn mower'),
('EQ-COL-003', 3, 'Bobcat', 'E32 Compact Excavator', 2021, 18000.00, 60000.00, 'Available', 3, '3.2-ton compact excavator'),
('EQ-COL-004', 4, 'Bose', 'F1 Model 812 PA System', 2023, 3500.00, 12000.00, 'Available', 3, 'Professional PA system'),
('EQ-COL-005', 5, 'Big Agnes', 'Copper Spur HV UL4', 2023, 1000.00, 4000.00, 'Available', 3, 'Ultralight 4-person tent'),
('EQ-COL-006', 1, 'Ryobi', 'ONE+ Combo Kit', 2022, 1200.00, 5000.00, 'Available', 3, '6-tool combo kit'),
('EQ-COL-007', 4, 'Generic', 'Folding Tables (10 pack)', 2021, 800.00, 2000.00, 'Available', 3, 'Event folding tables');

-- Insert Pricing Configuration
INSERT INTO pricing_config (long_rental_days_threshold, long_rental_discount_percentage, max_rental_days) VALUES
(7, 10.0, 30);

-- Insert Sample Reservations
INSERT INTO reservations (reservation_code, equipment_id, customer_id, branch_id, start_date, end_date, status, created_by) VALUES
('RES-2024-001', 13, 2, 2, '2024-01-15', '2024-01-18', 'Active', 3),
('RES-2024-002', 6, 7, 1, '2024-01-20', '2024-01-25', 'Active', 4),
('RES-2024-003', 19, 9, 3, '2024-01-22', '2024-01-29', 'Cancelled', 6);

-- Insert Sample Rentals (including active, returned, and overdue)
INSERT INTO rentals (rental_code, equipment_id, customer_id, branch_id, reservation_id, start_date, end_date, actual_return_date,
                     rental_days, base_rental_amount, long_rental_discount, membership_discount, final_rental_amount,
                     security_deposit, late_fee, damage_charge, damage_description, total_charges, deposit_refund, additional_payment,
                     payment_status, rental_status, created_by, returned_by, returned_at) VALUES
-- Active Rentals
('RNT-2024-001', 5, 1, 1, NULL, '2024-01-10', '2024-01-15', NULL, 5, 12500.00, 0, 0, 12500.00, 10000.00, 0, 0, NULL, 12500.00, 0, 0, 'Paid', 'Active', 4, NULL, NULL),
('RNT-2024-002', 3, 3, 1, NULL, '2024-01-08', '2024-01-15', NULL, 7, 8400.00, 840.00, 756.00, 6804.00, 5000.00, 0, 0, NULL, 6804.00, 0, 0, 'Paid', 'Active', 4, NULL, NULL),

-- Returned Rentals (no damage, on time)
('RNT-2024-003', 1, 4, 1, NULL, '2023-12-20', '2023-12-25', '2023-12-25', 5, 2500.00, 0, 0, 2500.00, 2000.00, 0, 0, NULL, 2500.00, 2000.00, 0, 'Paid', 'Returned', 4, 4, '2023-12-25 16:30:00'),
('RNT-2024-004', 8, 5, 2, NULL, '2023-12-15', '2023-12-22', '2023-12-22', 7, 6300.00, 630.00, 283.50, 5386.50, 3500.00, 0, 0, NULL, 5386.50, 3500.00, 0, 'Paid', 'Returned', 5, 5, '2023-12-22 14:00:00'),

-- Returned with Damages
('RNT-2024-005', 2, 6, 1, NULL, '2023-12-10', '2023-12-15', '2023-12-15', 5, 4000.00, 0, 0, 4000.00, 3000.00, 0, 500.00, 'Minor blade damage', 4500.00, 2500.00, 0, 'Paid', 'Returned', 4, 4, '2023-12-15 11:00:00'),

-- Overdue Rentals
('RNT-2024-006', 4, 8, 1, NULL, '2023-12-28', '2024-01-05', NULL, 8, 4800.00, 480.00, 216.00, 4104.00, 2500.00, 0, 0, NULL, 4104.00, 0, 0, 'Paid', 'Overdue', 4, NULL, NULL),
('RNT-2024-007', 9, 2, 2, NULL, '2023-12-25', '2024-01-02', NULL, 8, 5600.00, 560.00, 252.00, 4788.00, 2800.00, 0, 0, NULL, 4788.00, 0, 0, 'Paid', 'Overdue', 5, NULL, NULL),

-- Long Rental (10 days with discount)
('RNT-2024-008', 7, 10, 1, NULL, '2024-01-05', '2024-01-15', NULL, 10, 15000.00, 1500.00, 1350.00, 12150.00, 6000.00, 0, 0, NULL, 12150.00, 0, 0, 'Paid', 'Active', 4, NULL, NULL),

-- Returned Late with Fee
('RNT-2024-009', 15, 9, 3, NULL, '2023-12-18', '2023-12-25', '2023-12-28', 7, 4900.00, 490.00, 0, 4410.00, 2800.00, 450.00, 0, NULL, 4860.00, 1940.00, 0, 'Paid', 'Returned', 6, 6, '2023-12-28 10:00:00');

-- Update customer deposit held amounts
UPDATE customers SET total_deposit_held = 10000.00 WHERE customer_id = 1;
UPDATE customers SET total_deposit_held = 5000.00 WHERE customer_id = 3;
UPDATE customers SET total_deposit_held = 2500.00 WHERE customer_id = 8;
UPDATE customers SET total_deposit_held = 6000.00 WHERE customer_id = 10;
