# Promotion & Loyalty System Specification

# Overview

The restaurant supports two independent discount mechanisms:

1. Membership Benefits (fixed business rules)
2. Event Promotions (manager-created campaigns)

Both mechanisms may apply to the same order.

---

# Part 1: Membership Benefits

## Purpose

Reward loyal customers based on accumulated spending.

Membership benefits are predefined by the restaurant and cannot be created by managers.

---

# Membership Tiers

| Tier    | Required Total Spending | Point Accumulation Rate |
| ------- | ----------------------- | ----------------------- |
| Bronze  | 0 VND                   | 10%                     |
| Silver  | 10,000,000 VND          | 15%                     |
| Gold    | 30,000,000 VND          | 20%                     |
| Diamond | 50,000,000 VND          | 25%                     |

---

# Point Accumulation

Formula:

```text
earned_points = order_total × point_rate
```

Example:

```text
Order Total = 500,000 VND

Bronze (10%)
Earned Points = 50,000
```

Points are credited only after successful payment.

---

# Point Redemption

Customers may use accumulated points to pay for orders.

Rule:

```text
1 Point = 1 VND
```

Maximum redemption:

```text
500,000 VND per order
```

Formula:

```text
redeem_amount = MIN(
    customer_points,
    order_total,
    500,000
)
```

---

# Membership Upgrade

Membership level is automatically updated.

Rules:

```text
0 VND          → Bronze
10,000,000 VND → Silver
30,000,000 VND → Gold
50,000,000 VND → Diamond
```

Evaluation occurs after each completed order.

---

# Membership Database Structure

membership_levels

* membership_id
* level_name
* min_spend
* point_rate
* benefit_description

---

# Part 2: Event Promotions

## Purpose

Allow managers to create temporary marketing campaigns.

Examples:

* Happy Hour
* Buy 2 Get 1 Free
* Holiday Discounts
* Weekend Specials

---

# Promotion Lifecycle

Draft
→ Active
→ Expired
→ Archived

---

# Promotion Structure

event_promotions

* event_id
* name
* description
* promotion_type
* start_date
* end_date
* is_active
* created_by
* created_at

---

# Promotion Types

The system supports configurable promotion templates.

Managers must select one template when creating a promotion.

---

## Type 1: Percentage Discount

Example:

```text
10% OFF
```

Configuration:

* minimum_order_amount
* discount_percentage
* maximum_discount

Example:

```text
Minimum Order = 300,000

Discount = 10%

Maximum Discount = 100,000
```

---

## Type 2: Fixed Amount Discount

Example:

```text
50,000 OFF
```

Configuration:

* minimum_order_amount
* discount_amount

Example:

```text
Spend 500,000
Get 50,000 OFF
```

---

## Type 3: Buy X Get Y Free

Example:

```text
Buy 2 Burgers
Get 1 Burger Free
```

Configuration:

* required_item
* required_quantity
* free_item
* free_quantity

---

## Type 4: Free Gift

Example:

```text
Spend 1,000,000
Receive Dessert
```

Configuration:

* minimum_order_amount
* gift_item

Gift items are added to the order with:

```text
unit_price = 0
```

---

## Type 5: Category Discount

Example:

```text
20% OFF all beverages
```

Configuration:

* category_id
* discount_percentage

---

# Promotion Rules

## Rule 1

Promotion must be active.

```text
start_date <= current_date <= end_date
```

---

## Rule 2

Promotion conditions must be satisfied.

Example:

```text
Minimum spending
Required item quantity
Required category
```

---

## Rule 3

Multiple event promotions may be applied to one order.

Manager can configure:

* Stackable
* Non-stackable

---

## Rule 4

Membership benefits and Event Promotions are calculated separately.

Order Calculation:

Order Total

→ Apply Event Promotion

→ Calculate Final Amount

→ Redeem Membership Points

→ Payment

→ Earn New Membership Points

---

# Promotion Rule Tables

promotion_rules

* rule_id
* event_id
* rule_type

---

percentage_discount_rules

* rule_id
* minimum_order_amount
* discount_percentage
* maximum_discount

---

fixed_discount_rules

* rule_id
* minimum_order_amount
* discount_amount

---

buy_x_get_y_rules

* rule_id
* required_item_id
* required_quantity
* free_item_id
* free_quantity

---

gift_rules

* rule_id
* minimum_order_amount
* gift_item_id

---

category_discount_rules

* rule_id
* category_id
* discount_percentage

---

# Example Calculation

Order Total:

```text
800,000
```

Promotion:

```text
10% OFF
Maximum 100,000
```

Discount:

```text
80,000
```

Customer uses:

```text
50,000 points
```

Final Payment:

```text
800,000
-80,000
-50,000

= 670,000
```

Earned Points (Silver 15%):

```text
670,000 × 15%

= 100,500 points
```
