package es.iesquevedo.dao;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import es.iesquevedo.config.ConfigOkHttpRetrofitDigi;
import es.iesquevedo.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.retrofit.ServerDataApi;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PuntoInteresDao {
    private Retrofit retrofit;
    private Gson gson;

    public PuntoInteresDao() {
        retrofit= ConfigOkHttpRetrofitDigi.getInstance().getRetrofit();
        gson=ConfigOkHttpRetrofitDigi.getInstance().getGson();
    }

    public List<PuntoInteresDtoGetMaestro> getAll(){
        List<PuntoInteresDtoGetMaestro> pois=null;
        try {
            ServerDataApi serverDataApi = retrofit.create(ServerDataApi.class);
            Call<List<PuntoInteresDtoGetMaestro>> call = serverDataApi.getAllPois();
            Response<List<PuntoInteresDtoGetMaestro>> response = call.execute();
            if (response.isSuccessful()) {
                pois=response.body();
            }
        }catch (Exception e){
            Log.d("descubreespana",null,e);
        }
        return pois;
    }
}
