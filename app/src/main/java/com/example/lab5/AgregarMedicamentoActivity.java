package com.example.lab5;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5.DatabaseHelper;
import com.example.lab5.Medicamento;
import com.example.lab5.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgregarMedicamentoActivity extends AppCompatActivity {

    private EditText etNombre, etDosis;
    private Spinner spinnerTipo, spinnerFrecuencia;
    private TextView tvFechaInicio, tvHoraInicio;
    private Button btnFecha, btnHora, btnGuardar;
    private DatabaseHelper databaseHelper;

    private Calendar calendario;
    private String fechaSeleccionada = "";
    private String horaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_medicamento);

        initViews();
        setupSpinners();
        setupClickListeners();
        setDefaultDateTime();
    }

    private void initViews() {
        etNombre = findViewById(R.id.et_nombre_medicamento);
        etDosis = findViewById(R.id.et_dosis);
        spinnerTipo = findViewById(R.id.spinner_tipo);
/*        spinnerFrecuencia = findViewById(R.id.spinner_frecuencia);
        tvFechaInicio = findViewById(R.id.tv_fecha_inicio);
        tvHoraInicio = findViewById(R.id.tv_hora_inicio);*/
        btnFecha = findViewById(R.id.btn_seleccionar_fecha);
        btnHora = findViewById(R.id.btn_seleccionar_hora);
        btnGuardar = findViewById(R.id.btn_guardar_medicamento);

        calendario = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(this);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Agregar Medicamento");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupSpinners() {
        // Spinner de tipos de medicamento
        String[] tiposMedicamento = {"Pastilla", "Jarabe", "Ampolla", "Cápsula", "Gotas", "Inyección"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposMedicamento);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        // Spinner de frecuencias
        String[] frecuencias = {
                "1 - Cada hora",
                "2 - Cada 2 horas",
                "4 - Cada 4 horas",
                "6 - Cada 6 horas",
                "8 - Cada 8 horas",
                "12 - Cada 12 horas",
                "24 - Cada día",
                "48 - Cada 2 días",
                "72 - Cada 3 días",
                "168 - Cada semana"
        };
        ArrayAdapter<String> adapterFrecuencia = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frecuencias);
        adapterFrecuencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrecuencia.setAdapter(adapterFrecuencia);
        spinnerFrecuencia.setSelection(6); // Por defecto "Cada día"
    }

    private void setupClickListeners() {
        btnFecha.setOnClickListener(v -> showDatePicker());
        btnHora.setOnClickListener(v -> showTimePicker());
        btnGuardar.setOnClickListener(v -> guardarMedicamento());
    }

    private void setDefaultDateTime() {
        // Establecer fecha y hora actuales por defecto
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        fechaSeleccionada = dateFormat.format(calendario.getTime());
        horaSeleccionada = timeFormat.format(calendario.getTime());

        tvFechaInicio.setText(fechaSeleccionada);
        tvHoraInicio.setText(horaSeleccionada);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendario.set(Calendar.YEAR, year);
                    calendario.set(Calendar.MONTH, month);
                    calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    fechaSeleccionada = dateFormat.format(calendario.getTime());
                    tvFechaInicio.setText(fechaSeleccionada);
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendario.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendario.set(Calendar.MINUTE, minute);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    horaSeleccionada = timeFormat.format(calendario.getTime());
                    tvHoraInicio.setText(horaSeleccionada);
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void guardarMedicamento() {
        String nombre = etNombre.getText().toString().trim();
        String dosis = etDosis.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();

        // Extraer frecuencia en horas del spinner
        String frecuenciaTexto = spinnerFrecuencia.getSelectedItem().toString();
        int frecuenciaHoras = Integer.parseInt(frecuenciaTexto.split(" - ")[0]);

        // Validaciones
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre del medicamento es obligatorio");
            etNombre.requestFocus();
            return;
        }

        if (dosis.isEmpty()) {
            etDosis.setError("La dosis es obligatoria");
            etDosis.requestFocus();
            return;
        }

        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Selecciona fecha y hora de inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear medicamento
        Medicamento medicamento = new Medicamento(nombre, tipo, dosis, frecuenciaHoras, fechaSeleccionada, horaSeleccionada);

        // Guardar en base de datos
        long id = databaseHelper.insertMedicamento(medicamento);

        if (id > 0) {
            Toast.makeText(this, "Medicamento agregado correctamente", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar activity y regresar
        } else {
            Toast.makeText(this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}