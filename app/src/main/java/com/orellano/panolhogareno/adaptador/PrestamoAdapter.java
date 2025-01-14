package com.orellano.panolhogareno.adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orellano.panolhogareno.MainActivity;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.daoSQLite.DaoHelperInventario;
import com.orellano.panolhogareno.daoSQLite.DaoHelperPrestamo;
import com.orellano.panolhogareno.entidad.Inventario;
import com.orellano.panolhogareno.entidad.Prestamo;
import com.orellano.panolhogareno.entidad.Prestatario;
import com.orellano.panolhogareno.entidad.enumEntidad.EEstadoPrestamo;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;
import com.orellano.panolhogareno.ui.dialogo.DetailInventarioBottomSheetDialogFragment;
import com.orellano.panolhogareno.ui.dialogo.DetailPrestatarioBottomSheetDialogFragment;
import com.orellano.panolhogareno.utilidad.WhatsAppDialog;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.ViewHolder> {
    private List<Prestamo> lista;
    private Context context;

    public PrestamoAdapter(List<Prestamo> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public PrestamoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_prestamo_btn_on, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestamoAdapter.ViewHolder holder, int position) {
        Prestamo prestamo = lista.get(position);
        holder.tvDate.setText(prestamo.getFechaDevolucion()
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATE)));
        holder.tvTime.setText(prestamo.getFechaDevolucion()
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_TIME)));
        // ListView Small Inventario Detail
        List<Inventario> inventarioList = new ArrayList<Inventario>();
        inventarioList.add(prestamo.getId_inventario());
        InventarioDetailListSmallAdapter inventarioDetailListSmallAdapter = new InventarioDetailListSmallAdapter(context, inventarioList);
        holder.lvSmallInventarioDetail.setAdapter(inventarioDetailListSmallAdapter);
        // ListView Small Prestatario Detail
        List<Prestatario> prestatarioList = new ArrayList<Prestatario>();
        prestatarioList.add(prestamo.getId_prestatario());
        PrestatarioDetailListSmallAdapter prestatarioDetailListSmallAdapter = new PrestatarioDetailListSmallAdapter(context, prestatarioList);
        holder.lvSmallPrestatarioDetail.setAdapter(prestatarioDetailListSmallAdapter);
        // Button Avisar
        holder.btnAvisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Avisar";
                String message = String.format(
                        "Hola %s %s, tu prestamo %s %s ha sido %s, por favor recordar fecha y hora de devolucion %s.",
                        prestamo.getId_prestatario().getId_usuario().getNombre(),
                        prestamo.getId_prestatario().getId_usuario().getApellido(),
                        prestamo.getId_inventario().getNombre(),
                        prestamo.getId_inventario().getDescripcion(),
                        prestamo.getEstadoPrestamo().toString(),
                        prestamo.getFechaDevolucion()
                                .format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME))
                );
                WhatsAppDialog.showWhatsAppDialog(context,message);
            }
        });
        // Button Devolver
        holder.btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "Devolver";
                holder.btnDevolver.setEnabled(false);
                holder.pbAvisarDevolver.setVisibility(View.VISIBLE);
                // TODO: Devolver el prestamo
                Toast.makeText(context, R.string.devolver, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.devolver_inventario)
                        .setMessage(
                                String.format(
                                        "¿Estas seguro que quieres devolver %s %s por %s %s?",
                                        prestamo.getId_inventario().getNombre(),
                                        prestamo.getId_inventario().getDescripcion(),
                                        prestamo.getId_prestatario().getId_usuario().getNombre(),
                                        prestamo.getId_prestatario().getId_usuario().getApellido()
                                )
                        )
                        .setIcon(R.drawable.ic_handshake_24_first_color)
                        .setCancelable(true)
                        .setPositiveButton(R.string.si, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí";
                            Toast.makeText(context, R.string.devolucion_realizada, Toast.LENGTH_SHORT).show();
                            prestamo.getId_inventario().setUbicacion(EUbicacion.DEPOSITO);
                            DaoHelperInventario.actualizar(prestamo.getId_inventario(), context);
                            prestamo.setEstadoPrestamo(EEstadoPrestamo.CONCLUIDO);
                            DaoHelperPrestamo.actualizar(prestamo, context);
                            lista.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, lista.size());
                            notifyDataSetChanged();
                            // Actualizar notificacion
                            if (context instanceof MainActivity) {
                                ((MainActivity) context).updateNotificationBadge();
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // Acción al hacer clic en el botón "No";
                            Toast.makeText(context, R.string.cancelado_devolver, Toast.LENGTH_SHORT).show();
                        })
                        .setNeutralButton(R.string.salir, (dialog, which) -> {
                            // Acción al hacer clic en el botón "Salir";
                            Toast.makeText(context, R.string.cancelado_devolver, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                holder.btnDevolver.setEnabled(true);
                                holder.pbAvisarDevolver.setVisibility(View.GONE);
                            }
                        })
                        .show();
            }
        });
        holder.lvSmallInventarioDetail.setOnItemClickListener((parentI, viewI, positionI, idI) -> {
            // Acción al hacer clic en el ListView Small Inventario Detail;
            DetailInventarioBottomSheetDialogFragment bottomSheetDialogFragment = new DetailInventarioBottomSheetDialogFragment(prestamo.getId_inventario());
            bottomSheetDialogFragment.show(((MainActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        });
        holder.lvSmallPrestatarioDetail.setOnItemClickListener((parentP, viewP, positionP, idP) -> {
            // Acción al hacer clic en el ListView Small Prestatario Detail;
            DetailPrestatarioBottomSheetDialogFragment bottomSheetDialogFragment = new DetailPrestatarioBottomSheetDialogFragment(prestamo.getId_prestatario());
            bottomSheetDialogFragment.show(((MainActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvTime;
        // ListView Small Inventario Detail
        public ListView lvSmallInventarioDetail;
        // ListView Small Prestatario Detail
        public ListView lvSmallPrestatarioDetail;
        // Button Avisar
        public Button btnAvisar;
        // Button Devolver
        public Button btnDevolver;
        // ProgressBar
        public ProgressBar pbAvisarDevolver;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date_devolucion_template_prestamo_notifications);
            tvTime = itemView.findViewById(R.id.tv_time_devolucion_template_prestamo_notifications);
            lvSmallInventarioDetail = itemView.findViewById(R.id.lv_list_small_inventario_detail_prestamo_notifications);
            lvSmallPrestatarioDetail = itemView.findViewById(R.id.lv_list_small_prestatario_detail_prestamo_notifications);
            btnAvisar = itemView.findViewById(R.id.btn_avisar_prestamo_notifications);
            btnDevolver = itemView.findViewById(R.id.btn_devolver_prestamo_notifications);
            pbAvisarDevolver = itemView.findViewById(R.id.pb_avisar_devolver_prestamo_notifications);
        }
    }
}

