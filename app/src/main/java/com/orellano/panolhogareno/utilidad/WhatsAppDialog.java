package com.orellano.panolhogareno.utilidad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.orellano.panolhogareno.R;

public class WhatsAppDialog {

    public static void showWhatsAppDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.enviar_por_whatsapp);
        builder.setMessage(R.string.por_favor_ingresa_un_n_mero_de_tel_fono);
        builder.setIcon(R.drawable.ic_phone_24_success_color);

        // Inflar el layout personalizado para el diálogo
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_phone_number, null);
        EditText phoneNumberEditText = view.findViewById(R.id.phone_number_edit_text);
        builder.setView(view);

        builder.setPositiveButton(R.string.enviar, (dialog, which) -> {
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(context, R.string.por_favor_ingresa_un_n_mero_de_tel_fono, Toast.LENGTH_SHORT).show();
            }
            sendWhatsAppMessage(context, phoneNumber, message);
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.cancelar, (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void sendWhatsAppMessage(Context context, String phoneNumber, String message) {
        try {
            if (phoneNumber.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.putExtra("jid", phoneNumber + "@s.whatsapp.net"); // Agregar el número como JID
                intent.setPackage("com.whatsapp");
                context.startActivity(intent);
            } else {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        } catch (Exception e) {
            Toast.makeText(context, R.string.whatsapp_no_est_instalado, Toast.LENGTH_SHORT).show();
        }
    }
}

