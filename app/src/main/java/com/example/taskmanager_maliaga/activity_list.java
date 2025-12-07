package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// --- ESTA ES LA LÍNEA CORREGIDA ---
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class activity_list extends AppCompatActivity {

    ListView listTarea;
    ArrayList<String> datos;
    ArrayAdapter<String> adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 1. Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // 2. Inicializar lista y adaptador
        listTarea = findViewById(R.id.list_tarea);
        datos = new ArrayList<>();
        // Usamos el diseño simple de Android para la lista
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        listTarea.setAdapter(adapter);

        // 3. Cargar datos desde Firebase
        cargarDatosFirestore();

        // 4. Botón para ir a CREAR nueva tarea
        Button btnIngresar = findViewById(R.id.button_ingresar);
        if (btnIngresar != null) {
            btnIngresar.setOnClickListener(v -> {
                Intent intent = new Intent(activity_list.this, activity_ingresar.class);
                startActivity(intent);
            });
        }


        // 5. DETECTAR EL CLICK EN UNA TAREA
        listTarea.setOnItemClickListener((parent, view, position, id) -> {
            String itemTexto = datos.get(position);

            // El formato que guardamos es: "ID_DOCUMENTO - NombreTarea"
            // Separamos el ID del resto del texto usando el guión
            String[] partes = itemTexto.split(" - ");
            if (partes.length > 0) {
                String idTarea = partes[0].trim(); // Este es el ID de Firebase

                // Abrimos la pantalla de detalles enviando el ID
                Intent intent = new Intent(activity_list.this, activity_detalle.class);
                intent.putExtra("ID_TAREA", idTarea);
                startActivity(intent);
            }
        });
    }

    private void cargarDatosFirestore() {
        // Escuchamos la colección "tareas" en tiempo real
        db.collection("tareas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", "Error al cargar datos", error);
                    return;
                }

                if (value != null) {
                    datos.clear(); // Limpiamos la lista para no duplicar
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        // Obtenemos los datos tal cual se llaman en Firebase
                        String id = doc.getId(); // El ID del documento (ej: y7MSz...)
                        String nombre = doc.getString("nombreTarea");
                        String desc = doc.getString("descripcion");

                        // Formateamos para mostrar en la lista
                        // EJEMPLO: "y7MSz... - Comprar Pan (Descripción...)"
                        datos.add(id + " - " + nombre + " (" + desc + ")");
                    }
                    // Avisamos al adaptador que actualice la vista
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}