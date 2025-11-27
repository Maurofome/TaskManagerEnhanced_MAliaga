package com.example.taskmanager_maliaga;

public class Tarea {
    int idTarea;
    String nombreTarea;
    String descripcion;

    public Tarea() {}

    public Tarea(int idTarea, String nombreTarea, String descripcion) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.descripcion = descripcion;
    }

    public int getIdTask() {
        return idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setIdTask(int idTask) {
        this.idTarea = idTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}

