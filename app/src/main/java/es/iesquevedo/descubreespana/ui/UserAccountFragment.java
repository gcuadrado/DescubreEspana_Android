package es.iesquevedo.descubreespana.ui;

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
import es.iesquevedo.descubreespana.databinding.UserAccountFragmentBinding;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;

public class UserAccountFragment extends Fragment {

    private UserAccountViewModel mViewModel;
    private UserAccountFragmentBinding binding;

    public static UserAccountFragment newInstance() {
        return new UserAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=binding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserAccountViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        UsuarioDtoGet usuarioDtoGet=mViewModel.getmUsuario().getValue();
        binding.tvUsername.setText(usuarioDtoGet.getEmail());
    }

}
