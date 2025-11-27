package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainscreen);

        // Ajuste de barras del sistema (diseño edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuración del Botón
        Button btnIr = findViewById(R.id.button3);

        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- PASO 1: FEEDBACK VISUAL ---
                // Avisamos que el proceso inició (como encender un LED indicador)
                btnIr.setText("Cargando sistema...");
                btnIr.setEnabled(false); // Bloqueamos el botón para evitar múltiples clicks

                // --- PASO 2: INICIAR EL HILO (Thread) ---
                new Thread(() -> {
                    try {

                        Thread.sleep(5000);

                        // --- PASO 3: VOLVER AL HILO PRINCIPAL (UI) ---

                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        // En caso de error, volvemos a activar el botón (por seguridad)
                        runOnUiThread(() -> btnIr.setEnabled(true));
                    }
                }).start();
            }
        });
    }
}