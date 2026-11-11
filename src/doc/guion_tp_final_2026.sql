-- =====================================================================
-- TP Final 2026 - Registro censal de barrios y comunas (CABA)
-- Guion de creación de la base de datos y carga de datos precargados
--
-- Correspondencia con el diagrama de clases:
--   Comuna                   -> comunas
--   Barrio (abstracta)       -> barrios
--   Trazado_formal           -> trazados_formales
--   Urbanizacion_planificada -> urbanizaciones_planificadas
--   Complejo_habitacional    -> complejos_habitacionales
--   Persona                  -> personas
--   (Persona.situacion)      -> situaciones_laborales
--
-- La herencia de Barrio se resuelve con una tabla por clase: la tabla
-- "barrios" guarda los atributos comunes y cada subclase tiene su tabla
-- con los atributos propios, cuya clave primaria es a la vez clave
-- foránea hacia "barrios".
-- =====================================================================

DROP DATABASE IF EXISTS tp_final_2026;
CREATE DATABASE tp_final_2026
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
USE tp_final_2026;


-- ---------------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------------

CREATE TABLE comunas (
    id_comuna  INT          NOT NULL AUTO_INCREMENT,
    numero     INT          NOT NULL,
    sede       VARCHAR(80)  NOT NULL,
    junta      VARCHAR(80)  NOT NULL,
    PRIMARY KEY (id_comuna),
    UNIQUE KEY uq_comunas_numero (numero),
    CONSTRAINT ck_comunas_numero CHECK (numero > 0)
) ENGINE = InnoDB;


CREATE TABLE barrios (
    id_barrio   INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(60)  NOT NULL,
    superficie  INT          NOT NULL COMMENT 'en hectáreas',
    id_comuna   INT          NOT NULL,
    PRIMARY KEY (id_barrio),
    UNIQUE KEY uq_barrios_nombre (nombre),
    KEY idx_barrios_comuna (id_comuna),
    CONSTRAINT ck_barrios_superficie CHECK (superficie > 0),
    CONSTRAINT fk_barrios_comuna
        FOREIGN KEY (id_comuna) REFERENCES comunas (id_comuna)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB;


CREATE TABLE trazados_formales (
    id_barrio           INT       NOT NULL,
    cantidad_edificios  INT       NOT NULL COMMENT 'edificios de propiedad horizontal',
    porcentaje_asfalto  FLOAT     NOT NULL COMMENT 'porcentaje de asfalto en calles (0 a 100)',
    PRIMARY KEY (id_barrio),
    CONSTRAINT ck_trazados_edificios CHECK (cantidad_edificios >= 0),
    CONSTRAINT ck_trazados_asfalto   CHECK (porcentaje_asfalto BETWEEN 0 AND 100),
    CONSTRAINT fk_trazados_barrio
        FOREIGN KEY (id_barrio) REFERENCES barrios (id_barrio)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = InnoDB;


CREATE TABLE urbanizaciones_planificadas (
    id_barrio      INT          NOT NULL,
    nro_ley        VARCHAR(20)  NOT NULL COMMENT 'ley de integración de la Legislatura',
    conexion_agua  BIT(1)       NOT NULL COMMENT 'conexión formal a la red de agua potable',
    PRIMARY KEY (id_barrio),
    CONSTRAINT fk_urbanizaciones_barrio
        FOREIGN KEY (id_barrio) REFERENCES barrios (id_barrio)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = InnoDB;


CREATE TABLE complejos_habitacionales (
    id_barrio            INT     NOT NULL,
    cantidad_monoblocks  INT     NOT NULL,
    consorcio            BIT(1)  NOT NULL COMMENT 'consorcio de administración macro activo',
    PRIMARY KEY (id_barrio),
    CONSTRAINT ck_complejos_monoblocks CHECK (cantidad_monoblocks >= 0),
    CONSTRAINT fk_complejos_barrio
        FOREIGN KEY (id_barrio) REFERENCES barrios (id_barrio)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = InnoDB;


CREATE TABLE situaciones_laborales (
    id_situacion_laboral  INT          NOT NULL,
    nombre                VARCHAR(30)  NOT NULL,
    PRIMARY KEY (id_situacion_laboral),
    UNIQUE KEY uq_situaciones_nombre (nombre)
) ENGINE = InnoDB;


CREATE TABLE personas (
    id_persona            INT          NOT NULL AUTO_INCREMENT,
    dni                   VARCHAR(15)  NOT NULL,
    nombre                VARCHAR(80)  NOT NULL,
    nacimiento            DATE         NOT NULL,
    id_situacion_laboral  INT          NOT NULL,
    id_barrio             INT          NOT NULL,
    PRIMARY KEY (id_persona),
    UNIQUE KEY uq_personas_dni (dni),
    KEY idx_personas_situacion (id_situacion_laboral),
    KEY idx_personas_barrio (id_barrio),
    KEY idx_personas_nombre (nombre),
    CONSTRAINT fk_personas_situacion
        FOREIGN KEY (id_situacion_laboral) REFERENCES situaciones_laborales (id_situacion_laboral)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_personas_barrio
        FOREIGN KEY (id_barrio) REFERENCES barrios (id_barrio)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB;


-- ---------------------------------------------------------------------
-- Datos precargados
-- ---------------------------------------------------------------------

-- Situaciones laborales posibles
INSERT INTO situaciones_laborales (id_situacion_laboral, nombre) VALUES
    (1, 'Ocupado'),
    (2, 'Desocupado'),
    (3, 'Subocupado');

-- Las 15 comunas oficiales de la Ciudad Autónoma de Buenos Aires.
-- (Reemplazar sede y junta por los datos reales si se quieren mostrar tal cual.)
INSERT INTO comunas (numero, sede, junta) VALUES
    ( 1, 'Sede Comunal 1',  'Junta Comunal 1'),
    ( 2, 'Sede Comunal 2',  'Junta Comunal 2'),
    ( 3, 'Sede Comunal 3',  'Junta Comunal 3'),
    ( 4, 'Sede Comunal 4',  'Junta Comunal 4'),
    ( 5, 'Sede Comunal 5',  'Junta Comunal 5'),
    ( 6, 'Sede Comunal 6',  'Junta Comunal 6'),
    ( 7, 'Sede Comunal 7',  'Junta Comunal 7'),
    ( 8, 'Sede Comunal 8',  'Junta Comunal 8'),
    ( 9, 'Sede Comunal 9',  'Junta Comunal 9'),
    (10, 'Sede Comunal 10', 'Junta Comunal 10'),
    (11, 'Sede Comunal 11', 'Junta Comunal 11'),
    (12, 'Sede Comunal 12', 'Junta Comunal 12'),
    (13, 'Sede Comunal 13', 'Junta Comunal 13'),
    (14, 'Sede Comunal 14', 'Junta Comunal 14'),
    (15, 'Sede Comunal 15', 'Junta Comunal 15');
