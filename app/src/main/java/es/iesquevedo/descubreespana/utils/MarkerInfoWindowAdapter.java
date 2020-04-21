package es.iesquevedo.descubreespana.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private double distancia;
    private Marker lastMarker;

    public MarkerInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.infowindow_markers, null);
        PuntoInteresDtoGetMaestro poi = (PuntoInteresDtoGetMaestro) marker.getTag();
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView tvnombre = view.findViewById(R.id.tvNombre);
        TextView tvDistancia = view.findViewById(R.id.tvDistancia);
        DecimalFormat df2 = new DecimalFormat("#.##");
        if(lastMarker==null || !lastMarker.equals(marker)) {
            lastMarker=marker;
            LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Location poiLocation = new Location("poiLocation");
                    poiLocation.setLatitude(poi.getLatitud());
                    poiLocation.setLongitude(poi.getLongitud());
                    distancia = location.distanceTo(poiLocation);
                    tvDistancia.setText(df2.format(distancia/1000)+" km");
                    marker.showInfoWindow();
                }
            });
        }else{
            tvDistancia.setText(df2.format(distancia/1000)+" km");
        }
        tvnombre.setText(poi.getNombre());
        Glide.with(context).load(ConfigOkHttpRetrofit.getInstance().getRetrofit().baseUrl() + poi.getPathImagenPrincipal())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //Necesario recargar el InfoWindow porque la imagen se obtiene de forma asíncrona.
                        //Si la imagen viene de la cache, no se recarga.
                        if (!dataSource.equals(DataSource.MEMORY_CACHE)) {
                            marker.showInfoWindow();
                        }
                        return false;
                    }
                })
                .into(imageView);
        return view;
    }
}
