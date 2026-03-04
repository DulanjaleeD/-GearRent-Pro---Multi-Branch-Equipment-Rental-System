# GearRent Pro - Architecture Documentation

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         UI Layer (JavaFX)                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ LoginScreen  │  │  Dashboard   │  │ Management   │         │
│  │              │  │   Screen     │  │   Screens    │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Service Layer                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ AuthService  │  │RentalService │  │Reservation   │         │
│  │              │  │              │  │  Service     │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
│         │                  │                  │                 │
│         │   Business Logic & Validation       │                 │
│         │   Pricing Calculations              │                 │
│         │   Transaction Management            │                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        DAO Layer (JDBC)                          │
│  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  │
│  │ User │  │Branch│  │Equip │  │Rental│  │Reserv│  │Custom│  │
│  │ DAO  │  │ DAO  │  │ DAO  │  │ DAO  │  │ DAO  │  │ DAO  │  │
│  └──────┘  └──────┘  └──────┘  └──────┘  └──────┘  └──────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Database Connection                         │
│                      (DatabaseConnection)                        │
│                     Connection Pool Management                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      MySQL Database                              │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Tables: users, branches, equipment, customers,          │  │
│  │          rentals, reservations, categories, etc.         │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. UI Layer (JavaFX)
**Purpose**: User interaction and presentation
- Display data in tables and forms
- Handle user input events
- Navigate between screens
- Show error messages and confirmations
- Role-based UI rendering

**Key Classes**:
- `LoginScreen`: Authentication interface
- `DashboardScreen`: Main navigation hub
- `EquipmentListScreen`, `RentalListScreen`, etc.: Data display screens

### 2. Service Layer
**Purpose**: Business logic and orchestration
- Validate business rules
- Calculate pricing and discounts
- Manage transactions
- Coordinate multiple DAO operations
- Apply security policies

**Key Classes**:
- `AuthService`: Authentication and session management
- `RentalService`: Rental lifecycle management
- `ReservationService`: Reservation handling

**Business Logic Examples**:
```java
// Pricing calculation
basePrice × categoryFactor × weekendMultiplier
- longRentalDiscount (if ≥ 7 days)
- membershipDiscount
= finalPrice

// Late fee calculation
daysLate × categoryLateFeePerDay = totalLateFee

// Deposit refund
if (totalCharges <= securityDeposit)
    refund = securityDeposit - totalCharges
else
    additionalPayment = totalCharges - securityDeposit
```

### 3. DAO Layer (Data Access Objects)
**Purpose**: Database operations via JDBC
- Execute SQL queries
- Map ResultSet to entities
- Handle prepared statements
- Participate in transactions
- Prevent SQL injection

**Key Classes**:
- `UserDAO`: User CRUD operations
- `EquipmentDAO`: Equipment management
- `RentalDAO`: Rental data access
- `CustomerDAO`: Customer operations
- `ReservationDAO`: Reservation queries

**Transaction Example**:
```java
Connection conn = null;
try {
    conn = getConnection();
    conn.setAutoCommit(false);

    // Multiple database operations
    rentalDAO.create(rental, conn);
    equipmentDAO.updateStatus(equipmentId, RENTED);
    customerDAO.updateDeposit(customerId, newAmount);

    conn.commit();
} catch (Exception e) {
    conn.rollback();
    throw e;
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

### 4. Entity Layer
**Purpose**: Data models
- Represent database tables
- Encapsulate data with getters/setters
- Type-safe data handling
- Enum-based status values

**Key Classes**:
- `User`, `Branch`, `Customer`
- `Equipment`, `EquipmentCategory`
- `Rental`, `Reservation`
- `MembershipLevel`, `PricingConfig`

### 5. Utility Layer
**Purpose**: Cross-cutting concerns
- Database connection management
- Session management
- Password hashing
- Shared utilities

**Key Classes**:
- `DatabaseConnection`: JDBC connection singleton
- `SessionManager`: User session state
- `PasswordUtil`: BCrypt password operations

## Data Flow Examples

### 1. User Login Flow
```
User enters credentials
        ↓
LoginScreen captures input
        ↓
AuthService.login(username, password)
        ↓
UserDAO.findByUsername(username)
        ↓
Database query
        ↓
User entity returned
        ↓
PasswordUtil.checkPassword()
        ↓
SessionManager.setCurrentUser()
        ↓
Navigate to DashboardScreen
```

### 2. Create Rental Flow
```
User selects equipment and customer
        ↓
UI collects dates and details
        ↓
RentalService.calculateRentalCost()
        ├─ EquipmentDAO.findById()
        ├─ CustomerDAO.findById()
        ├─ MembershipLevelDAO.findById()
        └─ PricingConfigDAO.getConfig()
        ↓
Calculate pricing with discounts
        ↓
Display calculated cost
        ↓
User confirms
        ↓
RentalService.createRental()
        ├─ Begin Transaction
        ├─ RentalDAO.create()
        ├─ EquipmentDAO.updateStatus()
        ├─ CustomerDAO.updateDeposit()
        ├─ ReservationDAO.updateStatus() (if from reservation)
        └─ Commit Transaction
        ↓
Success confirmation
```

### 3. Process Return Flow
```
User searches for active rental
        ↓
RentalListScreen displays rentals
        ↓
User selects rental to return
        ↓
ReturnProcessScreen collects:
    - Return date
    - Damage info
    - Damage charges
        ↓
RentalService.processReturn()
        ├─ Calculate late fees
        ├─ Calculate total charges
        ├─ Calculate refund/additional payment
        ├─ Begin Transaction
        ├─ RentalDAO.processReturn()
        ├─ EquipmentDAO.updateStatus()
        ├─ CustomerDAO.updateDeposit()
        └─ Commit Transaction
        ↓
Display final charges and refund
```

## Security Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Security Layers                          │
├─────────────────────────────────────────────────────────────┤
│  1. Authentication                                           │
│     - BCrypt password hashing                                │
│     - Secure login validation                                │
│     - Session management                                     │
├─────────────────────────────────────────────────────────────┤
│  2. Authorization                                            │
│     - Role-based access control                              │
│     - Branch-level data isolation                            │
│     - Menu visibility control                                │
├─────────────────────────────────────────────────────────────┤
│  3. Data Security                                            │
│     - Prepared statements (SQL injection prevention)         │
│     - Input validation                                       │
│     - Transaction isolation                                  │
├─────────────────────────────────────────────────────────────┤
│  4. Application Security                                     │
│     - Session timeout handling                               │
│     - Secure configuration management                        │
│     - Error handling without information leakage             │
└─────────────────────────────────────────────────────────────┘
```

## Database Schema Relationships

```
┌──────────┐         ┌──────────┐         ┌──────────┐
│  roles   │◄────────│  users   │────────►│ branches │
└──────────┘         └──────────┘         └──────────┘
                           │
                           │ (created_by)
                           │
                           ▼
                     ┌──────────────┐
                     │ reservations │
                     └──────────────┘
                           │
                           │ (optional link)
                           ▼
┌──────────┐         ┌──────────┐         ┌──────────┐
│customers │◄────────│ rentals  │────────►│equipment │
└──────────┘         └──────────┘         └──────────┘
     │                     │                     │
     │                     │                     │
     ▼                     ▼                     ▼
┌──────────┐         ┌──────────┐         ┌──────────┐
│membership│         │ branches │         │categories│
│ _levels  │         └──────────┘         └──────────┘
└──────────┘
```

## Transaction Boundaries

### Critical Operations with Transactions

1. **Create Rental**
   ```
   BEGIN TRANSACTION
       INSERT INTO rentals
       UPDATE equipment SET status = 'Rented'
       UPDATE customers SET total_deposit_held
       UPDATE reservations SET status = 'Converted' (if applicable)
   COMMIT
   ```

2. **Process Return**
   ```
   BEGIN TRANSACTION
       UPDATE rentals (return date, charges, refund)
       UPDATE equipment SET status = 'Available' or 'Under Maintenance'
       UPDATE customers SET total_deposit_held (reduce)
   COMMIT
   ```

3. **Create Reservation**
   ```
   BEGIN TRANSACTION
       INSERT INTO reservations
       UPDATE equipment SET status = 'Reserved'
   COMMIT
   ```

4. **Cancel Reservation**
   ```
   BEGIN TRANSACTION
       UPDATE reservations SET status = 'Cancelled'
       UPDATE equipment SET status = 'Available'
   COMMIT
   ```

## Performance Optimization

### Database Indexes
```sql
-- Frequently queried columns
CREATE INDEX idx_equipment_status ON equipment(status);
CREATE INDEX idx_equipment_branch ON equipment(branch_id);
CREATE INDEX idx_rentals_status ON rentals(rental_status);
CREATE INDEX idx_rentals_dates ON rentals(start_date, end_date);
CREATE INDEX idx_customers_membership ON customers(membership_level_id);
```

### Query Optimization
- Use JOIN instead of multiple queries
- Select only required columns
- Use prepared statements for parameter binding
- Implement connection pooling

## Error Handling Strategy

```
┌─────────────────────────────────────────────────────────────┐
│                     Error Handling Flow                      │
├─────────────────────────────────────────────────────────────┤
│  UI Layer:         Display user-friendly messages           │
│                    Show Alert dialogs                        │
│                    Log errors to console                     │
├─────────────────────────────────────────────────────────────┤
│  Service Layer:    Catch and wrap exceptions                │
│                    Rollback transactions                     │
│                    Throw business exceptions                 │
├─────────────────────────────────────────────────────────────┤
│  DAO Layer:        Handle SQL exceptions                     │
│                    Close resources properly                  │
│                    Propagate errors to service               │
└─────────────────────────────────────────────────────────────┘
```

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                  Client Machine                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         GearRent Pro Application                       │ │
│  │         (JavaFX Desktop App)                           │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ JDBC Connection
                          │ (jdbc:mysql://...)
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                  Database Server                             │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         MySQL Server 8.0                               │ │
│  │         Database: gearrentpro                          │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Scalability Considerations

### Current Architecture
- Single database server
- Desktop application per user
- Direct JDBC connections

### Future Scalability Options
1. **Web-based UI**: Convert to web application with REST API
2. **Microservices**: Split services into independent microservices
3. **Database Replication**: Master-slave for read scalability
4. **Caching Layer**: Redis/Memcached for frequently accessed data
5. **Load Balancing**: Multiple application servers with load balancer

## Technology Decisions

| Component | Technology | Rationale |
|-----------|-----------|-----------|
| Language | Java 17 | Modern LTS version, robust ecosystem |
| UI Framework | JavaFX 21 | Modern desktop UI, rich controls |
| Database | MySQL 8.0 | Reliable, widely used, good performance |
| Build Tool | Maven | Industry standard, dependency management |
| Password Hashing | BCrypt | Industry standard, secure salting |
| Database Access | JDBC | Direct control, no ORM overhead |

## Design Principles Applied

1. **Separation of Concerns**: Clear layer boundaries
2. **Single Responsibility**: Each class has one purpose
3. **DRY (Don't Repeat Yourself)**: Shared utilities and base classes
4. **SOLID Principles**: Maintainable and extensible code
5. **Fail Fast**: Early validation and error detection
6. **Transaction Integrity**: ACID compliance for critical operations
7. **Security by Design**: Built-in authentication and authorization
8. **User-Centric Design**: Intuitive UI with role-based features
