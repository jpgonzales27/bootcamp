-- Insertar tipos de mascotas
INSERT INTO pet_type (type_name) VALUES ('Perro');
INSERT INTO pet_type (type_name) VALUES ('Gato');
INSERT INTO pet_type (type_name) VALUES ('Conejo');
INSERT INTO pet_type (type_name) VALUES ('Ave');

-- Insertar mascotas
INSERT INTO pet (name, age, available, pet_type_id) VALUES ('Dogui', 3, true, 1);
INSERT INTO pet (name, age, available, pet_type_id) VALUES ('Mila', 2, true, 2);
INSERT INTO pet (name, age, available, pet_type_id) VALUES ('Nala', 1, true, 3);
INSERT INTO pet (name, age, available, pet_type_id) VALUES ('Scooby', 4, false, 4);
INSERT INTO pet (name, age, available, pet_type_id) VALUES ('Kaiser', 5, true, 1);

-- Insertar usuarios
INSERT INTO user (name, email, phone) VALUES ('Juan Gonzales', 'juan.gonzales@gmail.com', '555-1234');
INSERT INTO user (name, email, phone) VALUES ('Daniela Santa Cruz', 'dani.santacruz@gmail.com', '555-5678');
INSERT INTO user (name, email, phone) VALUES ('Melissa lopez', 'melissa.lopez@gmail.com', '555-9012');

-- Insertar adopciones
INSERT INTO adoption (pet_id, user_id, adoption_date) VALUES (4, 2, '2023-09-15');
INSERT INTO adoption (pet_id, user_id, adoption_date) VALUES(2, 1, '2023-10-01'); -- Miau fue adoptado por Juan Pérez