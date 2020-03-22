package es.iesquevedo.descubreespana.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> mEmail;
    private MutableLiveData<String> mPassword;
    private MutableLiveData<UsuarioDtoGet> mUsuario;

    public AccountViewModel() {
        mEmail = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mEmail;
    }
}