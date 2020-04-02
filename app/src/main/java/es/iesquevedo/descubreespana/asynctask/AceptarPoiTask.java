package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class AceptarPoiTask extends AsyncTask<Integer,Void, Either<ApiError,String>> {

    private ServiciosPuntoInteres serviciosPuntoInteres;

    public AceptarPoiTask(ServiciosPuntoInteres serviciosPuntoInteres) {
        this.serviciosPuntoInteres = serviciosPuntoInteres;
    }

    @Override
    protected Either<ApiError, String> doInBackground(Integer... integers) {
        return serviciosPuntoInteres.aceptarPoi(integers[0]);
    }
}
