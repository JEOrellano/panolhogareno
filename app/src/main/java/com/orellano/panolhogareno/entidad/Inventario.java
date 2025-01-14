package com.orellano.panolhogareno.entidad;

import com.orellano.panolhogareno.entidad.enumEntidad.EUbicacion;

import java.util.Objects;

public class Inventario {
    private Long id;
    private String nombre;
    private String descripcion;
    private EUbicacion ubicacion;
    private String foto;
    private Boolean estado;

    public Inventario() {
    }

    public Inventario(Long id, String nombre, String descripcion, EUbicacion ubicacion, String foto, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.foto = foto;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(EUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ubicacion=" + ubicacion +
                ", foto='" + foto + '\'' +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventario that = (Inventario) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(descripcion, that.descripcion) && ubicacion == that.ubicacion && Objects.equals(foto, that.foto) && Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, ubicacion, foto, estado);
    }
}
