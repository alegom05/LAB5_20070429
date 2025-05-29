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
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Medicamentos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        listaMedicamentos = new ArrayList<>();
        adapter = new MedicamentosAdapter(listaMedicamentos, this::onMedicamentoClick);
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
        Intent intent = new Intent(MedicamentosActivity.this, MedicamentosAdapter.class);
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