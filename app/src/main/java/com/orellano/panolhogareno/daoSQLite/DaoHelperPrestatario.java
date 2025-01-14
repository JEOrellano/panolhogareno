package com.orellano.panolhogareno.daoSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orellano.panolhogareno.entidad.Prestatario;

import java.util.ArrayList;
import java.util.List;

public class DaoHelperPrestatario {
    public static boolean insertar(Prestatario prestatario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO, prestatario.getEstado());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO, prestatario.getId_usuario().getNombreUsuario());

            long count = con.insert(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO, 1);

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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

    public static Prestatario obtenerUno(Long id, Context context) {
        Prestatario prestatario = new Prestatario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return prestatario;
        }
    }

    public static Prestatario obtenerUnoActivo(Long id, Context context) {
        Prestatario prestatario = new Prestatario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return prestatario;
        }
    }

    public static Prestatario obtenerUnoPorNombreUsuario(String nombreUsuario, Context context) {
        Prestatario prestatario = new Prestatario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return prestatario;
        }
    }

    public static Prestatario obtenerUnoPorNombreUsuarioActivo(String nombreUsuario, Context context) {
        Prestatario prestatario = new Prestatario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return prestatario;
        }
    }

    public static boolean actualizar(Prestatario prestatario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID, prestatario.getId());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO, prestatario.getEstado());
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO, prestatario.getId_usuario().getNombreUsuario());

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(prestatario.getId())};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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

    public static boolean eliminarPorNombreUsuario(String nombreUsuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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

    public static boolean eliminarLogicoPorNombreUsuario(String nombreUsuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
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

    public static List<Prestatario> obtenerTodos(Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosActivo(Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosPorNombreUsuario(String nombreUsuario, Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " LIKE ?;";
            String[] selectionArgs = {"%" + nombreUsuario + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    // ... (poblar el objeto prestatario desde el cursor) ...
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosPorNombreUsuarioActivo(String nombreUsuario, Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1;";
            String[] selectionArgs = {"%" + nombreUsuario + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    // ... (poblar el objeto prestatario desde el cursor) ...
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosPorNombre(String nombre, Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String sql = "SELECT P.* " +
                    "FROM " + ConectSQLiteHelperDB.TABLE_PRESTATARIO + " P " +
                    "INNER JOIN " + ConectSQLiteHelperDB.TABLE_USUARIO + " U " +
                    "ON P." + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = U." + ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " " +
                    "WHERE U." + ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE + " LIKE ?;"; // Cambio aquí: LIKE en lugar de =

            try (Cursor cursor = con.rawQuery(sql, new String[]{"%" + nombre + "%"})) { // Y aquí: agregar % al argumento
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    // ... (poblar el objeto prestatario desde el cursor) ...
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosPorNombreActivo(String nombre, Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String sql = "SELECT P.* " +
                    "FROM " + ConectSQLiteHelperDB.TABLE_PRESTATARIO + " P " +
                    "INNER JOIN " + ConectSQLiteHelperDB.TABLE_USUARIO + " U " +
                    "ON P." + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = U." + ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " " +
                    "WHERE U." + ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE + " LIKE ? AND P." + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1 AND U." + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1;"; // Cambio aquí: LIKE en lugar de = y AND para el estado del usuario y del prestatario a la vez acitivos

            try (Cursor cursor = con.rawQuery(sql, new String[]{"%" + nombre + "%"})) { // Y aquí: agregar % al argumento
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    // ... (poblar el objeto prestatario desde el cursor) ...
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosPorApellido(String apellido, Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String sql = "SELECT P.* " +
                    "FROM " + ConectSQLiteHelperDB.TABLE_PRESTATARIO + " P " +
                    "INNER JOIN " + ConectSQLiteHelperDB.TABLE_USUARIO + " U " +
                    "ON P." + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = U." + ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " " +
                    "WHERE U." + ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO + " LIKE ?;"; // Cambio aquí: LIKE en lugar de =

            try (Cursor cursor = con.rawQuery(sql, new String[]{"%" + apellido + "%"})) { // Y aquí: agregar % al argumento
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    // ... (poblar el objeto prestatario desde el cursor) ...
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Prestatario> obtenerTodosPorApellidoActivo(String apellido, Context context) {
        List<Prestatario> lista = new ArrayList<Prestatario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String sql = "SELECT P.* " +
                    "FROM " + ConectSQLiteHelperDB.TABLE_PRESTATARIO + " P " +
                    "INNER JOIN " + ConectSQLiteHelperDB.TABLE_USUARIO + " U " +
                    "ON P." + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = U." + ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " " +
                    "WHERE U." + ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO + " LIKE ? AND P." + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1 AND U." + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1;"; // Cambio aquí: LIKE en lugar de = y AND para el estado del usuario y del prestatario a la vez acitivos

            try (Cursor cursor = con.rawQuery(sql, new String[]{"%" + apellido + "%"})) { // Y aquí: agregar % al argumento
                while (cursor.moveToNext()) {
                    Prestatario prestatario = new Prestatario();
                    // ... (poblar el objeto prestatario desde el cursor) ...
                    prestatario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID)));
                    prestatario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO)) == 1);
                    prestatario.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO)), context));
                    lista.add(prestatario);
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
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?;";
            String[] selectionArgs = {String.valueOf(id)};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null)
            ) {
                existe = cursor.getCount() > 0; // Verificar si se encontraron filas
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
            String[] projection = {ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID}; // Solo se selecciona el ID
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1;";
            String[] selectionArgs = {String.valueOf(id)};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null)
            ) {
                existe = cursor.getCount() > 0; // Verificar si se encontraron filas
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return existe;
        }
    }

    public static boolean existePorNombreUsuario(String nombreUsuario, Context context) {
        boolean existe = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID}; // Solo se selecciona el ID
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null)
            ) {
                existe = cursor.getCount() > 0; // Verificar si se encontraron filas
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return existe;
        }
    }

    public static boolean existePorNombreUsuarioActivo(String nombreUsuario, Context context) {
        boolean existe = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID}; // Solo se selecciona el ID
            String selection = ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID_USUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null)
            ) {
                existe = cursor.getCount() > 0; // Verificar si se encontraron filas
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return existe;
        }
    }

    public static Long proximoId(Context context) {
        long nextId = 1L;
        try (SQLiteDatabase db = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            // Verificar si la tabla tiene un campo autoincremental
            if (!hasAutoIncrementColumn(db, ConectSQLiteHelperDB.TABLE_PRESTATARIO)) {
                return nextId; // Si no tiene, devolver 1
            }
            try (Cursor cursor = db.rawQuery("SELECT seq FROM sqlite_sequence WHERE name = ?", new String[]{ConectSQLiteHelperDB.TABLE_PRESTATARIO})) {
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
            con.execSQL(ConectSQLiteHelperDB.TABLE_PRESTATARIO_RESET_AUTOINCREMENT);
            resultado = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }

    public static boolean eliminarCascadaPrestamoUsuario(Long id, String nombreUsuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            con.beginTransaction();
            try {
                // Eliminar registro en la tabla Prestatario
                int filasPrestatarioEliminadas = con.delete(
                        ConectSQLiteHelperDB.TABLE_PRESTATARIO,
                        ConectSQLiteHelperDB.COLUMN_PRESTATARIO_ID + " = ?",
                        new String[]{String.valueOf(id)}
                );

                // Eliminar registros relacionados en la tabla Prestamo
                int filasPrestamoEliminadas = con.delete(
                        ConectSQLiteHelperDB.TABLE_PRESTAMO,
                        ConectSQLiteHelperDB.COLUMN_PRESTAMO_ID_PRESTATARIO + " = ?",
                        new String[]{String.valueOf(id)}
                );

                // Eliminar registros relacionados en la tabla Usuario
                int filasUsuarioEliminadas = con.delete(
                        ConectSQLiteHelperDB.TABLE_USUARIO,
                        ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?",
                        new String[]{nombreUsuario}
                );

                con.setTransactionSuccessful();

                resultado = filasPrestatarioEliminadas > 0 && filasPrestamoEliminadas > 0 && filasUsuarioEliminadas > 0; // Verificar si se eliminaron filas

            } catch (SQLException e) {
                Log.e("eliminarCascadaPrestamo", "Error al eliminar prestatario y préstamos", e);
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

