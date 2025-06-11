CREATE DATABASE IF NOT EXISTS projectsoftwaredb;
CREATE DATABASE IF NOT EXISTS keycloakdb;

CREATE USER IF NOT EXISTS 'appuser'@'%' IDENTIFIED BY 'mariadb';

GRANT ALL PRIVILEGES ON projectsoftwaredb.* TO 'appuser'@'%';
GRANT ALL PRIVILEGES ON keycloakdb.* TO 'appuser'@'%';

FLUSH PRIVILEGES;

-- Ejecuta el siguiente script para borrar las tablas
--DROP TABLE IF EXISTS approved;
--DROP TABLE IF EXISTS postulated;
--DROP TABLE IF EXISTS jpa_project_entity;
--DROP TABLE IF EXISTS jpa_coordinator_entity;
--DROP TABLE IF EXISTS student;
--DROP TABLE IF EXISTS project;
--DROP TABLE IF EXISTS company;
--DROP TABLE IF EXISTS app_user;

USE projectsoftwaredb; 

CREATE TABLE company (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    company_name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255) NOT NULL,
    contact_last_name VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(255) NOT NULL,
    contact_position VARCHAR(255) NOT NULL,
    company_sector VARCHAR(50) NOT NULL
);

CREATE TABLE project (
    proid VARCHAR(255) PRIMARY KEY,
    protitle VARCHAR(100) NOT NULL,
    prodescription VARCHAR(1000) NOT NULL,
    proabstract TEXT NOT NULL,
    progoals TEXT NOT NULL,
    prodate DATE NOT NULL,
    prodeadline INT NOT NULL,
    probudget DOUBLE,
    prostate VARCHAR(50) NOT NULL,
    idcompany VARCHAR(255),
    idcoordinator VARCHAR(255),
    CONSTRAINT fk_project_company FOREIGN KEY (idcompany) REFERENCES Company(id)
);

CREATE TABLE student (
    studentid VARCHAR(255) PRIMARY KEY,
    studentemail VARCHAR(255) NOT NULL,
    studentpassword VARCHAR(255) NOT NULL
    -- Puedes agregar más campos si la entidad los tiene
);

CREATE TABLE jpa_coordinator_entity (
    id VARCHAR(255) PRIMARY KEY,
    coordinator_email VARCHAR(255) NOT NULL UNIQUE,
    coordinator_password VARCHAR(255) NOT NULL
);

CREATE TABLE jpa_project_entity (
    pro_id VARCHAR(255) PRIMARY KEY,
    pro_title VARCHAR(255) NOT NULL,
    pro_description TEXT NOT NULL,
    pro_abstract TEXT NOT NULL,
    pro_goals TEXT NOT NULL,
    pro_date DATE NOT NULL,
    pro_dead_line INT NOT NULL,
    pro_budget DOUBLE,
    pro_state VARCHAR(50) NOT NULL,
    idcompany VARCHAR(255),
    pro_coordinator VARCHAR(255)
    -- Add FOREIGN KEY constraints if needed
);


CREATE TABLE postulated (
    student_id VARCHAR(255),
    pro_id VARCHAR(255),
    PRIMARY KEY (student_id, pro_id),
    FOREIGN KEY (student_id) REFERENCES student(studentid),
    FOREIGN KEY (pro_id) REFERENCES Project(proid)
);

CREATE TABLE approved (
    student_id VARCHAR(255),
    pro_id VARCHAR(255),
    PRIMARY KEY (student_id, pro_id),
    FOREIGN KEY (student_id) REFERENCES student(studentid),
    FOREIGN KEY (pro_id) REFERENCES Project(proid)
);
CREATE TABLE app_user (
    id VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role INT NOT NULL
    -- 1=Estudiante, 2=Coordinador, 3=Empresa
);
