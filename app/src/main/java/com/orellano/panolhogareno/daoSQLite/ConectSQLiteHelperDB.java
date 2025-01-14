package com.orellano.panolhogareno.daoSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConectSQLiteHelperDB extends SQLiteOpenHelper {
    /* BD */
    // Constantes para crear la base de datos
    public static final String DATABASE_NAME = "DB_PANOL";
    public static final int DATABASE_VERSION = 1;

    /* TABLAS */
    // Inventario
    public static final String TABLE_INVENTARIO = "Inventario";
    public static final String COLUMN_INVENTARIO_ID = "id";
    public static final String COLUMN_INVENTARIO_NOMBRE = "nombre";
    public static final String COLUMN_INVENTARIO_DESCRIPCION = "descripcion";
    public static final String COLUMN_INVENTARIO_UBICACION = "ubicacion";
    public static final String COLUMN_INVENTARIO_FOTO = "foto";
    public static final String COLUMN_INVENTARIO_ESTADO = "estado";
    private static final String DROP_TABLE_INVENTARIO = "DROP TABLE IF EXISTS " +
            TABLE_INVENTARIO + ";";
    private static final String CREATE_TABLE_INVENTARIO = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INVENTARIO + "(" +
            COLUMN_INVENTARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_INVENTARIO_NOMBRE + " TEXT NOT NULL DEFAULT 'NOMBRE'," +
            COLUMN_INVENTARIO_DESCRIPCION + " TEXT NOT NULL DEFAULT 'DESCRIPCION'," +
            COLUMN_INVENTARIO_UBICACION + " TEXT NOT NULL DEFAULT 'DEPOSITO'," +
            COLUMN_INVENTARIO_FOTO + " TEXT NOT NULL DEFAULT 'img_item_default.jpg'," +
            COLUMN_INVENTARIO_ESTADO + " INTEGER NOT NULL DEFAULT 1," +
            "CONSTRAINT CK_Inventario_ubicacion CHECK(" + COLUMN_INVENTARIO_UBICACION + " IN ('DEPOSITO', 'PRESTADO'))" +
            ");";
    public static final String TABLE_INVENTARIO_RESET_AUTOINCREMENT = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + TABLE_INVENTARIO + "';";

    // Usuario
    public static final String TABLE_USUARIO = "Usuario";
    public static final String COLUMN_USUARIO_NOMBREUSUARIO = "nombreUsuario";
    public static final String COLUMN_USUARIO_CLAVE = "clave";
    public static final String COLUMN_USUARIO_NOMBRE = "nombre";
    public static final String COLUMN_USUARIO_APELLIDO = "apellido";
    public static final String COLUMN_USUARIO_ROL = "rol";
    public static final String COLUMN_USUARIO_FOTO = "foto";
    public static final String COLUMN_USUARIO_ESTADO = "estado";
    private static final String DROP_TABLE_USUARIO = "DROP TABLE IF EXISTS " +
            TABLE_USUARIO + ";";
    private static final String CREATE_TABLE_USUARIO = "CREATE TABLE IF NOT EXISTS " +
            TABLE_USUARIO + "(" +
            COLUMN_USUARIO_NOMBREUSUARIO + " TEXT NOT NULL," +
            COLUMN_USUARIO_CLAVE + " TEXT NOT NULL DEFAULT 'Clave1234@'," +
            COLUMN_USUARIO_NOMBRE + " TEXT NOT NULL DEFAULT 'NOMBRE'," +
            COLUMN_USUARIO_APELLIDO + " TEXT NOT NULL DEFAULT 'APELLIDO'," +
            COLUMN_USUARIO_ROL + " TEXT NOT NULL DEFAULT 'PRESTATARIO'," +
            COLUMN_USUARIO_FOTO + " TEXT NOT NULL DEFAULT 'img_avatar_default.jpg'," +
            COLUMN_USUARIO_ESTADO + " INTEGER NOT NULL DEFAULT 1," +
            "CONSTRAINT PK_Usuario PRIMARY KEY(" + COLUMN_USUARIO_NOMBREUSUARIO + ")," +
            "CONSTRAINT CK_Usuario_rol CHECK(" + COLUMN_USUARIO_ROL + " IN ('ADMIN', 'PRESTATARIO'))" +
            ");";

    // Admin
    public static final String TABLE_ADMIN = "Admin";
    public static final String COLUMN_ADMIN_ID = "id";
    public static final String COLUMN_ADMIN_ESTADO = "estado";
    public static final String COLUMN_ADMIN_ID_USUARIO = "id_usuario";
    private static final String DROP_TABLE_ADMIN = "DROP TABLE IF EXISTS " +
            TABLE_ADMIN + ";";
    private static final String CREATE_TABLE_ADMIN = "CREATE TABLE IF NOT EXISTS " +
            TABLE_ADMIN + "(" +
            COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ADMIN_ESTADO + " INTEGER NOT NULL DEFAULT 1," +
            COLUMN_ADMIN_ID_USUARIO + " TEXT NOT NULL," +
            "CONSTRAINT FK_Admin_Usuario FOREIGN KEY(" + COLUMN_ADMIN_ID_USUARIO + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_USUARIO_NOMBREUSUARIO + ")," +
            "CONSTRAINT UK_Admin_id_usuario UNIQUE(" + COLUMN_ADMIN_ID_USUARIO + ")" +
            ");";
    public static final String TABLE_ADMIN_RESET_AUTOINCREMENT = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + TABLE_ADMIN + "';";

    // Prestatario
    public static final String TABLE_PRESTATARIO = "Prestatario";
    public static final String COLUMN_PRESTATARIO_ID = "id";
    public static final String COLUMN_PRESTATARIO_ESTADO = "estado";
    public static final String COLUMN_PRESTATARIO_ID_USUARIO = "id_usuario";
    private static final String DROP_TABLE_PRESTATARIO = "DROP TABLE IF EXISTS " +
            TABLE_PRESTATARIO + ";";
    private static final String CREATE_TABLE_PRESTATARIO = "CREATE TABLE IF NOT EXISTS " +
            TABLE_PRESTATARIO + "(" +
            COLUMN_PRESTATARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_PRESTATARIO_ESTADO + " INTEGER NOT NULL DEFAULT 1," +
            COLUMN_PRESTATARIO_ID_USUARIO + " TEXT NOT NULL," +
            "CONSTRAINT FK_Prestatario_Usuario FOREIGN KEY(" + COLUMN_PRESTATARIO_ID_USUARIO + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_USUARIO_NOMBREUSUARIO + ")," +
            "CONSTRAINT UK_Prestatario_idusuario UNIQUE(" + COLUMN_PRESTATARIO_ID_USUARIO + ")" +
            ");";
    public static final String TABLE_PRESTATARIO_RESET_AUTOINCREMENT = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + TABLE_PRESTATARIO + "';";

    // Prestamo
    public static final String TABLE_PRESTAMO = "Prestamo";
    public static final String COLUMN_PRESTAMO_ID = "id";
    public static final String COLUMN_PRESTAMO_FECHASOLICITUD = "fechaSolicitud";
    public static final String COLUMN_PRESTAMO_FECHADEVOLUCION = "fechaDevolucion";
    public static final String COLUMN_PRESTAMO_ESTADOPRESTAMO = "estadoPrestamo";
    public static final String COLUMN_PRESTAMO_ESTADO = "estado";
    public static final String COLUMN_PRESTAMO_ID_INVENTARIO = "id_inventario";
    public static final String COLUMN_PRESTAMO_ID_PRESTATARIO = "id_prestatario";
    private static final String DROP_TABLE_PRESTAMO = "DROP TABLE IF EXISTS " +
            TABLE_PRESTAMO + ";";
    private static final String CREATE_TABLE_PRESTAMO = "CREATE TABLE IF NOT EXISTS " +
            TABLE_PRESTAMO + "(" +
            COLUMN_PRESTAMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_PRESTAMO_FECHASOLICITUD + " DATETIME NOT NULL DEFAULT (DATETIME('now', 'localtime'))," +
            COLUMN_PRESTAMO_FECHADEVOLUCION + " DATETIME NOT NULL DEFAULT (DATETIME('now', 'localtime','+3 days'))," +
            COLUMN_PRESTAMO_ESTADOPRESTAMO + " TEXT NOT NULL DEFAULT 'ACEPTADO'," +
            COLUMN_PRESTAMO_ESTADO + " INTEGER NOT NULL DEFAULT 1," +
            COLUMN_PRESTAMO_ID_INVENTARIO + " INTEGER NOT NULL," +
            COLUMN_PRESTAMO_ID_PRESTATARIO + " INTEGER NOT NULL," +
            "CONSTRAINT FK_Prestamo_Inventario FOREIGN KEY(" + COLUMN_PRESTAMO_ID_INVENTARIO + ") REFERENCES " + TABLE_INVENTARIO + "(" + COLUMN_INVENTARIO_ID + ")," +
            "CONSTRAINT FK_Prestamo_Prestatario FOREIGN KEY(" + COLUMN_PRESTAMO_ID_PRESTATARIO + ") REFERENCES " + TABLE_PRESTATARIO + "(" + COLUMN_PRESTATARIO_ID + ")," +
            "CONSTRAINT CK_Prestamo_estadoPrestamo CHECK(" + COLUMN_PRESTAMO_ESTADOPRESTAMO + " IN ('ACEPTADO', 'CONCLUIDO'))" +
            ");";
    public static final String TABLE_PRESTAMO_RESET_AUTOINCREMENT = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + TABLE_PRESTAMO + "';";
    public static final String TABLE_PRESTAMO_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String TABLE_PRESTAMO_FORMAT_DATE = "yyyy-MM-dd";
    public static final String TABLE_PRESTAMO_FORMAT_TIME = "HH:mm:ss";


    /* Metodos para crear implementar SQLiteOpenHelper */
    public ConectSQLiteHelperDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INVENTARIO);
        db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL(CREATE_TABLE_ADMIN);
        db.execSQL(CREATE_TABLE_PRESTATARIO);
        db.execSQL(CREATE_TABLE_PRESTAMO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_INVENTARIO);
        db.execSQL(DROP_TABLE_USUARIO);
        db.execSQL(DROP_TABLE_ADMIN);
        db.execSQL(DROP_TABLE_PRESTATARIO);
        db.execSQL(DROP_TABLE_PRESTAMO);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
