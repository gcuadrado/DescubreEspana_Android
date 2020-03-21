package es.iesquevedo.descubreespana.servicios;

import es.iesquevedo.descubreespana.dao.UsuarioDao;
import es.iesquevedo.descubreespana.modelo.UserKeystore;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoPost;

public class ServiciosUsuario {
    private UsuarioDao usuarioDao;

    public ServiciosUsuario() {
        usuarioDao=new UsuarioDao();
    }

    public UserKeystore registrar(String email, String password){
        UsuarioDtoPost usuarioDtoPost=UsuarioDtoPost.builder()
                .email(email)
                .password(password)
                .build();
        return usuarioDao.registrarUsuario(usuarioDtoPost);
    }
}
