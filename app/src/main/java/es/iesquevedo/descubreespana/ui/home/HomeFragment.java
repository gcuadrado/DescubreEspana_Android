package es.iesquevedo.descubreespana.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.schibstedspain.leku.LocationPickerActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.GetPoisTask;
import es.iesquevedo.descubreespana.databinding.FragmentHomeBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;
import io.vavr.control.Either;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATIOS_PERMISSION_REQUEST = 1;
    private HomeViewModel homeViewModel;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private FusedLocationProviderClient fusedLocationClient;
    private NavController navController;
    private FragmentHomeBinding binding;
    private GetPoisTask getPoisTask;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Seleccionar el item en la barra de navigación inferior cada vez que se pone este fragment
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mMapFragment == null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mMapFragment);
            ft.commit();
        }
        mMapFragment.getMapAsync(this);
        inicializarComponentes();
        return binding.getRoot();
    }

    private void inicializarComponentes() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        serviciosPuntoInteres = new ServiciosPuntoInteres();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setListeners();
        getPoisTask = getGetPoisAsyncTask();
    }

    private void setListeners() {
        binding.floatingAddPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GetSharedPreferences.getInstance().getCurrentUser(requireContext()) != null) {
                    Intent locationPickerIntent = new LocationPickerActivity.Builder()
                            .withGeolocApiKey(requireActivity().getString(R.string.google_maps_key))
                            .withSearchZone("es_ES")
                            .withZipCodeHidden()
                            .withSatelliteViewHidden()
                            .withGooglePlacesEnabled()
                            .withGoogleTimeZoneEnabled()
                            .withUnnamedRoadHidden()
                            .build(requireContext());

                    startActivityForResult(locationPickerIntent, 1);
                } else {
                    navController.navigate(R.id.navigation_login);
                }
            }
        });
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

        getPoisTask.execute();

        //Listener para llevar a la vista detalle cuando se clicke en un POI
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                navController.navigate(HomeFragmentDirections.actionNavigationHomeToDetallePoi((PuntoInteresDtoGetMaestro) marker.getTag()));
            }
        });
    }

    @NotNull
    private GetPoisTask getGetPoisAsyncTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();
        return new GetPoisTask(serviciosPuntoInteres){
            @Override
            protected void onPreExecute() { dialog.show();
            }

            @Override
            protected void onPostExecute(Either<ApiError, List<PuntoInteresDtoGetMaestro>> result) {
               dialog.dismiss();
                if (result.isRight()) {
                    List<PuntoInteresDtoGetMaestro> puntoInteresDtoGetMaestros = result.get();
                    if (mMap != null && puntoInteresDtoGetMaestros != null) {
                        for (PuntoInteresDtoGetMaestro poi : puntoInteresDtoGetMaestros) {
                            MarkerOptions poiMarker = new MarkerOptions()
                                    .position(new LatLng(poi.getLatitud(), poi.getLongitud()))
                                    .title(poi.getNombre());
                            Marker marker = mMap.addMarker(poiMarker);
                            marker.setTag(poi);
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
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
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            homeViewModel.getCameraPosition().setValue(mMap.getCameraPosition());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1) {
                Double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                String address = data.getStringExtra(LOCATION_ADDRESS);

                PuntoInteresDtoGetDetalle puntoInteresDtoGetDetalle = PuntoInteresDtoGetDetalle.builder()
                        .latitud(latitude)
                        .longitud(longitude)
                        .direccion(address)
                        .build();
                navController.navigate(HomeFragmentDirections.actionNavigationHomeToNuevoPuntoFragment(puntoInteresDtoGetDetalle));

            }
        }

    }
}

