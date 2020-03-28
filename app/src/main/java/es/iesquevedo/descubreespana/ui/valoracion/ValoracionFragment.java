package es.iesquevedo.descubreespana.ui.valoracion;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import es.iesquevedo.descubreespana.databinding.ValoracionFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import es.iesquevedo.descubreespana.servicios.ServiciosValoraciones;
import es.iesquevedo.descubreespana.utils.ValoracionAdapter;
import io.vavr.control.Either;

public class ValoracionFragment extends Fragment {

    private ValoracionViewModel valoracionViewModel;
    private ValoracionFragmentBinding binding;
    private ServiciosValoraciones serviciosValoraciones;
    private int idPoi;

    public static ValoracionFragment newInstance() {
        return new ValoracionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        valoracionViewModel = new ViewModelProvider(requireActivity()).get(ValoracionViewModel.class);
        binding=ValoracionFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serviciosValoraciones=new ServiciosValoraciones();
        idPoi=ValoracionFragmentArgs.fromBundle(getArguments()).getIdPoi();

        new GetValoraciones().execute();
        binding.btValorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddValoracion().execute();
            }
        });
    }


    private class AddValoracion extends AsyncTask<Void,Void, Either<ApiError, ValoracionDto>>{

        @Override
        protected Either<ApiError, ValoracionDto> doInBackground(Void... voids) {
            return serviciosValoraciones.addValoracion((int)binding.ratingBar.getRating(),binding.etValoracion.getText().toString(),idPoi);
        }

        @Override
        protected void onPostExecute(Either<ApiError, ValoracionDto> result) {
            if(result.isRight()){
                Toast.makeText(requireContext(),"Valoración añadida",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(requireContext(),result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetValoraciones extends AsyncTask<Void,Void, Either<ApiError, List<ValoracionDto>>>{

        @Override
        protected Either<ApiError, List<ValoracionDto>> doInBackground(Void... voids) {
            return serviciosValoraciones.getAll(idPoi);
        }

        @Override
        protected void onPostExecute(Either<ApiError, List<ValoracionDto>> result) {
            if(result.isRight()){
                binding.recyclerValoraciones.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerValoraciones.setAdapter(new ValoracionAdapter(result.get(),requireContext()));
            }else{
                Toast.makeText(requireContext(),result.getLeft().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

}
