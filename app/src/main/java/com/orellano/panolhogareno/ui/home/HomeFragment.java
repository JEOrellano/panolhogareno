package com.orellano.panolhogareno.ui.home;

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
import android.widget.Spinner;
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
import com.orellano.panolhogareno.adaptador.EUbicacionSpinnerAdapter;
import com.orellano.panolhogareno.adaptador.InventarioAdapter;
import com.orellano.panolhogareno.daoSQLite.DaoHelperInventario;
import com.orellano.panolhogareno.databinding.FragmentHomeBinding;
import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;
import com.orellano.panolhogareno.ui.dialogo.AddInventarioBottomSheetDialogFragment;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.util.List;

public class HomeFragment extends Fragment implements AddInventarioBottomSheetDialogFragment.OnDismissListener, InventarioAdapter.OnImageClickListener {

    private FragmentHomeBinding binding;
    private List<Inventario> lista;
    private InventarioAdapter inventarioAdapter;
    private ImageView ivFotoEdit;

    @Override
    public void onDismiss() {
        inventarioAdapter.notifyDataSetChanged();
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
        inventarioAdapter.notifyItemChanged(position);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        /*final List<Inventario> lista = DaoHelperInventario.obtenerTodos(getContext());
        final InventarioAdapter inventarioAdapter = new InventarioAdapter(lista, getContext());*/

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
    }

    private void iniciarRecyclerView() {
        lista = DaoHelperInventario.obtenerTodos(getContext());
        inventarioAdapter = new InventarioAdapter(lista, getContext(), getParentFragmentManager());
        inventarioAdapter.setOnImageClickListener(this);
        RecyclerView rvInventario = binding.rvInventarioFhome;
        rvInventario.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInventario.setAdapter(inventarioAdapter);

        /* Toolbar */
        iniciarToolbar();

        /* Boton flotante */
        inciarFabIcon();
    }

    private void iniciarToolbar() {
        Toolbar toolbar = binding.tbMainHome.getRoot();
        EditText etTitulo = toolbar.findViewById(R.id.et_titulo);
        Spinner spTitulo = toolbar.findViewById(R.id.sp_titulo);
        spTitulo.setAdapter(new EUbicacionSpinnerAdapter(getContext(), List.of(EUbicacion.values())));
        spTitulo.setVisibility(View.GONE);
        spTitulo.setEnabled(false);
        spTitulo.setSelection(-1);
        // Agregar un menú de opciones
        toolbar.inflateMenu(R.menu.menu_main_3);
        // Agregar titulos item menu
        toolbar.getMenu().findItem(R.id.opcion1).setTitle(R.string.opcion_1_menu_3_item_fhome);
        toolbar.getMenu().findItem(R.id.opcion2).setTitle(R.string.opcion_2_menu_3_item_fhome);
        toolbar.getMenu().findItem(R.id.opcion3).setTitle(R.string.opcion_3_menu_3_item_fhome);
        // Acceder al primer elemento del menú y establecerlo como marcado
        toolbar.getMenu().getItem(0).setChecked(true);
        // Establecer el título
        // toolbar.setTitle("Mi Fragmento");
        // Establecer el hint
        etTitulo.setText("");
        etTitulo.setHint(R.string.opcion_1_menu_3_item_fhome);
        // Establecer el color del icono de navegación
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
                        Toast.makeText(getContext(), item.getTitle() + ": " + spTitulo.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        lista.clear();
                        switch (i) {
                            case 0:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperInventario.obtenerTodos(getContext()));
                                } else {
                                    lista.addAll(DaoHelperInventario.obtenerTodosPorNombre(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            case 1:
                                if (etTitulo.getText().toString().trim().isEmpty()) {
                                    lista.addAll(DaoHelperInventario.obtenerTodos(getContext()));
                                } else {
                                    lista.addAll(DaoHelperInventario.obtenerTodosPorDescripcion(etTitulo.getText().toString(), getContext()));
                                }
                                break;
                            case 2:
                                switch ((EUbicacion) (spTitulo.getSelectedItem())) {
                                    case DEPOSITO:
                                        etTitulo.setText(EUbicacion.DEPOSITO.toString());
                                        spTitulo.setSelection(EUbicacion.DEPOSITO.ordinal());
                                        lista.addAll(DaoHelperInventario.obtenerTodosPorUbicacion(EUbicacion.valueOf(etTitulo.getText().toString()), getContext()));
                                        break;
                                    case PRESTADO:
                                        etTitulo.setText(EUbicacion.PRESTADO.toString());
                                        spTitulo.setSelection(EUbicacion.PRESTADO.ordinal());
                                        lista.addAll(DaoHelperInventario.obtenerTodosPorUbicacion(EUbicacion.valueOf(etTitulo.getText().toString()), getContext()));
                                        break;
                                    default:
                                        etTitulo.getText().clear();
                                        spTitulo.setSelection(-1);
                                        lista.addAll(DaoHelperInventario.obtenerTodos(getContext()));
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                        etTitulo.getText().clear();
                        inventarioAdapter.notifyDataSetChanged();
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
                Toast.makeText(getContext(), R.string.buscar_por_nombre, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.buscar_por_nombre);
                etTitulo.setVisibility(View.VISIBLE);
                etTitulo.setEnabled(true);

                spTitulo.setVisibility(View.GONE);
                spTitulo.setEnabled(false);
                spTitulo.setSelection(-1);

                lista.clear();
                lista.addAll(DaoHelperInventario.obtenerTodos(getContext()));
                inventarioAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion2) {
                // Acción al hacer clic en la opción 2
                Toast.makeText(getContext(), R.string.buscar_por_descripcion, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.buscar_por_descripcion);
                etTitulo.setVisibility(View.VISIBLE);
                etTitulo.setEnabled(true);

                spTitulo.setVisibility(View.GONE);
                spTitulo.setEnabled(false);
                spTitulo.setSelection(-1);

                lista.clear();
                lista.addAll(DaoHelperInventario.obtenerTodos(getContext()));
                inventarioAdapter.notifyDataSetChanged();
            }
            if (item.getItemId() == R.id.opcion3) {
                // Acción al hacer clic en la opción 3
                Toast.makeText(getContext(), R.string.buscar_por_ubicacion, Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etTitulo.setHint(R.string.buscar_por_ubicacion);
                etTitulo.setVisibility(View.GONE);
                etTitulo.setEnabled(false);

                spTitulo.setVisibility(View.VISIBLE);
                spTitulo.setEnabled(true);
                spTitulo.setSelection(-1);

                lista.clear();
                lista.addAll(DaoHelperInventario.obtenerTodos(getContext()));
                inventarioAdapter.notifyDataSetChanged();
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

    private void inciarFabIcon() {
        binding.fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón flotante
                binding.fabHome.setEnabled(false);

                AddInventarioBottomSheetDialogFragment dialogFragment = new AddInventarioBottomSheetDialogFragment(lista, inventarioAdapter, getParentFragment());
                dialogFragment.show(getParentFragmentManager(), "add_inventario_dialog");

                // Configurar el listener para el botón flotante
                dialogFragment.setOnDismissListener(new AddInventarioBottomSheetDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        binding.fabHome.setEnabled(true);
                        HomeFragment.this.onDismiss();
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
