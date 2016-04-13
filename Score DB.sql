-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema db_shuffle
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema db_shuffle
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_shuffle` DEFAULT CHARACTER SET utf8 ;
USE `db_shuffle` ;

-- -----------------------------------------------------
-- Table `db_shuffle`.`score`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_shuffle`.`shf_score`;

CREATE TABLE IF NOT EXISTS `db_shuffle`.`shf_score` (
  `scoreID` INT NOT NULL AUTO_INCREMENT,
  `artist` VARCHAR(45) NOT NULL,
  `album` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `score` INT NOT NULL,
  `mode` INT NOT NULL,
  `status` INT NOT NULL DEFAULT 1,
  `dateAdded` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`scoreID`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
