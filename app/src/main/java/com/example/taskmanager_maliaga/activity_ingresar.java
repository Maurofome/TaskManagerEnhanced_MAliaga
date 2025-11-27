package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_ingresar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Conexión a la base de datos
        SQLiteDatabase db = openOrCreateDatabase("TareasDB_v2", MODE_PRIVATE, null);

        // 2. Crear la tabla con nombres ESTÁNDAR (usaremos estos mismos en el otro archivo)
        String crearTabla = "CREATE TABLE IF NOT EXISTS tareas (" +
                "idTarea INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreTarea TEXT, " +
                "descripcion TEXT)";
        db.execSQL(crearTabla);

        // Botón agregar tarea
        Button addTask = findViewById(R.id.ingresar_Tarea);
        addTask.setOnClickListener(v -> {
            EditText newTask = findViewById(R.id.NombreTarea);
            EditText newDesc = findViewById(R.id.Descripcion);

            String task = newTask.getText().toString().trim();
            String desc = newDesc.getText().toString().trim();

            if (task.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // 3. Insertar usando LOS MISMOS nombres de la tabla creada arriba
                String insertSql = "INSERT INTO tareas (nombreTarea, descripcion) VALUES (?, ?)";
                SQLiteStatement stm = db.compileStatement(insertSql);
                stm.bindString(1, task);
                stm.bindString(2, desc);

                stm.executeInsert();

                Toast.makeText(this, "Tarea agregada con éxito", Toast.LENGTH_SHORT).show();

                // Limpiar campos
                newTask.setText("");
                newDesc.setText("");

                // Ir a la lista
                Intent intent = new Intent(this, activity_list.class);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}