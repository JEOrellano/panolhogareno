package com.orellano.panolhogareno.daoSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;

import java.util.ArrayList;
import java.util.List;

public class DaoHelperInventario {
    public static boolean insertar(Inventario inventario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE, inventario.getNombre());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION, inventario.getDescripcion());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION, inventario.getUbicacion().toString());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO, inventario.getFoto());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO, inventario.getEstado());

            long count = con.insert(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
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
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO, 1);

            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
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

    public static Inventario obtenerUno(Long id, Context context) {
        Inventario inventario = new Inventario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return inventario;
        }
    }

    public static Inventario obtenerUnoActivo(Long id, Context context) {
        Inventario inventario = new Inventario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                }
                Log.d("inventario", inventario.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return inventario;
        }
    }

    public static boolean actualizar(Inventario inventario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID, inventario.getId());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE, inventario.getNombre());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION, inventario.getDescripcion());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION, inventario.getUbicacion().toString());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO, inventario.getFoto());
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO, inventario.getEstado());

            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(inventario.getId())};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
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
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
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
            values.put(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
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

    public static List<Inventario> obtenerTodos(Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosActivo(Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static boolean existePorId(Long id, Context context) {
        boolean existe = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID}; // Only need the ID column
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null)
            ) {
                existe = cursor.getCount() > 0; // If count > 0, the record exists
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return existe;
        }
    }

    public static boolean existePorIdActivo(Long id, Context context) {
        boolean existe = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID}; // Only need the ID column
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null)
            ) {
                existe = cursor.getCount() > 0; // If count > 0, the record exists
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return existe;
        }
    }

    public static List<Inventario> obtenerTodosPorNombre(String nombre, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE + " LIKE ?";
            String[] selectionArgs = {"%" + nombre + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorNombreActivo(String nombre, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = {"%" + nombre + "%"};
            String sortOrder = null;
            String groupBy = null;
            String having = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorNombreUbicacion(String nombre, EUbicacion ubicacion, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION + " = ?";
            String[] selectionArgs = {"%" + nombre + "%", ubicacion.toString()};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorNombreUbicacionActivo(String nombre, EUbicacion ubicacion, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION + " = ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = {"%" + nombre + "%", ubicacion.toString()};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorDescripcion(String descripcion, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION + " LIKE ?";
            String[] selectionArgs = {"%" + descripcion + "%"};
            String sortOrder = null;
            String groupBy = null;
            String having = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorDescripcionActivo(String descripcion, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = {"%" + descripcion + "%"};
            String sortOrder = null;
            String groupBy = null;
            String having = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorUbicacion(EUbicacion ubicacion, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION + " = ?";
            String[] selectionArgs = {ubicacion.toString()};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Inventario> obtenerTodosPorUbicacionActivo(EUbicacion ubicacion, Context context) {
        List<Inventario> lista = new ArrayList<Inventario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION + " = ? AND " + ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO + " = 1";
            String[] selectionArgs = {ubicacion.toString()};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_INVENTARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Inventario inventario = new Inventario();
                    inventario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID)));
                    inventario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_NOMBRE)));
                    inventario.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_DESCRIPCION)));
                    inventario.setUbicacion(EUbicacion.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_UBICACION))));
                    inventario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_FOTO)));
                    inventario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_INVENTARIO_ESTADO)) == 1);
                    lista.add(inventario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    /*public static Long proximoId(Context context) {
        Long maxId = 1L;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {"SEQ"};
            String sortOrder = "SEQ DESC;";
            String selection = "SQLITE_SEQUENCE.NAME = ?";
            String[] selectionArgs = {ConectSQLiteHelperDB.TABLE_INVENTARIO};
            String limit = "1";
            //"UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + TABLE_INVENTARIO + "';";
            try (Cursor cursor = con.query(
                    "SQLITE_SEQUENCE",
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder,
                    limit)) { // Limitar a 1 fila
                if (cursor.moveToFirst()) {
                    Log.d("ENTROCURSOR", "ENTRO"+maxId);
                    maxId = cursor.getLong(0) + 1;
                }
                Log.d("ENTROCURSOR", "PASO"+maxId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return maxId;
        }
    }*/

    public static Long proximoId(Context context) {
        long nextId = 1L;
        try (SQLiteDatabase db = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            // Verificar si la tabla tiene un campo autoincremental
            if (!hasAutoIncrementColumn(db, ConectSQLiteHelperDB.TABLE_INVENTARIO)) {
                return nextId; // Si no tiene, devolver 1
            }
            try (Cursor cursor = db.rawQuery("SELECT seq FROM sqlite_sequence WHERE name = ?", new String[]{ConectSQLiteHelperDB.TABLE_INVENTARIO})) {
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

    public static boolean reiniciarAutoincremento(Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            con.execSQL(ConectSQLiteHelperDB.TABLE_INVENTARIO_RESET_AUTOINCREMENT);
            resultado = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    /*public static boolean reiniciarAutoincremento(Context context) {
        boolean resultado = false;
        try (SQLiteDatabase db = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            // 1. Eliminar todos los registros de la tabla
            db.delete(ConectSQLiteHelperDB.TABLE_INVENTARIO, null, null);
            // 2. Reiniciar la secuencia en sqlite_sequence
            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = ?", new String[]{ConectSQLiteHelperDB.TABLE_INVENTARIO});
            resultado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }*/

    public static boolean eliminarCascadaPrestamo(Long id, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            con.beginTransaction();
            try {
                // Eliminar registro en la tabla Inventario
                int filasInventarioEliminadas = con.delete(
                        ConectSQLiteHelperDB.TABLE_INVENTARIO,
                        ConectSQLiteHelperDB.COLUMN_INVENTARIO_ID + " = ?",
                        new String[]{String.valueOf(id)}
                );

                // Eliminar registros relacionados en la tabla Prestamo
                int filasPrestamoEliminadas = con.delete(
                        ConectSQLiteHelperDB.TABLE_PRESTAMO,
                        ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_INVENTARIO + " = ?",
                        new String[]{String.valueOf(id)}
                );

                con.setTransactionSuccessful();

                resultado = filasInventarioEliminadas > 0 && filasPrestamoEliminadas > 0; // Verificar si se eliminaron filas

            } catch (SQLException e) {
                Log.e("eliminarCascadaPrestamo", "Error al eliminar inventario y préstamos", e);
            } finally {
                con.endTransaction();
            }
        } catch (SQLException e) {
            Log.e("eliminarCascadaPrestamo", "Error al obtener la base de datos", e);
        } finally {
            return resultado;
        }
    }
}


