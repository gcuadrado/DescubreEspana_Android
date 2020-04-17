package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import io.vavr.control.Either;

public class ResetPasswordTask extends AsyncTask<Void,Void, Either<ApiError,String>> {
    private String email;
    private ServiciosUsuario serviciosUsuario;

    public ResetPasswordTask(ServiciosUsuario serviciosUsuario, String email) {
        this.email = email;
        this.serviciosUsuario=serviciosUsuario;
    }

    @Override
    protected Either<ApiError, String> doInBackground(Void...voids) {
        return serviciosUsuario.reestablecerPassword(email);
    }
}
