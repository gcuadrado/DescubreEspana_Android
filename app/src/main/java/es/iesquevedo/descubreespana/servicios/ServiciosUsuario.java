package es.iesquevedo.descubreespana.servicios;

import es.iesquevedo.descubreespana.dao.UsuarioDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import es.iesquevedo.descubreespana.utils.Validacion;
import io.vavr.control.Either;


public class ServiciosUsuario {
    private UsuarioDao usuarioDao;
    private Validacion validacion;

    public ServiciosUsuario() {
        usuarioDao = new UsuarioDao();
        //validacion = new Validacion();
    }


    public Either<ApiError, UserKeystore> registrarUsuario(String email, String password)  {
        Either<ApiError, UserKeystore> result = null;

        UsuarioDtoPost usuarioDtoPost = UsuarioDtoPost.builder()
                .email(email)
                .password(password)
                .build();
       // String erroresValidacion = validacion.validarObjeto(usuarioDtoPost);
        String erroresValidacion="";
        if (erroresValidacion.length() == 0) {
            result = usuarioDao.registrarUsuario(usuarioDtoPost);
        } else {
            result = Either.left(new ApiError(400, erroresValidacion));
        }

        return result;
    }
}
