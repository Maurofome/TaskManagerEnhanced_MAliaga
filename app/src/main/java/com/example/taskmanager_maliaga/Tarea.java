package com.example.taskmanager_maliaga;

import com.google.firebase.firestore.DocumentId;

public class Tarea {

    @DocumentId
    public String idTarea; // Para guardar el ID del documento

    // TIENEN QUE LLAMARSE IGUAL QUE EN FIREBASE
    public String nombreTarea;
    public String descripcion;

    // Constructor vacío (OBLIGATORIO)
    public Tarea() {}

    public Tarea(String nombreTarea, String descripcion) {
        this.nombreTarea = nombreTarea;
        this.descripcion = descripcion;
    }
}