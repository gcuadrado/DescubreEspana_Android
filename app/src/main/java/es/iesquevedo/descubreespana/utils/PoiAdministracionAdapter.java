package es.iesquevedo.descubreespana.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;

public class PoiAdministracionAdapter extends RecyclerView.Adapter<PoiAdministracionAdapter.PoiAdminViewHolder> {

    private List<PuntoInteresDtoGetMaestro> puntos;

    public PoiAdministracionAdapter(List<PuntoInteresDtoGetMaestro> puntos) {
        this.puntos = puntos;
    }

    @NonNull
    @Override
    public PoiAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoiAdminViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.poi_admin_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAdminViewHolder holder, int position) {
            holder.tvNombre.setText(puntos.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return puntos.size();
    }

    public class PoiAdminViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFoto;
        private TextView tvNombre;


        PoiAdminViewHolder(View itemView) {
            super(itemView);
            ivFoto=itemView.findViewById(R.id.ivFoto);
            tvNombre=itemView.findViewById(R.id.tvNombre);
        }
    }
}
