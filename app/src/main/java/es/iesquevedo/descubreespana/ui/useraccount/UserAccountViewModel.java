package es.iesquevedo.descubreespana.ui.useraccount;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;

public class UserAccountViewModel extends ViewModel {
    private MutableLiveData<UsuarioDtoGet> mUsuario;

    public UserAccountViewModel() {
        mUsuario=new MutableLiveData<>();
    }

    public MutableLiveData<UsuarioDtoGet> getmUsuario() {
        return mUsuario;
    }
}
