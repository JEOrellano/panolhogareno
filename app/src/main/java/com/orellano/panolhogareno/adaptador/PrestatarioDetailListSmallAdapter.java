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
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestatario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperUsuario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.Prestatario;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PrestatarioDetailListSmallAdapter extends ArrayAdapter<Prestatario> {
    private List<Prestatario> lista;

    public PrestatarioDetailListSmallAdapter(@NonNull Context context, @NonNull List<Prestatario> objects) {
        super(context, R.layout.card_template_list_small_prestatario_detail, objects);
        this.lista = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.card_template_list_small_prestatario_detail, null);

        Prestatario prestatario = getItem(position);

        Button btnAviso = item.findViewById(R.id.btn_aviso_card_template_small_prestatario);
        TextView tvNombre = item.findViewById(R.id.tv_nombre_prestatario_card_template_small_prestatario);
        TextView tvApellido = item.findViewById(R.id.tv_apellido_prestatario_card_template_small_prestatario);
        ImageView ivPrestatario = item.findViewById(R.id.iv_prestatario_card_template_small_prestatario);
        // frame layout
        ImageView ivFrame = item.findViewById(R.id.iv_frame_layout_prestatario_card_template_small_prestatario);
        TextView tvFrame = item.findViewById(R.id.tv_frame_layout_prestatario_card_template_small_prestatario);
        ImageButton ibtnFrameDelete = item.findViewById(R.id.ib_frame_layout_delete_prestatario_card_template_small_prestatario);
        ImageButton ibtnFrameRestore = item.findViewById(R.id.ib_frame_layout_restore_prestatario_card_template_small_prestatario);

        btnAviso.setEnabled(false);
        btnAviso.setText(prestatario.getEstado()
                ?
                (DaoHelperPrestamo.obtenerTodosPorIdPrestatarioAceptadoActivo(prestatario.getId(), getContext()).size() <= 0
                        ?
                        "ok"
                        :
                        (DaoHelperPrestamo.obtenerTodosAtrazadosPorIdPrestatarioActivo(prestatario.getId(), getContext()).size() > 0
                                ?
                                String.valueOf(
                                        DaoHelperPrestamo
                                                .obtenerTodosAtrazadosPorIdPrestatarioActivo(
                                                        prestatario.getId(),
                                                        getContext()
                                                )
                                                .size()
                                )
                                :
                                String.valueOf(
                                        DaoHelperPrestamo.
                                                obtenerTodosNoAtrazadosPorIdPrestatarioActivo(
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
                (DaoHelperPrestamo.obtenerTodosPorIdPrestatarioAceptadoActivo(prestatario.getId(), getContext()).size() <= 0
                        ?
                        getContext().getColor(R.color.success_color)
                        :
                        (DaoHelperPrestamo.obtenerTodosAtrazadosPorIdPrestatarioActivo(prestatario.getId(), getContext()).size() > 0
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
        //ivPrestatario.setImageResource(R.drawable.img_avatar_default);
        cargarImagen(ivPrestatario, prestatario.getId_usuario().getFoto());

        // frame layout
        if (prestatario.getEstado()) {
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
                Toast.makeText(getContext(), "Eliminar", Toast.LENGTH_SHORT).show();
                Long retrazo = 0L;
                Integer totalPrestamos = DaoHelperPrestamo.obtenerTodosPorIdPrestatario(prestatario.getId(), getContext()).size();
                List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdPrestatarioAceptado(prestatario.getId(), getContext());
                if (!prestamos.isEmpty()) {
                    retrazo = LocalDateTime.now().until(prestamos.get(0).getFechaDevolucion(), ChronoUnit.DAYS);
                }
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.eliminar_prestatario_permanentemente)
                        .setIcon(R.drawable.ic_delete_outline_24_first_color)
                        .setMessage(
                                prestamos.isEmpty()
                                        ?
                                        String.format(
                                                "¿Estas seguro de eliminar permanentemente %s %s?", prestatario.getId_usuario().getNombre(), prestatario.getId_usuario().getApellido()
                                        )
                                        :
                                        (retrazo >= 0
                                                ?
                                                String.format(
                                                        "No se puede eliminar %s %s permanentemente. No es seguro eliminarlo, no solo perderia hostorial de %s prestamos realizados ademas %s tiene %s días para devolerlo.",
                                                        prestatario.getId_usuario().getNombre(),
                                                        prestatario.getId_usuario().getApellido(),
                                                        totalPrestamos,
                                                        prestamos.get(0).getId_inventario().getNombre(),
                                                        retrazo
                                                )
                                                :
                                                String.format(
                                                        "No se puede eliminar %s %s permanentemente. No es seguro eliminarlo, no solo perderia hostorial de %s prestamos realizados ademas %s tiene %s días de retrazo.",
                                                        prestatario.getId_usuario().getNombre(),
                                                        prestatario.getId_usuario().getApellido(),
                                                        totalPrestamos,
                                                        prestamos.get(0).getId_inventario().getNombre(),
                                                        retrazo
                                                )
                                        )
                        )
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (prestamos.isEmpty()) {
                                if (totalPrestamos > 0) {
                                    if (DaoHelperPrestatario.eliminarCascadaPrestamoUsuario(prestatario.getId(), prestatario.getId_usuario().getNombreUsuario(), getContext())) {
                                        eliminarImagen(prestatario.getId_usuario().getFoto());
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), R.string.prestatario_y_sus_prestamos_eliminado_permanentemente, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), R.string.error_al_eliminar_prestatario_permanentemente, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (DaoHelperPrestatario.eliminar(prestatario.getId(), getContext()) && DaoHelperUsuario.eliminar(prestatario.getId_usuario().getNombreUsuario(), getContext())) {
                                        eliminarImagen(prestatario.getId_usuario().getFoto());
                                        Toast.makeText(getContext(), R.string.prestatario_eliminado_permanentemente, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), R.string.error_al_eliminar_prestatario_permanentemente, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                lista.remove(position);
                                notifyDataSetChanged();
                                if (DaoHelperPrestatario.obtenerTodos(getContext()).isEmpty()) {
                                    DaoHelperPrestatario.reiniciarAutoincremento(getContext());
                                }
                            } else {
                                Toast.makeText(getContext(), R.string.no_se_puede_eliminar_prestatario_permanentemente, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(getContext(), R.string.eliminacion_cancelada, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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
                        .setTitle(R.string.restaurar_prestatario)
                        .setIcon(R.drawable.ic_restore_from_trash_24_success_color)
                        .setMessage(String.format("¿Estas seguro de restaurar %s %s?", prestatario.getId_usuario().getNombre(), prestatario.getId_usuario().getApellido()))
                        .setPositiveButton("Si", (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (DaoHelperPrestatario.insertarLogico(prestatario.getId(), getContext())) {
                                Toast.makeText(getContext(), "Prestatario restaurado.", Toast.LENGTH_SHORT).show();
                                prestatario.setEstado(true);
                                //lista.set(position, prestatario);
                                //notifyItemChanged(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "Error al restaurar el prestatario.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(getContext(), R.string.restauraci_n_cancelada, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNeutralButton("Salir", (dialog, which) -> {
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

    private void cargarImagen(ImageView ivPrestatario, String foto) {
        File imagen = new File(foto);

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            ivPrestatario.setImageBitmap(bitmap);
        } else {
            // Cargar el icono por defecto
            ivPrestatario.setImageResource(R.drawable.img_avatar_default);
        }
    }
}

