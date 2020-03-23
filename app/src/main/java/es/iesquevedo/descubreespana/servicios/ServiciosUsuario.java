package es.iesquevedo.descubreespana.servicios;

import es.iesquevedo.descubreespana.dao.UsuarioDao;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;
import io.vavr.control.Either;


public class ServiciosUsuario {
    private UsuarioDao usuarioDao;


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

    public Either<ApiError, UsuarioDtoGet> loginUsuario(String email, String password)  {
        Either<ApiError, UsuarioDtoGet> result = null;

        UsuarioDtoPost usuarioDtoPost = UsuarioDtoPost.builder()
                .email(email)
                .password(password)
                .build();
        // String erroresValidacion = validacion.validarObjeto(usuarioDtoPost);
        String erroresValidacion="";
        if (erroresValidacion.length() == 0) {
            result = usuarioDao.loginUsuario(usuarioDtoPost);
        } else {
            result = Either.left(new ApiError(400, erroresValidacion));
        }

        return result;
    }
}
