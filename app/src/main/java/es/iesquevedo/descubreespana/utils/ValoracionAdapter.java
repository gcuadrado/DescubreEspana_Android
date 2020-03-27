package es.iesquevedo.descubreespana.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;

public class ValoracionAdapter extends RecyclerView.Adapter<ValoracionAdapter.ValoracionViewHolder> {
    private List<ValoracionDto> valoraciones;

    public ValoracionAdapter(List<ValoracionDto> valoraciones) {
        this.valoraciones = valoraciones;
    }


    @NonNull
    @Override
    public ValoracionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ValoracionViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.valoracion_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ValoracionViewHolder holder, int position) {
        holder.tvComentario.setText(valoraciones.get(position).getComentario());
        holder.tvEmail.setText(valoraciones.get(position).getUsuarioByIdUsuario().getEmail());
        holder.rbPuntuacion.setRating(valoraciones.get(position).getPuntuacion());
    }



    @Override
    public int getItemCount() {
        return valoraciones.size();
    }

    public class ValoracionViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivUserImage;
        private TextView tvComentario;
        private TextView tvEmail;
        private RatingBar rbPuntuacion;


        ValoracionViewHolder(View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvComentario =itemView.findViewById(R.id.tvComentario);
            tvEmail=itemView.findViewById(R.id.tvEmail);
            rbPuntuacion=itemView.findViewById(R.id.rbPuntuacion);
        }
    }
}
