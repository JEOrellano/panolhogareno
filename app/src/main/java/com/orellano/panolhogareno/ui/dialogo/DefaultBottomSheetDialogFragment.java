package com.orellano.panolhogareno.ui.dialogo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.adaptador.InventarioAdapter;
import com.orellano.panolhogareno.entidad.Inventario;

import java.util.List;

public class DefaultBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private List<Inventario> lista;
    private InventarioAdapter inventarioAdapter;
    private Fragment fragment;

    private ImageView ivIcono;
    private TextView tvTitulo;
    private TextView tvCampo1;
    private EditText etCampo1;
    private TextView tvCampo2;
    private EditText etCampo2;
    private TextView tvCampo3;
    private EditText etCampo3;
    private TextView tvCampo4;
    private Spinner spCampo4;
    private ImageView ivImg1;
    private Button btnAccion1;
    private Button btnAccion2;

    public DefaultBottomSheetDialogFragment(List<Inventario> lista, InventarioAdapter inventarioAdapter, Fragment fragment) {
        this.lista = lista;
        this.inventarioAdapter = inventarioAdapter;
        this.fragment = fragment;
    }

    // ... (Declara tus vistas aquí) ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.card_sheet_dialog_default_inventario, container, false);

        // ... (Obtén referencias a tus vistas, establece valores iniciales y configura listeners) ...

        ivIcono = dialogView.findViewById(R.id.iv_icono_card_sheet_dialog_default_inventario);
        tvTitulo = dialogView.findViewById(R.id.tv_titulo_card_sheet_dialog_default_inventario);
        tvCampo1 = dialogView.findViewById(R.id.tv_campo1_card_sheet_dialog_default_inventario);
        etCampo1 = dialogView.findViewById(R.id.et_campo1_card_sheet_dialog_default_inventario);
        tvCampo2 = dialogView.findViewById(R.id.tv_campo2_card_sheet_dialog_default_inventario);
        etCampo2 = dialogView.findViewById(R.id.et_campo2_card_sheet_dialog_default_inventario);
        tvCampo3 = dialogView.findViewById(R.id.tv_campo3_card_sheet_dialog_default_inventario);
        etCampo3 = dialogView.findViewById(R.id.et_campo3_card_sheet_dialog_default_inventario);
        tvCampo4 = dialogView.findViewById(R.id.tv_campo4_card_sheet_dialog_default_inventario);
        spCampo4 = dialogView.findViewById(R.id.sp_campo4_card_sheet_dialog_default_inventario);
        ivImg1 = dialogView.findViewById(R.id.iv_img1_card_sheet_dialog_default_inventario);
        btnAccion1 = dialogView.findViewById(R.id.btn_accion1_card_sheet_dialog_default_inventario);
        btnAccion2 = dialogView.findViewById(R.id.id_btn_accion2_card_sheet_dialog_default_inventario);
        // 3. Establece valores iniciales en las vistas
        ivIcono.setImageResource(R.drawable.ic_question_mark_24_first_color);
        tvTitulo.setText("Titulo");
        tvCampo1.setText("campo1:");
        etCampo1.setText("");
        etCampo1.setHint("Ingrese campo1");
        tvCampo2.setText("campo2");
        etCampo2.setText("");
        etCampo2.setHint("Ingrese campo2");
        tvCampo3.setText("campo3");
        etCampo3.setText("");
        etCampo3.setHint("Ingrese campo3");
        tvCampo4.setText("campo4:");
        //EUbicacionSpinnerAdapter adapter = new EUbicacionSpinnerAdapter(getContext(), List.of(EUbicacion.values()));
        spCampo4.setPrompt("Seleccione campo4");
        //spUbicacion.setSelection(inventario.getUbicacion().ordinal());
        spCampo4.setEnabled(true);
        ivImg1.setImageResource(R.drawable.img_empty_default);
        // 5. Establece los oyentes de clics de las vistas
        ivImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en la img1
                Toast.makeText(getContext(), "Imagen1", Toast.LENGTH_SHORT).show();
            }
        });
        btnAccion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "accion1"
                Toast.makeText(getContext(), "Accion1.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        btnAccion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "accion2"
                new AlertDialog.Builder(getContext())
                        .setTitle("Accion 2")
                        .setMessage("¿Estas seguro de Accion2?")
                        .setIcon(R.drawable.ic_question_mark_24_first_color)
                        .setCancelable(false)
                        .setPositiveButton("Si", (dialog, which) -> {
                            // Acción al hacer clic en el botón "Sí"
                            Toast.makeText(getContext(), "Accion2 si.", Toast.LENGTH_SHORT).show();
                            dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Acción al hacer clic en el botón "No"
                            Toast.makeText(getContext(), "Accion2 no.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        })
                        .setNeutralButton("Salir", (dialog, which) -> {
                            // Acción al hacer clic en el botón "Cancelar"
                            Toast.makeText(getContext(), "Accion2 salir.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dismiss();
                        })
                        .show();
            }
        });
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // ... (Métodos para manejar clics de botones y otra lógica) ...
}
