package es.iesquevedo.descubreespana.servicios;

import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.iesquevedo.descubreespana.dao.FotosDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import io.vavr.control.Either;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ServiciosFotos {
    private FotosDao fotosDao;

    public ServiciosFotos() {
        fotosDao=new FotosDao();
    }

    public Either<ApiError,String> deleteFoto (int id){
        return fotosDao.delete(id);
    }

    public Either<ApiError,List<FotoPuntoInteresDtoGet>> insertFotos(List<Image> images, PuntoInteresDtoGetDetalle poi){
        return fotosDao.insertFotos(getMultipartFromImages(images),poi);
    }

    public List<MultipartBody.Part> getMultipartFromImages(List<Image> images){
        List<MultipartBody.Part> multiparts=new ArrayList<>();
        for(Image image: images){
            //Obtenemos el archivo con el path de la imagen
            File file=new File(image.getPath());
            //Creamos el part con el archivo
            MultipartBody.Part multiPart=
                    MultipartBody.Part.createFormData("image", file.getName(),
                            RequestBody.create(file, MediaType.parse("image/*")));
            multiparts.add(multiPart);
        }
        return multiparts;
    }
}
