package es.iesquevedo.descubreespana.servicios;

import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.iesquevedo.descubreespana.dao.PuntoInteresDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import io.vavr.control.Either;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ServiciosPuntoInteres {
    private PuntoInteresDao puntoInteresDao;

    public ServiciosPuntoInteres() {
        this.puntoInteresDao = new PuntoInteresDao();
    }

    public Either<ApiError, List<PuntoInteresDtoGetMaestro>> getAll() {
        return puntoInteresDao.getAll();
    }

    public Either<ApiError, List<PuntoInteresDtoGetMaestro>> getAllSinActivar() {
        return puntoInteresDao.getAllSinActivar();
    }

    public Either<ApiError, PuntoInteresDtoGetDetalle> get(int id) {
        return puntoInteresDao.get(id);
    }

    public Either<ApiError, PuntoInteresDtoGetDetalle> addPoi(PuntoInteresDtoGetDetalle nuevoPuntoInteres, List<Image> images, Image imagenPrincipal) {
        //Crear Multipart de la imagen principal
        File file=new File(imagenPrincipal.getPath());
        MultipartBody.Part partImagenPrincipal=MultipartBody.Part.createFormData("imagenPrincipal", file.getName(), RequestBody.create(file,MediaType.parse("image/*")));
       return puntoInteresDao.addPoi(nuevoPuntoInteres,getMultipartFromImages(images),partImagenPrincipal);
    }

    public List<MultipartBody.Part> getMultipartFromImages( List<Image> images){
        List<MultipartBody.Part> multiparts=new ArrayList<>();
        for(Image image: images){
            File file=new File(image.getPath());
            MultipartBody.Part multiPart=MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(file,MediaType.parse("image/*")));
            multiparts.add(multiPart);
        }
        return multiparts;
    }

    public Either<ApiError, String> aceptarPoi(Integer id) {
        return puntoInteresDao.aceptar(id);
    }

    public Either<ApiError, String> eliminarPoi(Integer id) {
        return puntoInteresDao.eliminar(id);
    }
}
