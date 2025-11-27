package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_detalle extends AppCompatActivity {

    EditText editNombre, editDesc;
    Button btnEditar, btnEliminar, btnVolver;
    String idTarea; // Variable para guardar el ID de la tarea actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // 1. Vincular los elementos visuales
        editNombre = findViewById(R.id.editNombre);
        editDesc = findViewById(R.id.editDescripcion);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);

        // 2. Recibir el ID que nos mandó la lista
        Intent intent = getIntent();
        idTarea = intent.getStringExtra("ID_TAREA");

        // 3. Cargar los datos de esa tarea al abrir la pantalla
        cargarDetalles();

        // 4. Configurar acciones de los botones
        btnEditar.setOnClickListener(v -> editarTarea());
        btnEliminar.setOnClickListener(v -> eliminarTarea());

        btnVolver.setOnClickListener(v -> finish()); // Solo cierra la pantalla
    }

    private void cargarDetalles() {
        SQLiteDatabase db = openOrCreateDatabase("TareasDB_v2", MODE_PRIVATE, null);
        try {
            // Buscamos SOLO la tarea que tiene este ID
            Cursor cursor = db.rawQuery("SELECT * FROM tareas WHERE idTarea = ?", new String[]{idTarea});

            if (cursor.moveToFirst()) {
                // Llenamos los campos de texto con la info de la base de datos
                editNombre.setText(cursor.getString(1)); // Columna nombreTarea
                editDesc.setText(cursor.getString(2));   // Columna descripcion
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar detalle", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    private void editarTarea() {
        SQLiteDatabase db = openOrCreateDatabase("TareasDB_v2", MODE_PRIVATE, null);
        try {
            String nuevoNombre = editNombre.getText().toString();
            String nuevaDesc = editDesc.getText().toString();

            // Actualizamos (UPDATE) la tarea donde coincida el ID
            String sql = "UPDATE tareas SET nombreTarea = ?, descripcion = ? WHERE idTarea = ?";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, nuevoNombre);
            stmt.bindString(2, nuevaDesc);
            stmt.bindString(3, idTarea);
            stmt.execute();

            Toast.makeText(this, "Tarea Actualizada", Toast.LENGTH_SHORT).show();
            finish(); // Volver a la lista

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    private void eliminarTarea() {
        SQLiteDatabase db = openOrCreateDatabase("TareasDB_v2", MODE_PRIVATE, null);
        try {
            // Borramos (DELETE) la tarea donde coincida el ID
            String sql = "DELETE FROM tareas WHERE idTarea = ?";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, idTarea);
            stmt.execute();

            Toast.makeText(this, "Tarea Eliminada", Toast.LENGTH_SHORT).show();
            finish(); // Volver a la lista

        } catch (Exception e) {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}