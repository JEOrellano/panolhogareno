package com.orellano.panolhogareno.entidad.enumEntidad;

public enum EEstadoPrestamo {
    ACEPTADO,
    CONCLUIDO;

    public static EEstadoPrestamo getByIndex(int index) {
        return EEstadoPrestamo.values()[index];
    }
}
