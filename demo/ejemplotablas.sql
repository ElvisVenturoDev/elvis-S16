-- ============================================================
-- Base de datos: imprenta_db
-- Proyecto   : Sistema de Pedidos de Imprenta (JavaFX + MySQL)
-- Autor      : Elvis Leonardo Venturo Prieto
-- Motor      : MySQL 8.4 (contenedor Docker)
-- ============================================================

CREATE DATABASE IF NOT EXISTS imprenta_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE imprenta_db;

-- ------------------------------------------------------------
-- 1. CLIENTES
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente  INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    documento   VARCHAR(20)  NOT NULL,
    telefono    VARCHAR(20)  NULL,
    correo      VARCHAR(100) NULL,
    direccion   VARCHAR(150) NULL,
    PRIMARY KEY (id_cliente),
    UNIQUE KEY uq_clientes_documento (documento)
);

-- ------------------------------------------------------------
-- 2. EMPLEADOS
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS empleados (
    id_empleado INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    cargo       VARCHAR(60)  NOT NULL,
    telefono    VARCHAR(20)  NULL,
    correo      VARCHAR(100) NULL,
    PRIMARY KEY (id_empleado)
);

-- ------------------------------------------------------------
-- 3. IMPRETA (tabla principal de pedidos)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Impreta (
    id_pedido      INT          NOT NULL AUTO_INCREMENT,
    nombre_cliente VARCHAR(100) NOT NULL,
    tipo_trabajo   VARCHAR(100) NOT NULL,
    cantidad       INT          NOT NULL,
    formato        VARCHAR(50)  NOT NULL,
    fecha_pedido   DATE         NOT NULL,
    fecha_entrega  DATE         NOT NULL,
    estado         VARCHAR(30)  NOT NULL DEFAULT 'Pendiente',
    id_cliente     INT          NULL,
    id_empleado    INT          NULL,
    PRIMARY KEY (id_pedido),
    KEY idx_imprenta_cliente (id_cliente),
    KEY idx_imprenta_empleado (id_empleado),
    CONSTRAINT fk_imprenta_cliente
        FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_imprenta_empleado
        FOREIGN KEY (id_empleado) REFERENCES empleados (id_empleado)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- ------------------------------------------------------------
-- 4. PAGOS
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pagos (
    id_pago     INT           NOT NULL AUTO_INCREMENT,
    id_pedido   INT           NOT NULL,
    monto       DECIMAL(10,2) NOT NULL,
    metodo      VARCHAR(40)   NOT NULL DEFAULT 'Efectivo',
    fecha_pago  DATE          NOT NULL,
    estado      VARCHAR(30)   NOT NULL DEFAULT 'Pendiente',
    PRIMARY KEY (id_pago),
    KEY idx_pagos_pedido (id_pedido),
    CONSTRAINT fk_pagos_pedido
        FOREIGN KEY (id_pedido) REFERENCES Impreta (id_pedido)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- DATOS DE EJEMPLO
-- ============================================================

INSERT INTO clientes (id_cliente, nombre, documento, telefono, correo, direccion)
VALUES (1, 'Elvis Leonardo Venturo Prieto', '76543210', '987654321',
        'elvis.venturo@vallegrande.edu.pe', 'Av. Los Próceres 120, Lima'),
       (2, 'Comercial Andina S.A.C.', '20548796321', '01-4567890',
        'ventas@comercialandina.pe', 'Jr. Comercio 455, Lima'),
       (3, 'I.E. Valle Grande', '20481234567', '01-7894561',
        'secretaria@vallegrande.edu.pe', 'Av. Valle Grande 890, Lima')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO empleados (id_empleado, nombre, cargo, telefono, correo)
VALUES (1, 'Carlos Mendoza Ríos', 'Operador de impresión', '987111222',
        'carlos.mendoza@imprenta.pe'),
       (2, 'María Fernández Loayza', 'Diseñadora gráfica', '987333444',
        'maria.fernandez@imprenta.pe'),
       (3, 'José Ramírez Torres', 'Jefe de producción', '987555666',
        'jose.ramirez@imprenta.pe')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO Impreta (id_pedido, nombre_cliente, tipo_trabajo, cantidad, formato,
                     fecha_pedido, fecha_entrega, estado, id_cliente, id_empleado)
VALUES (1, 'Elvis Leonardo Venturo Prieto', 'Impreta', 500, 'A4',
        '2026-11-13', '2026-11-20', 'Pendiente', 1, 2),
       (2, 'Comercial Andina S.A.C.', 'Volantes publicitarios', 1000, 'A5',
        '2026-11-10', '2026-11-18', 'En proceso', 2, 1),
       (3, 'I.E. Valle Grande', 'Libros escolares', 250, 'A4',
        '2026-11-05', '2026-11-25', 'Terminado', 3, 3)
ON DUPLICATE KEY UPDATE nombre_cliente = VALUES(nombre_cliente);

INSERT INTO pagos (id_pago, id_pedido, monto, metodo, fecha_pago, estado)
VALUES (1, 1, 250.00, 'Efectivo', '2026-11-13', 'Adelanto'),
       (2, 2, 480.00, 'Yape', '2026-11-11', 'Pendiente'),
       (3, 3, 900.00, 'Transferencia', '2026-11-06', 'Pagado')
ON DUPLICATE KEY UPDATE monto = VALUES(monto);

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
SHOW TABLES;

SELECT * FROM clientes;
SELECT * FROM empleados;
SELECT * FROM Impreta;
SELECT * FROM pagos;
