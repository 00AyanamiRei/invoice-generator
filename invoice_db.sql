-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server Version:               8.0.31 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping database structure for billing_db
CREATE DATABASE IF NOT EXISTS `billing_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `billing_db`;

-- Dumping structure for table billing_db.authorities
CREATE TABLE IF NOT EXISTS `authorities` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `authority` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_authorities_unique` (`user_id`,`authority`),
  CONSTRAINT `fk_authorities_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table billing_db.authorities
REPLACE INTO `authorities` (`id`, `user_id`, `authority`) VALUES
 (1, 1, 'ROLE_USER'),
 (3, 2, 'ROLE_ADMIN'),
 (2, 2, 'ROLE_USER');

-- Dumping structure for table billing_db.customers
CREATE TABLE IF NOT EXISTS `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_name` varchar(255) DEFAULT NULL,
  `created_at` date NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table billing_db.customers
REPLACE INTO `customers` (`id`, `last_name`, `created_at`, `email`, `photo`, `first_name`) VALUES
 (18, 'Nicoll', '2017-08-18', 'stephane.nicoll@gmail.com', '', 'Stephane'),
 (19, 'Brannen', '2017-08-19', 'sam.brannen@gmail.com', '', 'Sam'),
 (20, 'Hoeller', '2017-08-20', 'juergen.Hoeller@gmail.com', '', 'Juergen'),
 (21, 'Roe', '2017-08-21', 'janie.roe@gmail.com', '', 'Janie'),
 (22, 'Smith', '2017-08-22', 'john.smith@gmail.com', '', 'John'),
 (23, 'Bloggs', '2017-08-23', 'joe.bloggs@gmail.com', '', 'Joe'),
 (24, 'Stiles', '2017-08-24', 'john.stiles@gmail.com', '', 'John'),
 (25, 'Roe', '2017-08-25', 'stiles.roe@gmail.com', '', 'Richard');

-- Dumping structure for table billing_db.invoices
CREATE TABLE IF NOT EXISTS `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `observation` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_customer_invoice` (`customer_id`),
  CONSTRAINT `FK_customer_invoice` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table billing_db.invoices
REPLACE INTO `invoices` (`id`, `created_at`, `description`, `observation`, `client_id`) VALUES
 (1, '2022-11-13', 'Office equipment invoice', NULL, 1),
 (2, '2022-11-13', 'Bicycle invoice', 'Some important note!', 1),
 (3, '2022-11-19', 'Office purchases', 'Something important', 2);

-- Dumping structure for table billing_db.invoice_items
CREATE TABLE IF NOT EXISTS `invoice_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `invoice_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_product_invoice_item` (`product_id`),
  KEY `FK_invoice_invoice_item` (`invoice_id`),
  CONSTRAINT `FK_invoice_invoice_item` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
  CONSTRAINT `FK_product_invoice_item` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table billing_db.invoice_items
REPLACE INTO `invoice_items` (`id`, `quantity`, `product_id`, `invoice_id`) VALUES
 (1, 1, 1, 1),
 (2, 2, 4, 1),
 (3, 1, 5, 1),
 (4, 1, 7, 1),
 (5, 3, 6, 2),
 (6, 2, 7, 3),
 (7, 1, 6, 3),
 (8, 1, 5, 3);

-- Dumping structure for table billing_db.products
CREATE TABLE IF NOT EXISTS `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table billing_db.products
REPLACE INTO `products` (`id`, `created_at`, `name`, `price`) VALUES
 (1, '2022-11-13', 'Panasonic LCD Screen', 259990),
 (2, '2022-11-13', 'Sony Digital Camera DSC-W320B', 123490),
 (3, '2022-11-13', 'Apple iPod shuffle', 1499990),
 (4, '2022-11-13', 'Sony Notebook Z110', 37990),
 (5, '2022-11-13', 'Hewlett Packard Multifunctional F2280', 69990),
 (6, '2022-11-13', 'Bianchi Bicycle Aro 26', 69990),
 (7, '2022-11-13', 'Mica Chest of 5 Drawers', 299990);

-- Dumping structure for table billing_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table billing_db.users
REPLACE INTO `users` (`id`, `username`, `password`, `enabled`) VALUES
 (1, 'andres', '$2a$10$Ok5BIHXFJpJFoFUPp.SFYun9O0fS1sh.5yuhuOn/t5wW6yvaFh94y', 1),
 (2, 'admin', '$2a$10$52xQraLQm0wG0rMnAz.IPuNm/tFRLsVWtM.675eaUQY9aGym0DyMa', 1);