package es.iesquevedo.descubreespana.dao;

import com.google.gson.Gson;

import java.net.HttpURLConnection;

import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofitDigi;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import es.iesquevedo.descubreespana.retrofit.ServerDataApi;
import io.vavr.control.Either;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsuarioDao {
    private Retrofit retrofit;
    private Gson gson;

    public UsuarioDao() {
        retrofit= ConfigOkHttpRetrofitDigi.getInstance().getRetrofit();
        gson=ConfigOkHttpRetrofitDigi.getInstance().getGson();
    }


   public Either<ApiError, UserKeystore> registrarUsuario(UsuarioDtoPost usuario)  {
        Either<ApiError,UserKeystore> result;
       ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<UserKeystore> call = serverDataApi.registrar(usuario);
        try {
            Response<UserKeystore> response = call.execute();
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
            } else {
                result = Either.left(gson.fromJson(response.errorBody().string(), ApiError.class));
            }
        }catch (Exception e){
            result=Either.left(new ApiError(HttpURLConnection.HTTP_UNAVAILABLE,"Error de conexión"));
        }
        return result;
    }
}
