package com.orellano.panolhogareno.ui.dialogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.ERolSpinnerAdapter;
import com.orellano.panolhogareno.adaptador.PrestatarioAdapter;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestatario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperUsuario;
import com.orellano.panolhogareno.entidad.Prestatario;
import com.orellano.panolhogareno.entidad.Usuario;
import com.orellano.panolhogareno.entidad.enumEntidad.ERol;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AddPrestatarioBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private List<Prestatario> lista;
    private PrestatarioAdapter prestatarioAdapter;
    private Fragment fragment;

    private ImageView ivIcono;
    private TextView tvTitulo;
    private TextView tvNombreUsuario;
    private EditText etNombreUsuario;
    private TextView tvNombre;
    private EditText etNombre;
    private TextView tvApellido;
    private EditText etApellido;
    private TextView tvRol;
    private Spinner spRol;
    private ImageView ivFoto;
    private Button btnCancelar;
    private Button btnAgregar;

    private File appDir; // Directorio de la app
    private String currentImagePath; // Ruta de la imagen actual

    // Interfaz de escucha
    public interface OnDismissListener {
        void onDismiss();// Cambiado para pasar la posición
    }

    // Listener
    private AddPrestatarioBottomSheetDialogFragment.OnDismissListener listener;

    // Método para configurar el listener
    public void setOnDismissListener(AddPrestatarioBottomSheetDialogFragment.OnDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // Notificar al listener cuando se cierra el diálogo
        if (listener != null) {
            listener.onDismiss(); // Pasa la posición
        }
    }

    // Métodos para foto de camara y galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constantes.CAMARA_REQUEST_CODE:
                    // La foto fue capturada exitosamente
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ivFoto.setImageBitmap(imageBitmap);
                    break;
                case Constantes.GALERIA_REQUEST_CODE:
                    // La imagen fue seleccionada de la galería
                    ivFoto.setImageURI(data.getData());
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Error al obtener la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    public AddPrestatarioBottomSheetDialogFragment(List<Prestatario> lista, PrestatarioAdapter prestatarioAdapter, Fragment parentFragment) {
        this.lista = lista;
        this.prestatarioAdapter = prestatarioAdapter;
        this.fragment = parentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.card_dialog_add_prestatario, container, false);

        // ... (Obtén referencias a tus vistas, establece valores iniciales y configura listeners) ...
        initAppDir();
        ivIcono = dialogView.findViewById(R.id.iv_icono_card_dialog_add_prestatario);
        tvTitulo = dialogView.findViewById(R.id.tv_titulo_card_dialog_add_prestatario);
        tvNombreUsuario = dialogView.findViewById(R.id.tv_nombre_usuario_card_dialog_add_prestatario);
        etNombreUsuario = dialogView.findViewById(R.id.et_nombre_usuario_card_dialog_add_prestatario);
        tvNombre = dialogView.findViewById(R.id.tv_nombre_card_dialog_add_prestatario);
        etNombre = dialogView.findViewById(R.id.et_nombre_card_dialog_add_prestatario);
        tvApellido = dialogView.findViewById(R.id.tv_apellido_card_dialog_add_prestatario);
        etApellido = dialogView.findViewById(R.id.et_apellido_card_dialog_add_prestatario);
        tvRol = dialogView.findViewById(R.id.tv_rol_card_dialog_add_prestatario);
        spRol = dialogView.findViewById(R.id.sp_rol_card_dialog_add_prestatario);
        ivFoto = dialogView.findViewById(R.id.iv_foto_card_dialog_add_prestatario);
        btnCancelar = dialogView.findViewById(R.id.btn_cancelar_card_dialog_add_prestatario);
        btnAgregar = dialogView.findViewById(R.id.id_btn_agregar_card_dialog_add_prestatario);
        // 3. Establece valores iniciales en las vistas
        ivIcono.setImageResource(R.drawable.ic_person_add_alt_1_24);
        tvTitulo.setText(R.string.agregar_prestatario);
        tvNombreUsuario.setText(R.string.nombre_usuario);
        etNombreUsuario.setHint(R.string.ingrese_el_nombre_de_usuario);
        tvNombre.setText(R.string.nombre);
        etNombre.setHint(R.string.ingrese_el_nombre);
        tvApellido.setText(R.string.apellido);
        etApellido.setHint(R.string.ingrese_el_apellido);
        tvRol.setText(R.string.rol);
        ERolSpinnerAdapter adapter = new ERolSpinnerAdapter(getContext(), List.of(ERol.values()));
        spRol.setAdapter(adapter);
        spRol.setSelection(ERol.PRESTATARIO.ordinal());
        spRol.setEnabled(false); // solo PRESTATARIOS se pueden agregar
        ivFoto.setImageResource(R.drawable.img_avatar_default);
        // 5. Establece los oyentes de clics de las vistas
        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en la imagen
                ivFoto.setEnabled(false);
                Toast.makeText(getContext(), "Imagen", Toast.LENGTH_SHORT).show();
                showImageSourceDialog();
            }
        });
        ivFoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Restablecer imagen prestatario
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restablecer iamgen prestatario
                        //eliminarPrestatarioYImagen(itemId);
                        cargarImagen();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.setTitle("Quitar Imagen Prestatario");
                builder.setMessage("¿Estás seguro de que deseas quitar esta imagen prestatario?");
                builder.setCancelable(true);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.create();
                builder.show();
                return true;
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Cancelar
                btnCancelar.setEnabled(false);
                Toast.makeText(getContext(), R.string.agregaci_n_cancelada, Toast.LENGTH_SHORT).show();
                dismiss();
                btnCancelar.setEnabled(true);
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Editar"
                if (validarCampos()) {
                    btnAgregar.setEnabled(false);
                    // TODO: Agregar el prestatario de SQLite
                    Usuario usuarioAgregado = new Usuario(
                            etNombreUsuario.getText().toString(),
                            "Clave1234@", // Reemplaza con la lógica para generar la clave"
                            etNombre.getText().toString(),
                            etApellido.getText().toString(),
                            ERol.getByIndex(spRol.getSelectedItemPosition()),
                            "img_avatar_default.jpg",
                            true);
                    Prestatario prestatarioAgregado = new Prestatario(
                            DaoHelperPrestatario.proximoId(getContext()),
                            true,
                            usuarioAgregado);

                    switch (ERol.getByIndex(spRol.getSelectedItemPosition())) {
                        case PRESTATARIO:
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.agregar_prestatario)
                                    .setMessage(R.string.estas_seguro_de_agregar_este_prestatario)
                                    .setIcon(R.drawable.ic_person_add_alt_1_24)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.si, (dialog, which) -> {
                                        // Guardar la imagen en la galeria
                                        ivFoto.setDrawingCacheEnabled(true);
                                        guardarImagenGaleria(ivFoto.getDrawingCache());
                                        ivFoto.setDrawingCacheEnabled(false);
                                        usuarioAgregado.setFoto(currentImagePath);
                                        // Acción al hacer clic en el botón "Sí"
                                        DaoHelperUsuario.insertar(usuarioAgregado, getContext());
                                        DaoHelperPrestatario.insertar(prestatarioAgregado, getContext());
                                        // Actualizar la lista del adaptador
                                        lista.add(prestatarioAgregado);
                                        prestatarioAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(), R.string.prestatario_agregado, Toast.LENGTH_SHORT).show();
                                        etNombreUsuario.setText("");
                                        currentImagePath = "img_avatar_default.jpg";
                                        etNombre.setText("");
                                        etApellido.setText("");
                                        spRol.setSelection(ERol.PRESTATARIO.ordinal());
                                        cargarImagen();
                                        dialog.dismiss();
                                    })
                                    .setNegativeButton(R.string.no, (dialog, which) -> {
                                        // Acción al hacer clic en el botón "No"
                                        Toast.makeText(getContext(), R.string.confirmacion_cancelada, Toast.LENGTH_SHORT).show();
                                    })
                                    .setNeutralButton(R.string.salir, (dialog, which) -> {
                                        // Acción al hacer clic en el botón "Salir"
                                        Toast.makeText(getContext(), R.string.agregaci_n_cancelada, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        dismiss();
                                    })
                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            btnAgregar.setEnabled(true);
                                        }
                                    })
                                    .show();
                            break;
                        case ADMIN:
                            // TODO: Agregar el admin de SQLite
                            Toast.makeText(getContext(), "Admin agregado", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            // TODO: Agregar el default de SQLite
                            Toast.makeText(getContext(), "default agregado", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getContext(), "Algo salio mal intente nuevamente porfavor.", Toast.LENGTH_SHORT).show();
                    btnAgregar.setEnabled(true);
                }
            }
        });

        cargarImagen();

        return dialogView;
    }

    private boolean validarCampos() {
        if (etNombreUsuario.getText().toString().isEmpty()){
            etNombreUsuario.setError(getString(R.string.campo_requerido));
            return false;
        }
        if (etNombreUsuario.getText().toString().isBlank()) {
            etNombreUsuario.setError(getString(R.string.campo_requerido));
            return false;
        }
        if (etNombreUsuario.getText().length() < 3){
            etNombreUsuario.setError(getString(R.string.minimo_3_caracteres));
            return false;
        }
        if (DaoHelperPrestatario.existePorNombreUsuario(etNombreUsuario.getText().toString(), getContext())) {
            etNombreUsuario.setError(getString(R.string.nombre_usuario_existente));
            return false;
        }
        if (DaoHelperUsuario.existePorNombreUsuario(etNombreUsuario.getText().toString(), getContext())) {
            etNombreUsuario.setError(getString(R.string.nombre_usuario_existente));
            return false;
        }
        return true;
    }

    // ... (Métodos para manejar clics de botones y otra lógica) ...
    private void initAppDir() {
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        appDir = new File(picturesDir, "PanolHerramientas"); // Reemplaza con el nombre de tu app
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                Log.e("PanolHerramientas", "Error al crear el directorio de la app");
            }
        }
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccionar fuente de imagen");
        builder.setItems(new CharSequence[]{"Camara", "Galeria"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Acción para la cámara
                        checkCameraPermissionAndOpenCamera();
                        break;
                    case 1:
                        // Acción para la galería
                        checkStoragePermissionAndOpenGallery();
                        break;
                }
            }
        });
        builder
                .setCancelable(true)
                .setIcon(R.drawable.ic_image_24_first_color)
                .setNegativeButton("Cancelar", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ivFoto.setEnabled(true);
                    }
                })
                .create()
                .show();
    }

    private void checkCameraPermissionAndOpenCamera() {
        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camara, Constantes.CAMARA_REQUEST_CODE);
    }

    private void checkStoragePermissionAndOpenGallery() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeria, Constantes.GALERIA_REQUEST_CODE);
    }

    private void cargarImagen() {
        String codigo = String.format("prestatario_%s", etNombreUsuario.getText().toString());
        String nombreArchivo = "IMG_" + codigo + ".jpg";
        File imagen = new File(appDir, nombreArchivo);

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            ivFoto.setImageBitmap(bitmap);
        } else {
            // Cargar el icono por defecto
            ivFoto.setImageResource(R.drawable.img_avatar_default);
        }
    }

    private void guardarImagenGaleria(Bitmap imageBitmap) {
        String codigo = String.format("prestatario_%s", etNombreUsuario.getText().toString());
        String nombreArchivo = "IMG_" + codigo + ".jpg";
        File imagen = new File(appDir, nombreArchivo);
        currentImagePath = imagen.getAbsolutePath();
        imageBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imagen);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(imagen));
            getContext().sendBroadcast(mediaScanIntent);

            Toast.makeText(getContext(), "Imagen guardada en la galería", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
