package es.iesquevedo.descubreespana.modelo;

import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserKeystore {
    private UsuarioDtoGet usuarioDtoGet;
    private String keystore;
}
