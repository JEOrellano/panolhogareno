package com.orellano.panolhogareno.adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.enumAdaptador.EAccion;
import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestatario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperUsuario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.Prestatario;
import com.orellano.panolhogareno.entidad.Usuario;
import com.orellano.panolhogareno.entidad.enumEntidad.ERol;
import com.orellano.panolhogareno.ui.dialogo.LendPrestatarioBottomSheetDialogFragment;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PrestatarioAdapter extends RecyclerView.Adapter<PrestatarioAdapter.ViewHolder> implements LendPrestatarioBottomSheetDialogFragment.OnDismissListener {
    private List<Prestatario> lista;
    private Context context;
    private FragmentManager fragmentManager;

    public PrestatarioAdapter(List<Prestatario> lista, Context context, FragmentManager fragmentManager) {
        this.lista = lista;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onDismiss(int position) {
        notifyItemChanged(position);
    }

    // Interfaz escucha para ImageView
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(ImageView imageView, int position);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrestatarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_prestatario_btn_on, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestatarioAdapter.ViewHolder holder, int position) {
        Prestatario prestatario = lista.get(position);
        List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdPrestatarioAceptado(prestatario.getId(), context);

        holder.btnAviso.setEnabled(false);
        holder.btnAviso.setText(prestatario.getEstado()
                ?
                (prestamos.isEmpty()
                        ?
                        "ok"
                        :
                        (DaoHelperPrestamo.obtenerTodosAtrazadosPorIdPrestatario(prestatario.getId(), context).size() > 0
                                ?
                                String.valueOf(
                                        DaoHelperPrestamo
                                                .obtenerTodosAtrazadosPorIdPrestatario(
                                                        prestatario.getId(),
                                                        context
                                                )
                                                .size()
                                )
                                :
                                String.valueOf(
                                        DaoHelperPrestamo.
                                                obtenerTodosNoAtrazadosPorIdPrestatario(
                                                        prestatario.getId(),
                                                        context
                                                )
                                                .size())
                        )
                )
                :
                ":("
        );
        holder.btnAviso.setBackgroundColor(prestatario.getEstado()
                ?
                (prestamos.isEmpty()
                        ?
                        context.getColor(R.color.success_color)
                        :
                        (DaoHelperPrestamo.obtenerTodosAtrazadosPorIdPrestatario(prestatario.getId(), context).size() > 0
                                ?
                                context.getColor(R.color.danger_color)
                                :
                                context.getColor(R.color.warning_color)
                        )
                )
                :
                context.getColor(R.color.gray_dark_color)
        );
        holder.tvNombre.setText(prestatario.getId_usuario().getNombre());
        holder.tvApellido.setText(prestatario.getId_usuario().getApellido());
        holder.ibtnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Eliminar"
                holder.ibtnEliminar.setEnabled(false);
                new AlertDialog.Builder(context)
                        .setTitle(R.string.suspender_prestatario)
                        .setMessage(String.format("¿Estas seguro de suspender %s %s?", prestatario.getId_usuario().getNombre(), prestatario.getId_usuario().getApellido()))
                        .setIcon(R.drawable.ic_delete_outline_24_first_color)
                        .setCancelable(false)
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (DaoHelperPrestatario.eliminarLogico(prestatario.getId(), context)) {
                                Toast.makeText(context, R.string.prestatario_suspendido, Toast.LENGTH_SHORT).show();
                                prestatario.setEstado(false);
                                lista.set(position, prestatario);
                                notifyItemChanged(position);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, R.string.error_al_suspender_el_prestatario, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton(R.string.cancelar, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Eliminar"
                            Toast.makeText(context, R.string.salir_suspender, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                holder.ibtnEliminar.setEnabled(true);
                            }
                        })
                        .show();
            }
        });
        holder.ibtnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Editar"
                holder.ibtnEditar.setEnabled(false);
                Toast.makeText(context, R.string.editar, Toast.LENGTH_SHORT).show();
                // Dialogo edicion
                // 1. Infla el diseño personalizado
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.card_dialog_edit_prestatario, null);
                // 2. Obtén referencias a las vistas del diseño
                ImageView ivIcono = dialogView.findViewById(R.id.iv_icono_card_dialog_edit_prestatario);
                TextView tvTitulo = dialogView.findViewById(R.id.tv_titulo_card_dialog_edit_prestatario);
                TextView tvNombreUsuario = dialogView.findViewById(R.id.tv_nombre_usuario_card_dialog_edit_prestatario);
                EditText etNombreUsuario = dialogView.findViewById(R.id.et_nombre_usuario_card_dialog_edit_prestatario);
                TextView tvNombre = dialogView.findViewById(R.id.tv_nombre_card_dialog_edit_prestatario);
                EditText etNombre = dialogView.findViewById(R.id.et_nombre_card_dialog_edit_prestatario);
                TextView tvApellido = dialogView.findViewById(R.id.tv_apellido_card_dialog_edit_prestatario);
                EditText etApellido = dialogView.findViewById(R.id.et_apellido_card_dialog_edit_prestatario);
                TextView tvRol = dialogView.findViewById(R.id.tv_rol_card_dialog_edit_prestatario);
                Spinner spRol = dialogView.findViewById(R.id.sp_rol_card_dialog_edit_prestatario);
                ImageView ivFoto = dialogView.findViewById(R.id.iv_foto_card_dialog_edit_prestatario);
                Button btnCancelar = dialogView.findViewById(R.id.btn_cancelar_card_dialog_edit_prestatario);
                Button btnEditar = dialogView.findViewById(R.id.id_btn_editar_card_dialog_edit_prestatario);
                // 3. Establece valores iniciales en las vistas
                ivIcono.setImageResource(R.drawable.ic_edit_24_first_color);
                tvTitulo.setText(R.string.editar_prestatario);
                tvNombreUsuario.setText(R.string.nombre_usuario);
                etNombreUsuario.setText(prestatario.getId_usuario().getNombreUsuario());
                etNombreUsuario.setHint(R.string.ingrese_el_nombre_de_usuario);
                tvNombre.setText(R.string.nombre);
                etNombre.setText(prestatario.getId_usuario().getNombre());
                etNombre.setHint(R.string.ingrese_el_nombre);
                tvApellido.setText(R.string.apellido);
                etApellido.setText(prestatario.getId_usuario().getApellido());
                etApellido.setHint(R.string.ingrese_el_apellido);
                tvRol.setText(R.string.rol);
                ERolSpinnerAdapter adapter = new ERolSpinnerAdapter(context, List.of(ERol.values()));
                spRol.setAdapter(adapter);
                spRol.setSelection(prestatario.getId_usuario().getRol().ordinal());
                spRol.setEnabled(false);
                cargarImagen(ivFoto, prestatario.getId_usuario().getFoto());
                // 4. Crea el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog editDialog = builder.create();
                // 5. Establece los oyentes de clics de las vistas
                ivFoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Acción al hacer clic en la imagen
                        ivFoto.setEnabled(false);
                        Toast.makeText(context, "Imagen", Toast.LENGTH_SHORT).show();
                        // Crear un diálogo para elegir entre cámara y galería
                        showImageSourceDialog(ivFoto, position);
                    }
                });
                ivFoto.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Restablecer imagen prestatario
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setPositiveButton(R.string.si, (dialog, which) -> {
                            // Restablecer imagen prestatario
                            cargarImagen(ivFoto, prestatario.getId_usuario().getFoto());
                        });
                        builder.setNegativeButton(R.string.no, null);
                        builder.setTitle(R.string.restablecer_imagen);
                        builder.setMessage(R.string.est_s_seguro_de_que_deseas_reiniciar_esta_imagen);
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
                        // Acción al hacer clic en el botón "Cancelar"
                        Toast.makeText(context, R.string.edici_n_cancelada, Toast.LENGTH_SHORT).show();
                        editDialog.dismiss();
                    }
                });
                btnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Acción al hacer clic en el botón "Editar"
                        btnEditar.setEnabled(false);
                        Usuario usuarioEditado = new Usuario(
                                etNombreUsuario.getText().toString(),
                                prestatario.getId_usuario().getClave(),
                                etNombre.getText().toString(),
                                etApellido.getText().toString(),
                                prestatario.getId_usuario().getRol(),
                                prestatario.getId_usuario().getFoto(),
                                prestatario.getId_usuario().getEstado()
                        );

                        switch ((ERol) spRol.getSelectedItem()) {
                            case ADMIN:
                                Toast.makeText(context, "Administrador", Toast.LENGTH_SHORT).show();
                                btnEditar.setEnabled(true);
                                break;
                            case PRESTATARIO:
                                AlertDialog.Builder confirmEditDialog = new AlertDialog.Builder(context)
                                        .setTitle(R.string.editar_prestatario)
                                        .setMessage(R.string.estas_seguro_de_editar_este_prestatario)
                                        .setIcon(R.drawable.ic_edit_24_first_color)
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.si, (dialog, which) -> {
                                            // Guardar la imagen en galeria
                                            ivFoto.setDrawingCacheEnabled(true);
                                            guardarImageGaleria(ivFoto.getDrawingCache(), prestatario.getId_usuario().getFoto());
                                            ivFoto.setDrawingCacheEnabled(false);
                                            // Acción al hacer clic en el botón "Sí"
                                            DaoHelperUsuario.actualizar(usuarioEditado, context);
                                            // Actualiza la lista del adaptador
                                            prestatario.setId_usuario(usuarioEditado);
                                            notifyItemChanged(position);
                                            Toast.makeText(context, R.string.prestatario_editado, Toast.LENGTH_SHORT).show();

                                            etNombreUsuario.setText(usuarioEditado.getNombreUsuario());
                                            etNombre.setText(usuarioEditado.getNombre());
                                            etApellido.setText(usuarioEditado.getApellido());
                                            spRol.setSelection(usuarioEditado.getRol().ordinal());
                                            cargarImagen(ivFoto, usuarioEditado.getFoto());
                                            btnEditar.setEnabled(true);

                                            dialog.dismiss();
                                        })
                                        .setNegativeButton(R.string.no, (dialog, which) -> {
                                            // Acción al hacer clic en el botón "No"
                                            Toast.makeText(context, R.string.edici_n_cancelada, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        })
                                        .setNegativeButton(R.string.no, (dialog, which) -> {
                                            // Acción al hacer clic en el botón "No"
                                            Toast.makeText(context, R.string.edici_n_cancelada, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        })
                                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                                            // Acción al hacer clic en el botón "Salir"
                                            Toast.makeText(context, R.string.salir_edici_n, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            editDialog.dismiss();
                                        })
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                btnEditar.setEnabled(true);
                                            }
                                        });
                                confirmEditDialog.show();
                                break;
                            default:
                                Toast.makeText(context, "Default", Toast.LENGTH_SHORT).show();
                                btnEditar.setEnabled(true);
                                break;
                        }
                    }
                });

                editDialog.setCanceledOnTouchOutside(false);
                editDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        holder.ibtnEditar.setEnabled(true);
                    }
                });
                // 6. Muestra el AlertDialog
                editDialog.show();
            }
        });
        //holder.ivFoto.setImageResource(R.drawable.img_avatar_default);
        cargarImagen(holder.ivFoto, prestatario.getId_usuario().getFoto());
        holder.tvNombreUsuario.setText(prestatario.getId_usuario().getNombreUsuario());
        holder.tvId.setText(prestatario.getId().toString());
        holder.tvComentario.setText(
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
        holder.btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Devolver"
                holder.btnDevolver.setEnabled(false);
                holder.pbDevolverPrestar.setVisibility(View.VISIBLE);
                Toast.makeText(context, R.string.devolver, Toast.LENGTH_SHORT).show();
                LendPrestatarioBottomSheetDialogFragment lendPrestatarioBottomSheetDialogFragment = new LendPrestatarioBottomSheetDialogFragment(PrestatarioAdapter.this, lista, position, EAccion.DEVOLVER);
                lendPrestatarioBottomSheetDialogFragment.show(fragmentManager, "lend_prestatario.dialog");
                // Configurar el listener
                lendPrestatarioBottomSheetDialogFragment.setOnDismissListener(new LendPrestatarioBottomSheetDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss(int position) {
                        holder.btnDevolver.setEnabled(true);
                        holder.pbDevolverPrestar.setVisibility(View.GONE);
                        PrestatarioAdapter.this.notifyItemChanged(position);
                    }
                });
            }
        });
        holder.btnPrestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Prestar
                holder.btnPrestar.setEnabled(false);
                holder.pbDevolverPrestar.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Prestar", Toast.LENGTH_SHORT).show();
                LendPrestatarioBottomSheetDialogFragment lendPrestatarioBottomSheetDialogFragment = new LendPrestatarioBottomSheetDialogFragment(PrestatarioAdapter.this, lista, position, EAccion.PRESTAR);
                lendPrestatarioBottomSheetDialogFragment.show(fragmentManager, "lend_prestatario.dialog");
                // Configurar el listener
                lendPrestatarioBottomSheetDialogFragment.setOnDismissListener(new LendPrestatarioBottomSheetDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss(int position) {
                        holder.btnPrestar.setEnabled(true);
                        holder.pbDevolverPrestar.setVisibility(View.GONE);
                        PrestatarioAdapter.this.notifyItemChanged(position);
                    }
                });
            }
        });
        // frame layout
        if (prestatario.getEstado()) {
            holder.ivFrame.setVisibility(View.GONE);
            holder.tvFrame.setVisibility(View.GONE);
            holder.ibtnFrameDelete.setVisibility(View.GONE);
            holder.ibtnFrameRestore.setVisibility(View.GONE);
            // habilitar acciones
            holder.ibtnEliminar.setEnabled(true);
            holder.ibtnEditar.setEnabled(true);
            holder.btnDevolver.setEnabled(!prestamos.isEmpty());
            holder.btnPrestar.setEnabled(true);
        } else {
            holder.ivFrame.setVisibility(View.VISIBLE);
            holder.tvFrame.setVisibility(View.VISIBLE);
            holder.ibtnFrameDelete.setVisibility(View.VISIBLE);
            holder.ibtnFrameRestore.setVisibility(View.VISIBLE);
            // deshabilitar acciones
            holder.ibtnEliminar.setEnabled(false);
            holder.ibtnEditar.setEnabled(false);
            holder.btnDevolver.setEnabled(false);
            holder.btnPrestar.setEnabled(false);
        }
        holder.ibtnFrameDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Eliminar"
                Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();
                Long retrazo = 0L;
                Integer totalPrestamos = DaoHelperPrestamo.obtenerTodosPorIdPrestatario(prestatario.getId(), context).size();
                if (!prestamos.isEmpty()) {
                    retrazo = LocalDateTime.now().until(prestamos.get(0).getFechaDevolucion(), ChronoUnit.DAYS);
                }
                new AlertDialog.Builder(context)
                        .setTitle(R.string.eliminar_prestatario_permanentemente)
                        .setIcon(R.drawable.ic_delete_outline_24_first_color)
                        .setMessage(
                                prestamos.isEmpty()
                                        ?
                                        String.format(
                                                "¿Estas seguro de eliminar permanentemente %s %s?. Es seguro eliminarlo, solo perdera  historial de %s prestamos realizados.",
                                                prestatario.getId_usuario().getNombre(),
                                                prestatario.getId_usuario().getApellido(),
                                                totalPrestamos
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
                                    if (DaoHelperPrestatario.eliminarCascadaPrestamoUsuario(prestatario.getId(), prestatario.getId_usuario().getNombreUsuario(), context)) {
                                        eliminarImagen(prestatario.getId_usuario().getFoto());
                                        lista.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, lista.size());
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                        Toast.makeText(context, R.string.prestatario_y_sus_prestamos_eliminado_permanentemente, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, R.string.error_al_eliminar_prestatario_permanentemente, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (DaoHelperPrestatario.eliminar(prestatario.getId(), context) && DaoHelperUsuario.eliminar(prestatario.getId_usuario().getNombreUsuario(), context)) {
                                        eliminarImagen(prestatario.getId_usuario().getFoto());
                                        lista.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, lista.size());
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, R.string.error_al_eliminar_prestatario_permanentemente, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (DaoHelperPrestatario.obtenerTodos(context).isEmpty()) {
                                    DaoHelperPrestatario.reiniciarAutoincremento(context);
                                }
                            } else {
                                Toast.makeText(context, R.string.no_se_puede_eliminar_prestatario_permanentemente, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(context, R.string.eliminacion_cancelada, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(context, R.string.salir_elimicaci_n, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        holder.ibtnFrameRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Restaurar"
                Toast.makeText(context, "Restaurar", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(context)
                        .setTitle(R.string.restaurar_prestatario)
                        .setIcon(R.drawable.ic_restore_from_trash_24_success_color)
                        .setMessage(String.format("¿Estas seguro de restaurar %s %s?", prestatario.getId_usuario().getNombre(), prestatario.getId_usuario().getApellido()))
                        .setPositiveButton("Si", (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (DaoHelperPrestatario.insertarLogico(prestatario.getId(), context)) {
                                Toast.makeText(context, "Prestatario restaurado.", Toast.LENGTH_SHORT).show();
                                prestatario.setEstado(true);
                                lista.set(position, prestatario);
                                notifyItemChanged(position);
                            } else {
                                Toast.makeText(context, "Error al restaurar el prestatario.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(context, "Restauracion cancelada.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNeutralButton("Salir", (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(context, "Salir restauracion.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    private void guardarImageGaleria(Bitmap imageBitmap, String foto) {
        File imagen = new File(foto);
        imageBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imagen);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(imagen));
            context.sendBroadcast(mediaScanIntent);

            Toast.makeText(context, R.string.imagen_guardada_en_la_galer_a, Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showImageSourceDialog(ImageView ivFoto, int position) {
        if (listener != null) {
            listener.onImageClick(ivFoto, position);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.seleccionar_opci_n);
        builder.setItems(new CharSequence[]{context.getString(R.string.c_mara), context.getString(R.string.galer_a)}, (dialog, which) -> {
            if (which == 0) { // Iniciar la cámara
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    (fragmentManager.getFragments().get(0)).startActivityForResult(takePictureIntent, Constantes.CAMARA_REQUEST_CODE);
                }
            } else if (which == 1) { // Abrir la galería
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                (fragmentManager.getFragments().get(0)).startActivityForResult(pickPhoto, Constantes.GALERIA_REQUEST_CODE);
            }
        });
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_image_24_first_color);
        builder.setNegativeButton(R.string.cancelar, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ivFoto.setEnabled(true);
            }
        });
        builder.create();
        builder.show();
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

    private void eliminarImagen(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                Toast.makeText(context, "Imagen eliminada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al eliminar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnAviso;
        public TextView tvNombre;
        public TextView tvApellido;
        public ImageButton ibtnEliminar;
        public ImageButton ibtnEditar;
        public ImageView ivFoto;
        public TextView tvNombreUsuario;
        public TextView tvId;
        public TextView tvComentario;
        public ProgressBar pbDevolverPrestar;
        public Button btnDevolver;
        public Button btnPrestar;
        // frame layout
        public ImageView ivFrame;
        public TextView tvFrame;
        public ImageButton ibtnFrameDelete;
        public ImageButton ibtnFrameRestore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAviso = itemView.findViewById(R.id.btn_aviso_card_btn_on_prestatario);
            tvNombre = itemView.findViewById(R.id.tv_titulo1_card_btn_on_prestatario);
            tvApellido = itemView.findViewById(R.id.tv_subtitulo1_card_btn_on_prestatario);
            ibtnEliminar = itemView.findViewById(R.id.iv_delete_card_btn_on_prestatario);
            ibtnEditar = itemView.findViewById(R.id.iv_edit_card_btn_on_prestatario);
            ivFoto = itemView.findViewById(R.id.iv_foto_card_btn_on_prestatario);
            tvNombreUsuario = itemView.findViewById(R.id.tv_titulo2_card_btn_on_prestatario);
            tvId = itemView.findViewById(R.id.tv_subtitulo2_card_btn_on_prestatario);
            tvComentario = itemView.findViewById(R.id.tv_comentario_card_btn_on_prestatario);
            pbDevolverPrestar = itemView.findViewById(R.id.pb_devolver_prestar_card_btn_on_prestatario);
            btnDevolver = itemView.findViewById(R.id.btn_devolver_card_btn_on_prestatario);
            btnPrestar = itemView.findViewById(R.id.btn_prestar_card_btn_on_prestatario);
            // frame layout
            ivFrame = itemView.findViewById(R.id.iv_frame_layout_prestatario);
            tvFrame = itemView.findViewById(R.id.tv_frame_layout_prestatario);
            ibtnFrameDelete = itemView.findViewById(R.id.ib_frame_layout_delete_prestatario);
            ibtnFrameRestore = itemView.findViewById(R.id.ib_frame_layout_restore_prestatario);
        }
    }
}

