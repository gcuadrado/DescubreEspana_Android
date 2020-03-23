package es.iesquevedo.descubreespana.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.CameraPosition;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<CameraPosition> mCameraPosition;

    public HomeViewModel() {
        mCameraPosition = new MutableLiveData<>();
    }

    public MutableLiveData<CameraPosition> getCameraPosition() {
        return mCameraPosition;
    }
}