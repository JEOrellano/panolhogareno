package com.orellano.panolhogareno;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.orellano.panolhogareno.daoSQLite.DaoHelperUsuario;
import com.orellano.panolhogareno.entidad.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText etNombreUsuario;
    private EditText etClave;
    public static final String SHARED_PREFERENCES_LOGIN_DATA = "spLoginData";
    public static final String SHARED_PREFERENCES_LOGIN_DATA_NOMBREUSUARIO = "nombreUsuario";
    public static final String SHARED_PREFERENCES_LOGIN_DATA_ROL = "rol";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciar();
    }

    private void iniciar() {
        accionVolver();
        etNombreUsuario = findViewById(R.id.et_nombre_usuario);
        etClave = findViewById(R.id.et_clave);

        etNombreUsuario.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etClave.requestFocus();
                return true;
            }
            return false;
        });

        etClave.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login(v);
                // Cerrar el teclado virtual
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etClave.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void accionVolver() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Mostrar un diálogo de confirmación
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Confirmar salida")
                        .setMessage("¿Estás seguro de que quieres salir de la aplicación?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            moveTaskToBack(true);
                        })
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable.ic_error_outline_24)
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void login(View view) {
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String clave = etClave.getText().toString().trim();
        if (validarCampos(nombreUsuario, clave)) {
            String mensaje = "";
            if (validarLogin(nombreUsuario, clave)) {
                switch (leerRolSP()) {
                    case "ADMIN":
                        mensaje = "Bienvenido " + nombreUsuario;
                        irMainActivity();
                        break;
                    case "PRESTATARIO":
                        mensaje = "Sin acceso, permisos insuficientes " + nombreUsuario;
                        new AlertDialog.Builder(this)
                                .setTitle("Acceso restringido")
                                .setMessage("Tu cuenta no tiene los permisos necesarios para acceder a esta aplicación. Contacta al administrador para obtener más información.")
                                .setPositiveButton("Aceptar", null)
                                .setIcon(R.drawable.ic_error_outline_24)
                                .show();
                        break;
                    default:
                        break;
                }
            } else {
                mensaje = "Usuario o contraseña incorrectos, intente de nuevo porfavor.";
            }
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    private boolean validarCampos(String nombreUsuario, String clave) {
        if (nombreUsuario.isEmpty() || nombreUsuario.length() < 3) {
            //Toast.makeText(this, "Ingrese un nombre de usuario válido porfavor.", Toast.LENGTH_SHORT).show();
            etNombreUsuario.setError("Ingrese un nombre de usuario válido porfavor, minimo 3 caracteres.");
            etNombreUsuario.requestFocus();
            return false;
        }

        if (clave.isEmpty() || clave.length() < 8) {
            //Toast.makeText(this, "Ingrese una contraseña válida porfavor.", Toast.LENGTH_SHORT).show();
            etClave.setError("Ingrese una contraseña válida porfavor, minimo 8 caracteres.");
            etClave.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarLogin(String nombreUsuario, String clave) {
        Usuario usuario = DaoHelperUsuario.login(nombreUsuario, clave, this);
        if (usuario.getNombreUsuario() != null) {
            cargarSesionSP(usuario.getNombreUsuario(), usuario.getRol().toString());
        }
        return usuario.getNombreUsuario() != null;
    }

    private void cargarSesionSP(String nombreUsuario, String rol) {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCES_LOGIN_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARED_PREFERENCES_LOGIN_DATA_NOMBREUSUARIO, nombreUsuario);
        editor.putString(SHARED_PREFERENCES_LOGIN_DATA_ROL, rol);
        editor.apply();
    }

    private String leerRolSP() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCES_LOGIN_DATA, MODE_PRIVATE);
        return sp.getString(SHARED_PREFERENCES_LOGIN_DATA_ROL, "default");
    }

    private void limpiarCampos() {
        etNombreUsuario.setText("");
        etClave.setText("");
    }

    private void irMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}