package es.iesquevedo.descubreespana.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import es.iesquevedo.descubreespana.servicios.ServiciosValoraciones;
import io.vavr.control.Either;

public class ValoracionAdapter extends RecyclerView.Adapter<ValoracionAdapter.ValoracionViewHolder> {
    private List<ValoracionDto> valoraciones;
    private Context context;
    private ServiciosValoraciones serviciosValoraciones;

    public ValoracionAdapter(List<ValoracionDto> valoraciones, Context context) {
        this.valoraciones = valoraciones;
        this.context=context;
        serviciosValoraciones=new ServiciosValoraciones();
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
        ValoracionDto valoracionDto=valoraciones.get(position);
        UsuarioDtoGet usuarioDtoGet= ConfigOkHttpRetrofit.getInstance().getGson()
                .fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.USUARIO,null),UsuarioDtoGet.class);
        holder.tvComentario.setText(valoracionDto.getComentario());
        holder.tvEmail.setText(valoracionDto.getUsuarioByIdUsuario().getEmail());
        holder.rbPuntuacion.setRating(valoracionDto.getPuntuacion());
        if(usuarioDtoGet!=null){
            if(usuarioDtoGet.getEmail().equals(valoracionDto.getUsuarioByIdUsuario().getEmail()) || usuarioDtoGet.getTipoUsuario()==Constantes.ADMIN){
                holder.btEliminar.setVisibility(View.VISIBLE);
            }
        }
        holder.btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BorrarValoracion(position).execute(valoracionDto.getIdValoracion());
            }
        });


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
        private Button btEliminar;


        ValoracionViewHolder(View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvComentario =itemView.findViewById(R.id.tvComentario);
            tvEmail=itemView.findViewById(R.id.tvEmail);
            rbPuntuacion=itemView.findViewById(R.id.rbPuntuacion);
            btEliminar=itemView.findViewById(R.id.btEliminar);
        }
    }

    private class BorrarValoracion extends AsyncTask<Integer,Void, Either<ApiError,String>>{
       private int position;
        public BorrarValoracion(int position){
            this.position=position;
        }
        @Override
        protected Either<ApiError, String> doInBackground(Integer... integers) {
            return serviciosValoraciones.borrarValoracion(integers[0]);
        }

        @Override
        protected void onPostExecute(Either<ApiError, String> result) {
            if(result.isRight()){
                valoraciones.remove(position);
                notifyDataSetChanged();
            }else{
                Toast.makeText(context,result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
