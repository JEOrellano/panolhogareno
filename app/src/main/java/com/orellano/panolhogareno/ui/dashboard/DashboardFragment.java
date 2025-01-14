package com.orellano.panolhogareno.ui.dashboard;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.PrestatarioAdapter;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestatario;
import com.orellano.panolhogareno.databinding.FragmentDashboardBinding;
import com.orellano.panolhogareno.entidad.Prestatario;
import com.orellano.panolhogareno.ui.dialogo.AddPrestatarioBottomSheetDialogFragment;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.util.List;

public class DashboardFragment extends Fragment implements AddPrestatarioBottomSheetDialogFragment.OnDismissListener, PrestatarioAdapter.OnImageClickListener {

    private FragmentDashboardBinding binding;
    private List<Prestatario> lista;
    private PrestatarioAdapter prestatarioAdapter;
    private ImageView ivFotoEdit;

    @Override
    public void onDismiss() {
        prestatarioAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constantes.CAMARA_REQUEST_CODE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                // Procesar el bitmap de la cámara
                ivFotoEdit.setImageBitmap(imageBitmap);
            } else if (requestCode == Constantes.GALERIA_REQUEST_CODE && data != null) {
                Uri selectedImage = data.getData();
                // Procesar la imagen de la galería
                ivFotoEdit.setImageURI(selectedImage);
            }
        }
    }

    @Override
    public void onImageClick(ImageView imageView, int position) {
        ivFotoEdit = imageView;
        prestatarioAdapter.notifyItemChanged(position);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        iniciar();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void iniciar() {
        /* ReciclerView */
        iniciarRecyclerView();

        /* Toolbar */
        iniciarToolbar();

        /* Boton flotante */
        iniciarFabIcon();
    }

    private void iniciarRecyclerView() {
        lista = DaoHelperPrestatario.obtenerTodos(getContext());
        prestatarioAdapter = new PrestatarioAdapter(lista, getContext(), getParentFragmentManager());
        prestatarioAdapter.setOnImageClickListener(this);
        RecyclerView rvPrestatario = binding.rvPrestatarioFdashboard;
        rvPrestatario.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPrestatario.setAdapter(prestatarioAdapter);
    }

    private void iniciarToolbar() {
        Toolbar toolbar = binding.tbMainDashboard.getRoot();
        EditText etTitulo = toolbar.findViewById(R.id.et_titulo);
        // Agregar un menú de opciones
        toolbar.inflateMenu(R.menu.menu_main_3);
        // Agregar titulos item menu
        toolbar.getMenu().findItem(R.id.opcion1).setTitle(R.string.opcion_1_menu_3_item_fdashboard);
        toolbar.getMenu().findItem(R.id.opcion2).setTitle(R.string.opcion_2_menu_3_item_fdashboard);
        toolbar.getMenu().findItem(R.id.opcion3).setTitle(R.string.opcion_3_menu_3_item_fdashboard);
        // Acceder al primer elemento del menú y establecerlo como marcado
        toolbar.getMenu().getItem(0).setChecked(true);
        etTitulo.setText("");
        etTitulo.setHint(R.string.opcion_1_menu_3_item_fdashboard);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.first_color), PorterDuff.Mode.SRC_ATOP);
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
                                    lista.addAll(DaoHelperPrestatario.obtenerTodos(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestatario.obtenerTodosPorNombre(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            case 1:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperPrestatario.obtenerTodos(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestatario.obtenerTodosPorApellido(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            case 2:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperPrestatario.obtenerTodos(getContext()));
                                } else {
                                    lista.addAll(DaoHelperPrestatario.obtenerTodosPorNombreUsuario(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            default:
                                break;
                        }
                        etTitulo.getText().clear();
                        prestatarioAdapter.notifyDataSetChanged();
                        break; // Salir del bucle una vez que se encuentra la opción seleccionada
                    }
                }
            }
        });
        // Establecer un listener para el menú de opciones
        toolbar.setOnMenuItemClickListener(item -> {
            // Acciones a realizar cuando se selecciona una opción del menú
            if (item.getItemId() == R.id.opcion1) {
                // Acción al hacer clic en la opción 1
                Toast.makeText(getContext(), R.string.opcion_1_menu_3_item_fdashboard, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_1_menu_3_item_fdashboard);

                lista.clear();
                lista.addAll(DaoHelperPrestatario.obtenerTodos(getContext()));
                prestatarioAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion2) {
                // Acción al hacer clic en la opción 2
                Toast.makeText(getContext(), R.string.opcion_2_menu_3_item_fdashboard, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_2_menu_3_item_fdashboard);

                lista.clear();
                lista.addAll(DaoHelperPrestatario.obtenerTodos(getContext()));
                prestatarioAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion3) {
                // Acción al hacer clic en la opción 3
                Toast.makeText(getContext(), R.string.opcion_3_menu_3_item_fdashboard, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.opcion_3_menu_3_item_fdashboard);

                lista.clear();
                lista.addAll(DaoHelperPrestatario.obtenerTodos(getContext()));
                prestatarioAdapter.notifyDataSetChanged();
            }
            // Desmarcar todos los elementos del menú primero
            for (int i = 0; i < toolbar.getMenu().size(); i++) {
                toolbar.getMenu().getItem(i).setChecked(false);
            }
            // Marcar la opción seleccionada
            item.setChecked(true);
            return true;
        });
    }

    private void iniciarFabIcon() {
        binding.fabDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón flotante
                binding.fabDashboard.setEnabled(false);

                AddPrestatarioBottomSheetDialogFragment dialogFragment = new AddPrestatarioBottomSheetDialogFragment(lista, prestatarioAdapter, getParentFragment());
                dialogFragment.show(getParentFragmentManager(), "add_prestatario_dialog");

                // Configurar el listener para el botón flotante
                dialogFragment.setOnDismissListener(new AddPrestatarioBottomSheetDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        binding.fabDashboard.setEnabled(true);
                        DashboardFragment.this.onDismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateNotificationBadge();
        }
    }
}