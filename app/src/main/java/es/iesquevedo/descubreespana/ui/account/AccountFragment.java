package es.iesquevedo.descubreespana.ui.account;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.databinding.FragmentAccountBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;
import io.vavr.control.Either;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private ServiciosUsuario serviciosUsuario;
    private EditText etEmail;
    private EditText etPassword;
    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        binding = FragmentAccountBinding.inflate(inflater, container, false);
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
                Toast.makeText(requireContext(),usuarioDtoGet.getIdUsuario()+":"+usuarioDtoGet.getEmail(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
       outState.putString("email",etEmail.getText().toString());
        outState.putString("password",etPassword.getText().toString());
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            etEmail.setText(savedInstanceState.getString("email", ""));
            etPassword.setText(savedInstanceState.getString("password", ""));
        }
    }


}
