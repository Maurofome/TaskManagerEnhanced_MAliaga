package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// IMPORTANTE: Librerías de Firebase
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class activity_ingresar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);

        // 1. INICIAR FIRESTORE (La Nube)
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button addTask = findViewById(R.id.ingresar_Tarea);
        addTask.setOnClickListener(v -> {
            EditText newTask = findViewById(R.id.NombreTarea);
            EditText newDesc = findViewById(R.id.Descripcion);

            String task = newTask.getText().toString().trim();
            String desc = newDesc.getText().toString().trim();

            if (task.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. EMPAQUETAR DATOS
            // En la nube usamos "Mapas" en vez de SQL
            Map<String, Object> tarea = new HashMap<>();
            tarea.put("nombreTarea", task);
            tarea.put("descripcion", desc);

            // 3. ENVIAR A LA NUBE
            db.collection("tareas") // Nombre de la tabla en la nube
                    .add(tarea)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "¡Guardado en Firebase!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, activity_list.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}