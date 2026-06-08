# Restaurant POS System - Database Design (ERD Specification)

## Overview

This document defines the database schema for the Restaurant POS & Management System.

Database Engine: PostgreSQL (already installed and working on this device)

---

# 1. Employee

Stores employee accounts used to access the POS system.

| Column        | Type         | Constraints      |
| ------------- | ------------ | ---------------- |
| employee_id   | UUID         | PK               |
| name          | VARCHAR(100) | NOT NULL         |
| role          | VARCHAR(20)  | NOT NULL         |
| phone         | VARCHAR(20)  | UNIQUE           |
| username      | VARCHAR(50)  | UNIQUE, NOT NULL |
| password_hash | VARCHAR(255) | NOT NULL         |
| is_active     | BOOLEAN      | DEFAULT TRUE     |

### Role Values

* STAFF
* MANAGER

---

# 2. Tables

Stores restaurant table information.

| Column       | Type         | Constraints |
| ------------ | ------------ | ----------- |
| table_id     | UUID         | PK          |
| table_number | VARCHAR(20)  | UNIQUE      |
| capacity     | INTEGER      | NOT NULL    |
| status       | VARCHAR(20)  | NOT NULL    |
| location     | VARCHAR(100) | NULL        |

### Status Values

* AVAILABLE
* OCCUPIED
* RESERVED

---

# 3. Membership Levels

Defines customer membership tiers.

| Column              | Type          | Constraints |
| ------------------- | ------------- | ----------- |
| membership_id       | UUID          | PK          |
| level_name          | VARCHAR(20)   | UNIQUE      |
| min_spend           | DECIMAL(12,2) | NOT NULL    |
| point_rate          | DECIMAL(5,2)  | NOT NULL    |
| benefit_description | TEXT          | NULL        |

### Example Levels

* Bronze
* Silver
* Gold

---

# 4. Customers

Stores customer information.

| Column        | Type          | Constraints               |
| ------------- | ------------- | ------------------------- |
| customer_id   | UUID          | PK                        |
| name          | VARCHAR(100)  | NOT NULL                  |
| phone         | VARCHAR(20)   | UNIQUE                    |
| email         | VARCHAR(100)  | UNIQUE                    |
| membership_id | UUID          | FK → membership_levels    |
| total_points  | INTEGER       | DEFAULT 0                 |
| total_spent   | DECIMAL(12,2) | DEFAULT 0                 |
| created_at    | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP |

### Relationships

* One Membership Level can have many Customers.
* One Customer can create many Orders.

---

# 5. Categories

Stores menu categories.

| Column        | Type        | Constraints |
| ------------- | ----------- | ----------- |
| category_id   | UUID        | PK          |
| category_name | VARCHAR(50) | UNIQUE      |

### Examples

* Appetizer
* Main Course
* Dessert
* Beverage

---

# 6. Menu Items

Stores dishes and beverages available for sale.

| Column       | Type          | Constraints     |
| ------------ | ------------- | --------------- |
| dish_id      | UUID          | PK              |
| category_id  | UUID          | FK → categories |
| dish_name    | VARCHAR(100)  | NOT NULL        |
| price        | DECIMAL(12,2) | NOT NULL        |
| is_available | BOOLEAN       | DEFAULT TRUE    |
| description  | TEXT          | NULL            |

### Relationships

* One Category contains many Menu Items.

---

# 7. Orders

Stores customer orders.

| Column       | Type          | Constraints               |
| ------------ | ------------- | ------------------------- |
| order_id     | UUID          | PK                        |
| order_time   | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP |
| status       | VARCHAR(20)   | NOT NULL                  |
| total_amount | DECIMAL(12,2) | NOT NULL                  |
| table_id     | UUID          | FK → tables, NULL         |
| employee_id  | UUID          | FK → employees            |
| customer_id  | UUID          | FK → customers, NULL      |
| notes        | TEXT          | NULL                      |

### Status Values

* PENDING
* PREPARING
* READY
* SERVED
* COMPLETED
* CANCELLED

### Relationships

* One Employee creates many Orders.
* One Customer places many Orders.
* One Table is associated with many Orders.
* One Order contains many Order Items.

---

# 8. Order Items

Stores items within an order.

| Column        | Type          | Constraints     |
| ------------- | ------------- | --------------- |
| order_item_id | UUID          | PK              |
| order_id      | UUID          | FK → orders     |
| dish_id       | UUID          | FK → menu_items |
| quantity      | INTEGER       | NOT NULL        |
| unit_price    | DECIMAL(12,2) | NOT NULL        |
| subtotal      | DECIMAL(12,2) | NOT NULL        |

### Relationships

* One Order contains many Order Items.
* One Menu Item can appear in many Order Items.

---

# 9. Event Promotions

Stores promotions and discounts.

| Column      | Type         | Constraints  |
| ----------- | ------------ | ------------ |
| event_id    | UUID         | PK           |
| name        | VARCHAR(100) | NOT NULL     |
| event_type  | VARCHAR(20)  | NOT NULL     |
| description | TEXT         | NULL         |
| start_date  | TIMESTAMP    | NOT NULL     |
| end_date    | TIMESTAMP    | NOT NULL     |
| is_active   | BOOLEAN      | DEFAULT TRUE |

### Event Types

* DISCOUNT
* PROMOTION

---

# 10. Order Event

Associative table for many-to-many relationship between Orders and Promotions.

| Column          | Type          | Constraints               |
| --------------- | ------------- | ------------------------- |
| order_id        | UUID          | PK, FK → orders           |
| event_id        | UUID          | PK, FK → event_promotions |
| discount_amount | DECIMAL(12,2) | DEFAULT 0                 |

### Relationships

* One Order may use multiple Promotions.
* One Promotion may apply to multiple Orders.

---

# 11. Payments

Stores payment transactions.

| Column         | Type          | Constraints |
| -------------- | ------------- | ----------- |
| payment_id     | UUID          | PK          |
| order_id       | UUID          | FK → orders |
| payment_method | VARCHAR(20)   | NOT NULL    |
| amount         | DECIMAL(12,2) | NOT NULL    |
| status         | VARCHAR(20)   | NOT NULL    |
| paid_at        | TIMESTAMP     | NULL        |

### Payment Methods

* CASH
* CARD
* QR
* POINTS

### Payment Status

* PENDING
* COMPLETED
* FAILED
* REFUNDED

---

# Business Rules

## BR-01

An Order must contain at least one Order Item.

## BR-02

An Order may optionally be associated with a Customer.

## BR-03

Takeaway Orders do not require a Table.

## BR-04

Only active Menu Items can be added to Orders.

## BR-05

Promotion validity must satisfy:

start_date <= current_date <= end_date

## BR-06

Customer points are accumulated after successful payment.

## BR-07

Membership level is determined by total spending.

Bronze → Silver → Gold

based on configurable thresholds.

---

# Relationship Summary

Employee (1) ------ (N) Orders

Customer (1) ------ (N) Orders

Membership Level (1) ------ (N) Customers

Table (1) ------ (N) Orders

Category (1) ------ (N) Menu Items

Order (1) ------ (N) Order Items

Menu Item (1) ------ (N) Order Items

Order (N) ------ (N) Promotions

Order (1) ------ (1..N) Payments
