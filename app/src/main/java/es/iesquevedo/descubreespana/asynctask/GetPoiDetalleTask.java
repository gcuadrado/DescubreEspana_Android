package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class GetPoiDetalleTask extends AsyncTask<Integer, Void, Either<ApiError, PuntoInteresDtoGetDetalle>> {
    private ServiciosPuntoInteres serviciosPuntoInteres;

    public GetPoiDetalleTask(ServiciosPuntoInteres serviciosPuntoInteres) {
        this.serviciosPuntoInteres = serviciosPuntoInteres;
    }

    @Override
    protected Either<ApiError, PuntoInteresDtoGetDetalle> doInBackground(Integer... integers) {
        return serviciosPuntoInteres.get(integers[0]);
    }
}
