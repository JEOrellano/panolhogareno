package com.orellano.panolhogareno.entidad;

import com.orellano.panolhogareno.daoSQLite.ConectSQLiteHelperDB;
import com.orellano.panolhogareno.entidad.enumEntidad.EEstadoPrestamo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Prestamo {
    private Long id;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaDevolucion;
    private EEstadoPrestamo estadoPrestamo;
    private Boolean estado;
    private Inventario id_inventario;
    private Prestatario id_prestatario;

    public Prestamo() {
    }

    public Prestamo(Long id, LocalDateTime fechaSolicitud, LocalDateTime fechaDevolucion, EEstadoPrestamo estadoPrestamo, Boolean estado, Inventario id_inventario, Prestatario id_prestatario) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaDevolucion = fechaDevolucion;
        this.estadoPrestamo = estadoPrestamo;
        this.estado = estado;
        this.id_inventario = id_inventario;
        this.id_prestatario = id_prestatario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EEstadoPrestamo getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public void setEstadoPrestamo(EEstadoPrestamo estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Inventario getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(Inventario id_inventario) {
        this.id_inventario = id_inventario;
    }

    public Prestatario getId_prestatario() {
        return id_prestatario;
    }

    public void setId_prestatario(Prestatario id_prestatario) {
        this.id_prestatario = id_prestatario;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", fechaSolicitud=" + fechaSolicitud.format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)) +
                ", fechaDevolucion=" + fechaDevolucion.format(DateTimeFormatter.ofPattern(ConectSQLiteHelperDB.TABLE_PRESTAMO_FORMAT_DATETIME)) +
                ", estadoPrestamo=" + estadoPrestamo +
                ", estado=" + estado +
                ", id_inventario=" + id_inventario +
                ", id_prestatario=" + id_prestatario +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return Objects.equals(id, prestamo.id) && Objects.equals(fechaSolicitud, prestamo.fechaSolicitud) && Objects.equals(fechaDevolucion, prestamo.fechaDevolucion) && estadoPrestamo == prestamo.estadoPrestamo && Objects.equals(estado, prestamo.estado) && Objects.equals(id_inventario, prestamo.id_inventario) && Objects.equals(id_prestatario, prestamo.id_prestatario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaSolicitud, fechaDevolucion, estadoPrestamo, estado, id_inventario, id_prestatario);
    }
}
