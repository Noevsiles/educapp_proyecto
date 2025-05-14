
-- 1) CAUSA
CREATE TABLE IF NOT EXISTS causa (
                       id_causa     BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nombre       VARCHAR(255)    NOT NULL,
                       descripcion  TEXT
);

-- 2) SOLUCION
CREATE TABLE IF NOT EXISTS solucion (
                          id_solucion    BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre         VARCHAR(255)    NOT NULL,
                          descripcion    TEXT,
                          categoria      VARCHAR(255),
                          dificultad     VARCHAR(255),
                          requerimientos TEXT,
                          notas          TEXT
);

-- 3) EDUCADOR
CREATE TABLE IF NOT EXISTS educador (
                          id_educador     BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre          VARCHAR(255),
                          apellidos       VARCHAR(255),
                          email           VARCHAR(255),
                          telefono        VARCHAR(255),
                          especializacion VARCHAR(255),
                          experiencia     INT,
                          formacion       VARCHAR(255),
                          descripcion     TEXT
);

-- 4) CLIENTE
CREATE TABLE IF NOT EXISTS cliente (
                         id_cliente   BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre       VARCHAR(255),
                         apellidos    VARCHAR(255),
                         email        VARCHAR(255),
                         telefono     VARCHAR(255),
                         educador_id  BIGINT,
                         CONSTRAINT fk_cliente_educador
                             FOREIGN KEY (educador_id)
                                 REFERENCES educador (id_educador)
                                 ON DELETE SET NULL
);

-- 5) PERRO
CREATE TABLE IF NOT EXISTS perro (
                       id_perro     BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nombre       VARCHAR(255),
                       raza         VARCHAR(255),
                       sexo         VARCHAR(255),
                       edad         INT,
                       esterilizado BOOLEAN,
                       cliente_id   BIGINT,
                       CONSTRAINT fk_perro_cliente
                           FOREIGN KEY (cliente_id)
                               REFERENCES cliente (id_cliente)
                               ON DELETE CASCADE
);

-- 6) PROBLEMA_DE_CONDUCTA
CREATE TABLE IF NOT EXISTS problema_de_conducta (
                                      id_problema_conducta BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      nombre               VARCHAR(255)    NOT NULL,
                                      descripcion          TEXT,
                                      perro_id             BIGINT          NULL,
                                      CONSTRAINT fk_problema_perro
                                          FOREIGN KEY (perro_id)
                                              REFERENCES perro (id_perro)
                                              ON DELETE CASCADE
);

-- 7) CAUSA_DE_PROBLEMA (join Problema ↔ Causa)
CREATE TABLE IF NOT EXISTS causa_de_problema (
                                   id_causa_de_problema  BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   problema_id           BIGINT NOT NULL,
                                   causa_id              BIGINT NOT NULL,
                                   CONSTRAINT fk_cdp_problema
                                       FOREIGN KEY (problema_id)
                                           REFERENCES problema_de_conducta (id_problema_conducta)
                                           ON DELETE CASCADE,
                                   CONSTRAINT fk_cdp_causa
                                       FOREIGN KEY (causa_id)
                                           REFERENCES causa (id_causa)
                                           ON DELETE CASCADE
);

-- 8) SOLUCION_APLICADA (join Problema ↔ Solucion)
CREATE TABLE IF NOT EXISTS solucion_aplicada (
                                   id_solucion_aplicada     BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   id_problema_de_conducta   BIGINT NOT NULL,
                                   id_solucion               BIGINT NOT NULL,
                                   CONSTRAINT fk_sa_problema
                                       FOREIGN KEY (id_problema_de_conducta)
                                           REFERENCES problema_de_conducta (id_problema_conducta)
                                           ON DELETE CASCADE,
                                   CONSTRAINT fk_sa_solucion
                                       FOREIGN KEY (id_solucion)
                                           REFERENCES solucion (id_solucion)
                                           ON DELETE CASCADE
);

-- 9) ACTIVIDAD
CREATE TABLE IF NOT EXISTS actividad (
                           id_actividad           BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre                 VARCHAR(255),
                           descripcion            TEXT,
                           duracion               INT,
                           solucion_aplicada_id   BIGINT,
                           completado             BOOLEAN,
                           CONSTRAINT fk_actividad_sa
                               FOREIGN KEY (solucion_aplicada_id)
                                   REFERENCES solucion_aplicada (id_solucion_aplicada)
                                   ON DELETE CASCADE
);

-- 10) SESION
CREATE TABLE IF NOT EXISTS sesion (
                        id_sesion     BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha_hora    DATETIME,
                        tipo_sesion   VARCHAR(255),
                        educador_id   BIGINT,
                        cliente_id    BIGINT,
                        CONSTRAINT fk_sesion_educador
                            FOREIGN KEY (educador_id)
                                REFERENCES educador (id_educador)
                                ON DELETE SET NULL,
                        CONSTRAINT fk_sesion_cliente
                            FOREIGN KEY (cliente_id)
                                REFERENCES cliente (id_cliente)
                                ON DELETE SET NULL
);

-- 11) TARIFA
CREATE TABLE IF NOT EXISTS tarifa (
                        id_tarifa    BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre       VARCHAR(255),
                        precio       DOUBLE,
                        descripcion  TEXT,
                        educador_id  BIGINT,
                        CONSTRAINT fk_tarifa_educador
                            FOREIGN KEY (educador_id)
                                REFERENCES educador (id_educador)
                                ON DELETE CASCADE
);
