package es.iesquevedo.descubreespana.ui.detalle_poi;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.DetallePoiFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.VPGaleriaAdapter;
import io.vavr.control.Either;
import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class DetallePoi extends Fragment {

    private DetallePoiViewModel detallePoiViewModel;
    private DetallePoiFragmentBinding binding;
    private PuntoInteresDtoGetMaestro poiMaestro;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private NavController navController;

    public static DetallePoi newInstance() {
        return new DetallePoi();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=DetallePoiFragmentBinding.inflate(inflater,container,false);
        detallePoiViewModel = new ViewModelProvider(this).get(DetallePoiViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        poiMaestro=DetallePoiArgs.fromBundle(getArguments()).getPuntoInteres();
        serviciosPuntoInteres=new ServiciosPuntoInteres();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);




       new GetPoiDetalle().execute(poiMaestro.getIdPuntoInteres());
    }

    private class GetPoiDetalle extends AsyncTask<Integer,Void, Either<ApiError,PuntoInteresDtoGetDetalle>>{

        @Override
        protected Either<ApiError,PuntoInteresDtoGetDetalle> doInBackground(Integer... integers) {
            return serviciosPuntoInteres.get(integers[0]);
        }

        @Override
        protected void onPostExecute(Either<ApiError,PuntoInteresDtoGetDetalle> result) {
            if(result.isRight()) {
                PuntoInteresDtoGetDetalle poi=result.get();

                binding.detallesInfobasica.setText(poi.getInfoDetallada());
                binding.detallesFecha.setText(poi.getFechaInicio());
                binding.detallesCategoria.setText(poi.getCategoria());
                binding.detallesDireccion.setText(poi.getDireccion());
                binding.detallesAccesibilidad.setText(poi.getAccesibilidad().toString());
                binding.detallesHorario.setText(poi.getHorario());
                binding.detallesCoste.setText(poi.getCoste().toString());
                binding.viewPagerGaleria.setAdapter(new VPGaleriaAdapter(poi));
                binding.indicator.setViewPager(binding.viewPagerGaleria);

            }else{
                Toast.makeText(requireContext(),result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
                navController.navigate(R.id.navigation_home);
            }
        }
    }

}
