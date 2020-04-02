package es.iesquevedo.descubreespana.ui.administracion;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;

public class AdministracionViewModel extends ViewModel {
    private MutableLiveData<PuntoInteresDtoGetMaestro> puntoMaestro;

    public AdministracionViewModel() {
        this.puntoMaestro = new MutableLiveData<>();
    }

    public MutableLiveData<PuntoInteresDtoGetMaestro> getPuntoMaestro() {
        return puntoMaestro;
    }
}
