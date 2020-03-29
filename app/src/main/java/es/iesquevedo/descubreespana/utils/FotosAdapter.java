package es.iesquevedo.descubreespana.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.model.Image;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.ui.nuevopunto.NuevoPuntoViewModel;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.FotoViewHolder> {
    private List<Image> images;
    private int selectedPosition=-1;
    private NuevoPuntoViewModel nuevoPuntoViewModel;

    public FotosAdapter(List<Image> images, NuevoPuntoViewModel nuevoPuntoViewModel) {
        this.images = images;
        this.nuevoPuntoViewModel=nuevoPuntoViewModel;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FotoViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.foto_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Bitmap myBitmap = BitmapFactory.decodeFile(images.get(position).getPath());
        holder.imageViewFoto.setImageBitmap(myBitmap);
        if(position==selectedPosition){
            holder.imageViewFoto.setBackgroundResource(R.drawable.highlight);
        }else{
            holder.imageViewFoto.setBackground(null);
        }
        holder.imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                nuevoPuntoViewModel.getmImagenPrincipal().setValue(images.get(position));
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
        return images.size();
    }

    public class FotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewFoto;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFoto=itemView.findViewById(R.id.imageViewFoto);
        }
    }
}
