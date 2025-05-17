--liquibase formatted sql

--changeset FDKost:1
ALTER TABLE "orders_for_cooking"
    ADD
        FOREIGN KEY (employee_number) references cook (employee_number);