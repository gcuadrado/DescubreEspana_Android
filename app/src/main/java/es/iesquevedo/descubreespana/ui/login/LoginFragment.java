package es.iesquevedo.descubreespana.ui.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.FragmentLoginBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import es.iesquevedo.descubreespana.ui.useraccount.UserAccountViewModel;
import io.vavr.control.Either;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private UserAccountViewModel userAccountViewModel;
    private ServiciosUsuario serviciosUsuario;
    private EditText etEmail;
    private EditText etPassword;
    private FragmentLoginBinding binding;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        userAccountViewModel=new ViewModelProvider(requireActivity()).get(UserAccountViewModel.class);
        /*if(loginViewModel.getBinding().getValue()!=null){
            binding=loginViewModel.getBinding().getValue();
        }else {
            binding = FragmentLoginBinding.inflate(inflater, container, false);
        }*/
        binding=FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        serviciosUsuario = new ServiciosUsuario();
        setListeners();

       // etEmail.setText(loginViewModel.getEmail().getValue());
       // etPassword.setText(loginViewModel.getmPassword().getValue());
    }

    private void setListeners() {
        Button registerButton = binding.registerButton;
        Button loginButton = binding.loginButton;
        Button resetPasswordButton=binding.restPasswordButton;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoLogin().execute(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.registroFragment);
            }
        });
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResetPassword().execute(etEmail.getText().toString());
            }
        });
    }


    private class DoLogin extends AsyncTask<String,Void, Either<ApiError,UsuarioDtoGet>>{

        @Override
        protected Either<ApiError,UsuarioDtoGet> doInBackground(String... strings) {
            return serviciosUsuario.loginUsuario(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Either<ApiError, UsuarioDtoGet> result) {
            if(result.isRight()){
                UsuarioDtoGet usuarioDtoGet=result.get();
                userAccountViewModel.getmUsuario().setValue(usuarioDtoGet);
                Toast.makeText(requireContext(),usuarioDtoGet.getIdUsuario()+":"+usuarioDtoGet.getEmail(), Toast.LENGTH_LONG).show();
                navController.navigate(R.id.userAccountFragment);
            }else{
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ResetPassword extends AsyncTask<String,Void,Either<ApiError,String>>{

        @Override
        protected Either<ApiError, String> doInBackground(String... strings) {
            return serviciosUsuario.reestablecerPassword(strings[0]);
        }

        @Override
        protected void onPostExecute(Either<ApiError, String> result) {
            if(result.isRight()){
                new AlertDialog.Builder(requireContext())
                        .setTitle("¡Hecho!")
                        .setMessage("Se ha enviado una neuva contraseña a tu email")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                       .setNeutralButton("OK",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                new AlertDialog.Builder(requireContext())
                        .setTitle("Error :(")
                        .setMessage(result.getLeft().getMessage())

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setNeutralButton("OK",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginViewModel.getEmail().setValue(etEmail.getText().toString());
        loginViewModel.getmPassword().setValue(etPassword.getText().toString());
        loginViewModel.getBinding().setValue(binding);
    }
}
