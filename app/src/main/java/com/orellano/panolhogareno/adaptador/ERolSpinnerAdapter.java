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
import com.orellano.panolhogareno.entidad.enumEntidad.ERol;

import java.util.List;

public class ERolSpinnerAdapter extends ArrayAdapter<ERol> {
    public ERolSpinnerAdapter(@NonNull Context context, @NonNull List<ERol> objects) {
        super(context, R.layout.card_spinner_default, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.card_spinner_default, null);

        TextView tvId = view.findViewById(R.id.tv_id_spiner_default);
        TextView tvDescripcion = view.findViewById(R.id.tv_decripcion_spiner_default);

        ERol rol = getItem(position);

        tvId.setText(String.valueOf(rol.ordinal()));
        tvDescripcion.setText(rol.toString());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.card_spinner_default, null);

        TextView tvId = view.findViewById(R.id.tv_id_spiner_default);
        TextView tvDescripcion = view.findViewById(R.id.tv_decripcion_spiner_default);

        tvId.setText(String.valueOf(position));
        tvDescripcion.setText(ERol.getByIndex(position).toString());

        return view;
    }
}

