package es.iesquevedo.descubreespana.dao;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import es.iesquevedo.descubreespana.retrofit.ServerDataApi;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import io.vavr.control.Either;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsuarioDao {
    private Retrofit retrofit;
    private Gson gson;

    public UsuarioDao() {
        retrofit= ConfigOkHttpRetrofit.getInstance().getRetrofit();
        gson=ConfigOkHttpRetrofit.getInstance().getGson();
    }


   public Either<ApiError, UsuarioDtoGet> registrarUsuario(UsuarioDtoPost usuario)  {
        Either<ApiError,UsuarioDtoGet> result;
       ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<UsuarioDtoGet> call = serverDataApi.registrar(usuario);
        try {
            Response<UsuarioDtoGet> response = call.execute();
            if (response.isSuccessful()) {
                result = Either.right(response.body());
            } else {
                result = Either.left(gson.fromJson(response.errorBody().string(), ApiError.class));
            }
        }catch (Exception e){
            result=Either.left(new ApiError(HttpURLConnection.HTTP_UNAVAILABLE,"Error de conexión"));
        }
        return result;
    }

    public Either<ApiError, UsuarioDtoGet> loginUsuario(UsuarioDtoPost usuario)  {
        Either<ApiError,UsuarioDtoGet> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<UsuarioDtoGet> call = serverDataApi.login(usuario);
        try {
            Response<UsuarioDtoGet> response = call.execute();
            if (response.isSuccessful()) {
                result = Either.right(response.body());
                ConfigOkHttpRetrofit.getInstance().setToken(response.headers().get("token"));
            } else {
                result = Either.left(gson.fromJson(response.errorBody().string(), ApiError.class));
            }
        }catch (Exception e){
            result=Either.left(new ApiError(HttpURLConnection.HTTP_UNAVAILABLE,"Error de conexión"));
        }
        return result;
    }

    public Either<ApiError,String> reestablecerPassword(String email) {
        Either<ApiError,String> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<String> call = serverDataApi.reestablecerPassword(email);
        try {
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                result = Either.right(response.body());
            } else {
                result = Either.left(gson.fromJson(response.errorBody().string(), ApiError.class));
            }
        }catch (Exception e){
            result=Either.left(new ApiError(HttpURLConnection.HTTP_UNAVAILABLE,"Error de conexión"));
        }
        return result;
    }
}
