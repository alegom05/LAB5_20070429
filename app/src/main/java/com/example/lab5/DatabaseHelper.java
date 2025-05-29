package com.example.lab5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lab5.Medicamento;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicamentos.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla medicamentos
    private static final String TABLE_MEDICAMENTOS = "medicamentos";
    private static final String COL_ID = "id";
    private static final String COL_NOMBRE = "nombre";
    private static final String COL_TIPO = "tipo";
    private static final String COL_DOSIS = "dosis";
    private static final String COL_FRECUENCIA = "frecuencia_horas";
    private static final String COL_FECHA_INICIO = "fecha_inicio";
    private static final String COL_HORA_INICIO = "hora_inicio";
    private static final String COL_ACTIVO = "activo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MEDICAMENTOS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE + " TEXT NOT NULL, " +
                COL_TIPO + " TEXT NOT NULL, " +
                COL_DOSIS + " TEXT NOT NULL, " +
                COL_FRECUENCIA + " INTEGER NOT NULL, " +
                COL_FECHA_INICIO + " TEXT NOT NULL, " +
                COL_HORA_INICIO + " TEXT NOT NULL, " +
                COL_ACTIVO + " INTEGER DEFAULT 1)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAMENTOS);
        onCreate(db);
    }

    // Insertar medicamento
    public long insertMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NOMBRE, medicamento.getNombre());
        values.put(COL_TIPO, medicamento.getTipo());
        values.put(COL_DOSIS, medicamento.getDosis());
        values.put(COL_FRECUENCIA, medicamento.getFrecuenciaHoras());
        values.put(COL_FECHA_INICIO, medicamento.getFechaInicio());
        values.put(COL_HORA_INICIO, medicamento.getHoraInicio());
        values.put(COL_ACTIVO, medicamento.isActivo() ? 1 : 0);

        long id = db.insert(TABLE_MEDICAMENTOS, null, values);
        db.close();
        return id;
    }

    // Obtener todos los medicamentos
    public List<Medicamento> getAllMedicamentos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MEDICAMENTOS + " ORDER BY " + COL_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Medicamento medicamento = new Medicamento();
                medicamento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                medicamento.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
                medicamento.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO)));
                medicamento.setDosis(cursor.getString(cursor.getColumnIndexOrThrow(COL_DOSIS)));
                medicamento.setFrecuenciaHoras(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FRECUENCIA)));
                medicamento.setFechaInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_INICIO)));
                medicamento.setHoraInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_HORA_INICIO)));
                medicamento.setActivo(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ACTIVO)) == 1);

                medicamentos.add(medicamento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicamentos;
    }

    // Obtener medicamento por ID
    public Medicamento getMedicamentoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MEDICAMENTOS + " WHERE " + COL_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        Medicamento medicamento = null;
        if (cursor.moveToFirst()) {
            medicamento = new Medicamento();
            medicamento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            medicamento.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
            medicamento.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO)));
            medicamento.setDosis(cursor.getString(cursor.getColumnIndexOrThrow(COL_DOSIS)));
            medicamento.setFrecuenciaHoras(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FRECUENCIA)));
            medicamento.setFechaInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_INICIO)));
            medicamento.setHoraInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_HORA_INICIO)));
            medicamento.setActivo(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ACTIVO)) == 1);
        }

        cursor.close();
        db.close();
        return medicamento;
    }

    // Actualizar medicamento
    public int updateMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NOMBRE, medicamento.getNombre());
        values.put(COL_TIPO, medicamento.getTipo());
        values.put(COL_DOSIS, medicamento.getDosis());
        values.put(COL_FRECUENCIA, medicamento.getFrecuenciaHoras());
        values.put(COL_FECHA_INICIO, medicamento.getFechaInicio());
        values.put(COL_HORA_INICIO, medicamento.getHoraInicio());
        values.put(COL_ACTIVO, medicamento.isActivo() ? 1 : 0);

        int rowsAffected = db.update(TABLE_MEDICAMENTOS, values, COL_ID + " = ?",
                new String[]{String.valueOf(medicamento.getId())});
        db.close();
        return rowsAffected;
    }

    // Eliminar medicamento
    public void deleteMedicamento(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICAMENTOS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Obtener medicamentos activos
    public List<Medicamento> getMedicamentosActivos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MEDICAMENTOS + " WHERE " + COL_ACTIVO + " = 1 ORDER BY " + COL_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Medicamento medicamento = new Medicamento();
                medicamento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                medicamento.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
                medicamento.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO)));
                medicamento.setDosis(cursor.getString(cursor.getColumnIndexOrThrow(COL_DOSIS)));
                medicamento.setFrecuenciaHoras(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FRECUENCIA)));
                medicamento.setFechaInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_INICIO)));
                medicamento.setHoraInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_HORA_INICIO)));
                medicamento.setActivo(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ACTIVO)) == 1);

                medicamentos.add(medicamento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicamentos;
    }
}