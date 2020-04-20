package es.iesquevedo.descubreespana.ui.edicion_poi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.UpdatePoiTask;
import es.iesquevedo.descubreespana.databinding.EdicionPoiFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import io.vavr.control.Either;

public class EdicionPoiFragment extends Fragment {

    private EdicionPoiViewModel edicionPoiViewModel;
    private EdicionPoiFragmentBinding binding;
    private PuntoInteresDtoGetDetalle poi;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private NavController navController;
    private AlertDialog dialog;

    public static EdicionPoiFragment newInstance() {
        return new EdicionPoiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Seleccionar el item en la barra de navigación inferior cada vez que se pone este fragment
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

        edicionPoiViewModel = new ViewModelProvider(requireActivity()).get(EdicionPoiViewModel.class);
        binding=EdicionPoiFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializarViews();
        setContenidoPoi();
        setListeners();
    }

    private void inicializarViews(){
        poi=EdicionPoiFragmentArgs.fromBundle(getArguments()).getPoi();
        navController= Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        serviciosPuntoInteres=new ServiciosPuntoInteres();
        dialog=new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setView(R.layout.layout_loading_dialog)
                .create();
    }

    @NotNull
    private UpdatePoiTask getUpdatePoiTask() {
        return new UpdatePoiTask(serviciosPuntoInteres) {
            @Override
            protected void onPostExecute(Either<ApiError, String> result) {
                dialog.dismiss();
                if (result.isRight()) {
                    Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setListeners() {
        binding.btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(EdicionPoiFragmentDirections.actionEdicionPoiFragmentToEdicionFotosFragment(poi));
            }
        });
        binding.btEditarPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                poi.setNombre(binding.etNombre.getText().toString());
                poi.setInfoDetallada(binding.etInformacion.getText().toString());
                poi.setResumen(binding.etInformacion.getText().toString());
                poi.setFechaInicio(binding.etFecha.getText().toString());
                poi.setCategoria(binding.spinnerCategoria.getSelectedItem().toString());
                poi.setDireccion(binding.etDireccion.getText().toString());
                poi.setContacto(binding.etContacto.getText().toString());
                poi.setCoste(Double.parseDouble(binding.etCoste.getText().toString()));
                poi.setEnlaceInfo(binding.etEnlace.getText().toString());
                poi.setAccesibilidad(binding.cbAccesibilidad.isChecked());
                poi.setHorario(binding.etHorario.getText().toString());


                getUpdatePoiTask().execute(poi);
            }
        });
    }

    private void setContenidoPoi() {
        binding.etNombre.setText(poi.getNombre());
        binding.etInformacion.setText(poi.getInfoDetallada());
        binding.etFecha.setText(poi.getFechaInicio());
        binding.etDireccion.setText(poi.getDireccion());
        binding.etCoste.setText(poi.getCoste().toString());
        binding.etContacto.setText(poi.getContacto());
        binding.etEnlace.setText(poi.getEnlaceInfo());
        binding.etHorario.setText(poi.getHorario());
        int spinnerPosition=((ArrayAdapter<String>) binding.spinnerCategoria.getAdapter()).getPosition(poi.getCategoria());
        binding.spinnerCategoria.setSelection(spinnerPosition);
        binding.cbAccesibilidad.setChecked(poi.getAccesibilidad());
    }

}
