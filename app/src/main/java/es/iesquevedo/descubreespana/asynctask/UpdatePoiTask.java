package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class UpdatePoiTask extends AsyncTask<PuntoInteresDtoGetDetalle,Void, Either<ApiError,String>> {
    private ServiciosPuntoInteres serviciosPuntoInteres;

    public UpdatePoiTask(ServiciosPuntoInteres serviciosPuntoInteres) {
        this.serviciosPuntoInteres = serviciosPuntoInteres;
    }

    @Override
    protected Either<ApiError, String> doInBackground(PuntoInteresDtoGetDetalle... puntoInteresDtoGetDetalles) {
        return serviciosPuntoInteres.updatePoi(puntoInteresDtoGetDetalles[0]);
    }
}
