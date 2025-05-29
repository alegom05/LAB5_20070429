package com.example.lab5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {

    private EditText etNombre, etMensaje;
    private Button btnGuardar;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MedicamentosPrefs";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_MENSAJE = "mensaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        initViews();
        loadCurrentSettings();
        setupClickListeners();
    }

    private void initViews() {
        etNombre = findViewById(R.id.et_nombre);
        etMensaje = findViewById(R.id.et_mensaje);
        btnGuardar = findViewById(R.id.btn_guardar);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Configuraciones");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadCurrentSettings() {
        String nombreActual = sharedPreferences.getString(KEY_USUARIO, "Usuario");
        String mensajeActual = sharedPreferences.getString(KEY_MENSAJE, "¡Recuerda tomar tus medicamentos a tiempo!");

        etNombre.setText(nombreActual);
        etMensaje.setText(mensajeActual);
    }

    private void setupClickListeners() {
        btnGuardar.setOnClickListener(v -> guardarConfiguracion());
    }

    private void guardarConfiguracion() {
        String nombre = etNombre.getText().toString().trim();
        String mensaje = etMensaje.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre no puede estar vacío");
            etNombre.requestFocus();
            return;
        }

        if (mensaje.isEmpty()) {
            etMensaje.setError("El mensaje no puede estar vacío");
            etMensaje.requestFocus();
            return;
        }

        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USUARIO, nombre);
        editor.putString(KEY_MENSAJE, mensaje);
        editor.apply();

        Toast.makeText(this, "Configuración guardada correctamente", Toast.LENGTH_SHORT).show();
        finish(); // Cerrar activity y regresar
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}