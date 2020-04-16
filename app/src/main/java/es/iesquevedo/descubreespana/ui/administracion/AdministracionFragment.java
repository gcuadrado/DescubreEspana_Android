package es.iesquevedo.descubreespana.ui.administracion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.AdministracionFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.PoiAdministracionAdapter;
import io.vavr.control.Either;

public class AdministracionFragment extends Fragment {

    private AdministracionViewModel administracionViewModel;
    private AdministracionFragmentBinding binding;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private NavController navController;

    public static AdministracionFragment newInstance() {
        return new AdministracionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Seleccionar el item en la barra de navigación inferior cada vez que se pone este fragment
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

        binding=AdministracionFragmentBinding.inflate(inflater,container,false);
        administracionViewModel = new ViewModelProvider(requireActivity()).get(AdministracionViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serviciosPuntoInteres=new ServiciosPuntoInteres();
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        new GetPoisSinActivar().execute();
    }

    private class GetPoisSinActivar extends AsyncTask<Void,Void, Either<ApiError,List<PuntoInteresDtoGetMaestro>>>{

        @Override
        protected Either<ApiError, List<PuntoInteresDtoGetMaestro>> doInBackground(Void... voids) {
            return serviciosPuntoInteres.getAllSinActivar();
        }

        @Override
        protected void onPostExecute(Either<ApiError, List<PuntoInteresDtoGetMaestro>> result) {
            if(result.isRight()){
                binding.recyclerPois.setAdapter(new PoiAdministracionAdapter(result.get(),navController));
            }else{
                Toast.makeText(requireContext(),result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

}
