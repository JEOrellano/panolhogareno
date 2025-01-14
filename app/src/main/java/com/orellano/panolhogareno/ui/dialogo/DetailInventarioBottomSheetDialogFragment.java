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
import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DetailInventarioBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private Inventario inventario;
    private File appDir; // Directorio de la app

    public Button btnRetraso;
    public TextView tvNombre;
    public TextView tvDescripcion;
    public ImageView ivFoto;
    public TextView tvUbicacion;
    public TextView tvId;
    public TextView tvComentario;


    public DetailInventarioBottomSheetDialogFragment(Inventario inventario) {
        // Constructor vacio requerido por BottomSheetDialogFragment
        this.inventario = inventario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View detailDialogView = inflater.inflate(R.layout.card_template_list_inventario_detail, container, false);
        // Referecniar las vistas
        initAppDir();
        btnRetraso = detailDialogView.findViewById(R.id.btn_aviso_card_template_inventario);
        tvNombre = detailDialogView.findViewById(R.id.tv_nombre_inventario_card_template_inventario);
        tvDescripcion = detailDialogView.findViewById(R.id.tv_descripcion_inventario_card_template_inventario);
        ivFoto = detailDialogView.findViewById(R.id.iv_inventario_card_template_inventario);
        tvUbicacion = detailDialogView.findViewById(R.id.tv_ubicacion_card_template_inventario);
        tvId = detailDialogView.findViewById(R.id.tv_id_card_template_inventario);
        tvComentario = detailDialogView.findViewById(R.id.tv_comentario_card_template_inventario);
        // Establecer valores iniciales en las vistas
        List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(inventario.getId(), getContext());
        btnRetraso.setText(inventario.getEstado()
                ?
                (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                        ?
                        "ok"
                        :
                        String.format(
                                "%s",
                                //prestamos.get(prestamos.size() - 1).getFechaDevolucion().until(LocalDateTime.now(), ChronoUnit.DAYS)
                                (prestamos.isEmpty()
                                        ?
                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                        :
                                        LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
                                )
                        )
                )
                :
                ":("
        );
        btnRetraso.setBackgroundColor(inventario.getEstado()
                ?
                (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                        ?
                        getContext().getColor(R.color.success_color)
                        :
                        (
                                (prestamos.isEmpty()
                                        ?
                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                        :
                                        LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
                                ) >= 0
                                        ?
                                        getContext().getColor(R.color.warning_color)
                                        :
                                        getContext().getColor(R.color.danger_color)
                        )
                )
                :
                getContext().getColor(R.color.gray_dark_color)
        );
        tvNombre.setText(inventario.getNombre());
        tvDescripcion.setText(inventario.getDescripcion());
        cargarImagen(ivFoto, inventario.getFoto());
        tvUbicacion.setText(inventario.getUbicacion().toString());
        tvId.setText(inventario.getId().toString());
        if (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)) {
            tvComentario.setText(
                    String.format(
                            "%s %s id %s %s.",
                            inventario.getNombre(),
                            inventario.getDescripcion(),
                            inventario.getId(),
                            inventario.getUbicacion()
                    )
            );
        } else {
            tvComentario.setText(
                    String.format(
                            "%s %s id %s %s %s %s.",
                            inventario.getNombre(),
                            inventario.getDescripcion(),
                            inventario.getId(),
                            inventario.getUbicacion(),
                            (prestamos.isEmpty()
                                    ?
                                    ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME
                                    :
                                    prestamos.get(prestamos.size() - 1).getFechaSolicitud().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME))
                            ),
                            prestamos.isEmpty() ? "Sin prestatario" : prestamos.get(prestamos.size() - 1).getId_prestatario().getId_usuario().getNombreUsuario()
                    )
            );
        }
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
            ivFoto.setImageResource(R.drawable.img_item_default);
        }
    }
}
