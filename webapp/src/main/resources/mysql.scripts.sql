-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema webapp
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `webapp` ;

-- -----------------------------------------------------
-- Schema webapp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `webapp` DEFAULT CHARACTER SET latin1 ;
USE `webapp` ;

-- -----------------------------------------------------
-- Table `webapp`.`note`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `webapp`.`note` ;

CREATE TABLE IF NOT EXISTS `webapp`.`note` (
  `id` VARCHAR(255) NOT NULL,
  `content` VARCHAR(255) NOT NULL,
  `created_on` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated` VARCHAR(255) NULL DEFAULT NULL,
  `title` VARCHAR(255) NOT NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKmoddtnuw3yy6ct34xnw6u0boh` (`user_id` ASC) VISIBLE)
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;

-- -----------------------------------------------------
-- Table `webapp`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `webapp`.`user` ;

CREATE TABLE IF NOT EXISTS `webapp`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
