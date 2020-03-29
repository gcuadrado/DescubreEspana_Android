package es.iesquevedo.descubreespana.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;

public class GetSharedPreferences {
    private static GetSharedPreferences instance;

    private GetSharedPreferences() {
        gson=new Gson();
    }

    public static GetSharedPreferences getInstance() {
        if (instance == null) {
            instance = new GetSharedPreferences();
        }
        return instance;
    }

    private Gson gson;

    public UsuarioDtoGet getCurrentUser(Context context){
        return gson.fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(Constantes.USUARIO,null),UsuarioDtoGet.class);
    }

    public void setCurrentUser(UsuarioDtoGet usuarioDtoGet,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constantes.USUARIO,gson.toJson(usuarioDtoGet)).commit();
    }

    public void clearCurrentUser(Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(Constantes.USUARIO).commit();
    }
}