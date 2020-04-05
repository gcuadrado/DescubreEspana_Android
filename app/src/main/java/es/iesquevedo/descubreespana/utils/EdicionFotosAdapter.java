package es.iesquevedo.descubreespana.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.EliminarFotoTask;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosFotos;
import io.vavr.control.Either;

public class EdicionFotosAdapter extends RecyclerView.Adapter<EdicionFotosAdapter.PhotoViewHolder> {
    private PuntoInteresDtoGetDetalle poi;
    private List<FotoPuntoInteresDtoGet> fotos;
    private Context mContext;
    private ServiciosFotos serviciosFotos;

    public EdicionFotosAdapter(PuntoInteresDtoGetDetalle poi) {
        this.poi = poi;
        this.fotos = poi.getFotoPuntoInteresByIdPuntoInteres();
        this.serviciosFotos = new ServiciosFotos();
    }

    @NonNull
    @Override
    public EdicionFotosAdapter.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new EdicionFotosAdapter.PhotoViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.edicion_foto_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EdicionFotosAdapter.PhotoViewHolder holder, int position) {
        String url = "http://192.168.1.101:8080" + fotos.get(position).getPath();
        Glide.with(mContext).load(url)
                .fitCenter()
                .centerCrop()
                .into(holder.imageView);

        holder.btBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EliminarFotoTask(serviciosFotos){
                    @Override
                    protected void onPostExecute(Either<ApiError, String> result) {
                        if(result.isRight()){
                            fotos.remove(fotos.get(position));
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(mContext,result.getLeft().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(fotos.get(position).getIdFoto());
            }
        });
    }


    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton btBorrar;

        PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPoi);
            btBorrar = itemView.findViewById(R.id.btBorrar);
        }
    }
}
