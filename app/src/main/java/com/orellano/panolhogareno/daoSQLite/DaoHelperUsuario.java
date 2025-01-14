package com.orellano.panolhogareno.daoSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orellano.panolhogareno.entidad.Usuario;
import com.orellano.panolhogareno.entidad.enumEntidad.ERol;

import java.util.ArrayList;
import java.util.List;

public class DaoHelperUsuario {
    public static boolean insertar(Usuario usuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO, usuario.getNombreUsuario());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE, usuario.getClave());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE, usuario.getNombre());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO, usuario.getApellido());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL, usuario.getRol().toString());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO, usuario.getFoto());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO, usuario.getEstado());

            long count = con.insert(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static boolean insertarLogico(String nombreUsuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO, 1);

            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static Usuario obtenerUno(String nombreUsuario, Context context) {
        Usuario usuario = new Usuario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return usuario;
        }
    }

    public static Usuario obtenerUnoActivo(String nombreUsuario, Context context) {
        Usuario usuario = new Usuario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return usuario;
        }
    }

    public static boolean actualizar(Usuario usuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO, usuario.getNombreUsuario());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE, usuario.getClave());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE, usuario.getNombre());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO, usuario.getApellido());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL, usuario.getRol().toString());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO, usuario.getFoto());
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO, usuario.getEstado());

            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?";
            String[] selectionArgs = {usuario.getNombreUsuario()};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static boolean eliminar(String nombreUsuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.delete(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static boolean eliminarLogico(String nombreUsuario, Context context) {
        boolean resultado = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO, 0);

            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            int count = con.update(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static List<Usuario> obtenerTodos(Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosActivo(Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosPorNombreUsuario(String nombreUsuario, Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " LIKE ?";
            String[] selectionArgs = {"%" + nombreUsuario + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosPorNombreUsuarioActivo(String nombreUsuario, Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = {"%" + nombreUsuario + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosPorNombre(String nombre, Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE + " LIKE ?";
            String[] selectionArgs = {"%" + nombre + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosPorNombreActivo(String nombre, Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = {"%" + nombre + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosPorApellido(String apellido, Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO + " LIKE ?";
            String[] selectionArgs = {"%" + apellido + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static List<Usuario> obtenerTodosPorApellidoActivo(String apellido, Context context) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO + " LIKE ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = {"%" + apellido + "%"};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                    lista.add(usuario);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public static boolean existePorNombreUsuario(String nombreUsuario, Context context) {
        boolean existe = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO}; // Only need the NOMBRE_USUARIO column
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ?";
            String[] selectionArgs = {nombreUsuario};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static boolean existePorNombreUsuarioActivo(String nombreUsuario, Context context) {
        boolean existe = false;
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = {ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO}; // Only need the NOMBRE_USUARIO column
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario};

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
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

    public static Usuario login(String nombreUsuario, String clave, Context context) {
        Usuario usuario = new Usuario();
        try (SQLiteDatabase con = new ConectSQLiteHelperDB(context).getReadableDatabase()) {
            String[] projection = null;
            String selection = ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO + " = ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE + " = ? AND " + ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO + " = 1";
            String[] selectionArgs = {nombreUsuario, clave};
            String groupBy = null;
            String having = null;
            String sortOrder = null;

            try (Cursor cursor = con.query(
                    ConectSQLiteHelperDB.TABLE_USUARIO,
                    projection,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    sortOrder)
            ) {
                while (cursor.moveToNext()) {
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBREUSUARIO)));
                    usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_CLAVE)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_NOMBRE)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_APELLIDO)));
                    usuario.setRol(ERol.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ROL))));
                    usuario.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_FOTO)));
                    usuario.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(ConectSQLiteHelperDB.COLUMN_USUARIO_ESTADO)) == 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return usuario;
        }
    }

}

