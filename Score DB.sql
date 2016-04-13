-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`score`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`score` (
  `scoreID` INT NOT NULL AUTO_INCREMENT,
  `artist` VARCHAR(45) NOT NULL,
  `playlist` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `score` INT NOT NULL,
  `mode` INT NOT NULL,
  `status` INT NOT NULL,
  `dateAdded` DATETIME NOT NULL,
  PRIMARY KEY (`scoreID`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
