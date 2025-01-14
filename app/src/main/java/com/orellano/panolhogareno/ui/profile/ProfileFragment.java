package com.orellano.panolhogareno.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orellano.panolhogareno.LoginActivity;
import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.daoSQLite.DaoHelperUsuario;
import com.orellano.panolhogareno.databinding.FragmentProfileBinding;
import com.orellano.panolhogareno.entidad.Usuario;
import com.orellano.panolhogareno.entidad.enumEntidad.ERol;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Usuario usuario;
    private File appDir; // Directorio de la app
    private String currentImagePath; // Ruta de la imagen actual

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        iniciar();

        binding.ivFotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para editar foto de perfil
                binding.ivFotoProfile.setEnabled(false);
                Toast.makeText(requireContext(), "Imagen", Toast.LENGTH_SHORT).show();
                // Crear un diálogo para elegir entre cámara y galería
                showImageSourceDialog();
            }
        });

        binding.ivFotoProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Restablecer la imagen a la imagen por defecto
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Restablecer imagen de perfil")
                        .setMessage("¿Estás seguro de que quieres restablecer la imagen de perfil?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Sí", (dialog, which) -> {
                            cargarImagen();
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(true)
                        .create()
                        .show();

                return true;
            }
        });

        binding.ibEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para editar perfil
                String clave = binding.etClaveProfile.getText().toString().trim();
                if (validarCampos(clave)) {
                    binding.ibEditProfile.setEnabled(false);
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Confirmar cambios")
                            .setMessage("¿Estás seguro de que quieres editar tu perfil?")
                            .setIcon(R.drawable.ic_edit_24_first_color)
                            .setPositiveButton("Sí", (dialog, which) -> {
                                String mensaje = "";
                                // Guardar la imagen en la galeria
                                binding.ivFotoProfile.setDrawingCacheEnabled(true);
                                guardarImagenGaleria(binding.ivFotoProfile.getDrawingCache());
                                binding.ivFotoProfile.setDrawingCacheEnabled(false);
                                Usuario usuarioEditado = new Usuario(
                                        usuario.getNombreUsuario(),
                                        binding.etClaveProfile.getText().toString(),
                                        binding.etNombreProfile.getText().toString(),
                                        binding.etApellidoProfile.getText().toString(),
                                        ERol.valueOf(binding.etRolProfile.getText().toString()),
                                        currentImagePath,
                                        usuario.getEstado()
                                );
                                if (DaoHelperUsuario.actualizar(usuarioEditado, requireContext())) {
                                    mensaje = "Perfil actualizado correctamente";
                                    Log.d("ProfileFragment", "Perfil actualizado correctamente");
                                    usuario = usuarioEditado;
                                    binding.etClaveProfile.clearFocus();
                                    binding.etNombreProfile.clearFocus();
                                    binding.etApellidoProfile.clearFocus();
                                } else {
                                    mensaje = "Error al actualizar el perfil";
                                    Log.e("ProfileFragment", "Error al actualizar el perfil");
                                }
                                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    binding.ibEditProfile.setEnabled(true);
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        binding.etClaveProfile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.etClaveProfile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_24_first_color, 0);
                    binding.etClaveProfile.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                } else {
                    binding.etClaveProfile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_24_first_color, 0);
                    binding.etClaveProfile.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

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
        initAppDir();
        cargarUsuario();
        try {
            cargarVistas();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProfileFragment", "Usuario no encontrado");
        }
        cargarImagen();
    }

    private void initAppDir() {
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        appDir = new File(picturesDir, "PanolHerramientas"); // Reemplaza con el nombre de tu app
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                Log.e("PanolHerramientas", "Error al crear el directorio de la app");
            }
        }
    }

    private void cargarVistas() {
        binding.etNombreUsuarioProfile.setText(usuario.getNombreUsuario());
        binding.etClaveProfile.setText(usuario.getClave());
        binding.etNombreProfile.setText(usuario.getNombre());
        binding.etApellidoProfile.setText(usuario.getApellido());
        binding.etRolProfile.setText(usuario.getRol().toString());
    }

    private void cargarUsuario() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN_DATA, Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString(LoginActivity.SHARED_PREFERENCES_LOGIN_DATA_NOMBREUSUARIO, "");
        usuario = DaoHelperUsuario.obtenerUno(nombreUsuario, requireContext());
    }

    private void cargarImagen() {
        File imagen = new File(usuario.getFoto());

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            binding.ivFotoProfile.setImageBitmap(bitmap);
        } else {
            // Cargar la imagen por defecto
            binding.ivFotoProfile.setImageResource(R.drawable.img_avatar_default);
        }
    }

    private boolean validarCampos(String clave) {
        if (clave.isEmpty() || clave.length() < 8) {
            binding.etClaveProfile.setError("Ingrese una contraseña válida porfavor, minimo 8 caracteres.");
            binding.etClaveProfile.requestFocus();
            return false;
        }
        return true;
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona una opción")
                .setItems(new String[]{"Cámara", "Galería"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // Lógica para abrir la cámara
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePictureIntent, Constantes.CAMARA_REQUEST_CODE);
                            break;
                        case 1:
                            // Lógica para abrir la galería
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, Constantes.GALERIA_REQUEST_CODE);
                            break;
                    }
                })
                .setCancelable(false)
                .setIcon(R.drawable.ic_image_24_first_color)
                .setNegativeButton("Cancelar", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        binding.ivFotoProfile.setEnabled(true);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Constantes.CAMARA_REQUEST_CODE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                binding.ivFotoProfile.setImageBitmap(imageBitmap);
            } else if (requestCode == Constantes.GALERIA_REQUEST_CODE && data != null) {
                Uri selectedImage = data.getData();
                binding.ivFotoProfile.setImageURI(selectedImage);
            }
        }
    }

    private void guardarImagenGaleria(Bitmap imageBitmap) {
        String codigo = String.format("usuario_%s", binding.etNombreUsuarioProfile.getText().toString());
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
            requireContext().sendBroadcast(mediaScanIntent);

            Toast.makeText(requireContext(), "Imagen guardada en la galería", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
