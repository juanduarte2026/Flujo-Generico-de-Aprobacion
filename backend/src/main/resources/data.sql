
-- =====================================================
-- 1. USUARIOS DEL SISTEMA
-- =====================================================

INSERT INTO users (username, password, full_name, email, active) VALUES
-- Solicitantes (Desarrolladores)
('jperez', 'password123', 'Juan Pérez', 'jperez@bank.com', true),
('mrodriguez', 'password123', 'María Rodríguez', 'mrodriguez@bank.com', true),
('lgarcia', 'password123', 'Luis García', 'lgarcia@bank.com', true),

-- Aprobadores (Líderes técnicos)
('mgomez', 'password123', 'Miguel Gómez', 'mgomez@bank.com', true),
('alopez', 'password123', 'Ana López', 'alopez@bank.com', true),
('csanchez', 'password123', 'Carlos Sánchez', 'csanchez@bank.com', true),

-- Usuario administrativo
('admin', 'admin123', 'Administrador del Sistema', 'admin@bank.com', true)
ON CONFLICT (username) DO NOTHING;

-- =====================================================
-- 2. TIPOS DE SOLICITUD
-- =====================================================
INSERT INTO request_types (code, name, description, is_active) VALUES
('DEPLOYMENT', 'Despliegue de Microservicio', 'Aprobación para publicar una nueva versión de un microservicio en producción', true),
('ACCESS', 'Acceso a Herramientas', 'Autorización de acceso a herramientas internas del banco (repositorios, servidores, bases de datos)', true),
('PIPELINE_CHANGE', 'Cambio en Pipeline CI/CD', 'Modificación en configuración de pipelines o procesos de integración continua', true),
('TECH_CATALOG', 'Incorporación al Catálogo Técnico', 'Aprobación para agregar una nueva herramienta o tecnología al catálogo oficial', true),
('CONFIG_CHANGE', 'Cambio de Configuración', 'Modificación en configuraciones de aplicaciones productivas', true),
('DATABASE_CHANGE', 'Cambio en Base de Datos', 'Aprobación para cambios en esquemas, migraciones o datos de producción', true)
ON CONFLICT (code) DO NOTHING;

-- =====================================================
-- 3. SOLICITUDES DE EJEMPLO
-- =====================================================

-- Solicitud 1: Despliegue PENDIENTE
INSERT INTO approval_requests (id, title, description, requester, approver, request_type_id, status, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    'Despliegue MS-Usuarios v2.1.0',
    'Nueva versión del microservicio de usuarios con correcciones de seguridad críticas y mejoras de rendimiento. Incluye actualización de dependencias y parches de vulnerabilidades.',
    'jperez',
    'mgomez',
    (SELECT id FROM request_types WHERE code = 'DEPLOYMENT'),
    'PENDING',
    NOW() - INTERVAL '2 hours',
    NOW() - INTERVAL '2 hours'
WHERE NOT EXISTS (SELECT 1 FROM approval_requests WHERE title = 'Despliegue MS-Usuarios v2.1.0');

-- Solicitud 2: Acceso APROBADO
INSERT INTO approval_requests (id, title, description, requester, approver, request_type_id, status, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    'Acceso a repositorio backend-core',
    'Solicitud de acceso de lectura y escritura al repositorio backend-core para el nuevo desarrollador del equipo.',
    'mrodriguez',
    'alopez',
    (SELECT id FROM request_types WHERE code = 'ACCESS'),
    'APPROVED',
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '3 hours'
WHERE NOT EXISTS (SELECT 1 FROM approval_requests WHERE title = 'Acceso a repositorio backend-core');

-- Solicitud 3: Pipeline RECHAZADO
INSERT INTO approval_requests (id, title, description, requester, approver, request_type_id, status, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    'Modificación pipeline MS-Pagos',
    'Cambio en el pipeline de MS-Pagos para agregar stage de pruebas de penetración automáticas.',
    'lgarcia',
    'csanchez',
    (SELECT id FROM request_types WHERE code = 'PIPELINE_CHANGE'),
    'REJECTED',
    NOW() - INTERVAL '3 days',
    NOW() - INTERVAL '2 days'
WHERE NOT EXISTS (SELECT 1 FROM approval_requests WHERE title = 'Modificación pipeline MS-Pagos');

-- Solicitud 4: Catálogo Técnico PENDIENTE
INSERT INTO approval_requests (id, title, description, requester, approver, request_type_id, status, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    'Incorporar Kotlin al catálogo de lenguajes',
    'Solicitud para agregar Kotlin como lenguaje aprobado para desarrollo de microservicios. Incluye justificación técnica y análisis de beneficios.',
    'jperez',
    'mgomez',
    (SELECT id FROM request_types WHERE code = 'TECH_CATALOG'),
    'PENDING',
    NOW() - INTERVAL '5 hours',
    NOW() - INTERVAL '5 hours'
WHERE NOT EXISTS (SELECT 1 FROM approval_requests WHERE title = 'Incorporar Kotlin al catálogo de lenguajes');

-- Solicitud 5: Cambio de Configuración PENDIENTE
INSERT INTO approval_requests (id, title, description, requester, approver, request_type_id, status, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    'Aumentar timeout MS-Transacciones',
    'Incrementar el timeout de conexión del microservicio de transacciones de 30s a 60s debido a latencias en servicios externos.',
    'mrodriguez',
    'alopez',
    (SELECT id FROM request_types WHERE code = 'CONFIG_CHANGE'),
    'PENDING',
    NOW() - INTERVAL '30 minutes',
    NOW() - INTERVAL '30 minutes'
WHERE NOT EXISTS (SELECT 1 FROM approval_requests WHERE title = 'Aumentar timeout MS-Transacciones');

-- =====================================================
-- 4. HISTORIAL DE SOLICITUDES
-- =====================================================

-- Historial para solicitud aprobada
INSERT INTO approval_history (request_id, action, comment, performed_by, performed_at)
SELECT 
    ar.id,
    'CREATED',
    'Solicitud creada',
    'mrodriguez',
    ar.created_at
FROM approval_requests ar
WHERE ar.title = 'Acceso a repositorio backend-core'
AND NOT EXISTS (
    SELECT 1 FROM approval_history ah 
    WHERE ah.request_id = ar.id AND ah.action = 'CREATED'
);

INSERT INTO approval_history (request_id, action, comment, performed_by, performed_at)
SELECT 
    ar.id,
    'APPROVED',
    'Acceso aprobado. El usuario ha completado la capacitación de seguridad requerida.',
    'alopez',
    ar.updated_at
FROM approval_requests ar
WHERE ar.title = 'Acceso a repositorio backend-core'
AND NOT EXISTS (
    SELECT 1 FROM approval_history ah 
    WHERE ah.request_id = ar.id AND ah.action = 'APPROVED'
);

-- Historial para solicitud rechazada
INSERT INTO approval_history (request_id, action, comment, performed_by, performed_at)
SELECT 
    ar.id,
    'CREATED',
    'Solicitud creada',
    'lgarcia',
    ar.created_at
FROM approval_requests ar
WHERE ar.title = 'Modificación pipeline MS-Pagos'
AND NOT EXISTS (
    SELECT 1 FROM approval_history ah 
    WHERE ah.request_id = ar.id AND ah.action = 'CREATED'
);

INSERT INTO approval_history (request_id, action, comment, performed_by, performed_at)
SELECT 
    ar.id,
    'REJECTED',
    'Rechazado temporalmente. Las pruebas de penetración deben coordinarse con el equipo de seguridad antes de automatizarlas en el pipeline.',
    'csanchez',
    ar.updated_at
FROM approval_requests ar
WHERE ar.title = 'Modificación pipeline MS-Pagos'
AND NOT EXISTS (
    SELECT 1 FROM approval_history ah 
    WHERE ah.request_id = ar.id AND ah.action = 'REJECTED'
);

-- Historial para solicitudes pendientes (solo creación)
INSERT INTO approval_history (request_id, action, comment, performed_by, performed_at)
SELECT 
    ar.id,
    'CREATED',
    'Solicitud creada',
    ar.requester,
    ar.created_at
FROM approval_requests ar
WHERE ar.status = 'PENDING'
AND NOT EXISTS (
    SELECT 1 FROM approval_history ah 
    WHERE ah.request_id = ar.id
);
