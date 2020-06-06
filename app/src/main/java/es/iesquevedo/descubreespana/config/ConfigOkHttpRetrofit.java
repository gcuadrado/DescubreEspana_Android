package es.iesquevedo.descubreespana.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigOkHttpRetrofit {
    private static ConfigOkHttpRetrofit instance = null;
    private OkHttpClient clientOK;
    private Retrofit retrofit;
    private final Gson gson;
    private String token;


    private ConfigOkHttpRetrofit() {


        gson = new GsonBuilder()
                .setLenient()
                .create();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);


        token="";
        clientOK = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Request.Builder builder1 = original.newBuilder()
                            .header("token", token)
                            .url(original.url());
                    Request request = builder1.build();
                    return chain.proceed(request);}
                )
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.173:8080/servidor/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientOK)
                .build();
    }

    public static ConfigOkHttpRetrofit getInstance() {
        if (instance == null){
            instance = new ConfigOkHttpRetrofit();

        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Gson getGson() {
        return gson;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
