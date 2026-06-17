-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-06-2026 a las 06:36:26
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `food_express`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_login_usuario` (IN `p_user` VARCHAR(20), IN `p_pass` VARCHAR(100))   BEGIN

    -- Usuario no existe
    IF NOT EXISTS (SELECT 1 FROM Usuario WHERE CodUsuario = p_user) THEN
        SELECT 'ERROR_USER' AS estado;

    -- Contraseña incorrecta
    ELSEIF NOT EXISTS (SELECT 1 FROM Usuario WHERE CodUsuario = p_user AND Contrasena = p_pass) THEN
        SELECT 'ERROR_PASS' AS estado;

    -- Usuario inactivo
    ELSEIF EXISTS (SELECT 1 FROM Usuario WHERE CodUsuario = p_user AND Estado = 'INA') THEN
        SELECT 'INACTIVO' AS estado;

    -- Login correcto
    ELSE
        SELECT 
            CodUsuario,
            Nombre,
            CodRol,
            'OK' AS estado
        FROM Usuario
        WHERE CodUsuario = p_user;
    END IF;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alimento`
--

CREATE TABLE `alimento` (
  `CodAlimento` varchar(20) NOT NULL,
  `CodCategoria` varchar(10) NOT NULL,
  `Nombre` varchar(160) NOT NULL,
  `Descripcion` varchar(500) DEFAULT NULL,
  `Rutaimagen` varchar(500) DEFAULT NULL,
  `DescuentoPromocion` decimal(10,2) NOT NULL DEFAULT 0.00,
  `Precio` decimal(10,2) NOT NULL,
  `Estado` varchar(3) NOT NULL DEFAULT 'ACT',
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alimento`
--

INSERT INTO `alimento` (`CodAlimento`, `CodCategoria`, `Nombre`, `Descripcion`, `Rutaimagen`, `DescuentoPromocion`, `Precio`, `Estado`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('ALI001', 'CAT001', 'Pollo a la Brasa', 'Pollo marinado con especias, papas fritas y ensalada fresca.', '/img/platos/1e469373-7301-4ec5-a007-a302a785a42f.jpg', 0.00, 60.00, 'ACT', 'dbo', '2026-05-27 20:51:28', 'ch4lzeus', '2026-06-06 03:05:00'),
('ALI002', 'CAT001', 'Lomo Saltado', 'Salteado de carne, tomate, cebolla, papas fritas y arroz blanco.', '/img/platos/bae017b7-d665-4004-867d-d01e33b617fe.jpg', 0.00, 32.00, 'ACT', 'dbo', '2026-05-27 20:51:28', 'ch4lzeus', '2026-06-05 01:52:26'),
('ALI003', 'CAT002', 'Chicha Morada', 'Bebida refrescante de maiz morado con frutas y especias.', '/img/platos/c94a1ebc-735d-4e5c-8646-d2504c8ab6b8.jpg', 0.00, 10.00, 'ACT', 'dbo', '2026-05-27 20:51:28', 'ch4lzeus', '2026-06-05 02:11:40');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `CodCategoria` varchar(10) NOT NULL,
  `Nombre` varchar(120) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`CodCategoria`, `Nombre`, `UsuCreacion`, `FecCreacion`) VALUES
('CAT001', 'Platos de fondo', 'dbo', '2026-05-27 20:51:28'),
('CAT002', 'Bebidas', 'dbo', '2026-05-27 20:51:28'),
('CAT003', 'Entradas', 'dbo', '2026-05-27 20:51:28'),
('CAT004', 'Postres', 'dbo', '2026-05-27 20:51:28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `CodCliente` varchar(20) NOT NULL,
  `CodUsuario` varchar(20) DEFAULT NULL,
  `CodTipoDocumento` varchar(10) NOT NULL,
  `NroDocumento` varchar(20) NOT NULL,
  `RazonSocial` varchar(250) NOT NULL,
  `Direccion` varchar(250) DEFAULT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`CodCliente`, `CodUsuario`, `CodTipoDocumento`, `NroDocumento`, `RazonSocial`, `Direccion`, `Telefono`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('CLI000', NULL, 'TD001', '00000000', 'Cliente General', NULL, NULL, 'dbo', '2026-06-03 21:03:10', NULL, NULL),
('CLI001', 'Cliente1', 'TD001', '40234756', 'Cliente Prueba', NULL, NULL, 'Cliente1', '2026-06-03 21:20:56', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comprobante`
--

CREATE TABLE `comprobante` (
  `CodComprobante` varchar(20) NOT NULL,
  `CodTipoComprobante` varchar(10) NOT NULL,
  `NroPedido` varchar(20) NOT NULL,
  `CodCliente` varchar(20) NOT NULL,
  `NroComprobante` varchar(30) NOT NULL,
  `Estado` varchar(20) NOT NULL DEFAULT 'EMITIDO',
  `FecEmision` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comprobante_detalle`
--

CREATE TABLE `comprobante_detalle` (
  `CodComprobanteDetalle` varchar(20) NOT NULL,
  `CodComprobante` varchar(20) NOT NULL,
  `CodAlimento` varchar(20) NOT NULL,
  `Cantidad` int(11) NOT NULL,
  `Precio` decimal(10,2) NOT NULL,
  `DescuentoPromocion` decimal(10,2) NOT NULL DEFAULT 0.00,
  `DescuentoMonto` decimal(10,2) NOT NULL DEFAULT 0.00,
  `SubTotal` decimal(10,2) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `correlativo_comprobante`
--

CREATE TABLE `correlativo_comprobante` (
  `CodCorrelativoComprobante` varchar(20) NOT NULL,
  `CodTipoComprobante` varchar(10) NOT NULL,
  `Serie` varchar(10) NOT NULL,
  `Correlativo` int(11) NOT NULL DEFAULT 1,
  `CorrelativoActual` int(11) NOT NULL DEFAULT 0,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `correlativo_comprobante`
--

INSERT INTO `correlativo_comprobante` (`CodCorrelativoComprobante`, `CodTipoComprobante`, `Serie`, `Correlativo`, `CorrelativoActual`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('COR001', 'TC001', 'B001', 1, 0, 'dbo', '2026-05-27 20:51:28', NULL, NULL),
('COR002', 'TC002', 'F001', 1, 0, 'dbo', '2026-05-27 20:51:28', NULL, NULL),
('COR003', 'TC003', 'T001', 1, 0, 'dbo', '2026-05-27 20:51:28', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mesa`
--

CREATE TABLE `mesa` (
  `NroMesa` varchar(10) NOT NULL,
  `Descripcion` varchar(160) DEFAULT NULL,
  `Capacidad` int(11) NOT NULL DEFAULT 1,
  `Estado` varchar(3) NOT NULL DEFAULT 'ACT',
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mesa`
--

INSERT INTO `mesa` (`NroMesa`, `Descripcion`, `Capacidad`, `Estado`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('M001', 'Mesa 1', 5, 'ACT', 'dbo', '2026-05-27 20:51:28', 'ch4lzeus', '2026-06-06 03:04:40'),
('M002', 'Mesa 2', 5, 'ACT', 'dbo', '2026-05-27 20:51:28', 'ch4lzeus', '2026-06-06 03:04:14'),
('M003', 'Mesa 3', 6, 'ACT', 'dbo', '2026-05-27 20:51:28', NULL, NULL),
('M004', 'Mesa 4', 2, 'ACT', 'dbo', '2026-05-27 20:51:28', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `metodo_pago`
--

CREATE TABLE `metodo_pago` (
  `CodMetodoPago` varchar(10) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `metodo_pago`
--

INSERT INTO `metodo_pago` (`CodMetodoPago`, `Nombre`, `UsuCreacion`, `FecCreacion`) VALUES
('MP001', 'Efectivo', 'dbo', '2026-05-27 20:51:28'),
('MP002', 'Tarjeta', 'dbo', '2026-05-27 20:51:28'),
('MP003', 'Yape', 'dbo', '2026-05-27 20:51:28'),
('MP004', 'Plin', 'dbo', '2026-05-27 20:51:28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `modulo`
--

CREATE TABLE `modulo` (
  `CodModulo` varchar(10) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `cod_modulo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `modulo`
--

INSERT INTO `modulo` (`CodModulo`, `nombre`, `UsuCreacion`, `FecCreacion`, `cod_modulo`) VALUES
('MOD001', 'Dashboard', 'dbo', '2026-05-27 20:51:26', ''),
('MOD002', 'Catalogo', 'dbo', '2026-05-27 20:51:26', ''),
('MOD003', 'Usuarios', 'dbo', '2026-05-27 20:51:26', ''),
('MOD004', 'Cocina', 'dbo', '2026-05-27 20:51:26', ''),
('MOD005', 'Pedidos', 'dbo', '2026-05-27 20:51:26', ''),
('MOD006', 'Pagos', 'dbo', '2026-05-27 20:51:26', ''),
('MOD007', 'Entregas', 'dbo', '2026-05-27 20:51:26', ''),
('MOD008', 'Reportes', 'dbo', '2026-05-27 20:51:26', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pago`
--

CREATE TABLE `pago` (
  `CodComprobante` varchar(20) NOT NULL,
  `CodMetodoPago` varchar(10) NOT NULL,
  `Monto` decimal(10,2) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido`
--

CREATE TABLE `pedido` (
  `NroPedido` varchar(20) NOT NULL,
  `NroMesa` varchar(10) DEFAULT NULL,
  `CodCliente` varchar(20) NOT NULL,
  `EsLocal` tinyint(1) NOT NULL DEFAULT 0,
  `Direccion` varchar(250) DEFAULT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Descripcion` varchar(500) DEFAULT NULL,
  `Estado` varchar(20) NOT NULL DEFAULT 'PENDIENTE',
  `FecPedido` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL,
  `Total` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedido`
--

INSERT INTO `pedido` (`NroPedido`, `NroMesa`, `CodCliente`, `EsLocal`, `Direccion`, `Telefono`, `Descripcion`, `Estado`, `FecPedido`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`, `Total`) VALUES
('PED001', NULL, 'CLI001', 0, 'Alfonso Ugarte', '933733449', 'Con la Av. Túpac amaru', 'LISTO', '2026-06-06 02:26:58', 'Cliente1', '2026-06-06 02:26:58', 'ch4lzeus', '2026-06-08 18:55:09', 32.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido_detalle`
--

CREATE TABLE `pedido_detalle` (
  `CodPedidoDetalle` varchar(20) NOT NULL,
  `NroPedido` varchar(20) NOT NULL,
  `CodAlimento` varchar(20) NOT NULL,
  `Cantidad` int(11) NOT NULL,
  `Precio` decimal(10,2) NOT NULL,
  `DescuentoPromocion` decimal(10,2) NOT NULL DEFAULT 0.00,
  `DescuentoMonto` decimal(10,2) NOT NULL DEFAULT 0.00,
  `SubTotal` decimal(10,2) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedido_detalle`
--

INSERT INTO `pedido_detalle` (`CodPedidoDetalle`, `NroPedido`, `CodAlimento`, `Cantidad`, `Precio`, `DescuentoPromocion`, `DescuentoMonto`, `SubTotal`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('DET001', 'PED001', 'ALI002', 1, 32.00, 0.00, 0.00, 32.00, 'Cliente1', '2026-06-06 02:26:58', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva`
--

CREATE TABLE `reserva` (
  `CodReserva` varchar(20) NOT NULL,
  `NroMesa` varchar(10) NOT NULL,
  `CodCliente` varchar(20) NOT NULL,
  `FecReserva` datetime NOT NULL,
  `Estado` varchar(3) NOT NULL DEFAULT 'ACT',
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `reserva`
--

INSERT INTO `reserva` (`CodReserva`, `NroMesa`, `CodCliente`, `FecReserva`, `Estado`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('RES001', 'M001', 'CLI000', '2026-06-08 01:47:00', 'ACT', 'ch4lzeus', '2026-06-06 01:47:39', 'ch4lzeus', '2026-06-06 03:06:09');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `CodRol` varchar(5) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `cod_rol` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`CodRol`, `nombre`, `UsuCreacion`, `FecCreacion`, `cod_rol`) VALUES
('ADMIN', 'Administrador', 'dbo', '2026-04-17 18:09:47', ''),
('CHEF', 'Cocinero', 'dbo', '2026-04-17 18:12:36', ''),
('CLI', 'Cliente', 'dbo', '2026-05-02 19:58:00', ''),
('DELI', 'Delivery', 'dbo', '2026-05-02 19:58:00', ''),
('MESE', 'Mesero', 'dbo', '2026-05-02 19:58:00', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_modulo`
--

CREATE TABLE `rol_modulo` (
  `CodRol` varchar(5) NOT NULL,
  `CodModulo` varchar(10) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol_modulo`
--

INSERT INTO `rol_modulo` (`CodRol`, `CodModulo`, `UsuCreacion`, `FecCreacion`) VALUES
('ADMIN', 'MOD001', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD002', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD003', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD004', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD005', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD006', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD007', 'dbo', '2026-05-27 20:51:26'),
('ADMIN', 'MOD008', 'dbo', '2026-05-27 20:51:26'),
('CHEF', 'MOD004', 'dbo', '2026-05-27 20:51:26'),
('CHEF', 'MOD005', 'dbo', '2026-05-27 20:51:26'),
('DELI', 'MOD007', 'dbo', '2026-05-27 20:51:26'),
('MESE', 'MOD004', 'dbo', '2026-05-27 20:51:26'),
('MESE', 'MOD005', 'dbo', '2026-05-27 20:51:26'),
('MESE', 'MOD006', 'dbo', '2026-05-27 20:51:26');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_comprobante`
--

CREATE TABLE `tipo_comprobante` (
  `CodTipoComprobante` varchar(10) NOT NULL,
  `Nombre` varchar(120) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_comprobante`
--

INSERT INTO `tipo_comprobante` (`CodTipoComprobante`, `Nombre`, `UsuCreacion`, `FecCreacion`) VALUES
('TC001', 'Boleta de Venta', 'dbo', '2026-05-27 20:51:28'),
('TC002', 'Factura', 'dbo', '2026-05-27 20:51:28'),
('TC003', 'Ticket', 'dbo', '2026-05-27 20:51:28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_documento`
--

CREATE TABLE `tipo_documento` (
  `CodTipoDocumento` varchar(10) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_documento`
--

INSERT INTO `tipo_documento` (`CodTipoDocumento`, `Nombre`, `UsuCreacion`, `FecCreacion`) VALUES
('TD001', 'DNI', 'dbo', '2026-05-27 20:51:28'),
('TD002', 'RUC', 'dbo', '2026-05-27 20:51:28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `CodUsuario` varchar(20) NOT NULL,
  `CodRol` varchar(5) NOT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `UsuCreacion` varchar(20) NOT NULL DEFAULT 'dbo',
  `FecCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `UsuModificacion` varchar(20) DEFAULT NULL,
  `FecModificacion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`CodUsuario`, `CodRol`, `dni`, `nombre`, `estado`, `contrasena`, `UsuCreacion`, `FecCreacion`, `UsuModificacion`, `FecModificacion`) VALUES
('ch4lzeus', 'ADMIN', '12345678', 'Luis Chalan', 'ACT', 'admin123', 'dbo', '2026-05-02 20:03:02', 'dbo', '2026-05-06 12:23:36'),
('Cliente1', 'CLI', '40234756', 'Cliente Prueba', 'ACT', 'cliente123', 'Cliente1', '2026-06-03 21:20:56', NULL, NULL),
('jcori', 'ADMIN', '76478687', 'Joel Test', 'INA', 'delivery123', 'dbo', '2026-04-17 18:22:28', 'dbo', '2026-05-23 13:58:00'),
('neymar', 'MESE', '12345678', 'Neymar delivery', 'INA', 'delivery123', 'neymar', '2026-05-25 17:52:59', NULL, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alimento`
--
ALTER TABLE `alimento`
  ADD PRIMARY KEY (`CodAlimento`),
  ADD KEY `fk_alimento_categoria` (`CodCategoria`);

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`CodCategoria`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`CodCliente`),
  ADD UNIQUE KEY `uk_cliente_usuario` (`CodUsuario`),
  ADD KEY `fk_cliente_tipodocumento` (`CodTipoDocumento`);

--
-- Indices de la tabla `comprobante`
--
ALTER TABLE `comprobante`
  ADD PRIMARY KEY (`CodComprobante`),
  ADD UNIQUE KEY `uk_comprobante_nro` (`NroComprobante`),
  ADD KEY `fk_comprobante_tipocomprobante` (`CodTipoComprobante`),
  ADD KEY `fk_comprobante_pedido` (`NroPedido`),
  ADD KEY `fk_comprobante_cliente` (`CodCliente`);

--
-- Indices de la tabla `comprobante_detalle`
--
ALTER TABLE `comprobante_detalle`
  ADD PRIMARY KEY (`CodComprobanteDetalle`),
  ADD KEY `fk_comprobantedetalle_comprobante` (`CodComprobante`),
  ADD KEY `fk_comprobantedetalle_alimento` (`CodAlimento`);

--
-- Indices de la tabla `correlativo_comprobante`
--
ALTER TABLE `correlativo_comprobante`
  ADD PRIMARY KEY (`CodCorrelativoComprobante`),
  ADD KEY `fk_correlativo_tipocomprobante` (`CodTipoComprobante`);

--
-- Indices de la tabla `mesa`
--
ALTER TABLE `mesa`
  ADD PRIMARY KEY (`NroMesa`);

--
-- Indices de la tabla `metodo_pago`
--
ALTER TABLE `metodo_pago`
  ADD PRIMARY KEY (`CodMetodoPago`);

--
-- Indices de la tabla `modulo`
--
ALTER TABLE `modulo`
  ADD PRIMARY KEY (`CodModulo`);

--
-- Indices de la tabla `pago`
--
ALTER TABLE `pago`
  ADD PRIMARY KEY (`CodComprobante`),
  ADD KEY `fk_pago_metodopago` (`CodMetodoPago`);

--
-- Indices de la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD PRIMARY KEY (`NroPedido`),
  ADD KEY `fk_pedido_mesa` (`NroMesa`),
  ADD KEY `fk_pedido_cliente` (`CodCliente`);

--
-- Indices de la tabla `pedido_detalle`
--
ALTER TABLE `pedido_detalle`
  ADD PRIMARY KEY (`CodPedidoDetalle`),
  ADD KEY `fk_pedidodetalle_pedido` (`NroPedido`),
  ADD KEY `fk_pedidodetalle_alimento` (`CodAlimento`);

--
-- Indices de la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD PRIMARY KEY (`CodReserva`),
  ADD KEY `fk_reserva_mesa` (`NroMesa`),
  ADD KEY `fk_reserva_cliente` (`CodCliente`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`CodRol`),
  ADD UNIQUE KEY `CodRol` (`CodRol`);

--
-- Indices de la tabla `rol_modulo`
--
ALTER TABLE `rol_modulo`
  ADD PRIMARY KEY (`CodRol`,`CodModulo`),
  ADD KEY `fk_rolmodulo_modulo` (`CodModulo`);

--
-- Indices de la tabla `tipo_comprobante`
--
ALTER TABLE `tipo_comprobante`
  ADD PRIMARY KEY (`CodTipoComprobante`);

--
-- Indices de la tabla `tipo_documento`
--
ALTER TABLE `tipo_documento`
  ADD PRIMARY KEY (`CodTipoDocumento`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`CodUsuario`),
  ADD KEY `fk_usuario_rol` (`CodRol`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alimento`
--
ALTER TABLE `alimento`
  ADD CONSTRAINT `fk_alimento_categoria` FOREIGN KEY (`CodCategoria`) REFERENCES `categoria` (`CodCategoria`);

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `fk_cliente_tipodocumento` FOREIGN KEY (`CodTipoDocumento`) REFERENCES `tipo_documento` (`CodTipoDocumento`),
  ADD CONSTRAINT `fk_cliente_usuario` FOREIGN KEY (`CodUsuario`) REFERENCES `usuario` (`CodUsuario`);

--
-- Filtros para la tabla `comprobante`
--
ALTER TABLE `comprobante`
  ADD CONSTRAINT `fk_comprobante_cliente` FOREIGN KEY (`CodCliente`) REFERENCES `cliente` (`CodCliente`),
  ADD CONSTRAINT `fk_comprobante_pedido` FOREIGN KEY (`NroPedido`) REFERENCES `pedido` (`NroPedido`),
  ADD CONSTRAINT `fk_comprobante_tipocomprobante` FOREIGN KEY (`CodTipoComprobante`) REFERENCES `tipo_comprobante` (`CodTipoComprobante`);

--
-- Filtros para la tabla `comprobante_detalle`
--
ALTER TABLE `comprobante_detalle`
  ADD CONSTRAINT `fk_comprobantedetalle_alimento` FOREIGN KEY (`CodAlimento`) REFERENCES `alimento` (`CodAlimento`),
  ADD CONSTRAINT `fk_comprobantedetalle_comprobante` FOREIGN KEY (`CodComprobante`) REFERENCES `comprobante` (`CodComprobante`);

--
-- Filtros para la tabla `correlativo_comprobante`
--
ALTER TABLE `correlativo_comprobante`
  ADD CONSTRAINT `fk_correlativo_tipocomprobante` FOREIGN KEY (`CodTipoComprobante`) REFERENCES `tipo_comprobante` (`CodTipoComprobante`);

--
-- Filtros para la tabla `pago`
--
ALTER TABLE `pago`
  ADD CONSTRAINT `fk_pago_comprobante` FOREIGN KEY (`CodComprobante`) REFERENCES `comprobante` (`CodComprobante`),
  ADD CONSTRAINT `fk_pago_metodopago` FOREIGN KEY (`CodMetodoPago`) REFERENCES `metodo_pago` (`CodMetodoPago`);

--
-- Filtros para la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD CONSTRAINT `fk_pedido_cliente` FOREIGN KEY (`CodCliente`) REFERENCES `cliente` (`CodCliente`),
  ADD CONSTRAINT `fk_pedido_mesa` FOREIGN KEY (`NroMesa`) REFERENCES `mesa` (`NroMesa`);

--
-- Filtros para la tabla `pedido_detalle`
--
ALTER TABLE `pedido_detalle`
  ADD CONSTRAINT `fk_pedidodetalle_alimento` FOREIGN KEY (`CodAlimento`) REFERENCES `alimento` (`CodAlimento`),
  ADD CONSTRAINT `fk_pedidodetalle_pedido` FOREIGN KEY (`NroPedido`) REFERENCES `pedido` (`NroPedido`);

--
-- Filtros para la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `fk_reserva_cliente` FOREIGN KEY (`CodCliente`) REFERENCES `cliente` (`CodCliente`),
  ADD CONSTRAINT `fk_reserva_mesa` FOREIGN KEY (`NroMesa`) REFERENCES `mesa` (`NroMesa`);

--
-- Filtros para la tabla `rol_modulo`
--
ALTER TABLE `rol_modulo`
  ADD CONSTRAINT `fk_rolmodulo_modulo` FOREIGN KEY (`CodModulo`) REFERENCES `modulo` (`CodModulo`),
  ADD CONSTRAINT `fk_rolmodulo_rol` FOREIGN KEY (`CodRol`) REFERENCES `rol` (`CodRol`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`CodRol`) REFERENCES `rol` (`CodRol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
