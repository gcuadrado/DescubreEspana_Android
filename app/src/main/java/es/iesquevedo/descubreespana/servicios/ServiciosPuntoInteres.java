package es.iesquevedo.descubreespana.servicios;

import java.util.List;

import es.iesquevedo.descubreespana.dao.PuntoInteresDao;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;

public class ServiciosPuntoInteres {
    private PuntoInteresDao puntoInteresDao;

    public ServiciosPuntoInteres() {
        this.puntoInteresDao=new PuntoInteresDao();
    }

    public List<PuntoInteresDtoGetMaestro> getAll(){
        return puntoInteresDao.getAll();
    }
}
