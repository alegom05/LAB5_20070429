package com.example.lab5;

public class Medicamento {
    private int id;
    private String nombre;
    private String tipo;
    private String dosis;
    private int frecuenciaHoras;
    private String fechaInicio;
    private String horaInicio;
    private boolean activo;

    public Medicamento() {}

    public Medicamento(String nombre, String tipo, String dosis, int frecuenciaHoras,
                       String fechaInicio, String horaInicio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dosis = dosis;
        this.frecuenciaHoras = frecuenciaHoras;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public int getFrecuenciaHoras() { return frecuenciaHoras; }
    public void setFrecuenciaHoras(int frecuenciaHoras) { this.frecuenciaHoras = frecuenciaHoras; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getFrecuenciaTexto() {
        if (frecuenciaHoras == 1) {
            return "Cada hora";
        } else if (frecuenciaHoras < 24) {
            return "Cada " + frecuenciaHoras + " horas";
        } else {
            int dias = frecuenciaHoras / 24;
            if (dias == 1) {
                return "Cada día";
            } else {
                return "Cada " + dias + " días";
            }
        }
    }

    public String getTipoYDosis() {
        return tipo + " - " + dosis;
    }
}