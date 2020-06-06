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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.LoginTask;
import es.iesquevedo.descubreespana.asynctask.ResetPasswordTask;
import es.iesquevedo.descubreespana.databinding.FragmentLoginBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import es.iesquevedo.descubreespana.ui.useraccount.UserAccountViewModel;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;
import io.vavr.control.Either;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private ServiciosUsuario serviciosUsuario;
    @Email(message = "Email no válido")
    private EditText etEmail;
    @Password(message = "Contraseña no válida")
    private EditText etPassword;
    private FragmentLoginBinding binding;
    private NavController navController;
    private Validator loginValidator;
    private AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Seleccionar el item en la barra de navigación inferior cada vez que se pone este fragment
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().findItem(R.id.navigation_login).setChecked(true);

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializarViews();
        setListeners();

    }

    private void inicializarViews() {
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        serviciosUsuario = new ServiciosUsuario();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
        loginValidator = new Validator(this);
    }

    private void setListeners() {
        Button registerButton = binding.registerButton;
        Button loginButton = binding.loginButton;
        Button resetPasswordButton = binding.restPasswordButton;
        loginValidator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                new LoginTask(serviciosUsuario, etEmail.getText().toString(), etPassword.getText().toString()) {
                    @Override
                    protected void onPreExecute() {
                        dialog.show();
                    }

                    @Override
                    protected void onPostExecute(Either<ApiError, UsuarioDtoGet> result) {
                        dialog.dismiss();
                        if (result.isRight()) {
                            UsuarioDtoGet usuarioDtoGet = result.get();
                            //Guardamos el usuario actual en Preferences
                            GetSharedPreferences.getInstance().setCurrentUser(usuarioDtoGet, requireContext());
                            Toast.makeText(requireContext(), "Has iniciado sesión como:" + usuarioDtoGet.getEmail(), Toast.LENGTH_LONG).show();

                            if (!navController.popBackStack()) {
                                navController.navigate(R.id.userAccountFragment);
                            }
                        } else {
                            Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginValidator.validate();
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
                new ResetPasswordTask(serviciosUsuario, etEmail.getText().toString()) {
                    @Override
                    protected void onPreExecute() {
                        dialog.show();
                    }

                    @Override
                    protected void onPostExecute(Either<ApiError, String> result) {
                        dialog.dismiss();
                        if (result.isRight()) {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("¡Hecho!")
                                    .setMessage("Se ha enviado una nueva contraseña a tu email")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setNeutralButton("OK", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Error :(")
                                    .setMessage(result.getLeft().getMessage())

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setNeutralButton("OK", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }.execute();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginViewModel.getEmail().setValue(etEmail.getText().toString());
        loginViewModel.getmPassword().setValue(etPassword.getText().toString());
        loginViewModel.getBinding().setValue(binding);
    }
}
