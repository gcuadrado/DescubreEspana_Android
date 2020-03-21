package es.iesquevedo.descubreespana.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.servicios.ServiciosPuntoInteres;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private ServiciosPuntoInteres serviciosPuntoInteres;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mMapFragment =(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        if(mMapFragment ==null){
            FragmentManager fm=getChildFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            mMapFragment =SupportMapFragment.newInstance();
            ft.replace(R.id.map, mMapFragment);
            ft.commit();
        }
        mMapFragment.getMapAsync(this);
        serviciosPuntoInteres=new ServiciosPuntoInteres();
       /* homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        new GetPois().execute();
    }

    private class GetPois extends AsyncTask<Void,Void, List<PuntoInteresDtoGetMaestro>>{

        @Override
        protected List<PuntoInteresDtoGetMaestro> doInBackground(Void... voids) {
            return serviciosPuntoInteres.getAll();
        }

        @Override
        protected void onPostExecute(List<PuntoInteresDtoGetMaestro> puntoInteresDtoGetMaestros) {
            if(mMap!=null && puntoInteresDtoGetMaestros!=null){
                for (PuntoInteresDtoGetMaestro poi: puntoInteresDtoGetMaestros){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(poi.getLatitud(),poi.getLongitud())).title(poi.getNombre()));
                }
            }
        }
    }

}

