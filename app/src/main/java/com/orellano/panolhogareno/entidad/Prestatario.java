package com.orellano.panolhogareno.entidad;

import java.util.Objects;

public class Prestatario {
    private Long id;
    private Boolean estado;
    private Usuario id_usuario;

    public Prestatario() {
    }

    public Prestatario(Long id, Boolean estado, Usuario id_usuario) {
        this.id = id;
        this.estado = estado;
        this.id_usuario = id_usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Usuario getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Usuario id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "Prestatario{" +
                "id=" + id +
                ", estado=" + estado +
                ", id_usuario=" + id_usuario +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestatario that = (Prestatario) o;
        return Objects.equals(id, that.id) && Objects.equals(estado, that.estado) && Objects.equals(id_usuario, that.id_usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estado, id_usuario);
    }
}
