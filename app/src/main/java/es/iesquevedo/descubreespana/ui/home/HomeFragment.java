package es.iesquevedo.descubreespana.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATIOS_PERMISSION_REQUEST = 1;
    private HomeViewModel homeViewModel;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private FusedLocationProviderClient fusedLocationClient;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mMapFragment =(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
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

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATIOS_PERMISSION_REQUEST);
        }else{
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                               mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));
                            }
                        }
                    });
        }


        if(homeViewModel.getCameraPosition().getValue()!=null){
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(homeViewModel.getCameraPosition().getValue()));
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATIOS_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mMap!=null){
            homeViewModel.getCameraPosition().setValue(mMap.getCameraPosition());
        }
    }
}

