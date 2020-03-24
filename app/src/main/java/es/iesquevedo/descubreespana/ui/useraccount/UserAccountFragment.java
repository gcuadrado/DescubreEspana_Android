package es.iesquevedo.descubreespana.ui.useraccount;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.databinding.UserAccountFragmentBinding;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;

public class UserAccountFragment extends Fragment {

    private UserAccountViewModel mViewModel;
    private UserAccountFragmentBinding binding;
    private UsuarioDtoGet usuarioDtoGet;
    private NavController navController;

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

        setListeners();
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        usuarioDtoGet = mViewModel.getmUsuario().getValue();
        binding.tvUsername.setText(usuarioDtoGet.getEmail()+ usuarioDtoGet.getTipoUsuario());
    }

    private void setListeners() {
        Button closeSessionButton=binding.closeSession;
        closeSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getmUsuario().setValue(null);
                ConfigOkHttpRetrofit.getInstance().setToken("");
                navController.navigate(R.id.action_userAccountFragment_to_navigation_login);
            }
        });
    }

}
