-- V2__add_version_columns.sql
-- Adds optimistic locking version columns to customers, payments, and promotion_rules.
-- Splits from V1 because V1 is already applied to the database in existing environments.

ALTER TABLE customers
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE payments
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE promotion_rules
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;
