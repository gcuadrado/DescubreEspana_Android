package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import com.esafirm.imagepicker.model.Image;

import java.util.List;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class AddPoiTask extends AsyncTask<Void, Void, Either<ApiError, PuntoInteresDtoGetDetalle>> {
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private PuntoInteresDtoGetDetalle nuevoPoi;
    private List<Image> images;
    private Image imagenPrincipal;

    public AddPoiTask(ServiciosPuntoInteres serviciosPuntoInteres, PuntoInteresDtoGetDetalle nuevoPoi, List<Image> images, Image imagenPrincipal) {
        this.serviciosPuntoInteres = serviciosPuntoInteres;
        this.nuevoPoi = nuevoPoi;
        this.images = images;
        this.imagenPrincipal = imagenPrincipal;
    }

    @Override
    protected Either<ApiError, PuntoInteresDtoGetDetalle> doInBackground(Void... voids) {
        return serviciosPuntoInteres.addPoi(nuevoPoi,images,imagenPrincipal);
    }
}
