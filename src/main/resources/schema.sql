-- Limpieza previa (no borra problemas, causas ni soluciones)
DELETE FROM plan_trabajo_actividades;
DELETE FROM plan_trabajo_problemas;
DELETE FROM actividad;
DELETE FROM solucion_aplicada;
DELETE FROM causa_de_problema;
DELETE FROM problema_de_conducta;
DELETE FROM perro;
DELETE FROM cliente;
DELETE FROM educador;

-- Reinicio de secuencias
ALTER TABLE actividad AUTO_INCREMENT = 1;
ALTER TABLE solucion_aplicada AUTO_INCREMENT = 1;
ALTER TABLE causa_de_problema AUTO_INCREMENT = 1;
ALTER TABLE problema_de_conducta AUTO_INCREMENT = 1;
ALTER TABLE perro AUTO_INCREMENT = 1;
ALTER TABLE cliente AUTO_INCREMENT = 1;
ALTER TABLE educador AUTO_INCREMENT = 1;
ALTER TABLE plan_trabajo AUTO_INCREMENT = 1;
