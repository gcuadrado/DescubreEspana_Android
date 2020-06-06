package es.iesquevedo.descubreespana.ui.valoracion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.AddValoracionTask;
import es.iesquevedo.descubreespana.databinding.ValoracionFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import es.iesquevedo.descubreespana.servicios.ServiciosValoraciones;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;
import es.iesquevedo.descubreespana.utils.ValoracionAdapter;
import io.vavr.control.Either;

public class ValoracionFragment extends Fragment {

    private ValoracionViewModel valoracionViewModel;
    private ValoracionFragmentBinding binding;
    private ServiciosValoraciones serviciosValoraciones;
    private PuntoInteresDtoGetDetalle poi;
    private NavController navController;
    private AlertDialog dialog;


    public static ValoracionFragment newInstance() {
        return new ValoracionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        valoracionViewModel = new ViewModelProvider(requireActivity()).get(ValoracionViewModel.class);
        binding = ValoracionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        poi = ValoracionFragmentArgs.fromBundle(getArguments()).getPoi();
        inicializarViews();
        binding.btValorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GetSharedPreferences.getInstance().getCurrentUser(requireContext()) != null) {
                    if (binding.ratingBar.getRating() != 0f) {
                        new AddValoracionTask(serviciosValoraciones, (int) binding.ratingBar.getRating(), binding.etValoracion.getText().toString(), poi.getIdPuntoInteres()) {
                            @Override
                            protected void onPreExecute() {
                                dialog.show();
                            }

                            @Override
                            protected void onPostExecute(Either<ApiError, ValoracionDto> result) {
                                dialog.dismiss();
                                if (result.isRight()) {
                                    Toast.makeText(requireContext(), "Valoración añadida", Toast.LENGTH_LONG).show();
                                    poi.getValoraciones().add(result.get());
                                    binding.recyclerValoraciones.getAdapter().notifyDataSetChanged();
                                } else {
                                    Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    }else{
                        Toast.makeText(requireContext(),"Es necesario que asignes una puntuación",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    navController.navigate(R.id.navigation_login);
                }
            }
        });
    }

    private void inicializarViews() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        serviciosValoraciones = new ServiciosValoraciones();
        binding.recyclerValoraciones.setAdapter(new ValoracionAdapter(poi.getValoraciones(), requireContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
    }


}
