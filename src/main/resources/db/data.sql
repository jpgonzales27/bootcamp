-- Insertar tipos de mascotas
INSERT INTO pet_type (type_name) VALUES ('Perro');
INSERT INTO pet_type (type_name) VALUES ('Gato');
INSERT INTO pet_type (type_name) VALUES ('Conejo');
INSERT INTO pet_type (type_name) VALUES ('Ave');
INSERT INTO pet_type (type_name) VALUES ('Caballo');

-- Insertar mascotas
INSERT INTO pet (name, age,genre, available, pet_type_id) VALUES ('Dogui', 3,'MALE', true, 1);
INSERT INTO pet (name, age,genre, available, pet_type_id) VALUES ('Mila', 2,'FEMALE', true, 2);
INSERT INTO pet (name, age,genre, available, pet_type_id) VALUES ('Nala', 1,'FEMALE', true, 3);
INSERT INTO pet (name, age,genre, available, pet_type_id) VALUES ('Scooby', 4,'MALE', false, 4);
INSERT INTO pet (name, age,genre, available, pet_type_id) VALUES ('Kaiser', 5,'FEMALE', true, 1);

-- Insertar usuarios
INSERT INTO user (name, email, phone) VALUES ('Juan Gonzales', 'juan.gonzales@gmail.com', '555-1234');
INSERT INTO user (name, email, phone) VALUES ('Daniela Santa Cruz', 'dani.santacruz@gmail.com', '555-5678');
INSERT INTO user (name, email, phone) VALUES ('Melissa Lopez', 'melissa.lopez@gmail.com', '555-9012');
INSERT INTO user (name, email, phone) VALUES ('Jaime Ortiz', 'jaime.ortiz@gmail.com', '555-4321');

-- Insertar adopciones
INSERT INTO adoption (pet_id, user_id, adoption_date) VALUES (4, 2, '2023-09-15');
INSERT INTO adoption (pet_id, user_id, adoption_date) VALUES(2, 1, '2023-10-01');