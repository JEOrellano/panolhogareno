package com.orellano.panolhogareno.ui.dialogo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.Prestatario;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetailPrestatarioBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private Prestatario prestatario;
    private File appDir; // Directorio de la app

    public Button btnAviso;
    public TextView tvNombre;
    public TextView tvApellido;
    public ImageView ivFoto;
    public TextView tvNombreUsuario;
    public TextView tvId;
    public TextView tvComentario;

    public DetailPrestatarioBottomSheetDialogFragment(Prestatario prestatario) {
        // Constructor vacio requerido por BottomSheetDialogFragment
        this.prestatario = prestatario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View detailDialogView = inflater.inflate(R.layout.card_template_list_prestatario_detail, container, false);
        // Referenciar las vistas
        initAppDir();
        btnAviso = detailDialogView.findViewById(R.id.btn_aviso_card_template_prestatario);
        tvNombre = detailDialogView.findViewById(R.id.tv_nombre_prestatario_card_template_prestatario);
        tvApellido = detailDialogView.findViewById(R.id.tv_apellido_prestatario_card_template_prestatario);
        ivFoto = detailDialogView.findViewById(R.id.iv_prestatario_card_template_prestatario);
        tvNombreUsuario = detailDialogView.findViewById(R.id.tv_nombre_usuario_card_template_prestatario);
        tvId = detailDialogView.findViewById(R.id.tv_id_card_template_prestatario);
        tvComentario = detailDialogView.findViewById(R.id.tv_comentario_card_template_prestatario);
        // Establecer los valores iniciales en las vistas
        List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdPrestatarioAceptado(prestatario.getId(), getContext());
        btnAviso.setEnabled(false);
        btnAviso.setText(prestatario.getEstado()
                ?
                (prestamos.isEmpty()
                        ?
                        "ok"
                        :
                        (DaoHelperPrestamo.obtenerTodosAtrazadosPorIdPrestatario(prestatario.getId(), getContext()).size() > 0
                                ?
                                String.valueOf(
                                        DaoHelperPrestamo
                                                .obtenerTodosAtrazadosPorIdPrestatario(
                                                        prestatario.getId(),
                                                        getContext()
                                                )
                                                .size()
                                )
                                :
                                String.valueOf(
                                        DaoHelperPrestamo.
                                                obtenerTodosNoAtrazadosPorIdPrestatario(
                                                        prestatario.getId(),
                                                        getContext()
                                                )
                                                .size())
                        )
                )
                :
                ":("
        );
        btnAviso.setBackgroundColor(prestatario.getEstado()
                ?
                (prestamos.isEmpty()
                        ?
                        getContext().getColor(R.color.success_color)
                        :
                        (DaoHelperPrestamo.obtenerTodosAtrazadosPorIdPrestatario(prestatario.getId(), getContext()).size() > 0
                                ?
                                getContext().getColor(R.color.danger_color)
                                :
                                getContext().getColor(R.color.warning_color)
                        )
                )
                :
                getContext().getColor(R.color.gray_dark_color)
        );
        tvNombre.setText(prestatario.getId_usuario().getNombre());
        tvApellido.setText(prestatario.getId_usuario().getApellido());
        cargarImagen(ivFoto, prestatario.getId_usuario().getFoto());
        tvNombreUsuario.setText(prestatario.getId_usuario().getNombreUsuario());
        tvId.setText(prestatario.getId().toString());
        tvComentario.setText(
                (prestamos.isEmpty()
                        ?
                        String.format(
                                "%s %s %s id: %s debe %s inventario",
                                prestatario.getId_usuario().getNombre(),
                                prestatario.getId_usuario().getApellido(),
                                prestatario.getId_usuario().getNombreUsuario(),
                                prestatario.getId(),
                                prestamos.size())
                        :
                        String.format(
                                "%s %s %s id: %s debe %s inventario %s",
                                prestatario.getId_usuario().getNombre(),
                                prestatario.getId_usuario().getApellido(),
                                prestatario.getId_usuario().getNombreUsuario(),
                                prestatario.getId(),
                                prestamos.size(),
                                prestamos.get(0).getFechaSolicitud().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)))
                )
        );
        return detailDialogView;
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

    private void cargarImagen(ImageView ivFoto, String foto) {
        File imagen = new File(foto);

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            ivFoto.setImageBitmap(bitmap);
        } else {
            // Cargar el icono por defecto
            ivFoto.setImageResource(R.drawable.img_avatar_default);
        }
    }
}
