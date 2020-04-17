package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import es.iesquevedo.descubreespana.servicios.ServiciosValoraciones;
import io.vavr.control.Either;

public class AddValoracionTask extends AsyncTask<Void,Void, Either<ApiError, ValoracionDto>> {
    private ServiciosValoraciones serviciosValoraciones;
    private int puntuacion;
    private String comentario;
    private int idPoi;

    public AddValoracionTask(ServiciosValoraciones serviciosValoraciones, int puntuacion, String comentario, int idPoi) {
        this.serviciosValoraciones = serviciosValoraciones;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.idPoi = idPoi;
    }

    @Override
    protected Either<ApiError, ValoracionDto> doInBackground(Void... voids) {
        return serviciosValoraciones.addValoracion(puntuacion,comentario,idPoi);
    }
}
