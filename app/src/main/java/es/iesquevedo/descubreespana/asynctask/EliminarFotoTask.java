package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.servicios.ServiciosFotos;
import io.vavr.control.Either;

public class EliminarFotoTask extends AsyncTask<Integer,Void, Either<ApiError,String>> {
    private ServiciosFotos serviciosFotos;

    public EliminarFotoTask(ServiciosFotos serviciosFotos) {
        this.serviciosFotos = serviciosFotos;
    }

    @Override
    protected Either<ApiError, String> doInBackground(Integer... integers) {
        return serviciosFotos.deleteFoto(integers[0]);
    }
}
