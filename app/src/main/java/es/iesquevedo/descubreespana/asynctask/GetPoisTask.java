package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import java.util.List;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class GetPoisTask extends AsyncTask<Void, Void, Either<ApiError, List<PuntoInteresDtoGetMaestro>>> {

    private ServiciosPuntoInteres serviciosPuntoInteres;

    public GetPoisTask(ServiciosPuntoInteres serviciosPuntoInteres) {
        this.serviciosPuntoInteres = serviciosPuntoInteres;
    }



    @Override
    protected Either<ApiError, List<PuntoInteresDtoGetMaestro>> doInBackground(Void... voids) {
        return serviciosPuntoInteres.getAll();
    }

}
