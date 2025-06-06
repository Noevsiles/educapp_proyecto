-- 1) CAUSAS
INSERT IGNORE INTO causa (id_causa, nombre, descripcion)
VALUES (1, 'Ansiedad', 'Estado de tensión frente a ausencia de estímulo o presencia de amenaza.'),
       (2, 'Aburrimiento', 'Falta de estímulos físicos o mentales.'),
       (3, 'Miedo', 'Respuesta emocional a estímulos percibidos como amenazantes.'),
       (4, 'Territorialidad', 'Defensa del espacio considerado propio.'),
       (5, 'Dentición', 'Molestias propias del cambio de dientes en cachorros.'),
       (6, 'Apego excesivo', 'Vínculo intenso que genera ansiedad ante la separación.'),
       (7, 'Falta de socialización', 'Poca exposición a otros individuos o entornos.'),
       (8, 'Dolor o enfermedad', 'Condiciones médicas que generan irritabilidad o agresividad.'),
       (9, 'Experiencia traumática', 'Evento previo negativo relacionado con estímulo.'),
       (10, 'Sensibilidad auditiva', 'Mayor sensibilidad a estímulos sonoros intensos.'),
       (11, 'Raza predispuesta', 'Tendencia genética a ciertas conductas.'),
       (12, 'Problema médico', 'Condición fisiológica que altera comportamiento.'),
       (13, 'Instinto de presa', 'Tendencia natural de persecución de estímulos móviles.'),
       (14, 'Estrés', 'Tensión continuada que genera comportamientos repetitivos.'),
       (15, 'Inseguridad', 'Falta de confianza en situaciones de recurso compartido.'),
       (16, 'Falta de entrenamiento', 'Ausencia de enseñanzas básicas de conducta.'),
       (17, 'Búsqueda de atención', 'Deseo de llamar la atención del dueño.'),
       (18, 'Regulación de temperatura', 'Cavar para controlar temperatura corporal.'),
       (19, 'Alergias', 'Reacciones cutáneas que generan picor.'),
       (20, 'Falta de autocontrol', 'Incapacidad de inhibir impulsos de persecución.'),
       (21, 'Falta de ejercicio', 'Insuficiente actividad física diaria.');

-- 2) SOLUCIONES
INSERT IGNORE INTO solucion (id_solucion, nombre, descripcion,
                             categoria, dificultad, requerimientos, notas)
VALUES (1, 'Redirección de atención mediante comandos',
        'Uso de comandos para desviar la atención del perro de estímulos desencadenantes.',
        'Técnicas de refuerzo positivo', 'Fácil', 'Clicker, Premios', 'Evitar métodos aversivos.'),
       (2, 'Refuerzo positivo por silencio',
        'Recompensar al perro cuando permanece en silencio tras estímulo.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Premios', 'No reforzar ladridos aislados.'),
       (3, 'Desensibilización progresiva y contracondicionamiento',
        'Exponer gradualmente al perro al estímulo mientras se asocia con recompensas.',
        'Desensibilización y contracondicionamiento', 'Avanzado',
        'Grabaciones del estímulo, Premios', 'Respetar el ritmo del perro.'),
       (4, 'Uso temporal y ético de dispositivos anti-ladrido',
        'Uso controlado de dispositivos que emiten estímulos inofensivos al ladrido.',
        'Técnicas avanzadas supervisadas', 'Avanzado',
        'Collar anti-ladrido', 'Solo como último recurso.'),
       (5, 'Proporcionar juguetes adecuados para masticar',
        'Ofrecer juguetes resistentes para canalizar el instinto de masticación.',
        'Modificación ambiental', 'Fácil', 'Juguetes de caucho',
        'Supervisar para evitar ingestión de piezas.'),
       (6, 'Incrementar el ejercicio físico y mental',
        'Aumentar paseos y juegos interactivos para reducir energía acumulada.',
        'Modificación ambiental', 'Intermedio', 'Fetch toys, Juguetes interactivos',
        'Evitar sobrecarga física.'),
       (7, 'Entrenamiento para redirigir comportamientos destructivos',
        'Enseñar órdenes básicas y ofrecer alternativas de masticación.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Clicker, Juguetes',
        'Reforzar solo la conducta deseada.'),
       (8, 'Entrenamiento gradual de ausencia',
        'Iniciar con separaciones cortas e ir aumentando progresivamente la duración.',
        'Desensibilización y contracondicionamiento', 'Avanzado',
        'Reloj, Juguetes interactivos', 'Evitar regresiones bruscas.'),
       (9, 'Uso de juguetes interactivos',
        'Mantener al perro ocupado con puzzles que dispensen premios.',
        'Modificación ambiental', 'Fácil', 'Puzzle feeders',
        'Supervisar la apropiación.'),
       (10, 'Desensibilización a señales de salida',
        'Practicar acciones asociadas a la marcha para reducir anticipación.',
        'Desensibilización y contracondicionamiento', 'Intermedio',
        'Zapatos, Llaves', 'Evitar ansiedad por sobreexposición.'),
       (11, 'Técnicas de gestión con bozales o arneses',
        'Uso de equipamiento que garantice seguridad durante la modificación.',
        'Técnicas avanzadas supervisadas', 'Avanzado', 'Bozal, Arnés',
        'Debe usarse bajo supervisión.'),
       (12, 'Entrenamiento en señales de calma',
        'Enseñar comandos como “quieto” o “mirar” para redirigir atención.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Clicker, Premios',
        'Practicar en ambientes controlados.'),
       (13, 'Consulta veterinaria o etológica',
        'Evaluación profesional para descartar causas médicas o de conducta.',
        'Técnicas avanzadas supervisadas', 'Fácil', 'Veterinario, Etólogo',
        'Imprescindible en casos severos.'),
       (14, 'Modificación ambiental para ruidos fuertes',
        'Crear un refugio cómodo y aislado para el perro durante eventos ruidosos.',
        'Modificación ambiental', 'Fácil', 'Caja aislante, Mantas',
        'Asegurar ventilación adecuada.'),
       (15, 'Uso de feromonas calmantes',
        'Difusores de feromonas para reducir estrés en el ambiente.',
        'Modificación ambiental', 'Fácil', 'Difusor de feromonas',
        'Colocar con antelación al evento.'),
       (16, 'Refuerzo de comportamientos alternativos al saludar',
        'Enseñar al perro a sentarse o permanecer tranquilo durante el saludo.',
        'Técnicas de refuerzo positivo', 'Fácil', 'Premios',
        'Practicar con distintos visitantes.'),
       (17, 'Ignorar comportamiento de salto',
        'No prestar atención al perro cuando salta para evitar refuerzo.',
        'Modificación ambiental', 'Fácil', 'Constancia', 'Mantener postura neutral.'),
       (18, 'Reforzar cuando mantiene las cuatro patas en el suelo',
        'Premiar solo cuando el perro saluda sin saltar.',
        'Técnicas de refuerzo positivo', 'Fácil', 'Premios', 'Recompensar inmediatamente.'),
       (19, 'Proporcionar zona permitida para excavar',
        'Crear un área con tierra blanda destinada a la excavación.',
        'Modificación ambiental', 'Fácil', 'Caja de arena', 'Limitar acceso al resto del jardín.'),
       (20, 'Modificar entorno para evitar excavación',
        'Colocar barreras o cubrir áreas no deseadas.',
        'Modificación ambiental', 'Fácil', 'Barreras, Malla', 'Supervisar eficacia.'),
       (21, 'Entrenamiento en horarios fijos de eliminación',
        'Establecer rutinas de paseo y horarios regulares para eliminar.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Agenda, Premios', 'Ser consistente.'),
       (22, 'Refuerzo positivo en higiene',
        'Premiar al perro tras eliminar en el lugar correcto.',
        'Técnicas de refuerzo positivo', 'Fácil', 'Premios', 'Evitar regaños.'),
       (23, 'Supervisión y control de objetos peligrosos',
        'Limitar acceso a elementos no comestibles o tóxicos.',
        'Modificación ambiental', 'Fácil', 'Cestas, Puertas', 'Mantener orden diario.'),
       (24, 'Técnicas de distracción con masticables',
        'Ofrecer premios duraderos cuando empiece a lamerse.',
        'Técnicas de refuerzo positivo', 'Fácil', 'Masticables duraderos', 'Evitar alergias.'),
       (25, 'Entrenamiento en autocontrol',
        'Enseñar comandos como “deja” o “quieto” para prevenir persecuciones.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Clicker, Premios', 'Practicar en entorno controlado.'),
       (26, 'Entrenamiento de caminata suelta',
        'Recompensar al perro cuando camine sin tirar de la correa.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Correa corta, Premios',
        'Si el perro tira, detenerse y reiniciar.'),
       (27, 'Uso de arnés de control',
        'Utilizar arnés antitirones para paseos más cómodos.',
        'Modificación ambiental', 'Fácil', 'Arnés antitirones', 'Ajustar bien para evitar rozaduras.'),
       (28, 'Desensibilización gradual para protección de recursos',
        'Acercar la mano al recurso mientras se recompensa la calma.',
        'Desensibilización y contracondicionamiento', 'Intermedio', 'Premios', 'Progresar lentamente.'),
       (29, 'Entrenamiento en intercambio positivo de recursos',
        'Ofrecer un premio a cambio de soltar el recurso sin tensión.',
        'Técnicas de refuerzo positivo', 'Fácil', 'Premios', 'Reforzar intercambio voluntario.'),
       (30, 'Evitar confrontaciones',
        'No forzar al perro a soltar recursos para prevenir agresión.',
        'Modificación ambiental', 'Fácil', 'Espacio seguro', 'Mantener distancia hasta que esté calmado.'),
       (31, 'Modificación ambiental para hiperactividad',
        'Crear un área tranquila con poco estímulo para descanso.',
        'Modificación ambiental', 'Fácil', 'Cama cómoda, Zona aislada', 'Evitar ruidos y estímulos.'),
       (32, 'Desensibilización progresiva para fobia a personas o entornos',
        'Exposición controlada al estímulo generando asociaciones positivas.',
        'Desensibilización y contracondicionamiento', 'Avanzado', 'Premios, Entorno seguro',
        'Incrementar dificultad gradualmente.'),
       (33, 'Contracondicionamiento para ruidos fuertes',
        'Asociar sonidos con experiencias positivas como comida.',
        'Desensibilización y contracondicionamiento', 'Intermedio', 'Grabaciones, Premios',
        'Progresar según tolerancia.'),
       (34, 'Refuerzo positivo por atención al dueño',
        'Premiar al perro cuando mantenga la atención en el propietario.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Premios', 'Usar recompensas atractivas.'),
       (35, 'Refuerzo positivo durante el paseo',
        'Premiar al perro por caminar junto sin tirar de la correa.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Premios', 'Reforzar frecuentemente al inicio.'),
       (36, 'Refuerzo positivo general',
        'Usar recompensas para fomentar acercamiento y calma frente a estímulos temidos.',
        'Técnicas de refuerzo positivo', 'Intermedio', 'Premios de alto valor', 'Incrementar valor al acercarse más.'),
       (37, 'Modificación ambiental para fobia',
        'Evitar exponer al perro a estímulos que generen miedo durante el entrenamiento.',
        'Modificación ambiental', 'Fácil', 'Barreras, Entorno controlado', 'Reducir gradualmente la exposición.');

-- 3) PROBLEMAS DE CONDUCTA
INSERT IGNORE INTO problema_de_conducta (id_problema_conducta, nombre, descripcion)
VALUES (1, 'Ladrido excesivo', 'El perro ladra de manera constante o en situaciones inapropiadas.'),
       (2, 'Mordisqueo o masticación destructiva', 'El perro destruye objetos del hogar al masticarlos.'),
       (3, 'Ansiedad por separación', 'Comportamientos ansiosos cuando el dueño se va.'),
       (4, 'Agresión hacia otros perros o personas',
        'Actitudes agresivas como gruñidos, mordidas o posturas amenazantes.'),
       (5, 'Fobia a ruidos fuertes', 'Reacciones de miedo extremo frente a ruidos intensos.'),
       (6, 'Saltos sobre las personas', 'El perro salta sobre las personas al saludar.'),
       (7, 'Excavación excesiva', 'El perro cava agujeros en el jardín o en espacios interiores.'),
       (8, 'Orinar o defecar en lugares inapropiados', 'El perro no sigue hábitos de higiene y elimina donde no debe.'),
       (9, 'Persecución de objetos o animales', 'Persigue bicicletas, coches, gatos u otros animales.'),
       (10, 'Lamerse compulsivamente', 'El perro se lame excesivamente hasta causar lesiones.'),
       (11, 'Comer cosas inapropiadas (pica)', 'Ingestión de objetos no comestibles como piedras o basura.'),
       (12, 'Tirar de la correa al pasear', 'El perro tira constantemente y dificulta el paseo.'),
       (13, 'Protección de recursos', 'Gruñidos o ataques cuando alguien se acerca a su comida o juguetes.'),
       (14, 'Hiperactividad o dificultad para calmarse', 'El perro está constantemente excitado y no descansa.'),
       (15, 'Fobia a personas específicas o entornos', 'Miedo extremo hacia hombres, niños o ciertos lugares.');

-- 4) ASOCIACIONES CAUSA ↔ PROBLEMA
INSERT IGNORE INTO causa_de_problema (problema_id, causa_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 5),
       (2, 2),
       (2, 1),
       (3, 6),
       (3, 7),
       (4, 7),
       (4, 3),
       (4, 4),
       (4, 8),
       (5, 9),
       (5, 10),
       (6, 16),
       (6, 17),
       (7, 11),
       (7, 2),
       (7, 13),
       (7, 18),
       (8, 16),
       (8, 1),
       (8, 12),
       (9, 13),
       (9, 14),
       (9, 20),
       (10, 14),
       (10, 19),
       (10, 12),
       (11, 2),
       (11, 14),
       (11, 12),
       (12, 16),
       (12, 21),
       (12, 14),
       (13, 15),
       (13, 9),
       (13, 4),
       (14, 21),
       (14, 14),
       (14, 11),
       (15, 7),
       (15, 9),
       (15, 3);

-- 5) ASOCIACIONES SOLUCIÓN ↔ PROBLEMA
INSERT IGNORE INTO solucion_aplicada (id_problema_de_conducta, id_solucion)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (3, 8),
       (3, 9),
       (3, 10),
       (4, 3),
       (4, 11),
       (4, 12),
       (4, 13),
       (5, 3),
       (5, 33),
       (5, 14),
       (5, 15),
       (6, 16),
       (6, 17),
       (6, 18),
       (7, 19),
       (7, 6),
       (7, 20),
       (8, 21),
       (8, 22),
       (8, 13),
       (9, 26),
       (9, 25),
       (9, 34),
       (10, 13),
       (10, 6),
       (10, 24),
       (11, 23),
       (11, 6),
       (11, 25),
       (12, 26),
       (12, 27),
       (12, 35),
       (13, 28),
       (13, 29),
       (13, 30),
       (14, 6),
       (14, 12),
       (14, 31),
       (15, 32),
       (15, 36),
       (15, 37);

-- EDUCADORES
INSERT IGNORE INTO educador (id_educador, nombre, apellidos, email, telefono, especializacion, experiencia, formacion, descripcion)
VALUES (1, 'Laura', 'Martínez', 'laura@adiestra.com', '666111222', 'Obediencia básica', 5, 'Técnico en adiestramiento canino', 'Especializada en modificación de conducta');

-- CLIENTES
INSERT IGNORE INTO cliente (id_cliente, nombre, apellidos, email, telefono, educador_id)
VALUES (1, 'Carlos', 'Pérez', 'carlos@cliente.com', '666333444', 1);

-- ACTIVIDADES
INSERT IGNORE INTO actividad (id_actividad, nombre, descripcion, duracion, solucion_aplicada_id, completado)
VALUES
    (1, 'Ejercicio de quieto', 'El perro debe permanecer quieto durante 10 segundos.', 10, 1, false),
    (2, 'Caminar junto', 'Paseo con correa corta sin tirar.', 15, 2, false);

-- 6) PLAN_TRABAJO
INSERT IGNORE INTO plan_trabajo (id, cliente_id, observaciones)
VALUES (1, 1, 'Plan personalizado para reducir ladridos y mejorar la conducta en casa');

-- 7) PLAN_TRABAJO_PROBLEMAS (relación con problemas de conducta)
INSERT IGNORE INTO plan_trabajo_problemas (plan_trabajo_id, problemas_id)
VALUES (1, 1), -- Ladrido excesivo
       (1, 2); -- Mordisqueo

-- 8) PLAN_TRABAJO_ACTIVIDADES (relación con actividades existentes)
INSERT IGNORE INTO plan_trabajo_actividades (plan_trabajo_id, actividades_id)
VALUES (1, 1), -- Actividad 1
       (1, 2); -- Actividad 2

-- EDUCADORES
INSERT INTO educador (nombre, apellidos, email, telefono, especializacion, experiencia, formacion, descripcion) VALUES
                                                                                                                    ('Laura', 'Vega', 'laura@educapp.com', '600111222', 'Obediencia', 5, 'Psicología canina', 'Especialista en obediencia'),
                                                                                                                    ('Miguel', 'Santos', 'miguel@educapp.com', '600222333', 'Agresividad', 7, 'Etología clínica', 'Experto en casos complejos'),
                                                                                                                    ('Carmen', 'López', 'carmen@educapp.com', '600333444', 'Miedos', 6, 'Educadora certificada', 'Terapias de exposición'),
                                                                                                                    ('Pedro', 'Ruiz', 'pedro@educapp.com', '600444555', 'Socialización', 4, 'Adiestramiento positivo', 'Especialista en grupos'),
                                                                                                                    ('Ana', 'Martínez', 'ana@educapp.com', '600555666', 'Cachorros', 3, 'Clicker training', 'Trabaja con primeras etapas'),
                                                                                                                    ('Sofía', 'Gómez', 'sofia@educapp.com', '600666777', 'Ansiedad', 8, 'Comportamiento animal', 'Enfoque emocional');

-- CLIENTES
INSERT INTO cliente (nombre, apellidos, email, telefono, educador_id) VALUES
                                                                          ('Carlos', 'Fernández', 'carlos@cliente.com', '612345678', 1),
                                                                          ('Elena', 'García', 'elena@cliente.com', '612345679', 2),
                                                                          ('Raúl', 'Hernández', 'raul@cliente.com', '612345680', 3),
                                                                          ('Sandra', 'López', 'sandra@cliente.com', '612345681', 4),
                                                                          ('Iván', 'Pérez', 'ivan@cliente.com', '612345682', 5),
                                                                          ('Lucía', 'Ramírez', 'lucia@cliente.com', '612345683', 6);

-- PERROS
INSERT INTO perro (nombre, raza, sexo, edad, esterilizado, cliente_id) VALUES
                                                                           ('Rex', 'Labrador', 'M', 3, true, 1),
                                                                           ('Luna', 'Golden Retriever', 'H', 2, false, 2),
                                                                           ('Max', 'Pastor Alemán', 'M', 4, true, 3),
                                                                           ('Nala', 'Border Collie', 'H', 1, false, 4),
                                                                           ('Rocky', 'Beagle', 'M', 5, true, 5),
                                                                           ('Kira', 'Bulldog Francés', 'H', 2, true, 6);

-- PLANES DE TRABAJO
INSERT INTO plan_trabajo (observaciones, cliente_id) VALUES
                                                         ('Control de ansiedad y paseos', 1),
                                                         ('Reforzar obediencia', 2),
                                                         ('Mejorar sociabilidad', 3),
                                                         ('Plan para problemas de correa', 4),
                                                         ('Reducción de miedo a sonidos', 5),
                                                         ('Entrenamiento de calma', 6);

-- ACTIVIDADES
INSERT INTO actividad (nombre, descripcion, duracion, completado) VALUES
                                                                      ('Sentado y quieto', 'Obediencia básica en casa', 15, false),
                                                                      ('Caminar junto', 'Control de correa', 20, false),
                                                                      ('Juego interactivo', 'Estimulación mental', 30, false),
                                                                      ('Trabajo de olfato', 'Calmar ansiedad con olfato', 25, false),
                                                                      ('Exposición a sonidos', 'Reducir miedos', 20, false),
                                                                      ('Ejercicio físico', 'Juegos de pelota', 40, false);

-- RELACIONES CORRECTAS con planes 14–19 (los creados automáticamente por autoincrement)
INSERT INTO plan_trabajo_actividades (plan_trabajo_id, actividades_id) VALUES
                                                                           (14, 2), (14, 4),
                                                                           (15, 1), (15, 3),
                                                                           (16, 3),
                                                                           (17, 2), (17, 6),
                                                                           (18, 5),
                                                                           (19, 1), (19, 4);

INSERT INTO plan_trabajo_problemas (plan_trabajo_id, problemas_id) VALUES
                                                                       (14, 1), (14, 2),
                                                                       (15, 3),
                                                                       (16, 4),
                                                                       (17, 5),
                                                                       (18, 6),
                                                                       (19, 1), (19, 4);
