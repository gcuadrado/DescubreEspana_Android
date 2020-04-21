package es.iesquevedo.descubreespana;

import android.os.AsyncTask;

import java.util.List;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class GetPoisCercanosTask extends AsyncTask<Void, Void, Either<ApiError, List<PuntoInteresDtoGetMaestro>>> {

    private ServiciosPuntoInteres serviciosPuntoInteres;
    private double latitud;
    private double longitud;

    public GetPoisCercanosTask(ServiciosPuntoInteres serviciosPuntoInteres, double latitud, double longitud) {
        this.serviciosPuntoInteres = serviciosPuntoInteres;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    protected Either<ApiError, List<PuntoInteresDtoGetMaestro>> doInBackground(Void... voids) {
        return serviciosPuntoInteres.getAllCercanos(latitud,longitud);
    }
}
