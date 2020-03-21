package es.iesquevedo.descubreespana.retrofit;

import java.util.List;

import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerDataApi {
   /* @POST("login")
    Call<LoginResponse> login(@Body DigiRequestBody digiRequestBody);*/

   @GET("api/puntos")
    Call<List<PuntoInteresDtoGetMaestro>> getAllPois();

   @POST("api/usuario/registro")
    Call<UserKeystore> registrar(@Body UsuarioDtoPost usuarioDtoPost);
}
