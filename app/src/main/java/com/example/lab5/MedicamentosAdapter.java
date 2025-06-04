package com.example.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MedicamentoViewHolder> {

    private Context context;
    private List<Medicamento> listaMedicamentos;
    private OnMedicamentoClickListener listener;

    public MedicamentosAdapter(List<Medicamento> listaMedicamentos, OnMedicamentoClickListener listener) {
        this.listaMedicamentos = listaMedicamentos;
        this.listener = listener;
    }

    // Interface para manejar clicks en los items
    public interface OnMedicamentoClickListener {
        void onMedicamentoClick(Medicamento medicamento, int position);
        void onMedicamentoLongClick(Medicamento medicamento, int position);

        void onClick(Medicamento medicamento);

    }

    public MedicamentosAdapter(Context context, List<Medicamento> listaMedicamentos) {
        this.context = context;
        this.listaMedicamentos = listaMedicamentos;
    }

    public void setOnMedicamentoClickListener(OnMedicamentoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicamento, parent, false);
        return new MedicamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoViewHolder holder, int position) {
        Medicamento medicamento = listaMedicamentos.get(position);
        holder.bind(medicamento);
    }

    @Override
    public int getItemCount() {
        return listaMedicamentos != null ? listaMedicamentos.size() : 0;
    }

    // Método para actualizar la lista
    public void actualizarLista(List<Medicamento> nuevaLista) {
        this.listaMedicamentos = nuevaLista;
        notifyDataSetChanged();
    }

    // Método para agregar un medicamento
    public void agregarMedicamento(Medicamento medicamento) {
        listaMedicamentos.add(medicamento);
        notifyItemInserted(listaMedicamentos.size() - 1);
    }

    // Método para eliminar un medicamento
    public void eliminarMedicamento(int position) {
        if (position >= 0 && position < listaMedicamentos.size()) {
            listaMedicamentos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class MedicamentoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombreMedicamento;
        private TextView tvTipoYDosis;
        private TextView tvFrecuencia;
        private TextView tvFechaHoraInicio;

        public MedicamentoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombreMedicamento = itemView.findViewById(R.id.tv_nombre_medicamento);
            tvTipoYDosis = itemView.findViewById(R.id.tv_tipo_dosis);
            tvFrecuencia = itemView.findViewById(R.id.tv_frecuencia);

            // Configurar listeners para clicks
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onMedicamentoClick(listaMedicamentos.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onMedicamentoLongClick(listaMedicamentos.get(getAdapterPosition()), getAdapterPosition());
                    return true;
                }
                return false;
            });
        }

        public void bind(Medicamento medicamento) {
            tvNombreMedicamento.setText(medicamento.getNombre());

            // Formato: "Tipo - Dosis"
            String tipoYDosis = medicamento.getTipo() + " - " + medicamento.getDosis();
            tvTipoYDosis.setText(tipoYDosis);

            // Formato: "Cada X horas"
            String frecuencia = "Cada " + medicamento.getFrecuenciaHoras() + " horas";
            tvFrecuencia.setText(frecuencia);

            // Formato de fecha y hora
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fechaHora = sdf.format(medicamento.getFechaInicio());
            tvFechaHoraInicio.setText("Inicio: " + fechaHora);
        }
    }
}