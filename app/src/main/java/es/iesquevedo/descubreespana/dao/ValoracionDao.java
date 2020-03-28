package es.iesquevedo.descubreespana.dao;

import android.util.Log;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.List;

import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import es.iesquevedo.descubreespana.retrofit.ServerDataApi;
import io.vavr.control.Either;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ValoracionDao {
    private Retrofit retrofit;
    private Gson gson;

    public ValoracionDao() {
        this.retrofit = ConfigOkHttpRetrofit.getInstance().getRetrofit();
        this.gson=ConfigOkHttpRetrofit.getInstance().getGson();
    }

    public Either<ApiError, List<ValoracionDto>> getAll(int id)  {
        Either<ApiError,List<ValoracionDto>> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        try {
            Call<List<ValoracionDto>> call = serverDataApi.getAllValoraciones(id);
            Response<List<ValoracionDto>> response = call.execute();
            if (response.isSuccessful()) {
                result = Either.right(response.body());
            } else {
                result = Either.left(gson.fromJson(response.errorBody().string(), ApiError.class));
            }
        }catch (Exception e){
            Log.d("descubreespana",null,e);
            result=Either.left(new ApiError(HttpURLConnection.HTTP_UNAVAILABLE,"Error de conexión"));
        }
        return result;
    }

    public Either<ApiError, ValoracionDto> addValoracion(ValoracionDto valoracionDto)  {
        Either<ApiError,ValoracionDto> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<ValoracionDto> call = serverDataApi.addValoracion(valoracionDto);
        try {
            Response<ValoracionDto> response = call.execute();
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
