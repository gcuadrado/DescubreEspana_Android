package es.iesquevedo.descubreespana.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.FragmentHomeBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;
import io.vavr.control.Either;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATIOS_PERMISSION_REQUEST = 1;
    private HomeViewModel homeViewModel;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private FusedLocationProviderClient fusedLocationClient;
    private NavController navController;
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding=FragmentHomeBinding.inflate(inflater,container,false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (mMapFragment == null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mMapFragment);
            ft.commit();
        }
        mMapFragment.getMapAsync(this);
        serviciosPuntoInteres = new ServiciosPuntoInteres();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        binding.floatingAddPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GetSharedPreferences.getInstance().getCurrentUser(requireContext())!=null){
                    navController.navigate(R.id.nuevoPuntoFragment);
                }else{
                    navController.navigate(R.id.navigation_login);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATIOS_PERMISSION_REQUEST);
        } else {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                if (homeViewModel.getCameraPosition().getValue() == null) {
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
                                }
                            }
                        }
                    });
        }


        if (homeViewModel.getCameraPosition().getValue() != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(homeViewModel.getCameraPosition().getValue()));
        }

        new GetPois().execute();
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                navController.navigate(HomeFragmentDirections.actionNavigationHomeToDetallePoi((PuntoInteresDtoGetMaestro) marker.getTag()));
            }
        });
    }

    private class GetPois extends AsyncTask<Void, Void, Either<ApiError, List<PuntoInteresDtoGetMaestro>>> {

        @Override
        protected Either<ApiError, List<PuntoInteresDtoGetMaestro>> doInBackground(Void... voids) {
            return serviciosPuntoInteres.getAll();
        }

        @Override
        protected void onPostExecute(Either<ApiError, List<PuntoInteresDtoGetMaestro>> result) {
            if (result.isRight()) {
                List<PuntoInteresDtoGetMaestro> puntoInteresDtoGetMaestros = result.get();
                if (mMap != null && puntoInteresDtoGetMaestros != null) {
                    for (PuntoInteresDtoGetMaestro poi : puntoInteresDtoGetMaestros) {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(poi.getLatitud(), poi.getLongitud())).title(poi.getNombre()));
                        marker.setTag(poi);
                    }
                }
            } else {
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
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
        if (mMap != null) {
            homeViewModel.getCameraPosition().setValue(mMap.getCameraPosition());
        }
    }
}

