package es.iesquevedo.descubreespana.ui.account;

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
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.servicios.ServiciosUsuario;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private ServiciosUsuario serviciosUsuario;
    private EditText etEmail;
    private EditText etPassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Button registerButton=requireActivity().findViewById(R.id.registerButton);
        Button loginButton=requireActivity().findViewById(R.id.loginButton);
        etEmail = requireActivity().findViewById(R.id.etEmail);
        etPassword = requireActivity().findViewById(R.id.etPassword);

        serviciosUsuario=new ServiciosUsuario();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoRegister().execute(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });
    }

    private class DoRegister extends AsyncTask<String,Void, UserKeystore>{

        @Override
        protected UserKeystore doInBackground(String... strings) {
            return serviciosUsuario.registrar(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(UserKeystore userKeystore) {
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
                System.out.println(certLoad.getIssuerX500Principal());
                Toast.makeText(requireContext(),certLoad.getIssuerX500Principal().toString(),Toast.LENGTH_LONG).show();

            }catch (Exception e){
                Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }
}
