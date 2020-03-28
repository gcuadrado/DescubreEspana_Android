package es.iesquevedo.descubreespana.servicios;

import java.util.List;

import es.iesquevedo.descubreespana.dao.ValoracionDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.ValoracionDto;
import io.vavr.control.Either;

public class ServiciosValoraciones {
    private ValoracionDao valoracionDao;

    public ServiciosValoraciones() {
        this.valoracionDao=new ValoracionDao();
    }

    public Either<ApiError, List<ValoracionDto>> getAll(int id) {
        return valoracionDao.getAll(id);
    }

    public Either<ApiError, ValoracionDto> addValoracion(int puntuacion, String comentario, int idPuntoInteres)  {
        Either<ApiError, ValoracionDto> result = null;

        ValoracionDto valoracionDto=ValoracionDto.builder()
                .puntuacion(puntuacion)
                .comentario(comentario)
                .idPuntoInteres(idPuntoInteres)
                .build();

        String erroresValidacion="";
        if (erroresValidacion.length() == 0) {
            result = valoracionDao.addValoracion(valoracionDto);
        } else {
            result = Either.left(new ApiError(400, erroresValidacion));
        }

        return result;
    }

    public Either<ApiError,String> borrarValoracion(int id){
        return valoracionDao.borrarValoracion(id);
    }
}
