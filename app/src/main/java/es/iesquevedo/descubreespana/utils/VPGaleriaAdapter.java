package es.iesquevedo.descubreespana.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;

public class VPGaleriaAdapter extends RecyclerView.Adapter<VPGaleriaAdapter.PhotoViewHolder> {
    private PuntoInteresDtoGetDetalle poi;
    private List<FotoPuntoInteresDtoGet> fotos;
    private Context mContext;

    public VPGaleriaAdapter(PuntoInteresDtoGetDetalle poi) {
        this.poi = poi;
        this.fotos = poi.getFotoPuntoInteresByIdPuntoInteres();

    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new PhotoViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.galeria_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String url = "http://192.168.1.101:8080" + fotos.get(position).getPath();
            Glide.with(mContext).load(url)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;


        PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.poi_imageView);
        }
    }
}
