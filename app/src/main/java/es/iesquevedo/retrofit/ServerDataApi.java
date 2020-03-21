package es.iesquevedo.retrofit;

import java.util.List;

import es.iesquevedo.modelo.dto.PuntoInteresDtoGetMaestro;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerDataApi {
   /* @POST("login")
    Call<LoginResponse> login(@Body DigiRequestBody digiRequestBody);*/

   @GET("api/puntos")
    Call<List<PuntoInteresDtoGetMaestro>> getAllPois();
}
