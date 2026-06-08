# Implementation Plan - POS Backend Schema, Entities, and Repositories

This plan details the design and generation of the relational database schema, Java persistence objects (JPA entities), and Spring Data JPA repositories based on the architecture files in the `architecture` folder.

## Proposed Changes

We will introduce a Java/Kotlin-compatible directory structure in the `backend` folder in the project root. The package name will be `com.restaurant.pos`.

### Database & Migrations

#### [NEW] [V1__init_schema.sql](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/resources/db/migration/V1__init_schema.sql)
A Flyway migration script containing:
* Tables creation (`employees`, `tables`, `membership_levels`, `customers`, `categories`, `menu_items`, `orders`, `order_items`, `event_promotions`, `order_events`, `payments`, `promotion_rules`, `percentage_discount_rules`, `fixed_discount_rules`, `buy_x_get_y_rules`, `gift_rules`, `category_discount_rules`).
* Foreign key constraints, unique constraints, and indexes.
* Enum check constraints for roles, statuses, and types.

---

### JPA Entities (`com.restaurant.pos.entity`)

#### [NEW] [Employee.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/Employee.java)
* Attributes: `id` (UUID), `name`, `role` (enum: STAFF, MANAGER), `phone`, `username`, `passwordHash`, `isActive`.
* Relationships: `@OneToMany` to `Order`.

#### [NEW] [RestaurantTable.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/RestaurantTable.java)
* Attributes: `id` (UUID), `tableNumber`, `capacity`, `status` (enum: AVAILABLE, OCCUPIED, RESERVED), `location`.

#### [NEW] [MembershipLevel.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/MembershipLevel.java)
* Attributes: `id` (UUID), `levelName`, `minSpend`, `pointRate`, `benefitDescription`.

#### [NEW] [Customer.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/Customer.java)
* Attributes: `id` (UUID), `name`, `phone`, `email`, `membershipLevel` (FK), `totalPoints`, `totalSpent`, `createdAt`.

#### [NEW] [Category.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/Category.java)
* Attributes: `id` (UUID), `name`.

#### [NEW] [MenuItem.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/MenuItem.java)
* Attributes: `id` (UUID), `category` (FK), `dishName`, `price`, `isAvailable`, `description`.

#### [NEW] [Order.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/Order.java)
* Attributes: `id` (UUID), `orderTime`, `status` (enum: PENDING, PREPARING, READY, SERVED, COMPLETED, CANCELLED), `totalAmount`, `table` (FK, nullable), `employee` (FK), `customer` (FK, nullable), `notes`.
* Note: Utilizes `@Version` for optimistic locking.

#### [NEW] [OrderItem.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/OrderItem.java)
* Attributes: `id` (UUID), `order` (FK), `menuItem` (FK), `quantity`, `unitPrice`, `subtotal`.

#### [NEW] [EventPromotion.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/EventPromotion.java)
* Attributes: `id` (UUID), `name`, `description`, `eventType` (enum: DISCOUNT, PROMOTION), `promotionType`, `startDate`, `endDate`, `isActive`, `createdBy` (FK), `createdAt`.

#### [NEW] [OrderEvent.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/OrderEvent.java)
* Attributes: Composite PK (`orderId`, `eventId`), `order` (FK), `event` (FK), `discountAmount`.

#### [NEW] [Payment.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/Payment.java)
* Attributes: `id` (UUID), `order` (FK), `paymentMethod` (enum: CASH, CARD, QR, POINTS), `amount`, `status` (enum: PENDING, COMPLETED, FAILED, REFUNDED), `paidAt`.

#### [NEW] [PromotionRule.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/PromotionRule.java)
* Base class using `@Inheritance(strategy = InheritanceType.JOINED)`.
* Attributes: `id` (UUID), `eventPromotion` (FK), `ruleType`.

#### [NEW] [PercentageDiscountRule.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/PercentageDiscountRule.java)
* Extends `PromotionRule`.
* Attributes: `minimumOrderAmount`, `discountPercentage`, `maximumDiscount`.

#### [NEW] [FixedDiscountRule.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/FixedDiscountRule.java)
* Extends `PromotionRule`.
* Attributes: `minimumOrderAmount`, `discountAmount`.

#### [NEW] [BuyXGetYRule.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/BuyXGetYRule.java)
* Extends `PromotionRule`.
* Attributes: `requiredItem` (FK), `requiredQuantity`, `freeItem` (FK), `freeQuantity`.

#### [NEW] [GiftRule.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/GiftRule.java)
* Extends `PromotionRule`.
* Attributes: `minimumOrderAmount`, `giftItem` (FK).

#### [NEW] [CategoryDiscountRule.java](file:///home/phamvanvuhoan/Documents/SE%20project/se_project/backend/src/main/java/com/restaurant/pos/entity/CategoryDiscountRule.java)
* Extends `PromotionRule`.
* Attributes: `category` (FK), `discountPercentage`.

---

### Spring Data JPA Repositories (`com.restaurant.pos.repository`)

We will create Spring Data JPA interfaces extending `JpaRepository<Entity, UUID>`:
* `EmployeeRepository.java`
* `RestaurantTableRepository.java`
* `MembershipLevelRepository.java`
* `CustomerRepository.java`
* `CategoryRepository.java`
* `MenuItemRepository.java`
* `OrderRepository.java`
* `OrderItemRepository.java`
* `EventPromotionRepository.java`
* `OrderEventRepository.java`
* `PaymentRepository.java`
* `PromotionRuleRepository.java`

---

## Verification Plan

### Automated Checks
* Since we are generating only the entities, Flyway files, and repositories, we will write a minimal Spring Boot setup if necessary, or execute a basic parse/syntax check of the SQL files and check the Java files compile.
* Let's verify that the Flyway SQL file parses properly and does not contain syntax errors.
