package es.iesquevedo.descubreespana.ui.listado_poi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListadoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListadoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}