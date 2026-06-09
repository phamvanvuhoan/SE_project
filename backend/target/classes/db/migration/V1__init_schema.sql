-- V1__init_schema.sql
-- Flyway migration script for Restaurant POS & Management System (PostgreSQL)

-- 1. Employees Table
CREATE TABLE employees (
    employee_id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('STAFF', 'MANAGER')),
    phone VARCHAR(20) UNIQUE,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL
);

-- 2. Tables Table
CREATE TABLE tables (
    table_id UUID PRIMARY KEY,
    table_number VARCHAR(20) UNIQUE NOT NULL,
    capacity INTEGER NOT NULL CHECK (capacity > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED')),
    location VARCHAR(100)
);

-- 3. Membership Levels Table
CREATE TABLE membership_levels (
    membership_id UUID PRIMARY KEY,
    level_name VARCHAR(20) UNIQUE NOT NULL,
    min_spend DECIMAL(12,2) NOT NULL CHECK (min_spend >= 0),
    point_rate DECIMAL(5,2) NOT NULL CHECK (point_rate >= 0),
    benefit_description TEXT
);

-- 4. Customers Table
CREATE TABLE customers (
    customer_id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    membership_id UUID REFERENCES membership_levels(membership_id),
    total_points INTEGER DEFAULT 0 NOT NULL CHECK (total_points >= 0),
    total_spent DECIMAL(12,2) DEFAULT 0.00 NOT NULL CHECK (total_spent >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 5. Categories Table
CREATE TABLE categories (
    category_id UUID PRIMARY KEY,
    category_name VARCHAR(50) UNIQUE NOT NULL
);

-- 6. Menu Items (Dishes) Table
CREATE TABLE menu_items (
    dish_id UUID PRIMARY KEY,
    category_id UUID NOT NULL REFERENCES categories(category_id),
    dish_name VARCHAR(100) NOT NULL,
    price DECIMAL(12,2) NOT NULL CHECK (price >= 0),
    is_available BOOLEAN DEFAULT TRUE NOT NULL,
    description TEXT
);

-- 7. Orders Table
CREATE TABLE orders (
    order_id UUID PRIMARY KEY,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'PREPARING', 'READY', 'SERVED', 'COMPLETED', 'CANCELLED')),
    subtotal DECIMAL(12,2) DEFAULT 0.00 NOT NULL CHECK (subtotal >= 0),
    promotion_discount DECIMAL(12,2) DEFAULT 0.00 NOT NULL CHECK (promotion_discount >= 0),
    point_discount DECIMAL(12,2) DEFAULT 0.00 NOT NULL CHECK (point_discount >= 0),
    total_amount DECIMAL(12,2) DEFAULT 0.00 NOT NULL CHECK (total_amount >= 0),
    table_id UUID REFERENCES tables(table_id) ON DELETE SET NULL,
    employee_id UUID NOT NULL REFERENCES employees(employee_id),
    customer_id UUID REFERENCES customers(customer_id) ON DELETE SET NULL,
    notes TEXT,
    version INTEGER DEFAULT 0 NOT NULL -- For optimistic locking
);

-- 8. Order Items Table
CREATE TABLE order_items (
    order_item_id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    dish_id UUID NOT NULL REFERENCES menu_items(dish_id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(12,2) NOT NULL CHECK (unit_price >= 0),
    subtotal DECIMAL(12,2) NOT NULL CHECK (subtotal >= 0),
    promotional_item BOOLEAN DEFAULT FALSE NOT NULL
);

-- 9. Event Promotions Table
CREATE TABLE event_promotions (
    event_id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    event_type VARCHAR(20) NOT NULL CHECK (event_type IN ('DISCOUNT', 'PROMOTION')),
    promotion_type VARCHAR(50) NOT NULL CHECK (promotion_type IN ('PERCENTAGE', 'FIXED', 'BUY_X_GET_Y', 'GIFT', 'CATEGORY')),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    is_stackable BOOLEAN DEFAULT FALSE NOT NULL,
    created_by UUID REFERENCES employees(employee_id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_dates CHECK (start_date <= end_date)
);

-- 10. Order Event Associative Table
CREATE TABLE order_events (
    order_id UUID NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    event_id UUID NOT NULL REFERENCES event_promotions(event_id) ON DELETE CASCADE,
    discount_amount DECIMAL(12,2) DEFAULT 0.00 NOT NULL CHECK (discount_amount >= 0),
    PRIMARY KEY (order_id, event_id)
);

-- 11. Payments Table
CREATE TABLE payments (
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('CASH', 'CARD', 'QR', 'POINTS')),
    amount DECIMAL(12,2) NOT NULL CHECK (amount > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    paid_at TIMESTAMP
);

-- 12. Base Promotion Rules Table
CREATE TABLE promotion_rules (
    rule_id UUID PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES event_promotions(event_id) ON DELETE CASCADE,
    rule_type VARCHAR(50) NOT NULL CHECK (rule_type IN ('PERCENTAGE', 'FIXED', 'BUY_X_GET_Y', 'GIFT', 'CATEGORY'))
);

-- 13. Percentage Discount Rules Table
CREATE TABLE percentage_discount_rules (
    rule_id UUID PRIMARY KEY REFERENCES promotion_rules(rule_id) ON DELETE CASCADE,
    minimum_order_amount DECIMAL(12,2) NOT NULL CHECK (minimum_order_amount >= 0),
    discount_percentage DECIMAL(5,2) NOT NULL CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
    maximum_discount DECIMAL(12,2) CHECK (maximum_discount >= 0)
);

-- 14. Fixed Discount Rules Table
CREATE TABLE fixed_discount_rules (
    rule_id UUID PRIMARY KEY REFERENCES promotion_rules(rule_id) ON DELETE CASCADE,
    minimum_order_amount DECIMAL(12,2) NOT NULL CHECK (minimum_order_amount >= 0),
    discount_amount DECIMAL(12,2) NOT NULL CHECK (discount_amount >= 0)
);

-- 15. Buy X Get Y Rules Table
CREATE TABLE buy_x_get_y_rules (
    rule_id UUID PRIMARY KEY REFERENCES promotion_rules(rule_id) ON DELETE CASCADE,
    required_item_id UUID NOT NULL REFERENCES menu_items(dish_id),
    required_quantity INTEGER NOT NULL CHECK (required_quantity > 0),
    free_item_id UUID NOT NULL REFERENCES menu_items(dish_id),
    free_quantity INTEGER NOT NULL CHECK (free_quantity > 0)
);

-- 16. Gift Rules Table
CREATE TABLE gift_rules (
    rule_id UUID PRIMARY KEY REFERENCES promotion_rules(rule_id) ON DELETE CASCADE,
    minimum_order_amount DECIMAL(12,2) NOT NULL CHECK (minimum_order_amount >= 0),
    gift_item_id UUID NOT NULL REFERENCES menu_items(dish_id)
);

-- 17. Category Discount Rules Table
CREATE TABLE category_discount_rules (
    rule_id UUID PRIMARY KEY REFERENCES promotion_rules(rule_id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(category_id),
    discount_percentage DECIMAL(5,2) NOT NULL CHECK (discount_percentage >= 0 AND discount_percentage <= 100)
);

-- Indexes for performance
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_employee ON orders(employee_id);
CREATE INDEX idx_orders_table ON orders(table_id);
CREATE INDEX idx_menu_items_category ON menu_items(category_id);
CREATE INDEX idx_payments_order ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_method ON payments(payment_method);
CREATE INDEX idx_promotion_rules_event ON promotion_rules(event_id);
CREATE INDEX idx_event_promotions_active_dates ON event_promotions(is_active, start_date, end_date);
