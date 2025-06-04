package com.example.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.Medicamento;
import com.example.lab5.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MedicamentosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicamentosAdapter adapter;
    private List<Medicamento> listaMedicamentos;
    private FloatingActionButton fabAgregar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        initViews();
        setupRecyclerView();
        loadMedicamentos();
        setupClickListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_medicamentos);
        fabAgregar = findViewById(R.id.fab_agregar);
        databaseHelper = new DatabaseHelper(this);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Medicamentos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        listaMedicamentos = new ArrayList<>();

        // Create the click listener
        MedicamentosAdapter.OnMedicamentoClickListener listener = new MedicamentosAdapter.OnMedicamentoClickListener() {
            @Override
            public void onMedicamentoClick(Medicamento medicamento, int position) {

            }

            @Override
            public void onMedicamentoLongClick(Medicamento medicamento, int position) {

            }

            @Override
            public void onClick(Medicamento medicamento) {

            }

            public void onMedicamentoClick(Medicamento medicamento) {
                MedicamentosActivity.this.onMedicamentoClick(medicamento);
            }
        };

        // Initialize adapter with the list and listener
        adapter = new MedicamentosAdapter(listaMedicamentos, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMedicamentos() {
        listaMedicamentos.clear();
        listaMedicamentos.addAll(databaseHelper.getAllMedicamentos());
        adapter.notifyDataSetChanged();

        if (listaMedicamentos.isEmpty()) {
            // Mostrar mensaje de lista vacía
            Toast.makeText(this, "No tienes medicamentos registrados", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MedicamentosActivity.this, AgregarMedicamentoActivity.class);
            startActivity(intent);
        });
    }

    private void onMedicamentoClick(Medicamento medicamento) {
        // Fixed: Should navigate to a detail activity, not to the adapter class
        Intent intent = new Intent(MedicamentosActivity.this, AgregarMedicamentoActivity.class);
        intent.putExtra("medicamento_id", medicamento.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicamentos(); // Recargar lista cuando se regrese
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}