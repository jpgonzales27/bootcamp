-- Insertar tipos de mascotas
INSERT INTO PetType (name) VALUES ('Perro');
INSERT INTO PetType (name) VALUES ('Gato');
INSERT INTO PetType (name) VALUES ('Conejo');
INSERT INTO PetType (name) VALUES ('Ave');

-- Insertar mascotas
INSERT INTO pet (name, age, available, petType_id) VALUES ('Dogui', 3, true, 1);
INSERT INTO mascota (name, age, available, petType_id) VALUES ('Mila', 2, true, 2);
INSERT INTO mascota (name, age, available, petType_id) VALUES ('Nala', 1, true, 3);
INSERT INTO mascota (name, age, available, petType_id) VALUES ('Scooby', 4, false, 4);
INSERT INTO mascota (name, age, available, petType_id) VALUES ('Kaiser', 5, true, 1);
-- Insertar usuarios
INSERT INTO usuario (id, nombre, correo_electronico, telefono) VALUES
(1, 'Juan Pérez', 'juan.perez@example.com', '555-1234'),
(2, 'Ana Gómez', 'ana.gomez@example.com', '555-5678'),
(3, 'Carlos López', 'carlos.lopez@example.com', '555-9012');

-- Insertar adopciones
INSERT INTO adopcion (id, mascota_id, usuario_id, fecha_adopcion) VALUES
(1, 4, 2, '2023-09-15'), -- Tweety fue adoptado por Ana Gómez
(2, 2, 1, '2023-10-01'); -- Miau fue adoptado por Juan Pérez