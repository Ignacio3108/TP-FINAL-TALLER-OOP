-- Guion de la base de datos del sistema CensoCABA
-- -----------------------------------------------------
-- Schema tp_final_26
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `tp_final_26` ;

-- -----------------------------------------------------
-- Schema tp_final_26
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tp_final_26`;
USE `tp_final_26` ;

-- -----------------------------------------------------
-- Table `tp_final_26`.`comunas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp_final_26`.`comunas` ;

CREATE TABLE IF NOT EXISTS `tp_final_26`.`comunas` (
  `id_comuna` INT NOT NULL AUTO_INCREMENT,
  `numero` INT NOT NULL,
  `sede` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_comuna`),
  UNIQUE INDEX `numero_UNIQUE` (`numero` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `tp_final_26`.`barrios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp_final_26`.`barrios` ;

CREATE TABLE IF NOT EXISTS `tp_final_26`.`barrios` (
  `id_barrio` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `hectareas` INT NOT NULL,
  `cantidad_ph` INT NULL DEFAULT NULL,
  `porcentaje_asfalto` FLOAT NULL DEFAULT NULL,
  `numero_ley` VARCHAR(45) NULL DEFAULT NULL,
  `agua_potable` BIT(1) NULL DEFAULT NULL,
  `cantidad_monoblocks` INT NULL DEFAULT NULL,
  `consorcio` BIT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id_barrio`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `tp_final_26`.`comunas_barrios`
-- Tabla intermedia que asocia cada barrio con su comuna.
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp_final_26`.`comunas_barrios` ;

CREATE TABLE IF NOT EXISTS `tp_final_26`.`comunas_barrios` (
  `id_barrio` INT NOT NULL,
  `id_comuna` INT NOT NULL,
  PRIMARY KEY (`id_barrio`),
  INDEX `fk_comuna_idx` (`id_comuna` ASC) VISIBLE,
  CONSTRAINT `fk_cb_barrio`
    FOREIGN KEY (`id_barrio`)
    REFERENCES `tp_final_26`.`barrios` (`id_barrio`),
  CONSTRAINT `fk_cb_comuna`
    FOREIGN KEY (`id_comuna`)
    REFERENCES `tp_final_26`.`comunas` (`id_comuna`));


-- -----------------------------------------------------
-- Table `tp_final_26`.`situaciones_laborales`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp_final_26`.`situaciones_laborales` ;

CREATE TABLE IF NOT EXISTS `tp_final_26`.`situaciones_laborales` (
  `id_situacion_laboral` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_situacion_laboral`));


-- -----------------------------------------------------
-- Table `tp_final_26`.`personas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp_final_26`.`personas` ;

CREATE TABLE IF NOT EXISTS `tp_final_26`.`personas` (
  `id_persona` INT NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(45) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `nacimiento` DATE NOT NULL,
  `id_situacion_laboral` INT NOT NULL,
  `id_barrio` INT NOT NULL,
  PRIMARY KEY (`id_persona`),
  UNIQUE INDEX `dni_UNIQUE` (`dni` ASC) VISIBLE,
  INDEX `fk_situacion_laboral_idx` (`id_situacion_laboral` ASC) VISIBLE,
  INDEX `fk_barrio_idx` (`id_barrio` ASC) VISIBLE,
  CONSTRAINT `fk_barrio`
    FOREIGN KEY (`id_barrio`)
    REFERENCES `tp_final_26`.`barrios` (`id_barrio`),
  CONSTRAINT `fk_situacion_laboral`
    FOREIGN KEY (`id_situacion_laboral`)
    REFERENCES `tp_final_26`.`situaciones_laborales` (`id_situacion_laboral`));


-- -----------------------------------------------------
-- Datos precargados
-- -----------------------------------------------------
INSERT INTO `tp_final_26`.`situaciones_laborales` (`id_situacion_laboral`, `nombre`) VALUES ('1', 'Ocupado');
INSERT INTO `tp_final_26`.`situaciones_laborales` (`id_situacion_laboral`, `nombre`) VALUES ('2', 'Desocupado');
INSERT INTO `tp_final_26`.`situaciones_laborales` (`id_situacion_laboral`, `nombre`) VALUES ('3', 'Subocupado');

-- Comunas de Prueba (1 a 5). Las sedes son datos inventados.
INSERT INTO `tp_final_26`.`comunas` (`numero`, `sede`) VALUES ('1', 'Sede Los Aromos');
INSERT INTO `tp_final_26`.`comunas` (`numero`, `sede`) VALUES ('2', 'Sede El Ombu');
INSERT INTO `tp_final_26`.`comunas` (`numero`, `sede`) VALUES ('3', 'Sede La Ceiba');
INSERT INTO `tp_final_26`.`comunas` (`numero`, `sede`) VALUES ('4', 'Sede Los Jacarandaes');
INSERT INTO `tp_final_26`.`comunas` (`numero`, `sede`) VALUES ('5', 'Sede El Algarrobo');

