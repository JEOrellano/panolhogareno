package com.orellano.panolhogareno.ui.dialogo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.InventarioDetailListSmallAdapter;
import com.orellano.panolhogareno.adaptador.PrestatarioAdapter;
import com.orellano.panolhogareno.adaptador.enumAdaptador.EAccion;
import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.daoSQLite.DaoHelperInventario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
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
import java.util.List;

public class LendPrestatarioBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private PrestatarioAdapter prestatarioAdapter;
    private List<Prestatario> listaPrestatario;
    private Integer positionPrestatario;
    private EAccion accion;
    private Inventario inventario;
    private Prestamo prestamo;

    ImageView ivIcono;
    TextView tvTitulo;
    Button btnAvisoPrestatario;
    TextView tvNombrePrestatario;
    TextView tvApellidoPrestatario;
    ImageView ivPrestatario;
    TextView tvLabelNombre;
    ImageButton ibtnBuscar;
    EditText etBuscarNombre;
    TextView tvLabelDateDevolucion;
    TextView tvDateDevolucion;
    TextView tvLabelHoraDevolucion;
    TextView tvTimeDevolucion;
    Button btnCancelar1;
    Button btnAceptar1;
    ListView lvInventario;

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
            listener.onDismiss(positionPrestatario); // Pasa la posición
        }
    }

    public LendPrestatarioBottomSheetDialogFragment(PrestatarioAdapter prestatarioAdapter, List<Prestatario> lista, int position, EAccion accion) {
        // Constructor vacío requerido por BottomSheetDialogFragment
        this.prestatarioAdapter = prestatarioAdapter;
        this.listaPrestatario = lista;
        this.positionPrestatario = position;
        this.accion = accion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lendDialogView = inflater.inflate(R.layout.card_sheet_dialog_lend_prestatario, container, false);
        // Referecniar las vistas
        ivIcono = lendDialogView.findViewById(R.id.iv_icono_card_sheet_dialog_lend_prestatario);
        tvTitulo = lendDialogView.findViewById(R.id.tv_titulo_card_sheet_dialog_lend_prestatario);
        btnAvisoPrestatario = lendDialogView.findViewById(R.id.btn_aviso_sheet_dialog_lend_prestatario);
        tvNombrePrestatario = lendDialogView.findViewById(R.id.tv_nombre_prestatario_sheet_dialog_lend_prestatario);
        tvApellidoPrestatario = lendDialogView.findViewById(R.id.tv_apellido_prestatario_sheet_dialog_lend_prestatario);
        ivPrestatario = lendDialogView.findViewById(R.id.iv_prestatario_sheet_dialog_lend_prestatario);
        tvLabelNombre = lendDialogView.findViewById(R.id.tv_label_nombre_sheet_dialog_lend_prestatario);
        ibtnBuscar = lendDialogView.findViewById(R.id.ibtn_buscar_sheet_dialog_lend_prestatario);
        etBuscarNombre = lendDialogView.findViewById(R.id.et_nombre_sheet_dialog_lend_prestatario);
        tvLabelDateDevolucion = lendDialogView.findViewById(R.id.tv_label_date_devolucion_sheet_dialog_lend_prestatario);
        tvDateDevolucion = lendDialogView.findViewById(R.id.tv_date_devolucion_sheet_dialog_lend_prestatario);
        tvLabelHoraDevolucion = lendDialogView.findViewById(R.id.tv_label_hora_devolucion_sheet_dialog_lend_prestatario);
        tvTimeDevolucion = lendDialogView.findViewById(R.id.tv_time_devolucion_sheet_dialog_lend_prestatario);
        btnCancelar1 = lendDialogView.findViewById(R.id.btn_cancelar1_card_sheet_dialog_lend_prestatario);
        btnAceptar1 = lendDialogView.findViewById(R.id.id_btn_aceptar1_card_sheet_dialog_lend_prestatario);
        lvInventario = lendDialogView.findViewById(R.id.lv_inventario_sheet_dialog_lend_prestatario);
        // Establecer valores iniciales en las vistas
        Prestatario prestatario = listaPrestatario.get(positionPrestatario);
        List<Prestamo> prestamos = DaoHelperPrestamo.obtenerTodosPorIdPrestatarioAceptado(prestatario.getId(), getContext());
        inventario = null;
        prestamo = null;
        ivIcono.setImageResource(R.drawable.ic_handshake_24_first_color);
        btnAvisoPrestatario.setEnabled(false);
        tvTitulo.setText(accion.equals(EAccion.PRESTAR)
                ?
                R.string.prestar_inventario
                :
                R.string.devolver_inventario
        );
        btnAvisoPrestatario.setText(prestatario.getEstado()
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
        btnAvisoPrestatario.setBackgroundColor(prestatario.getEstado()
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
        tvNombrePrestatario.setText(prestatario.getId_usuario().getNombre());
        tvApellidoPrestatario.setText(prestatario.getId_usuario().getApellido());
        cargarImagen(ivPrestatario, prestatario.getId_usuario().getFoto());
        List<Inventario> inventarioList = DaoHelperInventario.obtenerTodosPorUbicacion(EUbicacion.DEPOSITO, getContext());
        if (accion.equals(EAccion.DEVOLVER)) {
            ibtnBuscar.setEnabled(true);
            etBuscarNombre.setEnabled(true);
            tvDateDevolucion.setEnabled(false);
            tvTimeDevolucion.setEnabled(false);
            btnCancelar1.setVisibility(View.VISIBLE);
            btnAceptar1.setVisibility(View.VISIBLE);
            btnAceptar1.setText(R.string.devolver);

            inventarioList.clear();
            prestamos.forEach(prestamo -> {
                inventarioList.add(prestamo.getId_inventario());
            });
        } else {
            ibtnBuscar.setEnabled(true);
            etBuscarNombre.setEnabled(true);
            tvDateDevolucion.setEnabled(true);
            tvTimeDevolucion.setEnabled(true);
            btnCancelar1.setVisibility(View.GONE);
            btnAceptar1.setVisibility(View.GONE);

            tvDateDevolucion.setText(LocalDate.now()
                    .plusDays(Constantes.DIAS_PRESTAMO_DEFAULT)
                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE)));
            tvTimeDevolucion.setText(LocalTime.now()
                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME)));
            etBuscarNombre.setText("");
            etBuscarNombre.setHint(R.string.ingrese_nombre);
        }
        InventarioDetailListSmallAdapter inventarioDetailListSmallAdapter = new InventarioDetailListSmallAdapter(getContext(), inventarioList);
        lvInventario.setAdapter(inventarioDetailListSmallAdapter);
        ibtnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Buscar";
                inventarioList.clear();
                String nombre = etBuscarNombre.getText().toString().trim();
                switch (accion) {
                    case PRESTAR:
                        if (nombre.isEmpty()) {
                            inventarioList.addAll(DaoHelperInventario.obtenerTodosPorUbicacion(EUbicacion.DEPOSITO, getContext()));
                        } else {
                            inventarioList.addAll(DaoHelperInventario.obtenerTodosPorNombreUbicacion(nombre, EUbicacion.DEPOSITO, getContext()));
                        }
                        break;
                    case DEVOLVER:
                        if (nombre.isEmpty()) {
                            DaoHelperPrestamo
                                    .obtenerTodosPorIdPrestatarioAceptado(prestatario.getId(), getContext())
                                    .forEach(prestamo -> {
                                        inventarioList.add(prestamo.getId_inventario());
                                    });
                        } else {
                            DaoHelperPrestamo
                                    .obtenerTodosPorIdPrestatarioAceptadoNombreInventario(prestatario.getId(), nombre, getContext())
                                    .forEach(prestamo -> {
                                        inventarioList.add(prestamo.getId_inventario());
                                    });
                        }
                        inventario = null;
                        prestamo = null;
                        etBuscarNombre.setText("");
                        break;
                    default:
                        break;
                }
                inventarioDetailListSmallAdapter.notifyDataSetChanged();
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
                LendPrestatarioBottomSheetDialogFragment.this.dismiss();
            }
        });
        // TODO: Configurar el botón "btnAceptar1"
        btnAceptar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Aceptar";
                if (inventario != null) {
                    btnAceptar1.setEnabled(false);
                    // TODO: Realizar devolver inventario
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.devolver_inventario)
                            .setMessage(
                                    String.format(
                                            "¿Estas seguro de devolver este %s %s por %s %s?",
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
                                inventarioList.remove(inventario);
                                prestamos.remove(prestamo);

                                inventario.setUbicacion(EUbicacion.DEPOSITO);
                                DaoHelperInventario.actualizar(inventario, getContext());
                                prestamo.setEstadoPrestamo(EEstadoPrestamo.CONCLUIDO);
                                DaoHelperPrestamo.actualizar(prestamo, getContext());

                                inventarioDetailListSmallAdapter.notifyDataSetChanged();
                                listaPrestatario.set(positionPrestatario, prestatario);
                                prestatarioAdapter.notifyItemChanged(positionPrestatario);
                                // Actualizar el estado del bóton aviso prestatario
                                btnAvisoPrestatario.setText(prestatario.getEstado()
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
                                btnAvisoPrestatario.setBackgroundColor(prestatario.getEstado()
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
                                tvDateDevolucion.setText("");
                                tvTimeDevolucion.setText("");
                                inventario = null;
                                prestamo = null;
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
                                LendPrestatarioBottomSheetDialogFragment.this.dismiss();
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    btnAceptar1.setEnabled(true);
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(getContext(), R.string.no_selecciono_inventario, Toast.LENGTH_SHORT).show();
                    etBuscarNombre.setError(getString(R.string.debe_seleccionar_un_inventario));
                }
            }
        });
        lvInventario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Acción al hacer clic en un elemento de la lista;
                if (accion.equals(EAccion.DEVOLVER)) {
                    //inventarioList.remove(position);
                    etBuscarNombre.setError(null);
                    inventario = inventarioList.get(position);
                    prestamo = null;
                    for (Prestamo p : prestamos) {
                        if (p.getId_inventario().getId().equals(inventario.getId())) {
                            prestamo = p;
                            break;
                        }
                    }
                    tvDateDevolucion.setText(prestamo == null
                            ?
                            LocalDate.now()
                                    .plusDays(Constantes.DIAS_PRESTAMO_DEFAULT)
                                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE))
                            :
                            prestamo.getFechaDevolucion()
                                    .toLocalDate()
                                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE))
                    );
                    tvTimeDevolucion.setText(prestamo == null
                            ?
                            LocalTime.now()
                                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME))
                            :
                            prestamo.getFechaDevolucion()
                                    .toLocalTime()
                                    .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME))
                    );
                    etBuscarNombre.setText(prestamo == null
                            ?
                            ""
                            :
                            prestamo.getId_inventario().getNombre()
                    );
                    etBuscarNombre.setHint(R.string.ingrese_nombre);
                } else {
                    lvInventario.setEnabled(false);
                    inventario = inventarioList.get(position);
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

                                inventarioList.remove(position);
                                inventarioDetailListSmallAdapter.notifyDataSetChanged();
                                prestamos.add(prestamo);
                                listaPrestatario.set(positionPrestatario, prestatario);
                                prestatarioAdapter.notifyItemChanged(positionPrestatario);

                                // Actualizar el estado del bóton aviso prestatario
                                btnAvisoPrestatario.setText(prestatario.getEstado()
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
                                btnAvisoPrestatario.setBackgroundColor(prestatario.getEstado()
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
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                // Acción al hacer clic en el botón "No"
                                Toast.makeText(getContext(), "Prestamo cancelado.", Toast.LENGTH_SHORT).show();
                            })
                            .setNeutralButton("Salir", (dialog, which) -> {
                                // Acción al hacer clic en el botón "Cancelar"
                                Toast.makeText(getContext(), "Salir prestamo.", Toast.LENGTH_SHORT).show();
                                LendPrestatarioBottomSheetDialogFragment.this.dismiss();
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    lvInventario.setEnabled(true);
                                }
                            })
                            .show();
                }
            }
        });
        return lendDialogView;
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
