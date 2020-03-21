package es.iesquevedo.servicios;

import java.util.List;

import es.iesquevedo.dao.PuntoInteresDao;
import es.iesquevedo.modelo.dto.PuntoInteresDtoGetMaestro;

public class ServiciosPuntoInteres {
    private PuntoInteresDao puntoInteresDao;

    public ServiciosPuntoInteres() {
        this.puntoInteresDao=new PuntoInteresDao();
    }

    public List<PuntoInteresDtoGetMaestro> getAll(){
        return puntoInteresDao.getAll();
    }
}
