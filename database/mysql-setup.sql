CREATE DATABASE IF NOT EXISTS centre_medical
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON centre_medical.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
