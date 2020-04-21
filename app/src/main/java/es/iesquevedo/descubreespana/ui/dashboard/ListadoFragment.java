package es.iesquevedo.descubreespana.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.ListadoFragmentBinding;

public class ListadoFragment extends Fragment {

    private ListadoViewModel listadoViewModel;
    private ListadoFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listadoViewModel = new ViewModelProvider(this).get(ListadoViewModel.class);
        binding = ListadoFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
