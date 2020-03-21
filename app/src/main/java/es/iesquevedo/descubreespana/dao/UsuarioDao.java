package es.iesquevedo.descubreespana.dao;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofitDigi;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import es.iesquevedo.descubreespana.retrofit.ServerDataApi;
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

    public UserKeystore registrarUsuario(UsuarioDtoPost usuarioDtoPost){
        UserKeystore userKeystore=null;
        try {
            ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
            Call<UserKeystore> call = serverDataApi.registrar(usuarioDtoPost);
            Response<UserKeystore> response = call.execute();
            if (response.isSuccessful()) {
                userKeystore=response.body();
            }
        }catch (Exception e){
            Log.d("descubreespana",null,e);
        }
        return userKeystore;
    }
}
