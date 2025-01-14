package com.orellano.panolhogareno.ui.dialogo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.EUbicacionSpinnerAdapter;
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

public class AddInventarioBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private List<Inventario> lista;
    private InventarioAdapter inventarioAdapter;
    private Fragment fragment;

    private ImageView ivIcono;
    private TextView tvTitulo;
    private TextView tvId;
    private EditText etId;
    private TextView tvNombre;
    private EditText etNombre;
    private TextView tvDescripcion;
    private EditText etDescripcion;
    private TextView tvUbicacion;
    private Spinner spUbicacion;
    private ImageView ivFoto;
    private CardView llIP;
    private Button btnCancelar;
    private Button btnAgregar;

    private long itemId = 0L; // Reemplaza con tu ID de la base de datos
    private File appDir; // Directorio de la app
    private String currentImagePath; // Ruta de la imagen actual

    // Interfaz de escucha
    public interface OnDismissListener {
        void onDismiss();// Cambiado para pasar la posición
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
                    // Guardar la imagen en la galería
                    //guardarImagenGaleria(imageBitmap);
                    /*ivFoto.setDrawingCacheEnabled(true);
                    guardarImagenGaleria(ivFoto.getDrawingCache());
                    ivFoto.setDrawingCacheEnabled(false);*/
                    break;
                case Constantes.GALERIA_REQUEST_CODE:
                    // La imagen fue seleccionada de la galería
                    ivFoto.setImageURI(data.getData());
                    // Obtener la ruta de la imagen seleccionada
                    // currentImagePath = getPathFromUri(data.getData());
                    // Guardar la ruta en la base de datos
                    //guardarRutaEnBaseDeDatos();
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Error al obtener la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    public AddInventarioBottomSheetDialogFragment(List<Inventario> lista, InventarioAdapter inventarioAdapter, Fragment fragment) {
        this.lista = lista;
        this.inventarioAdapter = inventarioAdapter;
        this.fragment = fragment;
    }

    // ... (Declara tus vistas aquí) ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.card_dialog_add_inventario, container, false);

        // ... (Obtén referencias a tus vistas, establece valores iniciales y configura listeners) ...
        initAppDir();
        ivIcono = dialogView.findViewById(R.id.iv_icono_card_dialog_add_inventario);
        tvTitulo = dialogView.findViewById(R.id.tv_titulo_card_dialog_add_inventario);
        tvId = dialogView.findViewById(R.id.tv_id_card_dialog_add_inventario);
        etId = dialogView.findViewById(R.id.et_id_card_dialog_add_inventario);
        tvNombre = dialogView.findViewById(R.id.tv_nombre_card_dialog_add_inventario);
        etNombre = dialogView.findViewById(R.id.et_nombre_card_dialog_add_inventario);
        tvDescripcion = dialogView.findViewById(R.id.tv_descripcion_card_dialog_add_inventario);
        etDescripcion = dialogView.findViewById(R.id.et_descripcion_card_dialog_add_inventario);
        tvUbicacion = dialogView.findViewById(R.id.tv_ubicacion_card_dialog_add_inventario);
        spUbicacion = dialogView.findViewById(R.id.sp_ubicacion_card_dialog_add_inventario);
        ivFoto = dialogView.findViewById(R.id.iv_foto_card_dialog_add_inventario);
        llIP = dialogView.findViewById(R.id.ll_card_dialog_add_inventario_sheet_dialog_lend_inventario);
        btnCancelar = dialogView.findViewById(R.id.btn_cancelar_card_dialog_add_inventario);
        btnAgregar = dialogView.findViewById(R.id.id_btn_editar_card_dialog_add_inventario);
        // 3. Establece valores iniciales en las vistas
        ivIcono.setImageResource(R.drawable.ic_handyman_24_first_color);
        tvTitulo.setText(R.string.agregar_inventario);
        tvId.setText(R.string.id);
        etId.setText(DaoHelperInventario.proximoId(getContext()).toString());
        itemId = DaoHelperInventario.proximoId(getContext());
        tvNombre.setText(R.string.nombre);
        //etNombre.setText(inventario.getNombre());
        etNombre.setHint(R.string.ingrese_el_nombre);
        tvDescripcion.setText(R.string.descripcion);
        //etDescripcion.setText(inventario.getDescripcion());
        etDescripcion.setHint(R.string.ingrese_la_descripcion);
        tvUbicacion.setText(R.string.ubicacion);
        EUbicacionSpinnerAdapter adapter = new EUbicacionSpinnerAdapter(getContext(), List.of(EUbicacion.values()));
        spUbicacion.setAdapter(adapter);
        //spUbicacion.setSelection(inventario.getUbicacion().ordinal());
        spUbicacion.setEnabled(true);
        ivFoto.setImageResource(R.drawable.img_item_default);
        llIP.setVisibility(View.GONE);
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
                // Restablecer imagen inventario
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restablecer iamgen inventario
                        //eliminarInventarioYImagen(itemId);
                        cargarImagen();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.setTitle("Quitar Imagen Inventario");
                builder.setMessage("¿Estás seguro de que deseas quitar esta imagen inventario?");
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
                btnAgregar.setEnabled(false);
                // TODO: Agregar el inventario de SQLite
                Inventario inventarioAgregado = new Inventario(
                        Long.parseLong(etId.getText().toString()),
                        etNombre.getText().toString(),
                        etDescripcion.getText().toString(),
                        EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition()),
                        "img_item_default.jpg",
                        true);

                switch (EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition())) {
                    case DEPOSITO:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.agregar_inventario)
                                .setMessage(R.string.estas_seguro_de_agregar_este_inventario)
                                .setIcon(R.drawable.ic_handyman_24_first_color)
                                .setCancelable(false)
                                .setPositiveButton(R.string.si, (dialog, which) -> {
                                    // Guardar la imagen en la galería
                                    ivFoto.setDrawingCacheEnabled(true);
                                    guardarImagenGaleria(ivFoto.getDrawingCache());
                                    ivFoto.setDrawingCacheEnabled(false);
                                    inventarioAgregado.setFoto(currentImagePath);
                                    // Acción al hacer clic en el botón "Sí"
                                    DaoHelperInventario.insertar(inventarioAgregado, getContext());
                                    // Actualiza la lista del adaptador
                                    lista.add(inventarioAgregado);
                                    inventarioAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), R.string.inventario_agregado, Toast.LENGTH_SHORT).show();
                                    //dismiss();
                                    etId.setText(DaoHelperInventario.proximoId(getContext()).toString());
                                    currentImagePath = "img_item_default.jpg";
                                    itemId = DaoHelperInventario.proximoId(getContext());
                                    etNombre.setText("");
                                    etDescripcion.setText("");
                                    spUbicacion.setSelection(-1);
                                    cargarImagen();
                                    dialog.dismiss();
                                })
                                .setNegativeButton(R.string.no, (dialog, which) -> {
                                    // Acción al hacer clic en el botón "No"
                                    Toast.makeText(getContext(), R.string.confirmacion_cancelada, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                })
                                .setNeutralButton(R.string.salir, (dialog, which) -> {
                                    // Acción al hacer clic en el botón "Cancelar"
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
                    case PRESTADO:
                        Toast.makeText(getContext(), "PRESTADO", Toast.LENGTH_SHORT).show();
                        btnAgregar.setEnabled(true);
                        break;
                    default:
                        btnAgregar.setEnabled(true);
                        break;
                }
            }
        });

        spUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "Ubicacion: " + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Ubicacion: " + spUbicacion.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                switch (EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition())) {
                    case DEPOSITO:
                        llIP.setVisibility(View.GONE);
                        btnAgregar.setVisibility(View.VISIBLE);
                        btnCancelar.setVisibility(View.VISIBLE);
                        break;
                    case PRESTADO:
                        llIP.setVisibility(View.VISIBLE);
                        btnAgregar.setVisibility(View.GONE);
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
                        Inventario inventario = new Inventario(Long.parseLong(etId.getText().toString()), etNombre.getText().toString(), etDescripcion.getText().toString(), EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition()), "img_item_default.jpg", true);
                        final List<Prestamo>[] prestamos = new List[]{DaoHelperPrestamo.obtenerTodosPorIdInventarioAceptadoActivo(Long.parseLong(etId.getText().toString()), getContext())};
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
                        tvNombreInventario.setText(
                                etNombre.getText().toString()
                        );
                        tvDescripcionInventario.setText(
                                etDescripcion.getText().toString()
                        );
                        //ivInventario.setImageResource(R.drawable.img_item_default);
                        ivInventario.setImageDrawable(
                                ivFoto.getDrawable()
                        );
                        final ArrayList<Prestatario>[] prestatarios = new ArrayList[]{(ArrayList<Prestatario>) DaoHelperPrestatario.obtenerTodos(getContext())};
                        List<Prestatario> prestatarioList = prestatarios[0];
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
                                Toast.makeText(getContext(), "Cancelado devolver.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnAceptar1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Aceptado.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        lvPrestatario.setOnItemClickListener((parentlv1, viewlv1, positionlv1, idlv1) -> {
                            // Acción al hacer clic en un elemento de la lista
                            lvPrestatario.setEnabled(false);
                            Prestatario prestatario = prestatarios[0].get(positionlv1);
                            /*Prestatario prestatario = (Prestatario) lvPrestatario.getSelectedItem();*/
                            //inventario = new Inventario(Long.parseLong(etId.getText().toString()), etNombre.getText().toString(), etDescripcion.getText().toString(), EUbicacion.getByIndex(spUbicacion.getSelectedItemPosition()), "img_item_default.jpg", true);
                            inventario.setNombre(etNombre.getText().toString());
                            inventario.setDescripcion(etDescripcion.getText().toString());

                            tvNombreInventario.setText(
                                    etNombre.getText().toString()
                            );
                            tvDescripcionInventario.setText(
                                    etDescripcion.getText().toString()
                            );
                            //ivInventario.setImageResource(R.drawable.img_item_default);
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
                            btnAvisoInventario.setBackgroundColor(getContext().getColor(R.color.warning_color));

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Prestar Inventario")
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
                                    .setPositiveButton("Si", (dialog, which) -> {
                                        // Acción al hacer clic en el botón "Sí"
                                        // Guardar la imagen en la galería
                                        ivFoto.setDrawingCacheEnabled(true);
                                        guardarImagenGaleria(ivFoto.getDrawingCache());
                                        ivFoto.setDrawingCacheEnabled(false);
                                        inventario.setFoto(currentImagePath);
                                        inventario.setUbicacion(EUbicacion.PRESTADO);

                                        Prestamo prestamo = new Prestamo(
                                                DaoHelperPrestamo.proximoId(getContext()),
                                                LocalDateTime.now(),
                                                fechaDevolucion,
                                                EEstadoPrestamo.ACEPTADO,
                                                true,
                                                inventario,
                                                prestatario
                                        );

                                        DaoHelperInventario.insertar(inventario, getContext());
                                        DaoHelperPrestamo.insertar(prestamo, getContext());

                                        prestatarios[0].set(positionlv1, prestatario);
                                        prestatarioDetailListSmallAdapter.notifyDataSetChanged();
                                        lista.add(inventario);
                                        inventarioAdapter.notifyDataSetChanged();

                                        // Actualizar el estado del botón aviso inventario
                                        etBuscarNombreUsuario.setEnabled(true);
                                        ibtnBuscar.setEnabled(true);
                                        tvDateDevolucion.setEnabled(true);
                                        tvTimeDevolucion.setEnabled(true);
                                        btnCancelar1.setVisibility(View.GONE);
                                        btnAceptar1.setVisibility(View.GONE);
                                        lvPrestatario.setEnabled(true);

                                        etId.setText(DaoHelperInventario.proximoId(getContext()).toString());
                                        itemId = DaoHelperInventario.proximoId(getContext());
                                        etNombre.setText("");
                                        etDescripcion.setText("");
                                        spUbicacion.setSelection(EUbicacion.DEPOSITO.ordinal());
                                        cargarImagen();

                                        tvTitulo.setText(R.string.prestar_inventario);
                                        etBuscarNombreUsuario.setText("");
                                        etBuscarNombreUsuario.setHint(R.string.ingrese_nombre_de_usuario);
                                        btnCancelar1.setText(R.string.cancelar);
                                        btnAceptar1.setText(R.string.prestar_inventario);

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
                                                                        LocalDateTime.now().until(prestamo.getFechaDevolucion(), ChronoUnit.DAYS)
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
                                        AddInventarioBottomSheetDialogFragment.this.dismiss();
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
                //Toast.makeText(getContext(), "No selecciono nada", Toast.LENGTH_SHORT).show();
                llIP.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ubicacion: " + spUbicacion.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        cargarImagen();

        return dialogView;
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
        builder.setTitle("Seleccionar imagen de");
        builder.setItems(new CharSequence[]{"Cámara", "Galería"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Cámara
                        checkCameraPermissionAndOpenCamera();
                        break;
                    case 1: // Galería
                        checkStoragePermissionAndOpenGallery();
                        break;
                }
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


    private void checkCameraPermissionAndOpenCamera() {
        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camara, Constantes.CAMARA_REQUEST_CODE);
    }

    private void checkStoragePermissionAndOpenGallery() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeria, Constantes.GALERIA_REQUEST_CODE);
    }

    private void cargarImagen() {
        String codigo = String.format("inventario_%s", itemId);
        String nombreArchivo = "IMG_" + codigo + ".jpg";
        File imagen = new File(appDir, nombreArchivo);

        if (imagen.exists()) {
            // Cargar la imagen desde el archivo
            Bitmap bitmap = BitmapFactory.decodeFile(imagen.getAbsolutePath());
            ivFoto.setImageBitmap(bitmap);
        } else {
            // Cargar el icono por defecto
            ivFoto.setImageResource(R.drawable.img_item_default);
        }
    }

    private void guardarImagenGaleria(Bitmap imageBitmap) {
        String codigo = String.format("inventario_%s", itemId);
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

    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return path;
    }
}

