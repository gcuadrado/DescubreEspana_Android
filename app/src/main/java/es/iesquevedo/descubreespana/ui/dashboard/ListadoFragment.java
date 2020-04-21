package es.iesquevedo.descubreespana.ui.dashboard;

import android.app.AlertDialog;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import es.iesquevedo.descubreespana.GetPoisCercanosTask;
import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.ListadoFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.ListadoPoiAdapter;
import io.vavr.control.Either;

public class ListadoFragment extends Fragment {

    private ListadoViewModel listadoViewModel;
    private ListadoFragmentBinding binding;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listadoViewModel = new ViewModelProvider(this).get(ListadoViewModel.class);
        binding = ListadoFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serviciosPuntoInteres=new ServiciosPuntoInteres();
        NavController navController= Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        dialog=new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setView(R.layout.layout_loading_dialog)
                .create();

        LocationServices.getFusedLocationProviderClient(requireActivity()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                new GetPoisCercanosTask(serviciosPuntoInteres,location.getLatitude(),location.getLongitude()){
                    @Override
                    protected void onPreExecute() {
                        dialog.show();
                    }

                    @Override
                    protected void onPostExecute(Either<ApiError, List<PuntoInteresDtoGetMaestro>> result) {
                        dialog.dismiss();
                        if(result.isRight()){
                            binding.recyclerListado.setAdapter(new ListadoPoiAdapter(result.get(),navController,location));
                        }else{
                            Toast.makeText(requireContext(),result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
        });

    }
}
