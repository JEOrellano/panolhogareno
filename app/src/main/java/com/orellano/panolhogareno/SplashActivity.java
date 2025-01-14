package com.orellano.panolhogareno;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.orellano.panolhogareno.daoSQLite.DaoHelperAdmin;
import com.orellano.panolhogareno.daoSQLite.DaoHelperUsuario;
import com.orellano.panolhogareno.entidad.Admin;
import com.orellano.panolhogareno.entidad.Usuario;
import com.orellano.panolhogareno.entidad.enumEntidad.ERol;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciar();

        int tiempo = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, tiempo);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void iniciar() {
        accionVolver();
        iniciarBaseSQLite();
    }

    private void iniciarBaseSQLite() {
        /* tabla Usuario */
        cargarUsuarios();
        /* tabla Admin */
        cargarAdministradores();
    }

    private void accionVolver() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Mostrar un diálogo de confirmación
                new AlertDialog.Builder(SplashActivity.this)
                        .setTitle("Confirmar salida")
                        .setMessage("¿Estás seguro de que quieres salir de la aplicación?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            moveTaskToBack(true);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void cargarUsuarios() {
        DaoHelperUsuario.insertar(
                new Usuario(
                        "admin",
                        "Clave1234@",
                        "Nombre",
                        "Apellido",
                        ERol.ADMIN,
                        "img_avatar_default.jpg",
                        true
                ),
                this
        );
    }

    private void cargarAdministradores() {
        if (!DaoHelperAdmin.existePorNombreUsuario("admin", this)) {
            DaoHelperAdmin.insertar(
                    new Admin(null,
                            true,
                            DaoHelperUsuario.obtenerUno("admin", this)
                    ),
                    this
            );
        }
    }
}
