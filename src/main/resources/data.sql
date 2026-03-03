-- Solo insertamos los roles básicos. 
-- El ID es importante para que el DataLoader o el registro funcionen bien.
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('MANAGER');