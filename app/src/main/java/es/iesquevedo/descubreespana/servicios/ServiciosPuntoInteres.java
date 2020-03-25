package es.iesquevedo.descubreespana.servicios;

import java.util.List;

import es.iesquevedo.descubreespana.dao.PuntoInteresDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import io.vavr.control.Either;

public class ServiciosPuntoInteres {
    private PuntoInteresDao puntoInteresDao;

    public ServiciosPuntoInteres() {
        this.puntoInteresDao = new PuntoInteresDao();
    }

    public Either<ApiError, List<PuntoInteresDtoGetMaestro>> getAll() {
        return puntoInteresDao.getAll();
    }

    public Either<ApiError, PuntoInteresDtoGetDetalle> get(int id) {
        return puntoInteresDao.get(id);
    }
}
