package es.iesquevedo.descubreespana.retrofit;

import java.util.List;

import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerDataApi {
   /* @POST("login")
    Call<LoginResponse> login(@Body DigiRequestBody digiRequestBody);*/

    @GET("api/puntos")
    Call<List<PuntoInteresDtoGetMaestro>> getAllPois();

    @Multipart
    @POST("api/puntos")
    Call<PuntoInteresDtoGetDetalle> addPunto(
            @Part MultipartBody.Part partImagenPrincipal,
            @Part List<MultipartBody.Part> files,
            @Part("data") PuntoInteresDtoGetDetalle data);

    @POST("api/usuario/registro")
    Call<UserKeystore> registrar(@Body UsuarioDtoPost usuarioDtoPost);

    @POST("api/usuario/login")
    Call<UsuarioDtoGet> login(@Body UsuarioDtoPost usuario);

    @GET("reestablecer")
    Call<String> reestablecerPassword(@Query("email") String email);

    @GET("api/puntos/{id}")
    Call<PuntoInteresDtoGetDetalle> getPoi(@Path("id") int id);

    @GET("api/valoraciones")
    Call<List<ValoracionDto>> getAllValoraciones(@Query("poi_id") int id);

    @POST("api/valoraciones")
    Call<ValoracionDto> addValoracion(@Body ValoracionDto valoracionDto);

    @DELETE("api/valoraciones")
    Call<String> borrarValoracion(@Query("id") int id);

    @GET("api/puntos/administrar")
    Call<List<PuntoInteresDtoGetMaestro>> getAllPoisSinActivar();

    @PUT("api/puntos/administrar/{id}")
    Call<String> activarPoi(@Path("id") int id);

    @DELETE("api/puntos/administrar/{id}")
    Call<String> eliminarPoi(@Path("id") int id);

    @PUT("api/puntos/administrar")
    Call<String> updatePoi(@Body PuntoInteresDtoGetDetalle puntoInteresDtoGetDetalle);
}
