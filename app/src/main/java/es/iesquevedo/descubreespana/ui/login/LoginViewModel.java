package es.iesquevedo.descubreespana.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.iesquevedo.descubreespana.databinding.FragmentLoginBinding;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> mEmail;
    private MutableLiveData<String> mPassword;
    private MutableLiveData<UsuarioDtoGet> mUsuario;
    private MutableLiveData<FragmentLoginBinding> binding;

    public LoginViewModel() {
        mEmail = new MutableLiveData<>();
        mPassword = new MutableLiveData<>();
        mUsuario = new MutableLiveData<>();
        binding=new MutableLiveData<>();
    }

    public MutableLiveData<String> getEmail() {
        return mEmail;
    }


    public MutableLiveData<String> getmPassword() {
        return mPassword;
    }


    public MutableLiveData<UsuarioDtoGet> getmUsuario() {
        return mUsuario;
    }

    public MutableLiveData<FragmentLoginBinding> getBinding() {
        return binding;
    }
}