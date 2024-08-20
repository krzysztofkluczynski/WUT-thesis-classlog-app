INSERT INTO role (role_name) VALUES ('student');
INSERT INTO role (role_name) VALUES ('teacher');
INSERT INTO role (role_name) VALUES ('admin');

INSERT INTO app_user (first_name, last_name, login, password, role_id)
VALUES ('student', 'student', 'student', '$2b$12$h44AN2UJZTYLZ1.aqpNMV.IlaOcxHlMMO3cEg7SAaU3vMfb3vuCMe',
        (SELECT id FROM role WHERE role_name = 'student'));

INSERT INTO app_user (first_name, last_name, login, password, role_id)
VALUES ('teacher', 'teacher', 'teacher', '$2b$12$h44AN2UJZTYLZ1.aqpNMV.IlaOcxHlMMO3cEg7SAaU3vMfb3vuCMe',
        (SELECT id FROM role WHERE role_name = 'teacher'));

INSERT INTO app_user (first_name, last_name, login, password, role_id)
VALUES ('admin', 'admin', 'admin', '$2b$12$h44AN2UJZTYLZ1.aqpNMV.IlaOcxHlMMO3cEg7SAaU3vMfb3vuCMe',
        (SELECT id FROM role WHERE role_name = 'admin'));

SELECT * FROM app_user;
