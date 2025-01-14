package com.orellano.panolhogareno.entidad.enumEntidad;

public enum EUbicacion {
    DEPOSITO,
    PRESTADO;

    public static EUbicacion getByIndex(int index) {
        return EUbicacion.values()[index];
    }
}
