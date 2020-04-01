package es.iesquevedo.descubreespana.ui.useraccount;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.config.ConfigOkHttpRetrofit;
import es.iesquevedo.descubreespana.databinding.UserAccountFragmentBinding;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.utils.Constantes;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;

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
        usuarioDtoGet = ConfigOkHttpRetrofit.getInstance().getGson().fromJson(PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(Constantes.USUARIO,null),UsuarioDtoGet.class);
        binding.tvUsername.setText(usuarioDtoGet.getEmail()+ usuarioDtoGet.getTipoUsuario());
        UsuarioDtoGet currentUser = GetSharedPreferences.getInstance().getCurrentUser(requireContext());
        binding.btAdministracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.administracionFragment);
            }
        });
        if(currentUser !=null && currentUser.getTipoUsuario()==Constantes.ADMIN){
            binding.btAdministracion.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        Button closeSessionButton=binding.closeSession;
        closeSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getmUsuario().setValue(null);
                PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().remove(Constantes.USUARIO).commit();
                ConfigOkHttpRetrofit.getInstance().setToken("");
                navController.navigate(R.id.action_userAccountFragment_to_navigation_login);
            }
        });
    }

}
