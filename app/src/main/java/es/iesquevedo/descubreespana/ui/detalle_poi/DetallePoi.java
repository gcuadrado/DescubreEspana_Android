package es.iesquevedo.descubreespana.ui.detalle_poi;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.DetallePoiFragmentBinding;

public class DetallePoi extends Fragment {

    private DetallePoiViewModel detallePoiViewModel;
    private DetallePoiFragmentBinding binding;

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

    }

}
