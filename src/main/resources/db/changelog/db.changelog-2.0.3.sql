--liquibase formatted sql

--changeset FDKost:1
INSERT INTO pizza(code, name, time)
VALUES (001, 'Cheese 4 u', '0:05:00'),
       (002, 'Jalapeno hell', '0:04:30'),
       (003, 'Vegan choice', '0:03:30'),
       (004, 'Mozzarella lake', '0:05:00'),
       (005, 'Golden choice', '0:04:30'),
       (006, 'Shrimp paradise', '0:06:00'),
       (007, 'Imposter', '0:03:20'),
       (008, 'Hamslide', '0:04:00'),
       (009, 'Victor''s secret', '0:05:00'),
       (010, 'Biological hazard', '0:03:00'),
       (011, 'Fishing tale', '0:09:00')
;
INSERT INTO cook(employee_number, experience_coefficient, start_work_time, end_work_time)
VALUES (uuid_generate_v4(), 1.5, '12:00:00', '20:00:00'),
       (uuid_generate_v4(), 1.3, '06:00:00', '13:00:00'),
       (uuid_generate_v4(), 1.3, '07:00:00', '15:00:00'),
       (uuid_generate_v4(), 1.4, '06:00:00', '21:00:00'),
       (uuid_generate_v4(), 1.6, '06:00:00', '22:00:00'),
       (uuid_generate_v4(), 1.4, '06:00:00', '23:00:00'),
       (uuid_generate_v4(), 1.9, '06:00:00', '23:00:00'),
       (uuid_generate_v4(), 1.8, '10:00:00', '22:00:00')
;

