package com.orellano.panolhogareno;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_profile,
                R.id.navigation_logout
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Actualiza el badge de notificaciones
        updateNotificationBadge();

        // Agregar funcionalidad al elemento del menú logout
        navView.getMenu().findItem(R.id.navigation_logout).setOnMenuItemClickListener(item -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirmar salida")
                    .setMessage("¿Estás seguro de que quieres cerrar sesión de la aplicación?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        SharedPreferences sp = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .setIcon(R.drawable.ic_logout_24_first_color)
                    .show();

            return true; // Indicar que el evento de clic fue manejado
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    // Método para actualizar el badge de notificaciones
    public void updateNotificationBadge() {
        if (findViewById(R.id.nav_view) != null) {
            int atrasadosCount = DaoHelperPrestamo.obtenerTodosAceptadoAtrazados(this).size();
            Log.d("MainActivity", "Número de préstamos atrasados: " + atrasadosCount);

            BadgeDrawable badge = ((BottomNavigationView) findViewById(R.id.nav_view)).getOrCreateBadge(R.id.navigation_notifications);
            if (atrasadosCount > 0) {
                badge.setVisible(true);
                badge.setNumber(atrasadosCount);
            } else {
                badge.setVisible(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationBadge();
    }
}