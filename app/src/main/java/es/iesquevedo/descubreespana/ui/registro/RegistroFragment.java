package es.iesquevedo.descubreespana.ui.registro;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.RegistroFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import io.vavr.control.Either;

public class RegistroFragment extends Fragment {

    private RegistroViewModel mViewModel;
    private RegistroFragmentBinding binding;
    private ServiciosUsuario serviciosUsuario;
    private NavController navController;
    private Validator validator;
    @Email(message = "Debes indicar un email válido")
    private EditText etEmail;
    @Password
    private EditText etPassword;
    private AlertDialog dialog;

    public static RegistroFragment newInstance() {
        return new RegistroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().findItem(R.id.navigation_login).setChecked(true);

        mViewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);
        binding = RegistroFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serviciosUsuario = new ServiciosUsuario();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        etEmail = binding.etEmail;
        etPassword=binding.etPassword;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                new DoRegister().execute(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(requireContext());

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


    private class DoRegister extends AsyncTask<String, Void, Either<ApiError, UsuarioDtoGet>> {
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Either<ApiError, UsuarioDtoGet> doInBackground(String... strings) {
            return serviciosUsuario.registrarUsuario(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Either<ApiError, UsuarioDtoGet> result) {
            dialog.dismiss();
            if (result.isRight()) {
                UsuarioDtoGet usuarioDtoGet = result.get();
                new AlertDialog.Builder(requireContext())
                        .setTitle("Enhorabuena!")
                        .setMessage("Ya casi estamos, te hemos enviado un email a: "+usuarioDtoGet.getEmail()
                                +" para que actives tu cuenta")
                        .setCancelable(false)
                        .setNeutralButton("OK",null)
                        .create()
                        .show();
                    navController.navigate(R.id.navigation_home);

            } else {
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
