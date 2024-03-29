package es.iesquevedo.descubreespana.dao;

import android.util.Log;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.List;

import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.retrofit.ServerDataApi;
import io.vavr.control.Either;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FotosDao {
    private Retrofit retrofit;
    private Gson gson;

    public FotosDao() {
        retrofit= ConfigOkHttpRetrofit.getInstance().getRetrofit();
        gson=ConfigOkHttpRetrofit.getInstance().getGson();
    }

    public Either<ApiError, String> delete(int id) {
        Either<ApiError,String> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<String> call = serverDataApi.deleteFoto(id);
        try {
            Response<String> response = call.execute();
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

    public Either<ApiError, List<FotoPuntoInteresDtoGet>> insertFotos(List<MultipartBody.Part> multipartFromImages, PuntoInteresDtoGetDetalle poi) {
        Either<ApiError,List<FotoPuntoInteresDtoGet>> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<List<FotoPuntoInteresDtoGet>> call = serverDataApi.insertFotos(multipartFromImages,poi);
        try {
            Response<List<FotoPuntoInteresDtoGet>> response = call.execute();
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
}
