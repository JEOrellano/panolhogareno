package com.orellano.panolhogareno.ui.notifications;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.PrestamoAdapter;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.databinding.FragmentNotificationsBinding;
import com.orellano.panolhogareno.entidad.Prestamo;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<Prestamo> lista;
    private PrestamoAdapter prestamoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        iniciar();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateNotificationBadge();
        }
    }

    private void iniciar() {
        /* ReciclerView */
        iniciarRecyclerView();
    }

    private void iniciarRecyclerView() {
        lista = DaoHelperPrestamo.obtenerTodosAceptado(getContext());
        prestamoAdapter = new PrestamoAdapter(lista, getContext());
        RecyclerView rvPrestamo = binding.rvPrestamoFnotifications;
        rvPrestamo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPrestamo.setAdapter(prestamoAdapter);

        /* Toolbar */
        iniciarToolbar();
    }

    private void iniciarToolbar() {
        Toolbar toolbar = binding.tbMainNotifications.getRoot();
        EditText etTitulo = toolbar.findViewById(R.id.et_titulo);
        // Agregar un menú de opciones personalizado
        toolbar.inflateMenu(R.menu.menu_main_4);
        // Agregar titulos item menu
        toolbar.getMenu().findItem(R.id.opcion1_4).setTitle(R.string.opcion_1_menu_4_item_fnotifications);
        toolbar.getMenu().findItem(R.id.opcion2_4).setTitle(R.string.opcion_2_menu_4_item_fnotifications);
        toolbar.getMenu().findItem(R.id.opcion3_4).setTitle(R.string.opcion_3_menu_4_item_fnotifications);
        toolbar.getMenu().findItem(R.id.opcion4_4).setTitle(R.string.opcion_4_menu_4_item_fnotifications);
        // Acceder al primer elemento del menú y establecerlo como marcado
        toolbar.getMenu().getItem(0).setChecked(true);
        etTitulo.setText("");
        etTitulo.setHint(R.string.opcion_1_menu_4_item_fnotifications);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(getResources().getColor(R.color.first_color), android.graphics.PorterDuff.Mode.SRC_ATOP);
        }
        // Agregar un botón de navegación
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón de navegación
                // Toast.makeText(getContext(), "Botón de navegación", Toast.LENGTH_SHORT).show();
                // Iterar a través de los elementos del menú y encontrar el seleccionado
                for (int i = 0; i < toolbar.getMenu().size(); i++) {
                    MenuItem item = toolbar.getMenu().getItem(i);
                    if (item.isChecked()) {
                        // Esta es la opción seleccionada
                        int itemId = item.getItemId();
                        // Hacer algo con el ID de la opción seleccionada
                        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        lista.clear();
                        switch (i) {
                            case 0:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosPorFechaSolicitud(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            case 1:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosPorFechaDevolucion(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            case 2:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosPorIdInventario(Long.valueOf(etTitulo.getText().toString()), getContext()));
                                }
                                break;
                            case 3:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestamo.obtenerTodosPorIdPrestatario(Long.valueOf(etTitulo.getText().toString()), getContext()));
                                }
                                break;
                            default:
                                break;
                        }
                        etTitulo.getText().clear();
                        prestamoAdapter.notifyDataSetChanged();
                        break; // Salir del bucle una vez que se encuentra la opción seleccionada
                    }
                }
            }
        });
        // Establecer un listener para el menú de opciones
        toolbar.setOnMenuItemClickListener(item -> {
            // Acciones a realizar cuando se selecciona una opción del menú
            if (item.getItemId() == R.id.opcion1_4) {
                // Acción al hacer clic en la opción 1
                Toast.makeText(getContext(), R.string.opcion_1_menu_4_item_fnotifications, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_1_menu_4_item_fnotifications);
                lista.clear();
                lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                prestamoAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion2_4) {
                // Acción al hacer clic en la opción 2
                Toast.makeText(getContext(), R.string.opcion_2_menu_4_item_fnotifications, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_2_menu_4_item_fnotifications);
                lista.clear();
                lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                prestamoAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion3_4) {
                // Acción al hacer clic en la opción 3
                Toast.makeText(getContext(), R.string.opcion_3_menu_4_item_fnotifications, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_3_menu_4_item_fnotifications);
                lista.clear();
                lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                prestamoAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion4_4) {
                // Acción al hacer clic en la opción 4
                Toast.makeText(getContext(), R.string.opcion_4_menu_4_item_fnotifications, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_4_menu_4_item_fnotifications);
                lista.clear();
                lista.addAll(DaoHelperPrestamo.obtenerTodosAceptado(getContext()));
                prestamoAdapter.notifyDataSetChanged();
            }
            // Desmarcar todos los elementos del menú primero
            for (int i = 0; i < toolbar.getMenu().size(); i++) {
                toolbar.getMenu().getItem(i).setChecked(false);
            }
            // Marcar el elemento seleccionado
            item.setChecked(true);
            return true;
        });
    }
}