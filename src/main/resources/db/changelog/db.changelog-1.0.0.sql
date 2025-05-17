--liquibase formatted sql

--changeset FDKost:1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--changeset FDKost:2
CREATE TABLE IF NOT EXISTS "cook"
(
    employee_number        uuid PRIMARY KEY,
    experience_coefficient float,
    start_work_time        time,
    end_work_time          time
);
CREATE TABLE IF NOT EXISTS "orders_for_cooking"
(
    order_number              uuid PRIMARY KEY,
    employee_number           uuid,
    estimated_completion_time time,
    pizza_list                varchar
);
CREATE TABLE IF NOT EXISTS "pizza"
(
    code varchar PRIMARY KEY,
    name varchar,
    time time
);