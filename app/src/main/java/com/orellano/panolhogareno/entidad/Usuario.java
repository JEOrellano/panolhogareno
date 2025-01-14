package com.orellano.panolhogareno.entidad;

import com.orellano.panolhogareno.entidad.enumEntidad.ERol;

import java.util.Objects;

public class Usuario {
    private String nombreUsuario;
    private String clave;
    private String nombre;
    private String apellido;
    private ERol rol;
    private String foto;
    private Boolean estado;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String clave, String nombre, String apellido, ERol rol, String foto, Boolean estado) {
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.foto = foto;
        this.estado = estado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public ERol getRol() {
        return rol;
    }

    public void setRol(ERol rol) {
        this.rol = rol;
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
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", rol=" + rol +
                ", foto='" + foto + '\'' +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nombreUsuario, usuario.nombreUsuario) && Objects.equals(clave, usuario.clave) && Objects.equals(nombre, usuario.nombre) && Objects.equals(apellido, usuario.apellido) && rol == usuario.rol && Objects.equals(foto, usuario.foto) && Objects.equals(estado, usuario.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreUsuario, clave, nombre, apellido, rol, foto, estado);
    }
}
