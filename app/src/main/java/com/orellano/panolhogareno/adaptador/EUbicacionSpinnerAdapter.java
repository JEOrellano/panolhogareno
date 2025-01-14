package com.orellano.panolhogareno.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orellano.panolhogareno.R;
import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;

import java.util.List;

public class EUbicacionSpinnerAdapter extends ArrayAdapter<EUbicacion> {
    public EUbicacionSpinnerAdapter(@NonNull Context context, @NonNull List<EUbicacion> objects) {
        super(context, R.layout.card_spinner_default, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.card_spinner_default, null);

        TextView tvId = view.findViewById(R.id.tv_id_spiner_default);
        TextView tvDescripcion = view.findViewById(R.id.tv_decripcion_spiner_default);

        EUbicacion ubicacion = getItem(position);

        tvId.setText(String.valueOf(ubicacion.ordinal()));
        tvDescripcion.setText(ubicacion.toString());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.card_spinner_default, null);

        TextView tvId = view.findViewById(R.id.tv_id_spiner_default);
        TextView tvDescripcion = view.findViewById(R.id.tv_decripcion_spiner_default);

        tvId.setText(String.valueOf(position));
        tvDescripcion.setText(EUbicacion.getByIndex(position).toString());

        return view;
    }
}

