package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class activity_mainscreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TareaAdapter adapter;
    private List<Tarea> listaTareas;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainscreen);

        // 1. Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        listaTareas = new ArrayList<>();

        // 2. Conectar la lista
        recyclerView = findViewById(R.id.recyclerTareas);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TareaAdapter(listaTareas);
            recyclerView.setAdapter(adapter);

            // 3. Cargar datos
            cargarDatos();
        } else {
            Toast.makeText(this, "ERROR: Revisa el XML, falta el ID recyclerTareas", Toast.LENGTH_LONG).show();
        }

        // Ajuste de márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón de Volver (Opcional)
        Button btnIr = findViewById(R.id.button3);
        if (btnIr != null) {
            btnIr.setOnClickListener(v -> {
                Intent intent = new Intent(activity_mainscreen.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }

    private void cargarDatos() {
        // Escuchamos la colección "tareas"
        db.collection("tareas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("ErrorFire", "Error escuchando datos", error);
                    return;
                }

                if (value != null) {
                    listaTareas.clear(); // Limpiamos la lista visual
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        // Convierte el JSON de Firebase a nuestra clase Tarea
                        Tarea t = doc.toObject(Tarea.class);
                        if (t != null) {
                            // Guardamos el ID por si queremos borrar/editar luego
                            t.idTarea = doc.getId();
                            listaTareas.add(t);
                        }
                    }
                    // Le decimos a la pantalla que se actualice
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}