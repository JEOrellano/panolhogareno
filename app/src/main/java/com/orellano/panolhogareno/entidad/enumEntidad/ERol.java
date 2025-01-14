package com.orellano.panolhogareno.entidad.enumEntidad;

public enum ERol {
    ADMIN,
    PRESTATARIO;

    public static ERol getByIndex(int index) {
        return ERol.values()[index];
    }
}
