package es.iesquevedo.descubreespana.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.AceptarPoiTask;
import es.iesquevedo.descubreespana.asynctask.EliminarPoiTask;
import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.ui.administracion.AdministracionFragmentDirections;
import io.vavr.control.Either;

public class PoiAdministracionAdapter extends RecyclerView.Adapter<PoiAdministracionAdapter.PoiAdminViewHolder> {

    private List<PuntoInteresDtoGetMaestro> puntos;
    private NavController navController;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private Context mContext;

    public PoiAdministracionAdapter(List<PuntoInteresDtoGetMaestro> puntos, NavController navController) {
        this.puntos = puntos;
        this.navController=navController;
        serviciosPuntoInteres=new ServiciosPuntoInteres();
    }

    @NonNull
    @Override
    public PoiAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        return new PoiAdminViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.poi_admin_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAdminViewHolder holder, int position) {
            holder.tvNombre.setText(puntos.get(position).getNombre());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   navController.navigate(AdministracionFragmentDirections.actionAdministracionFragmentToDetallePoi(puntos.get(position)));
                }
            });
            holder.btAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AceptarPoiTask(serviciosPuntoInteres){
                        @Override
                        protected void onPostExecute(Either<ApiError, String> result) {
                            if(result.isRight()){
                                puntos.remove(puntos.get(position));
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(mContext,result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute(puntos.get(position).getIdPuntoInteres());

                }
            });
        String url = ConfigOkHttpRetrofit.getInstance().getRetrofit().baseUrl().toString() + puntos.get(position).getPathImagenPrincipal();
        Glide.with(mContext)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.ivFoto);

        holder.btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EliminarPoiTask(serviciosPuntoInteres){
                    @Override
                    protected void onPostExecute(Either<ApiError, String> result) {
                        if(result.isRight()){
                            puntos.remove(puntos.get(position));
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(mContext,result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(puntos.get(position).getIdPuntoInteres());

            }
        });



    }

    @Override
    public int getItemCount() {
        return puntos.size();
    }

    public class PoiAdminViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFoto;
        private TextView tvNombre;
        private CardView cardView;
        private Button btAceptar;
        private Button btEliminar;



        PoiAdminViewHolder(View itemView) {
            super(itemView);
            ivFoto=itemView.findViewById(R.id.ivFoto);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            cardView=itemView.findViewById(R.id.cardViewPoi);
            btAceptar=itemView.findViewById(R.id.btAceptar);
            btEliminar=itemView.findViewById(R.id.btEliminar);
        }
    }

}
