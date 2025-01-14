--DROP TABLE IF EXISTS Inventario;
CREATE TABLE IF NOT EXISTS Inventario(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL DEFAULT 'NOMBRE',
    descripcion TEXT NOT NULL DEFAULT 'DESCRIPCION',
    ubicacion TEXT NOT NULL DEFAULT 'DEPOSITO',
    foto TEXT NOT NULL DEFAULT 'img_item_default.jpg',
    estado INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT CK_Inventario_ubicacion CHECK(ubicacion IN ('DEPOSITO', 'PRESTADO'))
);

--DROP TABLE IF EXISTS Usuario;
CREATE TABLE IF NOT EXISTS Usuario(
    nombreUsuario TEXT NOT NULL,
    clave TEXT NOT NULL DEFAULT 'Clave1234@',
    nombre TEXT NOT NULL DEFAULT 'NOMBRE',
    apellido TEXT NOT NULL DEFAULT 'APELLIDO',
    rol TEXT NOT NULL DEFAULT 'PRESTATARIO',
    foto TEXT NOT NULL DEFAULT 'img_avatar_default.jpg',
    estado INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT PK_Usuario PRIMARY KEY(nombreUsuario),
    CONSTRAINT CK_Usuario_rol CHECK(rol IN ('ADMIN', 'PRESTATARIO'))
);

--DROP TABLE IF EXISTS Admin;
CREATE TABLE IF NOT EXISTS Admin(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    estado INTEGER NOT NULL DEFAULT 1,
    id_usuario TEXT NOT NULL,
    CONSTRAINT FK_Admin_Usuario FOREIGN KEY(id_usuario) REFERENCES Usuario(nombreUsuario),
    CONSTRAINT UK_Admin_idusuario UNIQUE(id_usuario)
);

--DROP TABLE IF EXISTS Prestatario;
CREATE TABLE IF NOT EXISTS Prestatario(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    estado INTEGER NOT NULL DEFAULT 1,
    id_usuario TEXT NOT NULL,
    CONSTRAINT FK_Prestatario_Usuario FOREIGN KEY(id_usuario) REFERENCES Usuario(nombreUsuario),
    CONSTRAINT UK_Prestatario_idusuario UNIQUE(id_usuario)
);

--DROP TABLE IF EXISTS Prestamo;
CREATE TABLE IF NOT EXISTS Prestamo(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaSolicitud DATETIME NOT NULL DEFAULT (DATETIME('now', 'localtime')),
    fechaDevolucion DATETIME NOT NULL DEFAULT (DATETIME('now', 'localtime','+3 days')),
    estadoPrestamo TEXT NOT NULL DEFAULT 'ACEPTADO',
    estado INTEGER NOT NULL DEFAULT 1,
    id_inventario INTEGER NOT NULL,
    id_prestatario INTEGER NOT NULL,
    CONSTRAINT FK_Prestamo_Inventario FOREIGN KEY(id_inventario) REFERENCES Inventario(id),
    CONSTRAINT FK_Prestamo_Prestatario FOREIGN KEY(id_prestatario) REFERENCES Prestatario(id),
    CONSTRAINT CK_Prestamo_estadoPrestamo CHECK(estadoPrestamo IN ('ACEPTADO', 'CONCLUIDO'))
);

--PRUEBAS VARIAS
/*
INSERT INTO	Inventario(estado)
VALUES(1);

INSERT INTO	Usuario(nombreUsuario)
VALUES("Nombre1Apellido1");

INSERT INTO	Prestatario(id_usuario)
VALUES("Nombre1Apellido1");

INSERT INTO	Prestamo(id_inventario,id_prestatario)
VALUES(1,1);

SELECT * FROM Inventario;
SELECT * FROM Usuario;
SELECT * FROM  Prestatario;
SELECT * FROM Prestamo;

SELECT * FROM Prestamo WHERE fechaSolicitud >= '2024-12-13';

SELECT DATETIME('now','localtime');
SELECT datetime(DATETIME(), '3 days'); 
SELECT DATETIME('now', 'localtime', '+7 days');
SELECT CURRENT_TIMESTAMP;
SELECT TIMESTAMP;

SELECT '2024-12-18 00:15:00' > DATETIME('now','localtime');

SELECT * FROM Prestamo;
INSERT INTO Prestamo(fechaDevolucion,id_inventario,id_prestatario)
VALUES ('2024-12-30 20:17:00',1,1);
*/