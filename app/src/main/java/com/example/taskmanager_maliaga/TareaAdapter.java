package com.example.taskmanager_maliaga;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> listaTareas;

    public TareaAdapter(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos el diseño con letras negras que creamos
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);

        // 1. Poner los textos
        if (tarea.nombreTarea != null) holder.textTitulo.setText(tarea.nombreTarea);
        else holder.textTitulo.setText("Sin Título");

        if (tarea.descripcion != null) holder.textDescripcion.setText(tarea.descripcion);
        else holder.textDescripcion.setText("");

        // 2. --- ESTO ES LO QUE FALTABA: DETECTAR EL CLICK ---
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el "Contexto" desde la vista para poder abrir otra pantalla
                Intent intent = new Intent(v.getContext(), activity_detalle.class);

                // Le enviamos el ID de esta tarea específica a la pantalla de detalle
                intent.putExtra("ID_TAREA", tarea.idTarea);

                // Iniciamos la actividad
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textDescripcion;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.tituloTarea);
            textDescripcion = itemView.findViewById(R.id.descripcionTarea);
        }
    }
}