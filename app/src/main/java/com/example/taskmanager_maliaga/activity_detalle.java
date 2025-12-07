package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_detalle extends AppCompatActivity {

    // Usamos los nombres de variables que tenías en tu código original
    EditText editNombre, editDesc;
    Button btnEditar, btnEliminar, btnVolver;
    String idTarea;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle); // Tu XML original

        // 1. Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // 2. Vincular con tus IDs ORIGINALES (Sin cambios en XML)
        editNombre = findViewById(R.id.editNombre);
        editDesc = findViewById(R.id.editDescripcion); // Ojo: en tu código anterior el ID era 'editDescripcion'
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);

        // 3. Recibir el ID que viene de la lista
        Intent intent = getIntent();
        idTarea = intent.getStringExtra("ID_TAREA");

        // Si no hay ID, cerramos para evitar errores
        if (idTarea == null || idTarea.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró la tarea", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 4. Cargar datos desde Firebase al abrir
        cargarDetalles();

        // 5. Configurar los botones
        btnEditar.setOnClickListener(v -> editarTarea());
        btnEliminar.setOnClickListener(v -> eliminarTarea());

        // Botón volver
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }

    private void cargarDetalles() {
        // Buscamos en Firebase usando el ID
        db.collection("tareas").document(idTarea).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Obtenemos los textos de la nube
                        String nombre = documentSnapshot.getString("nombreTarea");
                        String descripcion = documentSnapshot.getString("descripcion");

                        // Los ponemos en tus cajas de texto originales
                        editNombre.setText(nombre);
                        editDesc.setText(descripcion);
                    } else {
                        Toast.makeText(this, "La tarea ya no existe", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                });
    }

    private void editarTarea() {
        String nuevoNombre = editNombre.getText().toString();
        String nuevaDesc = editDesc.getText().toString();

        if (nuevoNombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Preparamos los datos para actualizar
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("nombreTarea", nuevoNombre);
        updateMap.put("descripcion", nuevaDesc);

        // Enviamos la actualización a Firebase
        db.collection("tareas").document(idTarea)
                .update(updateMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "¡Tarea Actualizada!", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a la lista
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                });
    }

    private void eliminarTarea() {
        // Borramos el documento en Firebase
        db.collection("tareas").document(idTarea)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Tarea Eliminada", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a la lista
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
    }
}