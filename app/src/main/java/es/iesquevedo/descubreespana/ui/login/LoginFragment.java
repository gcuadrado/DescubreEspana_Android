package es.iesquevedo.descubreespana.ui.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.FragmentLoginBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        userAccountViewModel=new ViewModelProvider(requireActivity()).get(UserAccountViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button registerButton = binding.registerButton;
        Button loginButton = binding.loginButton;
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;

        serviciosUsuario = new ServiciosUsuario();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoRegister().execute(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoLogin().execute(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        etEmail.setText(loginViewModel.getEmail().getValue());
        etPassword.setText(loginViewModel.getmPassword().getValue());
    }

    private class DoRegister extends AsyncTask<String,Void, Either<ApiError,UserKeystore>>{

        @Override
        protected Either<ApiError, UserKeystore> doInBackground(String... strings) {
            return serviciosUsuario.registrarUsuario(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Either<ApiError,UserKeystore> result) {
            if(result.isRight()) {
                UserKeystore userKeystore=result.get();
                try {
                    char[] password = etPassword.getText().toString().toCharArray();
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

                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.userAccountFragment);
            }else{
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginViewModel.getEmail().setValue(etEmail.getText().toString());
        loginViewModel.getmPassword().setValue(etPassword.getText().toString());
    }
}
