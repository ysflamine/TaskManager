package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String titulo;
    private String descripcion;
    private int prioridad; // 1/5
    private TaskStatus estado;
    private String responsable;
    private LocalDate fechaLimite;
    private LocalDateTime fechaCreacion;

    public Task() {}

    public Task(String titulo, String descripcion, int prioridad,
                TaskStatus estado, String responsable, LocalDate fechaLimite) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.responsable = responsable;
        this.fechaLimite = fechaLimite;
        // fechaCreacion se asignará automáticamente e la base de datos, no lo pasamos aquí
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public TaskStatus getEstado() {
        return estado;
    }

    public void setEstado(TaskStatus estado) {
        this.estado = estado;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    @Override
    public String toString() {
        return String.format("Task{id=%d, titulo='%s', estado=%s, prioridad=%d, responsable='%s'}",
                id, titulo, estado, prioridad, responsable);
    }
}

