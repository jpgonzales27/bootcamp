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
INSERT INTO user (name, email,password, phone) VALUES ('Juan Gonzales', 'juan.gonzales@gmail.com',"$2a$10$.ucU0WWMHxOgFdMo5CKuWOJWBXj82Nh484FW1/Qanm0TY7MhLYbhO", '555-1234');
INSERT INTO user (name, email,password, phone) VALUES ('Daniela Santa Cruz', 'dani.santacruz@gmail.com',"$2a$10$.ucU0WWMHxOgFdMo5CKuWOJWBXj82Nh484FW1/Qanm0TY7MhLYbhO", '555-5678');
INSERT INTO user (name, email,password, phone) VALUES ('Melissa Lopez', 'melissa.lopez@gmail.com',"$2a$10$.ucU0WWMHxOgFdMo5CKuWOJWBXj82Nh484FW1/Qanm0TY7MhLYbhO", '555-9012');
INSERT INTO user (name, email,password, phone) VALUES ('Jaime Ortiz', 'jaime.ortiz@gmail.com',"$2a$10$.ucU0WWMHxOgFdMo5CKuWOJWBXj82Nh484FW1/Qanm0TY7MhLYbhO", '555-4321');

-- Insertar roles
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO role (name) VALUES ('USER');

-- Relacionar usuarios con roles (user_roles)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); --ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (4, 2);

-- Insertar adopciones
INSERT INTO adoption (pet_id, user_id, adoption_date) VALUES (4, 2, '2023-09-15');
INSERT INTO adoption (pet_id, user_id, adoption_date) VALUES(2, 1, '2023-10-01');