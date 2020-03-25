package es.iesquevedo.descubreespana.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import me.relex.circleindicator.CircleIndicator3;

public class VPGaleriaAdapter extends RecyclerView.Adapter<VPGaleriaAdapter.PhotoViewHolder> {
   private PuntoInteresDtoGetDetalle poi;
   private List<FotoPuntoInteresDtoGet> fotos;

    public VPGaleriaAdapter(PuntoInteresDtoGetDetalle poi) {
        this.poi = poi;
        this.fotos=poi.getFotoPuntoInteresByIdPuntoInteres();

    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.galeria_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String url="http://192.168.1.101:8080"+fotos.get(position).getPath();
        Picasso.get()
                .load(url)
                .fit()
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
