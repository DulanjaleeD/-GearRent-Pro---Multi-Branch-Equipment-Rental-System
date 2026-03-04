# GearRent Pro - Quick Start Guide

## Setup in 5 Minutes

### Step 1: Database Setup (2 minutes)

```bash
# Start MySQL
mysql -u root -p

# Run in MySQL console
source database/schema.sql
source database/sample_data.sql
exit
```

### Step 2: Configure Connection (1 minute)

Edit `src/main/resources/database.properties`:
```properties
db.username=root
db.password=your_mysql_password
```

### Step 3: Run Application (2 minutes)

```bash
mvn clean install
mvn javafx:run
```

## First Login

**Admin Access:**
- Username: `admin`
- Password: `password123`

**Branch Staff:**
- Username: `staff_pan`
- Password: `password123`

## Quick Tour

### As Admin:
1. Click "Branches" → View all branches
2. Click "Equipment Categories" → See pricing configuration
3. Click "Membership Levels" → View discount tiers
4. Click "View All Rentals" → Monitor all transactions

### As Staff:
1. Click "Equipment" → Browse available equipment
2. Click "Customers" → View customer list
3. Click "Reservations" → Create new reservation
4. Click "Rentals" → Process rental transaction
5. Click "Overdue Rentals" → Check late returns

## Key Workflows

### Creating a Reservation
1. Navigate to "Reservations"
2. Click "New Reservation"
3. Select equipment, customer, and dates
4. System validates availability and deposit limits
5. Confirm reservation

### Processing a Rental
1. Navigate to "Rentals"
2. Click "New Rental"
3. Select equipment and customer
4. Choose rental period (max 30 days)
5. System calculates pricing with discounts
6. Process payment and confirm

### Handling Returns
1. Navigate to "Process Return"
2. Search for active rental
3. Record return date
4. Check for damages (if any)
5. System calculates late fees
6. Process deposit refund or collect additional payment

## Sample Data Overview

**Branches:**
- Panadura Branch (PAN001)
- Galle Branch (GAL001)
- Colombo Branch (COL001)

**Equipment Examples:**
- Power tools (drills, saws)
- Garden equipment (mowers, trimmers)
- Construction equipment (excavators)
- Party & event items (tents, sound systems)
- Camping gear (tents, sleeping bags)

**Customers:**
- 10 sample customers
- Various membership levels
- Different deposit limits

**Pricing Rules:**
- Long rental discount: 10% for 7+ days
- Weekend multiplier: 1.2-2.0x depending on category
- Membership discounts: 0%, 5%, 10%
- Late fees: LKR 80-500 per day

## Troubleshooting

**Can't connect to database?**
→ Check MySQL is running and credentials are correct

**JavaFX not found?**
→ Run: `mvn clean install -U`

**No data showing?**
→ Verify sample data was loaded: `mysql -u root -p gearrentpro -e "SELECT COUNT(*) FROM equipment;"`

## Need Help?

See the full README.md for:
- Detailed feature documentation
- Architecture overview
- Complete API reference
- Advanced configuration options
