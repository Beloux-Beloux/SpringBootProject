CREATE DATABASE IF NOT EXISTS centre_medical
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'admincm'@'localhost' IDENTIFIED BY 'MdpAdminCm@2026';
GRANT ALL PRIVILEGES ON centre_medical.* TO 'admincm'@'localhost';
FLUSH PRIVILEGES;
