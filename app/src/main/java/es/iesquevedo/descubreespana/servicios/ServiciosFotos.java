package es.iesquevedo.descubreespana.servicios;

import es.iesquevedo.descubreespana.dao.FotosDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import io.vavr.control.Either;

public class ServiciosFotos {
    private FotosDao fotosDao;

    public ServiciosFotos() {
        fotosDao=new FotosDao();
    }

    public Either<ApiError,String> deleteFoto (int id){
        return fotosDao.delete(id);
    }
}
