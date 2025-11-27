package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_list extends AppCompatActivity {

    ListView listTarea;
    ArrayList<String> datos;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 1. Inicializar lista y adaptador
        listTarea = findViewById(R.id.list_tarea);
        datos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        listTarea.setAdapter(adapter);

        // 2. Cargar datos iniciales
        cargarDatos();

        // 3. Botón para ir a CREAR nueva tarea
        Button btnIngresar = findViewById(R.id.button_ingresar);
        btnIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(activity_list.this, activity_ingresar.class);
            startActivity(intent);
        });

        // 4. DETECTAR EL CLICK EN UNA TAREA
        listTarea.setOnItemClickListener((parent, view, position, id) -> {
            String itemTexto = datos.get(position);

            // Separamos el ID del resto del texto usando el punto "."
            String[] partes = itemTexto.split("\\.");
            String idTarea = partes[0].trim(); // "5"

            // Abrimos la pantalla de detalles enviando el ID
            Intent intent = new Intent(activity_list.this, activity_detalle.class);
            intent.putExtra("ID_TAREA", idTarea);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        datos.clear();
        SQLiteDatabase db = openOrCreateDatabase("TareasDB_v2", MODE_PRIVATE, null);

        try {
            // Nos aseguramos que la tabla exista
            db.execSQL("CREATE TABLE IF NOT EXISTS tareas (idTarea INTEGER PRIMARY KEY AUTOINCREMENT, nombreTarea TEXT, descripcion TEXT)");

            Cursor cursor = db.rawQuery("SELECT * FROM tareas", null);
            if (cursor.moveToFirst()) {
                do {
                    // Indices: 0=id, 1=nombre, 2=descripcion
                    String id = cursor.getString(0);
                    String nombre = cursor.getString(1);
                    String desc = cursor.getString(2);

                    datos.add(id + ". " + nombre + " - " + desc);
                } while (cursor.moveToNext());
            }
            cursor.close();
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar lista", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}