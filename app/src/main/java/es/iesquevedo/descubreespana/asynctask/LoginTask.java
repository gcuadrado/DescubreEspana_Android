package es.iesquevedo.descubreespana.asynctask;

import android.os.AsyncTask;

import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import io.vavr.control.Either;

public class LoginTask extends AsyncTask<String,Void, Either<ApiError, UsuarioDtoGet>> {
    private ServiciosUsuario serviciosUsuario;
    private String email;
    private String password;

    public LoginTask(ServiciosUsuario serviciosUsuario, String email, String password) {
        this.serviciosUsuario = serviciosUsuario;
        this.email = email;
        this.password = password;
    }

    @Override
    protected Either<ApiError, UsuarioDtoGet> doInBackground(String... strings) {
        return serviciosUsuario.loginUsuario(email,password);
    }
}
