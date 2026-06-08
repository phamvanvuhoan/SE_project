# Restaurant POS System - API Design Guidelines

# Purpose

This document defines high-level API architecture and design rules.

The goal is to ensure all generated APIs remain consistent across the system.

---

# Architecture Style

The backend follows:

```text
REST API
```

Pattern:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Business logic must reside in the Service layer.

Controllers should only:

* validate requests
* call services
* return responses

Repositories must not contain business logic.

---

# API Base URL

```text
/api/v1
```

Example:

```text
/api/v1/orders
/api/v1/customers
/api/v1/menu-items
```

---

# Standard Response Format

Success:

```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {}
}
```

Error:

```json
{
  "success": false,
  "message": "Customer not found",
  "errors": []
}
```

---

# Authentication

Login endpoint:

```http
POST /auth/login
```

Authentication method:

```text
JWT
```

Roles:

```text
STAFF
MANAGER
```

---

# Authorization Rules

## STAFF

Allowed:

* Manage Orders
* Manage Order Items
* View Menu
* View Customer Information
* Process Payments
* Redeem Membership Points

Not Allowed:

* Create Promotions
* Delete Promotions
* View Revenue Reports
* Manage Employees

---

## MANAGER

Allowed:

* All STAFF permissions
* Manage Promotions
* Manage Employees
* Manage Membership Settings
* View Reports
* View Analytics

---

# Resource Design

Every entity follows standard CRUD endpoints.

Pattern:

```http
GET    /resource
GET    /resource/{id}

POST   /resource

PUT    /resource/{id}

DELETE /resource/{id}
```

---

# Employee APIs

Resource:

```text
employees
```

Operations:

* Create Employee
* Update Employee
* Disable Employee
* Get Employee Details
* List Employees

---

# Table APIs

Resource:

```text
tables
```

Operations:

* Create Table
* Update Table
* Change Table Status
* View Table Availability

---

# Customer APIs

Resource:

```text
customers
```

Operations:

* Create Customer
* Update Customer
* View Membership Information
* View Point Balance
* View Order History

---

# Membership APIs

Resource:

```text
membership-levels
```

Operations:

* List Levels
* Update Benefits
* Update Point Rates

Only Managers may modify membership settings.

---

# Menu APIs

Resource:

```text
menu-items
```

Operations:

* Create Menu Item
* Update Menu Item
* Delete Menu Item
* Change Availability
* Search Menu Item

---

# Category APIs

Resource:

```text
categories
```

Operations:

* Create Category
* Update Category
* Delete Category
* List Categories

---

# Order APIs

Resource:

```text
orders
```

Orders are the core business entity.

---

## Create Order

Creates a new order.

Supports:

* Dine In
* Takeaway

---

## Add Item

Adds menu item into order.

Business validation:

* Menu item must exist.
* Menu item must be available.

---

## Remove Item

Removes item from order.

---

## Update Quantity

Updates quantity of existing item.

---

## Submit Order

Changes status:

```text
PENDING
→ PREPARING
```

---

## Complete Order

Changes status:

```text
PREPARING
→ READY
→ COMPLETED
```

---

## Cancel Order

Allowed only when:

```text
PENDING
PREPARING
```

Not allowed:

```text
COMPLETED
```

---

# Order State Machine

Valid transitions:

```text
PENDING
    ├── PREPARING
    └── CANCELLED

PREPARING
    ├── READY
    └── CANCELLED

READY
    └── COMPLETED

COMPLETED
    └── TERMINAL

CANCELLED
    └── TERMINAL
```

Invalid transitions must be rejected.

---

# Promotion APIs

Resource:

```text
promotions
```

Managers only.

Operations:

* Create Promotion
* Activate Promotion
* Deactivate Promotion
* Archive Promotion
* Update Promotion
* List Promotions

Promotion templates:

* Percentage Discount
* Fixed Discount
* Buy X Get Y
* Free Gift
* Category Discount

---

# Payment APIs

Resource:

```text
payments
```

Operations:

* Create Payment
* Process Payment
* Refund Payment
* View Payment Details

Supported methods:

```text
CASH
CARD
QR
POINTS
```

---

# Order Calculation Rules

Order calculation sequence:

```text
1. Calculate Item Subtotal

2. Calculate Order Total

3. Apply Event Promotions

4. Redeem Membership Points

5. Generate Final Amount

6. Process Payment

7. Earn Membership Points

8. Update Membership Level
```

This order must never be changed.

---

# Reporting APIs

Resource:

```text
reports
```

Manager only.

Supported reports:

* Daily Revenue
* Monthly Revenue
* Top Selling Dishes
* Membership Statistics
* Promotion Usage Statistics
* Order Statistics

---

# Search APIs

Every major resource should support:

```http
GET /resource
```

with filters:

```text
page
size
sort

keyword

status

dateFrom
dateTo
```

Example:

GET /orders?status=COMPLETED&page=1&size=20

---

# Soft Delete Policy

Use soft delete whenever possible.

Fields:

```text
is_active
deleted_at
```

Avoid physical deletion of:

* Orders
* Payments
* Customers

for auditing purposes.

---

# AI Implementation Rules

The code generator must follow:

1. One Service per Resource.
2. One Repository per Entity.
3. DTOs for all requests/responses.
4. No business logic inside Controllers.
5. Use database transactions for:

   * Order Creation
   * Payment Processing
   * Membership Point Updates
   * Promotion Application
6. Validate all foreign keys before persistence.
7. Never trust client-side calculations.
8. Recalculate totals on the server.
9. Use optimistic locking for Order updates.
10. Log all payment operations.

```
```
