package com.orellano.panolhogareno.ui.dialogo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.InventarioAdapter;
import com.orellano.panolhogareno.adaptador.PrestatarioDetailListSmallAdapter;
import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.daoSQLite.DaoHelperInventario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestatario;
import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.Prestatario;
import com.orellano.panolhogareno.entidad.enumEntidad.EEstadoPrestamo;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;
import com.orellano.panolhogareno.utilidad.Constantes;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LendInventarioBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private InventarioAdapter inventarioAdapter;
    private List<Inventario> listaInventario;
    private Integer positionInventario;

    ImageView ivIcono;
    TextView tvTitulo;
    Button btnAvisoInventario;
    TextView tvNombreInventario;
    TextView tvDescripcionInventario;
    ImageView ivInventario;
    TextView tvLabelNombreUsuario;
    ImageButton ibtnBuscar;
    EditText etBuscarNombreUsuario;
    TextView tvLabelDateDevolucion;
    TextView tvDateDevolucion;
    TextView tvLabelHoraDevolucion;
    TextView tvTimeDevolucion;
    Button btnCancelar1;
    Button btnAceptar1;
    ListView lvPrestatario;

    private File appDir; // Directorio de la app

    // Interfaz de escucha
    public interface OnDismissListener {
        void onDismiss(int position); // Cambiado para pasar la posición
    }

    // Listener
    private OnDismissListener listener;

    // Método para configurar el listener
    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // Notificar al listener cuando se cierra el diálogo
        if (listener != null) {
            listener.onDismiss(positionInventario); // Pasa la posición
        }
    }

    public LendInventarioBottomSheetDialogFragment(InventarioAdapter inventarioAdapter, List<Inventario> lista, int position) {
        // Constructor vacío requerido por BottomSheetDialogFragment
        this.inventarioAdapter = inventarioAdapter;
        this.listaInventario = lista;
        this.positionInventario = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lendDialogView = inflater.inflate(R.layout.card_sheet_dialog_lend_inventario, container, false);
        // Referecniar las vistas
        initAppDir();
        ivIcono = lendDialogView.findViewById(R.id.iv_icono_card_sheet_dialog_lend_inventario);
        tvTitulo = lendDialogView.findViewById(R.id.tv_titulo_card_sheet_dialog_lend_inventario);
        btnAvisoInventario = lendDialogView.findViewById(R.id.btn_aviso_sheet_dialog_lend_inventario);
        tvNombreInventario = lendDialogView.findViewById(R.id.tv_nombre_iventario_sheet_dialog_lend_inventario);
        tvDescripcionInventario = lendDialogView.findViewById(R.id.tv_descripcion_inventario_sheet_dialog_lend_inventario);
        ivInventario = lendDialogView.findViewById(R.id.iv_inventario_sheet_dialog_lend_inventario);
        tvLabelNombreUsuario = lendDialogView.findViewById(R.id.tv_label_nombre_usuario_sheet_dialog_lend_inventario);
        ibtnBuscar = lendDialogView.findViewById(R.id.ibtn_buscar_sheet_dialog_lend_inventario);
        etBuscarNombreUsuario = lendDialogView.findViewById(R.id.et_nombre_usuario_sheet_dialog_lend_inventario);
        tvLabelDateDevolucion = lendDialogView.findViewById(R.id.tv_label_date_devolucion_sheet_dialog_lend_inventario);
        tvDateDevolucion = lendDialogView.findViewById(R.id.tv_date_devolucion_sheet_dialog_lend_inventario);
        tvLabelHoraDevolucion = lendDialogView.findViewById(R.id.tv_label_hora_devolucion_sheet_dialog_lend_inventario);
        tvTimeDevolucion = lendDialogView.findViewById(R.id.tv_time_devolucion_sheet_dialog_lend_inventario);
        btnCancelar1 = lendDialogView.findViewById(R.id.btn_cancelar1_card_sheet_dialog_lend_inventario);
        btnAceptar1 = lendDialogView.findViewById(R.id.id_btn_aceptar1_card_sheet_dialog_lend_inventario);
        lvPrestatario = lendDialogView.findViewById(R.id.lv_prestatario_sheet_dialog_lend_inventario);
        // Establecer valores iniciales en las vistas
        Inventario inventario = listaInventario.get(positionInventario);
        final List<Prestamo>[] prestamos = new List[]{DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(inventario.getId(), getContext())};
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
                                (prestamos[0].isEmpty()
                                        ?
                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                        :
                                        LocalDateTime.now().until(prestamos[0].get(prestamos[0].size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
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
                        getContext().getColor(R.color.success_color)
                        :
                        (
                                (prestamos[0].isEmpty()
                                        ?
                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                        :
                                        LocalDateTime.now().until(prestamos[0].get(prestamos[0].size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
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
        tvNombreInventario.setText(inventario.getNombre());
        tvDescripcionInventario.setText(inventario.getDescripcion());
        ivInventario.setImageResource(R.drawable.img_item_default);
        cargarImagen(ivInventario, inventario.getFoto());
        final ArrayList<Prestatario>[] prestatarios = new ArrayList[]{(ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodos(getContext())};
        List<Prestatario> prestatarioList = prestatarios[0];
        if (inventario.getUbicacion().equals(EUbicacion.PRESTADO)) {
            ibtnBuscar.setEnabled(false);
            etBuscarNombreUsuario.setEnabled(false);
            tvDateDevolucion.setEnabled(false);
            tvTimeDevolucion.setEnabled(false);
            btnCancelar1.setVisibility(View.VISIBLE);
            btnAceptar1.setVisibility(View.VISIBLE);
            lvPrestatario.setEnabled(false);

            etBuscarNombreUsuario.setText(prestamos[0].get(prestamos[0].size() - 1)
                    .getId_prestatario()
                    .getId_usuario()
                    .getNombreUsuario());
            etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);
            tvDateDevolucion.setText(
                    prestamos[0].get(prestamos[0].size() - 1).getFechaDevolucion()
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE)));
            tvTimeDevolucion.setText(
                    prestamos[0].get(prestamos[0].size() - 1).getFechaDevolucion()
                            .toLocalTime()
                            .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME)));
            btnCancelar1.setText(R.string.cancelar);
            btnAceptar1.setText(R.string.devolver);

            prestatarioList.clear();
            prestatarioList.add(prestamos[0].get(prestamos[0].size() - 1).getId_prestatario());
        } else {
            etBuscarNombreUsuario.setEnabled(true);
            ibtnBuscar.setEnabled(true);
            lvPrestatario.setEnabled(true);
            tvDateDevolucion.setEnabled(true);
            tvTimeDevolucion.setEnabled(true);
            btnCancelar1.setVisibility(View.GONE);
            btnAceptar1.setVisibility(View.GONE);

            tvDateDevolucion.setText(LocalDate.now()
                    .plusDays(Constantes.DIAS_PRESTAMO_DEFAULT)
                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE)));
            tvTimeDevolucion.setText(LocalTime.now()
                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME)));
            etBuscarNombreUsuario.setText("");
            etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);
        }
        PrestatarioDetailListSmallAdapter prestatarioDetailListSmallAdapter = new PrestatarioDetailListSmallAdapter(getContext(), prestatarioList);
        lvPrestatario.setAdapter(prestatarioDetailListSmallAdapter);
        ibtnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prestatarios[0].clear();
                String nombreUsuario = etBuscarNombreUsuario.getText().toString().trim();
                // Acción al hacer clic en el botón "Buscar";
                if (nombreUsuario.isEmpty()) {
                    prestatarios[0].addAll((ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodos(getContext()));
                } else {
                    prestatarios[0].addAll((ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodosPorNombreUsuario(nombreUsuario, getContext()));
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
                            Toast.makeText(getContext(), "La hora no puede ser menor a la hora actual.", Toast.LENGTH_SHORT).show();

                        }
                        tvDateDevolucion.setText(LocalDate
                                .of(year, month + 1, dayOfMonth)
                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE))
                        );
                        Toast.makeText(getContext(), "Fecha seleccionada.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Fecha cancelada.", Toast.LENGTH_SHORT).show();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (
                                now.toLocalDate().isEqual(LocalDate.now())
                                        && LocalTime.of(hourOfDay, minute, 0).isBefore(LocalTime.now())
                        ) {
                            hourOfDay = LocalTime.now().getHour();
                            minute = LocalTime.now().getMinute();
                            Toast.makeText(getContext(), "La hora no puede ser menor a la hora actual.", Toast.LENGTH_SHORT).show();
                        }
                        tvTimeDevolucion.setText(LocalTime
                                .of(hourOfDay, minute)
                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME))
                        );
                        Toast.makeText(getContext(), "Hora seleccionada.", Toast.LENGTH_SHORT).show();
                    }
                }, now.getHour(), now.getMinute(), true);

                timePickerDialog.setTitle("Hora devolucion.");
                timePickerDialog.setMessage("En el mismo dia la hora no puede ser menor a la hora actual y se configura a la actual por defecto.");
                timePickerDialog.setIcon(R.drawable.ic_access_time_24_first_color);
                timePickerDialog.setCanceledOnTouchOutside(false);

                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Hora cancelada.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), R.string.cancelado_devolver, Toast.LENGTH_SHORT).show();
                LendInventarioBottomSheetDialogFragment.this.dismiss();
            }
        });
        btnAceptar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prestamos[0].isEmpty()) {
                    Prestatario prestatario = prestamos[0].get(prestamos[0].size() - 1).getId_prestatario();
                    // Acción al hacer clic en el botón "Aceptar";
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.devolver_inventario)
                            .setMessage(
                                    String.format(
                                            "¿Estas seguro de devolver %s %s por %s %s?",
                                            inventario.getNombre(),
                                            inventario.getDescripcion(),
                                            prestatario.getId_usuario().getNombre(),
                                            prestatario.getId_usuario().getApellido()
                                    )
                            )
                            .setIcon(R.drawable.ic_handshake_24_first_color)
                            .setCancelable(true)
                            .setPositiveButton(R.string.si, (dialog, which) -> {
                                // Acción al hacer clic en el botón "Sí"
                                Toast.makeText(getContext(), R.string.devolucion_realizada, Toast.LENGTH_SHORT).show();
                                inventario.setUbicacion(EUbicacion.DEPOSITO);
                                DaoHelperInventario.actualizar(inventario, getContext());
                                Prestamo prestamo = prestamos[0].get(prestamos[0].size() - 1);
                                prestamo.setEstadoPrestamo(EEstadoPrestamo.CONCLUIDO);
                                DaoHelperPrestamo.actualizar(prestamo, getContext());

                                listaInventario.set(positionInventario, inventario);
                                inventarioAdapter.notifyItemChanged(positionInventario);
                                prestatarioDetailListSmallAdapter.notifyDataSetChanged();

                                LendInventarioBottomSheetDialogFragment.this.dismiss();
                                // Actualizar notificacion
                                if (getActivity() instanceof MainActivity) {
                                    ((MainActivity) getActivity()).updateNotificationBadge();
                                }
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> {
                                // Acción al hacer clic en el botón "No"
                                Toast.makeText(getContext(), R.string.cancelado_devolver, Toast.LENGTH_SHORT).show();
                            })
                            .setNeutralButton(R.string.salir, (dialog, which) -> {
                                // Acción al hacer clic en el botón "Salir"
                                Toast.makeText(getContext(), R.string.salir_devolver, Toast.LENGTH_SHORT).show();
                                LendInventarioBottomSheetDialogFragment.this.dismiss();
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)) {
                                        lvPrestatario.setEnabled(true);
                                    }
                                }
                            })
                            .show();

                    /*inventario.setUbicacion(EUbicacion.DEPOSITO);
                    DaoHelperInventario.actualizar(inventario, getContext());
                    Prestamo prestamo = prestamos[0].get(prestamos[0].size() - 1);
                    prestamo.setEstadoPrestamo(EEstadoPrestamo.CONCLUIDO);
                    DaoHelperPrestamo.actualizar(prestamo, getContext());

                    listaInventario.set(positionInventario, inventario);
                    inventarioAdapter.notifyItemChanged(positionInventario);
                    prestatarioDetailListSmallAdapter.notifyDataSetChanged();

                    LendInventarioBottomSheetDialogFragment.this.dismiss();*/
                }
            }
        });
        lvPrestatario.setOnItemClickListener((parent, view, position, id) -> {
            // Acción al hacer clic en un elemento de la lista
            lvPrestatario.setEnabled(false);
            Prestatario prestatario = prestatarios[0].get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.prestar_inventario)
                    .setMessage(
                            String.format(
                                    "¿Estas seguro de prestar este %s %s a %s %s?",
                                    inventario.getNombre(),
                                    inventario.getDescripcion(),
                                    prestatario.getId_usuario().getNombre(),
                                    prestatario.getId_usuario().getApellido()
                            )
                    )
                    .setIcon(R.drawable.ic_handshake_24_first_color)
                    .setCancelable(true)
                    .setPositiveButton(R.string.si, (dialog, which) -> {
                        // Acción al hacer clic en el botón "Sí"
                        Toast.makeText(getContext(), R.string.prestamo_realizado, Toast.LENGTH_SHORT).show();
                        inventario.setUbicacion(EUbicacion.PRESTADO);
                        /*LocalDateTime fechaDevolucion = LocalDate.parse(etDateDevolucion.getText()).atTime(LocalTime.parse(etTimeDevolucion.getText()));*/
                        LocalDate dateDevolucion = tvDateDevolucion.getText().toString().isEmpty()
                                ? LocalDate.now()
                                : LocalDate.parse(tvDateDevolucion.getText());
                        LocalTime timeDevolucion = tvTimeDevolucion.getText().toString().isEmpty()
                                ? LocalTime.now()
                                : LocalTime.parse(tvTimeDevolucion.getText());
                        LocalDateTime fechaDevolucion = LocalDateTime.of(dateDevolucion, timeDevolucion).isBefore(LocalDateTime.now())
                                ? LocalDateTime.now()
                                : LocalDate.parse(tvDateDevolucion.getText()).atTime(LocalTime.parse(tvTimeDevolucion.getText()));
                        Prestamo prestamo = new Prestamo(
                                DaoHelperPrestamo.proximoId(getContext()),
                                LocalDateTime.now(),
                                fechaDevolucion,
                                EEstadoPrestamo.ACEPTADO,
                                true,
                                inventario,
                                prestatario
                        );
                        DaoHelperPrestamo.insertar(prestamo, getContext());
                        DaoHelperInventario.actualizar(inventario, getContext());

                        prestatarios[0].set(position, prestatario);
                        prestatarioDetailListSmallAdapter.notifyDataSetChanged();
                        listaInventario.set(positionInventario, inventario);
                        inventarioAdapter.notifyItemChanged(positionInventario);

                        // Actualizar el estado del botón aviso inventario
                        if (inventario.getUbicacion().equals(EUbicacion.PRESTADO)) {
                            etBuscarNombreUsuario.setEnabled(false);
                            ibtnBuscar.setEnabled(false);
                            tvDateDevolucion.setEnabled(false);
                            tvTimeDevolucion.setEnabled(false);
                            btnCancelar1.setVisibility(View.VISIBLE);
                            btnAceptar1.setVisibility(View.VISIBLE);
                            lvPrestatario.setEnabled(false);

                            tvTitulo.setText(R.string.devolver_inventario);
                            etBuscarNombreUsuario.setText(prestatario.getId_usuario().getNombreUsuario());
                            etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);
                            btnCancelar1.setText(R.string.cancelar);
                            btnAceptar1.setText(R.string.devolver);
                        } else {
                            etBuscarNombreUsuario.setEnabled(true);
                            ibtnBuscar.setEnabled(true);
                            tvDateDevolucion.setEnabled(true);
                            tvTimeDevolucion.setEnabled(true);
                            btnCancelar1.setVisibility(View.GONE);
                            btnAceptar1.setVisibility(View.GONE);
                            lvPrestatario.setEnabled(true);

                            tvTitulo.setText(R.string.prestar_inventario);
                            etBuscarNombreUsuario.setText("");
                            etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);
                            btnCancelar1.setText(R.string.cancelar);
                            btnAceptar1.setText(R.string.prestar_inventario);
                        }
                        prestamos[0] = DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(inventario.getId(), getContext());
                        btnAvisoInventario.setText(inventario.getEstado()
                                ?
                                (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)
                                        ?
                                        "ok"
                                        :
                                        String.format(
                                                "%s",
                                                //prestamos.get(prestamos.size() - 1).getFechaDevolucion().until(LocalDateTime.now(), ChronoUnit.DAYS)
                                                (prestamos[0].isEmpty()
                                                        ?
                                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                                        :
                                                        LocalDateTime.now().until(prestamos[0].get(prestamos[0].size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
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
                                        getContext().getColor(R.color.success_color)
                                        :
                                        (
                                                (prestamos[0].isEmpty()
                                                        ?
                                                        Constantes.DIAS_PRESTAMO_DEFAULT
                                                        :
                                                        LocalDateTime.now().until(prestamos[0].get(prestamos[0].size() - 1).getFechaDevolucion(), ChronoUnit.DAYS)
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
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Acción al hacer clic en el botón "No"
                        Toast.makeText(getContext(), "Prestamo cancelado.", Toast.LENGTH_SHORT).show();
                    })
                    .setNeutralButton("Salir", (dialog, which) -> {
                        // Acción al hacer clic en el botón "Cancelar"
                        Toast.makeText(getContext(), "Salir prestamo.", Toast.LENGTH_SHORT).show();
                        LendInventarioBottomSheetDialogFragment.this.dismiss();
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (inventario.getUbicacion().equals(EUbicacion.DEPOSITO)) {
                                lvPrestatario.setEnabled(true);
                            }
                        }
                    })
                    .show();

        });

        return lendDialogView;
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

    private void cargarImagen(ImageView ivInventario, String foto) {
        File imagen = new File(foto);

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            ivInventario.setImageBitmap(bitmap);
        } else {
            // Cargar el icono por defecto
            ivInventario.setImageResource(R.drawable.img_item_default);
        }
    }
}

