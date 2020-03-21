package es.iesquevedo.descubreespana.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigOkHttpRetrofitDigi {
    private static ConfigOkHttpRetrofitDigi instance = null;
    private OkHttpClient clientOK;
    private Retrofit retrofit;
    private final Gson gson;


    private ConfigOkHttpRetrofitDigi() {


        gson = new GsonBuilder()
                .setLenient()
                .create();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);



        clientOK = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080/servidor/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientOK)
                .build();

    }

    public static ConfigOkHttpRetrofitDigi getInstance() {
        if (instance == null){
            instance = new ConfigOkHttpRetrofitDigi();

        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public OkHttpClient getClientOK(){
        return clientOK;
    }

    public Gson getGson() {
        return gson;
    }
}
