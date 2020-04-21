package es.iesquevedo.descubreespana.utils;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.ui.dashboard.ListadoFragmentDirections;

public class ListadoPoiAdapter extends RecyclerView.Adapter<ListadoPoiAdapter.ListadoPoiViewHolder> {
    private List<PuntoInteresDtoGetMaestro> puntos;
    private NavController navController;
    private Context mContext;
    private Location location;

    public ListadoPoiAdapter(List<PuntoInteresDtoGetMaestro> puntos, NavController navController, Location location) {
        this.puntos = puntos;
        this.navController=navController;
        this.location=location;
    }

    @NonNull
    @Override
    public ListadoPoiAdapter.ListadoPoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        ListadoPoiViewHolder view = new ListadoPoiViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.lista_poi_row, parent, false));
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoPoiAdapter.ListadoPoiViewHolder holder, int position) {
        PuntoInteresDtoGetMaestro poi=puntos.get(position);
        holder.tvNombre.setText(poi.getNombre());
        Glide.with(mContext).load(ConfigOkHttpRetrofit.getInstance().getRetrofit().baseUrl()+poi.getPathImagenPrincipal())
                .into(holder.ivFoto);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(ListadoFragmentDirections.actionNavigationDashboardToDetallePoi(poi));
            }
        });

        Location poiLocation = new Location("poiLocation");
        poiLocation.setLatitude(poi.getLatitud());
        poiLocation.setLongitude(poi.getLongitud());
        double distancia = location.distanceTo(poiLocation);
        DecimalFormat df2 = new DecimalFormat("#.##");
        holder.tvDistancia.setText(df2.format(distancia/1000)+" km");





    }

    @Override
    public int getItemCount() {
        return puntos.size();
    }

    public class ListadoPoiViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFoto;
        private TextView tvNombre;
        private TextView tvDistancia;
        private CardView cardView;


        ListadoPoiViewHolder(View itemView) {
            super(itemView);
            ivFoto=itemView.findViewById(R.id.imageViewListado);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            cardView=itemView.findViewById(R.id.cardViewPoi);
            tvDistancia=itemView.findViewById(R.id.tvDistancia);
        }
    }
}
