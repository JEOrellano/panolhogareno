package com.orellano.panolhogareno.adaptador;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.daoSQLite.DaoHelperInventario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestatario;
import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.Prestatario;
import com.orellano.panolhogareno.entidad.enumEntidad.EEstadoPrestamo;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;
import com.orellano.panolhogareno.ui.dialogo.LendInventarioBottomSheetDialogFragment;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> implements LendInventarioBottomSheetDialogFragment.OnDismissListener {
    private List<Inventario> lista;
    private Context context;
    private FragmentManager fragmentManager;
    private File appDir; // Directorio de la app

    public InventarioAdapter(List<Inventario> lista, Context context, FragmentManager fragmentManager) {
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
    public InventarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_default_btn_on, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull InventarioAdapter.ViewHolder holder, int position) {
        initAppDir();
        Inventario inventario = lista.get(position);
        List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(inventario.getId(), context);
        holder.btnRetraso.setText(inventario.getEstado()
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
        holder.btnRetraso.setBackgroundColor(inventario.getEstado()
                ?
                (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                        ?
                        context.getColor(R.color.success_color)
                        :
                        (
                                (prestamos.isEmpty()
                                        ?
                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                        :
                                        LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
                                ) >= 0
                                        ?
                                        context.getColor(R.color.warning_color)
                                        :
                                        context.getColor(R.color.danger_color)
                        )
                )
                :
                context.getColor(R.color.gray_dark_color)
        );
        holder.tvNombre.setText(inventario.getNombre());
        holder.tvDescripcion.setText(inventario.getDescripcion());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Eliminar"
                holder.ivDelete.setEnabled(false);
                new AlertDialog.Builder(context)
                        .setTitle(R.string.suspender_inventario)
                        .setMessage(String.format("¿Estas seguro de suspender %s %s?", inventario.getNombre(), inventario.getDescripcion()))
                        .setIcon(R.drawable.ic_delete_outline_24_first_color)
                        .setCancelable(false)
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            // TODO: Eliminar el inventario de SQLite
                            if (DaoHelperInventario.eliminarLogico(inventario.getId(), context)) {
                                Toast.makeText(context, R.string.inventario_suspendido, Toast.LENGTH_SHORT).show();
                                inventario.setEstado(false);
                                lista.set(position, inventario);
                                notifyItemChanged(position);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, R.string.error_al_suspender_el_inventario, Toast.LENGTH_SHORT).show();
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
                                holder.ivDelete.setEnabled(true);
                            }
                        })
                        .show();
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Editar"
                holder.ivEdit.setEnabled(false);
                Toast.makeText(context, R.string.editar, Toast.LENGTH_SHORT).show();
                // Dialogo edicion
                // 1. Infla el diseño personalizado
                /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.card_dialog_edit_inventario, null);
                // 2. Obtén referencias a las vistas del diseño
                ImageView ivIcono = dialogView.findViewById(R.id.iv_icono_card_dialog_edit_inventario);
                TextView tvTitulo = dialogView.findViewById(R.id.tv_titulo_card_dialog_edit_inventario);
                TextView tvId = dialogView.findViewById(R.id.tv_id_card_dialog_edit_inventario);
                EditText etId = dialogView.findViewById(R.id.et_id_card_dialog_edit_inventario);
                TextView tvNombre = dialogView.findViewById(R.id.tv_nombre_card_dialog_edit_inventario);
                EditText etNombre = dialogView.findViewById(R.id.et_nombre_card_dialog_edit_inventario);
                TextView tvDescripcion = dialogView.findViewById(R.id.tv_descripcion_card_dialog_edit_inventario);
                EditText etDescripcion = dialogView.findViewById(R.id.et_descripcion_card_dialog_edit_inventario);
                TextView tvUbicacion = dialogView.findViewById(R.id.tv_ubicacion_card_dialog_edit_inventario);
                Spinner spUbicacion = dialogView.findViewById(R.id.sp_ubicacion_card_dialog_edit_inventario);
                ImageView ivFoto = dialogView.findViewById(R.id.iv_foto_card_dialog_edit_inventario);
                CardView llIP = dialogView.findViewById(R.id.ll_card_dialog_edit_inventario_sheet_dialog_lend_inventario);
                Button btnCancelar = dialogView.findViewById(R.id.btn_cancelar_card_dialog_edit_inventario);
                Button btnEditar = dialogView.findViewById(R.id.id_btn_editar_card_dialog_edit_inventario);
                // 3. Establece valores iniciales en las vistas
                ivIcono.setImageResource(R.drawable.ic_edit_24_first_color);
                tvTitulo.setText(R.string.editar_inventario);
                tvId.setText(R.string.id);
                etId.setText(inventario.getId().toString());
                tvNombre.setText(R.string.nombre);
                etNombre.setText(inventario.getNombre());
                etNombre.setHint(R.string.ingrese_el_nombre);
                tvDescripcion.setText(R.string.descripcion);
                etDescripcion.setText(inventario.getDescripcion());
                etDescripcion.setHint(R.string.ingrese_la_descripcion);
                tvUbicacion.setText(R.string.ubicacion);
                EUbicacionSpinnerAdapter adapter = new EUbicacionSpinnerAdapter(context, List.of(EUbicacion.values()));
                spUbicacion.setAdapter(adapter);
                spUbicacion.setSelection(inventario.getUbicacion().ordinal());
                spUbicacion.setEnabled(true);
                /*ivFoto.setImageResource(R.drawable.img_item_default);*/
                cargarImagen(ivFoto, inventario.getFoto());
                llIP.setVisibility(inventario.getUbicacion().equals(EUbicacion.DEPOSITO) ? View.GONE : View.VISIBLE);
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
                        // Restablecer imagen inventario
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Restablecer iamgen inventario
                                //eliminarInventarioYImagen(itemId);
                                cargarImagen(ivFoto, inventario.getFoto());
                            }
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
                        // TODO: Editar el inventario de SQLite
                        Inventario inventarioEditado = new Inventario(
                                Long.parseLong(etId.getText().toString()),
                                etNombre.getText().toString(),
                                etDescripcion.getText().toString(),
                                EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition()),
                                inventario.getFoto(),
                                inventario.getEstado());

                        switch (EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition())) {
                            case DEPOSITO:
                                llIP.setVisibility(View.GONE);
                                btnEditar.setVisibility(View.VISIBLE);
                                btnCancelar.setVisibility(View.VISIBLE);
                                AlertDialog.Builder confirmEditDialog = new AlertDialog.Builder(context)
                                        .setTitle(R.string.editar_inventario)
                                        .setMessage(R.string.estas_seguro_de_editar_este_inventario)
                                        .setIcon(R.drawable.ic_edit_24_first_color)
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.si, (dialog, which) -> {
                                            // Guardar la imagen en la galería
                                            ivFoto.setDrawingCacheEnabled(true);
                                            guardarImagenGaleria(ivFoto.getDrawingCache(), inventarioEditado.getFoto());
                                            ivFoto.setDrawingCacheEnabled(false);
                                            if (!prestamos.isEmpty()) {
                                                prestamos.get(prestamos.size() - 1).setEstadoPrestamo(EEstadoPrestamo.CONCLUIDO);
                                                DaoHelperPrestamo.actualizar(prestamos.get(prestamos.size() - 1), context);
                                            }
                                            // Acción al hacer clic en el botón "Sí"
                                            DaoHelperInventario.actualizar(inventarioEditado, context);
                                            // Actualiza la lista del adaptador
                                            lista.set(position, inventarioEditado);
                                            notifyItemChanged(position);
                                            Toast.makeText(context, R.string.inventario_editado, Toast.LENGTH_SHORT).show();

                                            etId.setText(inventarioEditado.getId().toString());
                                            etNombre.setText(inventarioEditado.getNombre());
                                            etDescripcion.setText(inventarioEditado.getDescripcion());
                                            spUbicacion.setSelection(inventarioEditado.getUbicacion().ordinal());
                                            /*ivFoto.setImageResource(R.drawable.img_item_default);*/
                                            cargarImagen(ivFoto, inventarioEditado.getFoto());
                                            prestamos.clear();
                                            prestamos.addAll(DaoHelperPrestamo.obtenerTodosPorIdInventarioActivo(Long.parseLong(etId.getText().toString()), context));
                                            btnEditar.setEnabled(true);
                                            // Actualizar notificacion
                                            if (context instanceof MainActivity) {
                                                ((MainActivity) context).updateNotificationBadge();
                                            }

                                            dialog.dismiss();
                                    /*DaoHelperInventario.actualizar(inventarioEditado, context);
                                    // Actualiza la lista del adaptador
                                    lista.set(position, inventarioEditado);
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Inventario editado.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();*/
                                        })
                                        .setNegativeButton(R.string.no, (dialog, which) -> {
                                            // Acción al hacer clic en el botón "No"
                                            Toast.makeText(context, R.string.edici_n_cancelada, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        })
                                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                                            // Acción al hacer clic en el botón "Cancelar"
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
                            case PRESTADO:
                                llIP.setVisibility(View.VISIBLE);
                                btnEditar.setVisibility(View.GONE);
                                btnCancelar.setVisibility(View.GONE);
                                Toast.makeText(context, "PRESTADO", Toast.LENGTH_SHORT).show();
                                btnEditar.setEnabled(true);
                                break;
                            default:
                                btnEditar.setEnabled(true);
                                break;
                        }
                    }
                });
                spUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentSPUbicacion1, View viewSPUbicacion1, int positionSPUbicacion1, long idSPUbicacion1) {
                        Toast.makeText(context, "Ubicacion: " + spUbicacion.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        switch (EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition())) {
                            case DEPOSITO:
                                llIP.setVisibility(View.GONE);
                                btnEditar.setVisibility(View.VISIBLE);
                                btnCancelar.setVisibility(View.VISIBLE);
                                break;
                            case PRESTADO:
                                llIP.setVisibility(View.VISIBLE);
                                btnEditar.setVisibility(View.GONE);
                                btnCancelar.setVisibility(View.GONE);
                                ImageView ivIcono = dialogView.findViewById(R.id.iv_icono_card_sheet_dialog_lend_inventario);
                                TextView tvTitulo = dialogView.findViewById(R.id.tv_titulo_card_sheet_dialog_lend_inventario);
                                Button btnAvisoInventario = dialogView.findViewById(R.id.btn_aviso_sheet_dialog_lend_inventario);
                                TextView tvNombreInventario = dialogView.findViewById(R.id.tv_nombre_iventario_sheet_dialog_lend_inventario);
                                TextView tvDescripcionInventario = dialogView.findViewById(R.id.tv_descripcion_inventario_sheet_dialog_lend_inventario);
                                ImageView ivInventario = dialogView.findViewById(R.id.iv_inventario_sheet_dialog_lend_inventario);
                                TextView tvLabelNombreUsuario = dialogView.findViewById(R.id.tv_label_nombre_usuario_sheet_dialog_lend_inventario);
                                ImageButton ibtnBuscar = dialogView.findViewById(R.id.ibtn_buscar_sheet_dialog_lend_inventario);
                                EditText etBuscarNombreUsuario = dialogView.findViewById(R.id.et_nombre_usuario_sheet_dialog_lend_inventario);
                                TextView tvLabelDateDevolucion = dialogView.findViewById(R.id.tv_label_date_devolucion_sheet_dialog_lend_inventario);
                                TextView tvDateDevolucion = dialogView.findViewById(R.id.tv_date_devolucion_sheet_dialog_lend_inventario);
                                TextView tvLabelHoraDevolucion = dialogView.findViewById(R.id.tv_label_hora_devolucion_sheet_dialog_lend_inventario);
                                TextView tvTimeDevolucion = dialogView.findViewById(R.id.tv_time_devolucion_sheet_dialog_lend_inventario);
                                Button btnCancelar1 = dialogView.findViewById(R.id.btn_cancelar1_card_sheet_dialog_lend_inventario);
                                Button btnAceptar1 = dialogView.findViewById(R.id.id_btn_aceptar1_card_sheet_dialog_lend_inventario);
                                ListView lvPrestatario = dialogView.findViewById(R.id.lv_prestatario_sheet_dialog_lend_inventario);

                                // Establecer valores iniciales en las vistas
                                /*Inventario inventario = new Inventario(
                                        Long.parseLong(etId.getText().toString()),
                                        etNombre.getText().toString(),
                                        etDescripcion.getText().toString(),
                                        EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition()),
                                        ivFoto.toString(),
                                        true);*/
                                //inventario.setNombre(etNombre.getText().toString());
                                //inventario.setDescripcion(etDescripcion.getText().toString());
                                //inventario.setUbicacion(EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition()));
                                //inventario.setFoto(ivFoto.toString());

                                //final List<Prestamo>[] prestamos = new List[]{DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(Long.parseLong(etId.getText().toString()), context)};
                                ivIcono.setImageResource(R.drawable.ic_handshake_24_first_color);
                                tvTitulo.setText(inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                                        ?
                                        R.string.prestar_inventario
                                        :
                                        R.string.devolver_inventario
                                );
                                btnAvisoInventario.setText(inventario.getEstado()
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
                                btnAvisoInventario.setBackgroundColor(inventario.getEstado()
                                        ?
                                        (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                                                ?
                                                context.getColor(R.color.success_color)
                                                :
                                                (
                                                        (prestamos.isEmpty()
                                                                ?
                                                                Constantes.DIAS_PRESTAMO_DEFAULT
                                                                :
                                                                LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
                                                        ) >= 0
                                                                ?
                                                                context.getColor(R.color.warning_color)
                                                                :
                                                                context.getColor(R.color.danger_color)
                                                )
                                        )
                                        :
                                        context.getColor(R.color.gray_dark_color)
                                );
                                tvNombreInventario.setText(
                                        etNombre.getText().toString()
                                );
                                tvDescripcionInventario.setText(
                                        etDescripcion.getText().toString()
                                );
                                /*ivInventario.setImageResource(R.drawable.img_item_default);*/
                                ivInventario.setImageDrawable(
                                        ivFoto.getDrawable()
                                );
                                final ArrayList<Prestatario>[] prestatarios = new ArrayList[]{(ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodos(context)};
                                List<Prestatario> prestatarioList = prestatarios[0];
                                etBuscarNombreUsuario.setEnabled(true);
                                ibtnBuscar.setEnabled(true);
                                lvPrestatario.setEnabled(true);
                                tvDateDevolucion.setEnabled(true);
                                tvTimeDevolucion.setEnabled(true);
                                btnCancelar1.setVisibility(View.GONE);
                                btnAceptar1.setVisibility(View.GONE);

                                tvDateDevolucion.setText(prestamos.isEmpty()
                                        ?
                                        LocalDate.now()
                                                .plusDays(Constantes.DIAS_PRESTAMO_DEFAULT)
                                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE))
                                        :
                                        prestamos.get(prestamos.size() - 1).getFechaDevolucion()
                                                .toLocalDate()
                                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE))
                                );
                                tvTimeDevolucion.setText(prestamos.isEmpty()
                                        ?
                                        LocalTime.now()
                                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME))
                                        :
                                        prestamos.get(prestamos.size() - 1).getFechaDevolucion()
                                                .toLocalTime()
                                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME))
                                );
                                etBuscarNombreUsuario.setText(prestamos.isEmpty()
                                        ?
                                        ""
                                        :
                                        prestamos.get(prestamos.size() - 1).getId_prestatario().getId_usuario().getNombreUsuario()
                                );
                                etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);

                                PrestatarioDetailListSmallAdapter prestatarioDetailListSmallAdapter = new PrestatarioDetailListSmallAdapter(context, prestatarioList);
                                lvPrestatario.setAdapter(prestatarioDetailListSmallAdapter);
                                ibtnBuscar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        prestatarios[0].clear();
                                        String nombreUsuario = etBuscarNombreUsuario.getText().toString().trim();
                                        // Acción al hacer clic en el botón "Buscar";
                                        if (nombreUsuario.isEmpty()) {
                                            prestatarios[0].addAll((ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodos(context));
                                        } else {
                                            prestatarios[0].addAll((ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodosPorNombreUsuario(nombreUsuario, context));
                                        }
                                        prestatarioDetailListSmallAdapter.notifyDataSetChanged();
                                    }
                                });
                                tvDateDevolucion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Acción al hacer clic en el TextView "Date";
                                        tvDateDevolucion.setEnabled(false);
                                        LocalDateTime now = LocalDate.parse(tvDateDevolucion.getText()).atTime(LocalTime.parse(tvTimeDevolucion.getText()));
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                if (
                                                        LocalDate.of(year, month + 1, dayOfMonth).isEqual(LocalDate.now())
                                                                && now.toLocalTime().isBefore(LocalTime.now())
                                                ) {
                                                    year = LocalDate.now().getYear();
                                                    month = LocalDate.now().getMonthValue() - 1;
                                                    dayOfMonth = LocalDate.now().getDayOfMonth();

                                                    tvTimeDevolucion.setText(LocalTime.now().format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME)));
                                                    Toast.makeText(context, "La hora no puede ser menor a la hora actual.", Toast.LENGTH_SHORT).show();

                                                }
                                                tvDateDevolucion.setText(LocalDate
                                                        .of(year, month + 1, dayOfMonth)
                                                        .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE))
                                                );
                                                Toast.makeText(context, "Fecha seleccionada.", Toast.LENGTH_SHORT).show();
                                            }
                                        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());

                                        // Establecer el título del DatePickerDialog
                                        datePickerDialog.setTitle("Fecha devolucion.");
                                        datePickerDialog.setMessage("Se suman tres dias a la fecha del dia de hoy por defecto.");
                                        datePickerDialog.setIcon(R.drawable.ic_calendar_month_24_first_day);
                                        datePickerDialog.setCanceledOnTouchOutside(false);
                                        // Establecer la fecha mínima en la que se puede seleccionar
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        // Handling Cancel
                                        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(context, "Fecha cancelada.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // Handling Dismiss
                                        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                tvDateDevolucion.setEnabled(true);
                                            }
                                        });

                                        datePickerDialog.show();
                                    }
                                });
                                tvTimeDevolucion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Acción al hacer clic en el ImageButton "Time";
                                        tvTimeDevolucion.setEnabled(false);
                                        LocalDateTime now = LocalDate.parse(tvDateDevolucion.getText()).atTime(LocalTime.parse(tvTimeDevolucion.getText()));
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                if (
                                                        now.toLocalDate().isEqual(LocalDate.now())
                                                                && LocalTime.of(hourOfDay, minute, 0).isBefore(LocalTime.now())
                                                ) {
                                                    hourOfDay = LocalTime.now().getHour();
                                                    minute = LocalTime.now().getMinute();
                                                    Toast.makeText(context, "La hora no puede ser menor a la hora actual.", Toast.LENGTH_SHORT).show();
                                                }
                                                tvTimeDevolucion.setText(LocalTime
                                                        .of(hourOfDay, minute)
                                                        .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME))
                                                );
                                                Toast.makeText(context, "Hora seleccionada.", Toast.LENGTH_SHORT).show();
                                            }
                                        }, now.getHour(), now.getMinute(), true);

                                        timePickerDialog.setTitle("Hora devolucion.");
                                        timePickerDialog.setMessage("En el mismo dia la hora no puede ser menor a la hora actual y se configura a la actual por defecto.");
                                        timePickerDialog.setIcon(R.drawable.ic_access_time_24_first_color);
                                        timePickerDialog.setCanceledOnTouchOutside(false);

                                        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(context, "Hora cancelada.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                tvTimeDevolucion.setEnabled(true);
                                            }
                                        });

                                        timePickerDialog.show();
                                    }
                                });
                                btnCancelar1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Acción al hacer clic en el botón "Cancelar";
                                        Toast.makeText(context, "Cancelado devolver.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                btnAceptar1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(context, "Aceptado.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                lvPrestatario.setOnItemClickListener((parentlv1, viewlv1, positionlv1, idlv1) -> {
                                    // Acción al hacer clic en un elemento de la lista
                                    lvPrestatario.setEnabled(false);
                                    Prestatario prestatario = prestatarios[0].get(positionlv1);
                                    tvNombreInventario.setText(
                                            etNombre.getText().toString()
                                    );
                                    tvDescripcionInventario.setText(
                                            etDescripcion.getText().toString()
                                    );
                                    ivInventario.setImageDrawable(
                                            ivFoto.getDrawable()
                                    );
                                    /*LocalDateTime fechaDevolucion = LocalDate.parse(etDateDevolucion.getText()).atTime(LocalTime.parse(etTimeDevolucion.getText()));*/
                                    LocalDate dateDevolucion = tvDateDevolucion.getText().toString().isEmpty()
                                            ? LocalDate.now().plusDays(Constantes.DIAS_PRESTAMO_DEFAULT)
                                            : LocalDate.parse(tvDateDevolucion.getText());
                                    LocalTime timeDevolucion = tvTimeDevolucion.getText().toString().isEmpty()
                                            ? LocalTime.now()
                                            : LocalTime.parse(tvTimeDevolucion.getText());
                                    LocalDateTime fechaDevolucion = LocalDateTime.of(dateDevolucion, timeDevolucion).isBefore(LocalDateTime.now())
                                            ? LocalDateTime.now()
                                            : LocalDate.parse(tvDateDevolucion.getText()).atTime(LocalTime.parse(tvTimeDevolucion.getText()));

                                    btnAvisoInventario.setText(String.format("%s", LocalDateTime.now().until(fechaDevolucion, ChronoUnit.DAYS)));
                                    btnAvisoInventario.setBackgroundColor(context.getColor(R.color.warning_color));

                                    new AlertDialog.Builder(context)
                                            .setTitle("Prestar Inventario")
                                            .setMessage(
                                                    String.format(
                                                            "¿Estas seguro de prestar este %s %s a %s %s?",
                                                            etNombre.getText().toString(),
                                                            etDescripcion.getText().toString(),
                                                            prestatario.getId_usuario().getNombre(),
                                                            prestatario.getId_usuario().getApellido()
                                                    )
                                            )
                                            .setIcon(R.drawable.ic_handshake_24_first_color)
                                            .setCancelable(true)
                                            .setPositiveButton("Si", (dialog, which) -> {
                                                // Acción al hacer clic en el botón "Sí"
                                                // Guardar la imagen en la galería
                                                Toast.makeText(context, R.string.prestamo_realizado, Toast.LENGTH_SHORT).show();
                                                ivFoto.setDrawingCacheEnabled(true);
                                                guardarImagenGaleria(ivFoto.getDrawingCache(), inventario.getFoto());
                                                ivFoto.setDrawingCacheEnabled(false);
                                                inventario.setUbicacion(EUbicacion.PRESTADO);
                                                inventario.setNombre(etNombre.getText().toString());
                                                inventario.setDescripcion(etDescripcion.getText().toString());

                                                DaoHelperInventario.actualizar(inventario, context);

                                                Prestamo prestamo = new Prestamo(
                                                        DaoHelperPrestamo.proximoId(context),
                                                        LocalDateTime.now(),
                                                        fechaDevolucion,
                                                        EEstadoPrestamo.ACEPTADO,
                                                        true,
                                                        inventario,
                                                        prestatario
                                                );

                                                if (!prestamos.isEmpty()) {
                                                    prestamos.get(prestamos.size() - 1).setFechaSolicitud(LocalDateTime.now());
                                                    prestamos.get(prestamos.size() - 1).setFechaDevolucion(fechaDevolucion);
                                                    prestamos.get(prestamos.size() - 1).setEstadoPrestamo(EEstadoPrestamo.ACEPTADO);
                                                    prestamos.get(prestamos.size() - 1).setEstado(true);
                                                    prestamos.get(prestamos.size() - 1).setId_inventario(inventario);
                                                    prestamos.get(prestamos.size() - 1).setId_prestatario(prestatario);
                                                    prestamo.setId(prestamos.get(prestamos.size() - 1).getId());
                                                    DaoHelperPrestamo.actualizar(prestamo, context);
                                                } else {
                                                    DaoHelperPrestamo.insertar(prestamo, context);
                                                }

                                                prestatarios[0].set(positionlv1, prestatario);
                                                prestatarioDetailListSmallAdapter.notifyDataSetChanged();
                                                lista.set(position, inventario);
                                                InventarioAdapter.this.notifyItemChanged(position);

                                                // Actualizar el estado del botón aviso inventario
                                                etBuscarNombreUsuario.setEnabled(true);
                                                ibtnBuscar.setEnabled(true);
                                                tvDateDevolucion.setEnabled(true);
                                                tvTimeDevolucion.setEnabled(true);
                                                btnCancelar1.setVisibility(View.GONE);
                                                btnAceptar1.setVisibility(View.GONE);
                                                lvPrestatario.setEnabled(true);

                                                etId.setText(inventario.getId().toString());
                                                etNombre.setText(inventario.getNombre());
                                                etDescripcion.setText(inventario.getDescripcion());
                                                spUbicacion.setSelection(inventario.getUbicacion().ordinal());
                                                /*ivFoto.setImageResource(R.drawable.img_item_default);*/
                                                cargarImagen(ivFoto, inventario.getFoto());

                                                tvTitulo.setText(R.string.prestar_inventario);
                                                etBuscarNombreUsuario.setText(prestatario.getId_usuario().getNombreUsuario());
                                                etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);
                                                btnCancelar1.setText(R.string.cancelar);
                                                btnAceptar1.setText(R.string.prestar_inventario);

                                                //prestamos = DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(inventario.getId(), context);
                                                prestamos.clear();
                                                prestamos.addAll(DaoHelperPrestamo.obtenerTodosPorIdInventarioActivo(Long.parseLong(etId.getText().toString()), context));
                                                btnAvisoInventario.setText(inventario.getEstado()
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
                                                btnAvisoInventario.setBackgroundColor(inventario.getEstado()
                                                        ?
                                                        (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                                                                ?
                                                                context.getColor(R.color.success_color)
                                                                :
                                                                (
                                                                        (prestamos.isEmpty()
                                                                                ?
                                                                                LocalDateTime.now().until(prestamo.getFechaDevolucion(), ChronoUnit.DAYS)
                                                                                :
                                                                                LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
                                                                        ) >= 0
                                                                                ?
                                                                                context.getColor(R.color.warning_color)
                                                                                :
                                                                                context.getColor(R.color.danger_color)
                                                                )
                                                        )
                                                        :
                                                        context.getColor(R.color.gray_dark_color)
                                                );
                                            })
                                            .setNegativeButton("No", (dialog, which) -> {
                                                // Acción al hacer clic en el botón "No"
                                                Toast.makeText(context, "Prestamo cancelado.", Toast.LENGTH_SHORT).show();
                                            })
                                            .setNeutralButton("Salir", (dialog, which) -> {
                                                // Acción al hacer clic en el botón "Cancelar"
                                                Toast.makeText(context, "Salir prestamo.", Toast.LENGTH_SHORT).show();
                                                editDialog.dismiss();
                                            })
                                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    lvPrestatario.setEnabled(true);
                                                }
                                            })
                                            .show();

                                });
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                editDialog.setCanceledOnTouchOutside(false);
                editDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        holder.ivEdit.setEnabled(true);
                    }
                });
                // 6. Muestra el AlertDialog
                editDialog.show();
            }
        });
        //holder.ivFoto.setImageResource(R.drawable.img_item_default);
        cargarImagen(holder.ivFoto, inventario.getFoto());
        holder.tvUbicacion.setText(inventario.getUbicacion().toString());
        holder.tvId.setText(inventario.getId().toString());

        if (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)) {
            holder.tvComentario.setText(
                    String.format(
                            "%s %s id %s %s.",
                            inventario.getNombre(),
                            inventario.getDescripcion(),
                            inventario.getId(),
                            inventario.getUbicacion()
                    )
            );
        } else {
            holder.tvComentario.setText(
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

        holder.btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Devolver"
                holder.btnDevolver.setEnabled(false);
                holder.pbDevolverPrestar.setVisibility(View.VISIBLE);

                Toast.makeText(context, R.string.devolver, Toast.LENGTH_SHORT).show();
                LendInventarioBottomSheetDialogFragment lendDialogFragment = new LendInventarioBottomSheetDialogFragment(InventarioAdapter.this, lista, position);
                lendDialogFragment.show(fragmentManager, "lend_inventario_dialog");

                // Configurar el listener
                lendDialogFragment.setOnDismissListener(new LendInventarioBottomSheetDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss(int position) {
                        // Actualizar la vista del ViewHolder cuando se cierra el diálogo
                        holder.btnPrestar.setEnabled(true);
                        holder.pbDevolverPrestar.setVisibility(View.GONE);
                        InventarioAdapter.this.onDismiss(position);
                    }
                });
            }
        });
        holder.btnPrestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Prestar"
                holder.btnPrestar.setEnabled(false);
                holder.pbDevolverPrestar.setVisibility(View.VISIBLE);

                Toast.makeText(context, "Prestar", Toast.LENGTH_SHORT).show();
                LendInventarioBottomSheetDialogFragment lendDialogFragment = new LendInventarioBottomSheetDialogFragment(InventarioAdapter.this, lista, position);
                lendDialogFragment.show(fragmentManager, "lend_inventario_dialog");

                // Configurar el listener
                lendDialogFragment.setOnDismissListener(new LendInventarioBottomSheetDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss(int position) {
                        // Actualizar la vista del ViewHolder cuando se cierra el diálogo
                        holder.btnPrestar.setEnabled(true);
                        holder.pbDevolverPrestar.setVisibility(View.GONE);
                        InventarioAdapter.this.onDismiss(position);
                    }
                });
            }
        });

        if (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)) {
            holder.btnDevolver.setVisibility(View.GONE);
            holder.btnPrestar.setVisibility(View.VISIBLE);
        } else {
            holder.btnDevolver.setVisibility(View.VISIBLE);
            holder.btnPrestar.setVisibility(View.GONE);
        }
        // frame layout
        if (inventario.getEstado()) {
            holder.ivFrame.setVisibility(View.GONE);
            holder.tvFrame.setVisibility(View.GONE);
            holder.ibFrameDelete.setVisibility(View.GONE);
            holder.ibFrameRestore.setVisibility(View.GONE);
            // habilitar acciones
            holder.ivDelete.setEnabled(true);
            holder.ivEdit.setEnabled(true);
            holder.btnDevolver.setEnabled(true);
            holder.btnPrestar.setEnabled(true);
        } else {
            holder.ivFrame.setVisibility(View.VISIBLE);
            holder.tvFrame.setVisibility(View.VISIBLE);
            holder.ibFrameDelete.setVisibility(View.VISIBLE);
            holder.ibFrameRestore.setVisibility(View.VISIBLE);
            // deshabilitar acciones
            holder.ivDelete.setEnabled(false);
            holder.ivEdit.setEnabled(false);
            holder.btnDevolver.setEnabled(false);
            holder.btnPrestar.setEnabled(false);
        }
        holder.ibFrameDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Eliminar"
                // TODO: Eliminar el inventario de SQLite
                Long retrazo = 0L;
                String nombreUsuario = "";
                Integer totalPrestamos = DaoHelperPrestamo.obtenerTodosPorIdInventario(inventario.getId(), context).size();
                if (!prestamos.isEmpty()) {
                    retrazo = LocalDateTime.now().until(prestamos.get(prestamos.size() - 1).getFechaDevolucion(), ChronoUnit.DAYS);
                    nombreUsuario = prestamos.get(prestamos.size() - 1).getId_prestatario().getId_usuario().getNombreUsuario();
                }
                new AlertDialog.Builder(context)
                        .setTitle(R.string.eliminar_inventario_permanentemente)
                        .setIcon(R.drawable.ic_delete_forever_24_danger_color)
                        .setMessage(
                                inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                                        ?
                                        String.format(
                                                "¿Estas seguro de eliminar %s %s permanentemente?. Es seguro eliminarlo, solo perdera historial de %s prestamos realizados.",
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
                                if (DaoHelperInventario.eliminarCascadaPrestamo(inventario.getId(), context)) {
                                    eliminarImagen(inventario.getFoto());
                                    lista.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, lista.size());
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                    Toast.makeText(context, R.string.inventario_y_sus_prestamos_eliminado_permanentemente, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, R.string.error_al_eliminar_el_inventario_y_sus_prestamos, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (DaoHelperInventario.eliminar(inventario.getId(), context)) {
                                    eliminarImagen(inventario.getFoto());
                                    lista.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, lista.size());
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, R.string.error_al_eliminar_el_inventario, Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (DaoHelperInventario.obtenerTodos(context).isEmpty()) {
                                DaoHelperInventario.reiniciarAutoincremento(context);
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(context, R.string.eliminacion_cancelada, Toast.LENGTH_SHORT).show();
                        })
                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(context, R.string.salir_elimicaci_n, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        holder.ibFrameRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Restaurar"
                new AlertDialog.Builder(context)
                        .setTitle(R.string.restaurar_inventario)
                        .setIcon(R.drawable.ic_restore_from_trash_24_success_color)
                        .setMessage("¿Estas seguro de restaurar este inventario?")
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            if (DaoHelperInventario.insertarLogico(inventario.getId(), context)) {
                                inventario.setEstado(true);
                                lista.set(position, inventario);
                                notifyItemChanged(position);
                                Toast.makeText(context, R.string.inventario_restaurado, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.error_al_restaurar_el_inventario, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(context, R.string.restauraci_n_cancelada, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(context, R.string.salir_restauraci_n, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    private void showImageSourceDialog(ImageView ivFoto, int position) {
        if (listener != null) {
            listener.onImageClick(ivFoto, position);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seleccionar opción");
        builder.setItems(new CharSequence[]{"Cámara", "Galería"}, (dialog, which) -> {
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
        builder.setNegativeButton("Cancelar", null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ivFoto.setEnabled(true);
            }
        });
        builder.create();
        builder.show();
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

    private void guardarImagenGaleria(Bitmap imageBitmap, String foto) {
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

    private void eliminarImagen(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                Toast.makeText(context, R.string.imagen_eliminada, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.error_al_eliminar_la_imagen, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnRetraso;
        public TextView tvNombre;
        public TextView tvDescripcion;
        public ImageView ivDelete;
        public ImageView ivEdit;
        public ImageView ivFoto;
        public TextView tvUbicacion;
        public TextView tvId;
        public TextView tvComentario;
        public ProgressBar pbDevolverPrestar;
        public Button btnDevolver;
        public Button btnPrestar;
        // frame layout
        public ImageView ivFrame;
        public TextView tvFrame;
        public ImageButton ibFrameDelete;
        public ImageButton ibFrameRestore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRetraso = itemView.findViewById(R.id.btn_aviso_card_btn_on);
            tvNombre = itemView.findViewById(R.id.tv_titulo1_card_btn_on);
            tvDescripcion = itemView.findViewById(R.id.tv_subtitulo1_card_btn_on);
            ivDelete = itemView.findViewById(R.id.iv_delete_card_btn_on);
            ivEdit = itemView.findViewById(R.id.iv_edit_card_btn_on);
            ivFoto = itemView.findViewById(R.id.iv_foto_card_btn_on);
            tvUbicacion = itemView.findViewById(R.id.tv_titulo2_card_btn_on);
            tvId = itemView.findViewById(R.id.tv_subtitulo2_card_btn_on);
            tvComentario = itemView.findViewById(R.id.tv_comentario_card_btn_on);
            pbDevolverPrestar = itemView.findViewById(R.id.pb_devolver_prestar_card_btn_on);
            btnDevolver = itemView.findViewById(R.id.btn_devolver_card_btn_on);
            btnPrestar = itemView.findViewById(R.id.btn_prestar_card_btn_on);
            // frame layout
            ivFrame = itemView.findViewById(R.id.iv_frame_layout_inventario);
            tvFrame = itemView.findViewById(R.id.tv_frame_layout_inventario);
            ibFrameDelete = itemView.findViewById(R.id.ib_frame_layout_delete_inventario);
            ibFrameRestore = itemView.findViewById(R.id.ib_frame_layout_restore_inventario);
        }
    }
}
