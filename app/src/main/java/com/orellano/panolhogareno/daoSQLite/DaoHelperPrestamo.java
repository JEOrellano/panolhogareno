package com.orellano.panolhogareno.daoSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.enumEntidad.EEstadoPrestamo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DaoHelperPrestamo {
    public static boolean insertar(Prestamo prestamo, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD, prestamo.getFechaSolicitud().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION, prestamo.getFechaDevolucion().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO, prestamo.getEstadoPrestamo().toString());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO, prestamo.getEstado());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO, prestamo.getId_inventario().getId());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO, prestamo.getId_prestatario().getId());

            long count = con.insert(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    null,
                    values
            );

            if (count != -1) {
                resultado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static boolean insertarLogico(Long id, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO, 1);

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    values,
                    selection,
                    selectionArgs
            );

            if (count > 0) {
                resultado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static Prestamo obtenerUno(Long id, Context context) {
        Prestamo prestamo = new Prestamo();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return prestamo;
        }
    }

    public static Prestamo obtenerUnoActivo(Long id, Context context) {
        Prestamo prestamo = new Prestamo();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return prestamo;
        }
    }

    public static boolean actualizar(Prestamo prestamo, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID, prestamo.getId());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD, prestamo.getFechaSolicitud().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION, prestamo.getFechaDevolucion().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO, prestamo.getEstadoPrestamo().toString());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO, prestamo.getEstado());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO, prestamo.getId_inventario().getId());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO, prestamo.getId_prestatario().getId());

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(prestamo.getId())};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    values,
                    selection,
                    selectionArgs
            );

            if (count > 0) {
                resultado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static boolean eliminar(Long id, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    selection,
                    selectionArgs
            );

            if (count > 0) {
                resultado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static boolean eliminarLogico(Long id, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    values,
                    selection,
                    selectionArgs
            );

            if (count > 0) {
                resultado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static List<Prestamo> obtenerTodos(Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosActivo(Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosAceptado(Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosAceptado", "Error al obtener los prestamos aceptados : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosAceptadoActivo(Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO' AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosAceptado", "Error al obtener los prestamos aceptados activos: " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorFechaSolicitud(String fecha, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD + " >= ?";
            String[] selectionArgs = {fecha};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorFechaSolicitudActivo(String fecha, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD + " >= ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = {fecha};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder
            )
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }

                cursor.close();
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorFechaDevolucion(String fecha, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " >= ?";
            String[] selectionArgs = {fecha};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorFechaDevolucionActivo(String fecha, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " >= ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = {fecha};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdInventario(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdInventarioActivo(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdInventarioAceptado(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioAceptado", "Error al obtener los prestamos aceptados por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdInventarioAceptadoActivo(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1 AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioAceptadoActivo", "Error al obtener los prestamos aceptados activos por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdPrestatario(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdPrestatarioActivo(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static boolean reiniciarAutoincremento(Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            con.execSQL(ConectSQLiteHelperDB.TABLE_PRESTAMO_RESET_AUTOINCREMENT);
            resultado = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static boolean eliminarTodosPorIdInventario(Long id, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    selection,
                    selectionArgs
            );

            if (count > 0) {
                resultado = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static List<Prestamo> obtenerTodosAtrazadosPorIdPrestatario(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " < DATETIME('now','localtime') AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioActivo", "Error al obtener los prestamos retrazados por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosAtrazadosPorIdPrestatarioActivo(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1 AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " < DATETIME('now','localtime') AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosAtrazadosPorIdPrestatarioActivo", "Error al obtener los prestamos atrazados activos por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosNoAtrazadosPorIdPrestatario(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " >= DATETIME('now','localtime') AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosNoAtrazadosPorIdPrestatario", "Error al obtener los prestamos no atrazados por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosNoAtrazadosPorIdPrestatarioActivo(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1 AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " >= DATETIME('now','localtime') AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosNoAtrazadosPorIdPrestatarioActivo", "Error al obtener los prestamos no atrazados activos por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdPrestatarioAceptado(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioAceptado", "Error al obtener los prestamos aceptados por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdPrestatarioAceptadoActivo(Long id, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1 AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioAceptadoActivo", "Error al obtener los prestamos aceptados activos por id de prestatario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdPrestatarioAceptadoNombreInventario(Long id, String nombre, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String sql = "SELECT P.* " +
                    "FROM " + ConectSQLiteHelperDB.TABLE_PRESTAMO + " P " +
                    "INNER JOIN " + ConectSQLiteHelperDB.TABLE_INVENTARIO + " I " +
                    "ON P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = I." + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " " +
                    "WHERE P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? " +
                    "AND P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO' " +
                    "AND I." + ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE + " LIKE ?";

            try (Cursor cursor = con.rawQuery(sql, new String[]{String.valueOf(id), "%" + nombre + "%"})) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }
        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioAceptadoNombreInventario", "Error al obtener los prestamos aceptados por id de prestatario y nombre de inventario : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosPorIdPrestatarioAceptadoNombreInventarioActivo(Long id, String nombre, Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String sql = "SELECT P.* " +
                    "FROM " + ConectSQLiteHelperDB.TABLE_PRESTAMO + " P " +
                    "INNER JOIN " + ConectSQLiteHelperDB.TABLE_INVENTARIO + " I " +
                    "ON P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = I." + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " " +
                    "WHERE P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ? " +
                    "AND P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO' " +
                    "AND I." + ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE + " LIKE ? " +
                    "AND P." + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";

            try (Cursor cursor = con.rawQuery(sql, new String[]{String.valueOf(id), "%" + nombre + "%"})) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }
        } catch (SQLException e) {
            Log.e("obtenerTodosPorIdPrestatarioAceptadoNombreInventarioActivo", "Error al obtener los prestamos aceptados por id de prestatario y nombre de inventario activo : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static Long proximoId(Context context) {
        long nextId = 1L;
        try (SQLiteDatabase db = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            // Verificar si la tabla tiene un campo autoincremental
            if (!hasAutoIncrementColumn(db, ConectSQLiteHelperDB.TABLE_PRESTAMO)) {
                return nextId; // Si no tiene, devolver 1
            }
            try (Cursor cursor = db.rawQuery("SELECT seq FROM sqlite_sequence WHERE name = ?", new String[]{ConectSQLiteHelperDB.TABLE_PRESTAMO})) {
                if (cursor.moveToFirst()) {
                    nextId = cursor.getLong(0) + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextId;
    }

    private static boolean hasAutoIncrementColumn(SQLiteDatabase db, String tableName) {
        try (Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null)) {
            if (cursor.moveToFirst()) {
                do {
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    int pk = cursor.getInt(cursor.getColumnIndexOrThrow("pk"));
                    if (type.equalsIgnoreCase("INTEGER") && pk == 1) {
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        }
        return false;
    }

    public static List<Prestamo> obtenerTodosAceptadoAtrazados(Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " < DATETIME('now','localtime') AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO'";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosAceptadoAtrazados", "Error al obtener los prestamos retrazados aceptado : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }

    public static List<Prestamo> obtenerTodosAceptadoAtrazadosActivo(Context context) {
        List<Prestamo> lista = new ArrayList<Prestamo>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION + " < DATETIME('now','localtime') AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO + " = 'ACEPTADO' AND " + ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTAMO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID)));
                    prestamo.setFechaSolicitud(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHASOLICITUD)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setFechaDevolucion(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_FECHADEVOLUCION)), DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)));
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADOPRESTAMO))));
                    prestamo.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ESTADO)) == 1);
                    prestamo.setId_inventario(DaoHelperInventario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO)), context));
                    prestamo.setId_prestatario(DaoHelperPrestatario.obtenerUno(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO)), context));
                    lista.add(prestamo);
                }
            }

        } catch (SQLException e) {
            Log.e("obtenerTodosAceptadoAtrazados", "Error al obtener los prestamos retrazados aceptado : " + e.getMessage(), e);
        } finally {
            return lista;
        }
    }
}

