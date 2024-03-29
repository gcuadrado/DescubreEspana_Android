package es.iesquevedo.descubreespana.dao;

import android.util.Log;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.List;

import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.retrofit.ServerDataApi;
import io.vavr.control.Either;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PuntoInteresDao {
    private Retrofit retrofit;
    private Gson gson;

    public PuntoInteresDao() {
        retrofit= ConfigOkHttpRetrofit.getInstance().getRetrofit();
        gson=ConfigOkHttpRetrofit.getInstance().getGson();
    }

    public Either<ApiError, List<PuntoInteresDtoGetMaestro>> getAll()  {
        Either<ApiError,List<PuntoInteresDtoGetMaestro>> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<List<PuntoInteresDtoGetMaestro>> call = serverDataApi.getAllPois();
        try {
            Response<List<PuntoInteresDtoGetMaestro>> response = call.execute();
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

    public Either<ApiError, List<PuntoInteresDtoGetMaestro>> getAllCercanos(double latitud, double longitud) {
        Either<ApiError,List<PuntoInteresDtoGetMaestro>> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<List<PuntoInteresDtoGetMaestro>> call = serverDataApi.getAllCercanos(latitud,longitud);
        try {
            Response<List<PuntoInteresDtoGetMaestro>> response = call.execute();
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

    public Either<ApiError, List<PuntoInteresDtoGetMaestro>> getAllSinActivar()  {
        Either<ApiError,List<PuntoInteresDtoGetMaestro>> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<List<PuntoInteresDtoGetMaestro>> call = serverDataApi.getAllPoisSinActivar();
        try {
            Response<List<PuntoInteresDtoGetMaestro>> response = call.execute();
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


    public Either<ApiError, PuntoInteresDtoGetDetalle> get(int id)  {
        Either<ApiError,PuntoInteresDtoGetDetalle> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<PuntoInteresDtoGetDetalle> call = serverDataApi.getPoi(id);
        try {
            Response<PuntoInteresDtoGetDetalle> response = call.execute();
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

    public Either<ApiError,PuntoInteresDtoGetDetalle> addPoi(PuntoInteresDtoGetDetalle partPuntoInteres, List<MultipartBody.Part> multipartFromImages, MultipartBody.Part partImagenPrincipal) {
        Either<ApiError,PuntoInteresDtoGetDetalle> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<PuntoInteresDtoGetDetalle> call = serverDataApi.addPunto(partImagenPrincipal,multipartFromImages,partPuntoInteres);
        try {
            Response<PuntoInteresDtoGetDetalle> response = call.execute();
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

    public Either<ApiError, String> aceptar(Integer id) {
        Either<ApiError,String> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<String> call = serverDataApi.activarPoi(id);
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

    public Either<ApiError, String> eliminar(Integer id) {
        Either<ApiError,String> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<String> call = serverDataApi.eliminarPoi(id);
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

    public Either<ApiError, String> updatePoi(PuntoInteresDtoGetDetalle puntoInteresDtoGetDetalle) {
        Either<ApiError,String> result;
        ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
        Call<String> call = serverDataApi.updatePoi(puntoInteresDtoGetDetalle);
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


}
