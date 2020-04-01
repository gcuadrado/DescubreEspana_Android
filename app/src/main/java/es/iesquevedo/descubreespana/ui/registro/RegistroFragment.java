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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;

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

    public static RegistroFragment newInstance() {
        return new RegistroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
               // new DoRegister().execute(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

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


    private class DoRegister extends AsyncTask<String, Void, Either<ApiError, UserKeystore>> {

        @Override
        protected Either<ApiError, UserKeystore> doInBackground(String... strings) {
            return serviciosUsuario.registrarUsuario(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Either<ApiError, UserKeystore> result) {
            if (result.isRight()) {
                UserKeystore userKeystore = result.get();
                try {
                    char[] password = binding.etPassword.getText().toString().toCharArray();
                    ByteArrayInputStream input = new ByteArrayInputStream(Base64.decode(userKeystore.getKeystore(), Base64.URL_SAFE));
                    KeyStore ksLoad = KeyStore.getInstance("PKCS12");
                    ksLoad.load(input, password);

                    X509Certificate certLoad = (X509Certificate) ksLoad.getCertificate("publica");
                    KeyStore.PasswordProtection pt = new KeyStore.PasswordProtection(null);
                    KeyStore.PrivateKeyEntry privateKeyEntry =
                            (KeyStore.PrivateKeyEntry) ksLoad.getEntry("privada", pt);
                    RSAPrivateKey keyLoad = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

                    FileOutputStream fos = requireContext().openFileOutput("keystore.pfx", Context.MODE_PRIVATE);
                    ksLoad.store(fos, password);
                    fos.close();
                    Toast.makeText(requireContext(), certLoad.getSubjectX500Principal().toString(), Toast.LENGTH_LONG).show();
                    navController.navigate(R.id.navigation_login);

                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
