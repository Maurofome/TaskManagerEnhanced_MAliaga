package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_list extends AppCompatActivity {

    ListView listTarea;

    String[] tareas = {
            "Tarea Gato 1",
            "Tarea Gato 2",
            "Tarea Gato 3",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        // Ajuste visual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Botón INGRESAR ---
        Button btnIngresar = findViewById(R.id.button_ingresar);
        btnIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(activity_list.this, activity_ingresar.class);
            startActivity(intent);
        });

        // --- Botón REVISAR ---
        Button btnRevisar = findViewById(R.id.button_revisar);
        btnRevisar.setOnClickListener(v -> {
            Intent intent = new Intent(activity_list.this, activity_revisar.class);
            startActivity(intent);
        });

        // --- ListView de tareas ---
        listTarea = findViewById(R.id.list_tarea);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                tareas
        );

        listTarea.setAdapter(adapter);

        listTarea.setOnItemClickListener((parent, view, position, id) -> {
            String tareaSeleccionada = tareas[position];
            Toast.makeText(this, "Seleccionaste: " + tareaSeleccionada, Toast.LENGTH_SHORT).show();
        });
    }
}