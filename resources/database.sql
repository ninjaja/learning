DROP DATABASE IF EXISTS learning;
CREATE DATABASE IF NOT EXISTS learning;
CREATE TABLE IF NOT EXISTS learning.students(id TINYINT NOT NULL AUTO_INCREMENT, surname CHAR(30) NOT NULL, name CHAR(15) NOT NULL, PRIMARY KEY(id));
CREATE TABLE IF NOT EXISTS learning.students_audit(id TINYINT NOT NULL AUTO_INCREMENT, studentId TINYINT NOT NULL, surname CHAR(30) NOT NULL, name CHAR(15) NOT NULL, changedate DATETIME DEFAULT NULL, action VARCHAR(50) DEFAULT NULL, PRIMARY KEY(id));
INSERT INTO learning.students (surname, name) VALUES ('Ivanov','Peter');
INSERT INTO learning.students (surname, name) VALUES ('Petrov','Ivan');
INSERT INTO learning.students (surname, name) VALUES ('Sidorov','Nikita');
