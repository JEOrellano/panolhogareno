package com.orellano.panolhogareno.adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.daoSQLite.DaoHelperInventario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class InventarioDetailListSmallAdapter extends ArrayAdapter<Inventario> {
    private List<Inventario> lista;

    public InventarioDetailListSmallAdapter(@NonNull Context context, @NonNull List<Inventario> objects) {
        super(context, R.layout.card_template_list_small_inventario_detail, objects);
        this.lista = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.card_template_list_small_inventario_detail, null);

        Inventario inventario = getItem(position);
        List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(inventario.getId(), getContext());

        Button btnAviso = item.findViewById(R.id.btn_aviso_card_template_small_inventario);
        TextView tvNombre = item.findViewById(R.id.tv_nombre_inventario_card_template_small_inventario);
        TextView tvDescripcion = item.findViewById(R.id.tv_descripcion_inventario_card_template_small_inventario);
        ImageView ivInventario = item.findViewById(R.id.iv_inventario_card_template_small_inventario);
        // frame layout
        ImageView ivFrame = item.findViewById(R.id.iv_frame_layout_inventario_card_template_small_inventario);
        TextView tvFrame = item.findViewById(R.id.tv_frame_layout_inventario_card_template_small_inventario);
        ImageButton ibtnFrameDelete = item.findViewById(R.id.ib_frame_layout_delete_inventario_card_template_small_inventario);
        ImageButton ibtnFrameRestore = item.findViewById(R.id.ib_frame_layout_restore_inventario_card_template_small_inventario);

        btnAviso.setEnabled(false);
        btnAviso.setText(inventario.getEstado()
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
        btnAviso.setBackgroundColor(inventario.getEstado()
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
        ivInventario.setImageResource(R.drawable.img_item_default);
        cargarImagen(ivInventario, inventario.getFoto());

        // frame layout
        if (inventario.getEstado()) {
            ivFrame.setVisibility(View.GONE);
            tvFrame.setVisibility(View.GONE);
            ibtnFrameDelete.setVisibility(View.GONE);
            ibtnFrameRestore.setVisibility(View.GONE);
        } else {
            ivFrame.setVisibility(View.VISIBLE);
            tvFrame.setVisibility(View.VISIBLE);
            ibtnFrameDelete.setVisibility(View.VISIBLE);
            ibtnFrameRestore.setVisibility(View.VISIBLE);
        }
        ibtnFrameDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Eliminar"
                Long retrazo = 0L;
                String nombreUsuario = "";
                Integer totalPrestamos = DaoHelperPrestamo.obtenerTodosPorIdInventario(inventario.getId(), getContext()).size();
                if (!prestamos.isEmpty()) {
                    retrazo = LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS);
                    nombreUsuario = prestamos.get(prestamos.size() - 1).getId_prestatario().getId_usuario().getNombreUsuario();
                }
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.eliminar_inventario_permanentemente)
                        .setIcon(R.drawable.ic_delete_forever_24_danger_color)
                        .setMessage(
                                inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                                        ?
                                        String.format(
                                                "¿Estas seguro de eliminar %s %s permanentemente. Es seguro eliminarlo, solo perdera hostorial de %s prestamos realizados",
                                                inventario.getNombre(),
                                                inventario.getDescripcion(),
                                                totalPrestamos
                                        )
                                        :
                                        (retrazo >= 0
                                                ?
                                                String.format(
                                                        "¿Estas seguro de eliminar %s %s permanentemente? No es seguro eliminarlo, no solo perdera hostorial de %s prestamos realizados ademas %s tiene %s días para devolerlo.",
                                                        inventario.getNombre(),
                                                        inventario.getDescripcion(),
                                                        totalPrestamos,
                                                        nombreUsuario,
                                                        retrazo
                                                )
                                                :
                                                String.format(
                                                        "¿Estas seguro de eliminar %s %s permanentemente? No es seguro eliminarlo, no solo perdera hostorial de %s prestamos realizados ademas %s tiene %s días de retrazo.",
                                                        inventario.getNombre(),
                                                        inventario.getDescripcion(),
                                                        totalPrestamos,
                                                        nombreUsuario,
                                                        retrazo
                                                )
                                        )
                        )
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (totalPrestamos > 0) {
                                if (DaoHelperInventario.eliminarCascadaPrestamo(inventario.getId(), getContext())) {
                                    eliminarImagen(inventario.getFoto());
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), R.string.inventario_y_sus_prestamos_eliminado_permanentemente, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), R.string.error_al_eliminar_el_inventario_y_sus_prestamos, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (DaoHelperInventario.eliminar(inventario.getId(), getContext())) {
                                    eliminarImagen(inventario.getFoto());
                                } else {
                                    Toast.makeText(getContext(), R.string.error_al_eliminar_el_inventario, Toast.LENGTH_SHORT).show();
                                }
                            }

                            lista.remove(position);
                            notifyDataSetChanged();
                            if (DaoHelperInventario.obtenerTodos(getContext()).isEmpty()) {
                                DaoHelperInventario.reiniciarAutoincremento(getContext());
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(getContext(), R.string.eliminacion_cancelada, Toast.LENGTH_SHORT).show();
                        })
                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(getContext(), R.string.salir_elimicaci_n, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        ibtnFrameRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Restaurar"
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.restaurar_inventario)
                        .setIcon(R.drawable.ic_restore_from_trash_24_success_color)
                        .setMessage(String.format("¿Estas seguro de restaurar %s %s?", inventario.getNombre(), inventario.getDescripcion()))
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (DaoHelperInventario.insertarLogico(inventario.getId(), getContext())) {
                                Toast.makeText(getContext(), R.string.inventario_restaurado, Toast.LENGTH_SHORT).show();
                                inventario.setEstado(true);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), R.string.error_al_restaurar_el_inventario, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(getContext(), R.string.restauraci_n_cancelada, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(getContext(), R.string.salir_restauraci_n, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        return item;
    }

    private void eliminarImagen(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                Toast.makeText(getContext(), R.string.imagen_eliminada, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.error_al_eliminar_la_imagen, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarImagen(ImageView ivInventario, String foto) {
        File imagen = new File(foto);

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            ivInventario.setImageBitmap(bitmap);
        } else {
            // Cargar el icono por defecto
            ivInventario.setImageResource(R.drawable.img_avatar_default);
        }
    }
}

