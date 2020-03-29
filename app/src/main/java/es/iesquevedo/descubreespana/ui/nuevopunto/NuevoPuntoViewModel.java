package es.iesquevedo.descubreespana.ui.nuevopunto;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.esafirm.imagepicker.model.Image;

public class NuevoPuntoViewModel extends ViewModel {
    private MutableLiveData<Image> mImagenPrincipal;

    public NuevoPuntoViewModel() {
        mImagenPrincipal=new MutableLiveData<>();
    }

    public MutableLiveData<Image> getmImagenPrincipal() {
        return mImagenPrincipal;
    }

    public LiveData<Image> getImagen(){
        return mImagenPrincipal;
    }
}
