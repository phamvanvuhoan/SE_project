-- V3__business_logic_fixes.sql
-- Adds business-rule constraints and schema fields surfaced by the Phase 2 business logic review.

-- 1. Cap point_rate at 100 to prevent misconfiguration (e.g. storing 0.10 instead of 10.00).
ALTER TABLE membership_levels
    ADD CONSTRAINT chk_point_rate_max CHECK (point_rate <= 100);

-- 2. Add configurable redemption cap to buy_x_get_y_rules.
--    max_redemptions = 0 means no cap (original behaviour). Positive value limits the multiplier.
ALTER TABLE buy_x_get_y_rules
    ADD COLUMN IF NOT EXISTS max_redemptions INTEGER NOT NULL DEFAULT 0,
    ADD CONSTRAINT chk_max_redemptions CHECK (max_redemptions >= 0);
