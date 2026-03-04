# GearRent Pro - Equipment Rental Management System

A comprehensive desktop application for managing multi-branch equipment rental operations. Built with Java, JavaFX, and MySQL with a strict layered architecture.

## Features

### Core Functionality

- **Multi-Branch Support**: Manage multiple branches (e.g., Panadura, Galle, Colombo)
- **Role-Based Access Control**: Admin, Branch Manager, and Staff roles with appropriate permissions
- **Equipment Management**: Track inventory, categories, pricing, and availability across branches
- **Customer Management**: Handle customer records with membership levels and deposit limits
- **Reservations**: Create, manage, and cancel equipment reservations
- **Rentals**: Process rentals with automatic pricing calculations including:
  - Weekend multipliers
  - Long rental discounts (7+ days)
  - Membership-based discounts
  - Security deposits
- **Returns Processing**: Handle equipment returns with:
  - Late fee calculations
  - Damage charge assessments
  - Security deposit refunds
  - Equipment status updates
- **Overdue Tracking**: Automatically identify and monitor overdue rentals
- **Transaction Management**: Database transactions ensure data consistency

### User Roles

#### Admin
- Manage branches across the organization
- Configure equipment categories and pricing rules
- Set up membership levels and discount percentages
- View data across all branches
- Monitor system-wide operations

#### Branch Manager
- Manage equipment for assigned branch
- Handle rentals and reservations
- Generate branch-level reports
- Oversee staff operations
- Monitor branch performance

#### Staff
- Register and manage customers
- Create reservations
- Process rental transactions
- Handle equipment returns
- Daily operational tasks

## Technology Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 21
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Database Connectivity**: JDBC
- **Password Security**: BCrypt

## Architecture

The application follows a strict layered architecture:

```
┌─────────────────────┐
│   UI Layer          │  JavaFX screens and controls
├─────────────────────┤
│   Controller Layer  │  UI event handlers
├─────────────────────┤
│   Service Layer     │  Business logic and validations
├─────────────────────┤
│   DAO Layer         │  Database operations (JDBC)
├─────────────────────┤
│   Entity Layer      │  Data models
└─────────────────────┘
```

### Package Structure

```
com.gearrentpro/
├── entity/           # Data models (Branch, Equipment, Customer, etc.)
├── dao/              # Data Access Objects with JDBC
├── service/          # Business logic layer
├── ui/               # JavaFX screens
├── util/             # Utility classes (DB connection, Session, Password)
└── Main.java         # Application entry point
```

## Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: Version 3.6 or higher
- **MySQL Server**: Version 8.0 or higher
- **IDE** (Optional): IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## Setup Instructions

### 1. Database Setup

1. Install and start MySQL server
2. Create the database and tables:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
3. Load sample data:
   ```bash
   mysql -u root -p < database/sample_data.sql
   ```

### 2. Configure Database Connection

1. Copy the example configuration file:
   ```bash
   cp src/main/resources/database.properties.example src/main/resources/database.properties
   ```

2. Edit `src/main/resources/database.properties` with your MySQL credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/gearrentpro
   db.username=your_mysql_username
   db.password=your_mysql_password
   db.driver=com.mysql.cj.jdbc.Driver
   ```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn javafx:run
```

Or run the Main class from your IDE:
```
com.gearrentpro.Main
```

## Default Login Credentials

### Admin Account
- Username: `admin`
- Password: `password123`
- Access: Full system access

### Branch Manager (Panadura)
- Username: `manager_pan`
- Password: `password123`
- Access: Panadura branch management

### Branch Manager (Galle)
- Username: `manager_gal`
- Password: `password123`
- Access: Galle branch management

### Staff (Panadura)
- Username: `staff_pan`
- Password: `password123`
- Access: Panadura branch operations

### Staff (Galle)
- Username: `staff_gal`
- Password: `password123`
- Access: Galle branch operations

### Staff (Colombo)
- Username: `staff_col`
- Password: `password123`
- Access: Colombo branch operations

## Database Schema

### Key Tables

- **branches**: Branch locations and details
- **users**: System users with role-based access
- **roles**: User role definitions
- **customers**: Customer information and membership levels
- **membership_levels**: Membership tiers with discount rates
- **equipment_categories**: Equipment types with pricing factors
- **equipment**: Individual equipment items
- **reservations**: Equipment reservations
- **rentals**: Rental transactions with pricing details
- **pricing_config**: System-wide pricing rules

## Business Logic

### Rental Pricing Calculation

```
Daily Price = Base Price × Category Factor × Weekend Multiplier (if weekend)
Total Amount = Sum of all daily prices
Long Rental Discount = Total × 10% (if ≥ 7 days)
Membership Discount = Remaining Total × Membership %
Final Amount = Total - Long Rental Discount - Membership Discount
```

### Return Processing

```
Days Late = Return Date - End Date
Late Fee = Days Late × Category Late Fee Per Day
Total Charges = Rental Amount + Late Fee + Damage Charges

If Total Charges ≤ Security Deposit:
    Refund = Security Deposit - Total Charges
Else:
    Additional Payment = Total Charges - Security Deposit
```

### Validations

- Maximum rental duration: 30 days
- Customer deposit limit: LKR 500,000 (configurable per customer)
- No overlapping rentals/reservations for same equipment
- Equipment availability checks before rental/reservation
- Date validations for all transactions

## Key Features Implementation

### Transaction Management

Critical operations use database transactions:
- Creating rentals (updates equipment status, customer deposits)
- Processing returns (calculates fees, updates multiple tables)
- Converting reservations to rentals
- Cancelling reservations (releases equipment)

### Security

- Passwords hashed using BCrypt
- Role-based menu visibility
- Branch-level data isolation for managers and staff
- Session management for user state

### Data Integrity

- Foreign key constraints
- Status enums for equipment and rentals
- Automatic overdue status detection
- Deposit limit enforcement
- Equipment availability tracking

## Sample Data

The database includes:
- 3 branches (Panadura, Galle, Colombo)
- 5 equipment categories
- 21 equipment items across branches
- 10 sample customers with various membership levels
- Active rentals, reservations, and overdue examples
- 3 membership levels (Regular, Silver, Gold)

## Troubleshooting

### Database Connection Issues

1. Verify MySQL is running:
   ```bash
   mysql -u root -p -e "status"
   ```

2. Check database exists:
   ```bash
   mysql -u root -p -e "SHOW DATABASES LIKE 'gearrentpro';"
   ```

3. Verify credentials in `database.properties`

### JavaFX Issues

If you encounter JavaFX module errors:
```bash
mvn clean install -U
```

### Build Issues

1. Verify Java version:
   ```bash
   java -version
   ```

2. Verify Maven version:
   ```bash
   mvn -version
   ```

3. Clean and rebuild:
   ```bash
   mvn clean install -DskipTests
   ```

## Project Structure

```
gearrent-pro/
├── database/
│   ├── schema.sql           # Database structure
│   └── sample_data.sql      # Test data
├── src/
│   └── main/
│       ├── java/
│       │   └── com/gearrentpro/
│       │       ├── entity/      # Data models
│       │       ├── dao/         # Database access
│       │       ├── service/     # Business logic
│       │       ├── ui/          # JavaFX screens
│       │       ├── util/        # Utilities
│       │       └── Main.java    # Entry point
│       └── resources/
│           └── database.properties.example
├── pom.xml                  # Maven configuration
└── README.md               # This file
```

## Future Enhancements

Potential areas for expansion:
- Branch revenue reports
- Equipment utilization reports
- Customer rental history
- Advanced search and filtering
- Export functionality (PDF, Excel)
- Email notifications for overdue rentals
- Equipment maintenance scheduling
- Barcode scanning integration
- Mobile app integration

## License

This is an educational project for demonstration purposes.

## Contact

For questions or issues, please refer to the project documentation.
