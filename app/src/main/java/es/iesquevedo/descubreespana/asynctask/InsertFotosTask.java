package es.iesquevedo.descubreespana.asynctask;
import android.os.AsyncTask;

import com.esafirm.imagepicker.model.Image;

import java.util.List;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosFotos;
import io.vavr.control.Either;

public class InsertFotosTask extends AsyncTask<Void,Void, Either<ApiError,List<FotoPuntoInteresDtoGet>>> {
    private ServiciosFotos serviciosFotos;
    private PuntoInteresDtoGetDetalle poi;
    private List<com.esafirm.imagepicker.model.Image> images;

    public InsertFotosTask(ServiciosFotos serviciosFotos, PuntoInteresDtoGetDetalle poi, List<Image> images) {
        this.serviciosFotos = serviciosFotos;
        this.poi = poi;
        this.images = images;
    }

    @Override
    protected Either<ApiError,List<FotoPuntoInteresDtoGet>> doInBackground(Void... voids) {
        return serviciosFotos.insertFotos(images,poi);
    }
}
