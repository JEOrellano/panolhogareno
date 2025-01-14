package com.orellano.panolhogareno.daoSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orellano.panolhogareno.entidad.Admin;

import java.util.ArrayList;
import java.util.List;

public class DaoHelperAdmin {
    public static boolean insertar(Admin admin, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO, admin.getEstado());
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO, admin.getId_usuario().getNombreUsuario());

            long count = con.insert(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO, 1);

            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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

    public static Admin obtenerUno(Long id, Context context) {
        Admin admin = new Admin();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    admin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID)));
                    admin.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO)) == 1);
                    admin.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return admin;
        }
    }

    public static Admin obtenerUnoActivo(Long id, Context context) {
        Admin admin = new Admin();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    admin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID)));
                    admin.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO)) == 1);
                    admin.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return admin;
        }
    }

    public static Admin obtenerUnoPorNombreUsuario(String nombreUsuario, Context context) {
        Admin admin = new Admin();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    admin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID)));
                    admin.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO)) == 1);
                    admin.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return admin;
        }
    }

    public static Admin obtenerUnoPorNombreUsuarioActivo(String nombreUsuario, Context context) {
        Admin admin = new Admin();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    admin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID)));
                    admin.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO)) == 1);
                    admin.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO)), context));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return admin;
        }
    }

    public static boolean actualizar(Admin admin, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ID, admin.getId());
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO, admin.getEstado());
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO, admin.getId_usuario().getNombreUsuario());

            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(admin.getId())};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            values.put(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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

    public static List<Admin> obtenerTodos(Context context) {
        List<Admin> lista = new ArrayList<Admin>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Admin admin = new Admin();
                    admin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID)));
                    admin.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO)) == 1);
                    admin.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO)), context));
                    lista.add(admin);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Admin> obtenerTodosActivo(Context context) {
        List<Admin> lista = new ArrayList<Admin>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Admin admin = new Admin();
                    admin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID)));
                    admin.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO)) == 1);
                    admin.setId_usuario(DaoHelperUsuario.obtenerUno(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO)), context));
                    lista.add(admin);
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
            String[] projection = {ConectSQLiteHelperDB.COLUMN_ADMIN_ID}; // Columnas a seleccionar
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            String[] projection = {ConectSQLiteHelperDB.COLUMN_ADMIN_ID}; // Columnas a seleccionar
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID + " = ? AND " + ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO + " = 1";
            String[] selectionArgs = {String.valueOf(id)};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            String[] projection = {ConectSQLiteHelperDB.COLUMN_ADMIN_ID}; // Columnas a seleccionar
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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
            String[] projection = {ConectSQLiteHelperDB.COLUMN_ADMIN_ID}; // Columnas a seleccionar
            String selection = ConectSQLiteHelperDB.COLUMN_ADMIN_ID_USUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_ADMIN_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_ADMIN,
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

    public static boolean reiniciarAutoincremento(Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            con.execSQL(ConectSQLiteHelperDB.TABLE_ADMIN_RESET_AUTOINCREMENT);
            resultado = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultado;
        }
    }
}

