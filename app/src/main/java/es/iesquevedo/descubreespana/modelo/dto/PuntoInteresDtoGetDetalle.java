package es.iesquevedo.descubreespana.modelo.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoInteresDtoGetDetalle {
    private int idPuntoInteres;
    private String nombre;
    private String resumen;
    private String infoDetallada;
    private String fechaInicio;
    private String direccion;
    private String horario;
    private Double coste;
    private Boolean accesibilidad;
    private Double puntuacion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String enlaceInfo;
    private String contacto;
    private Collection<FotoPuntoInteresDtoGet> fotoPuntoInteresByIdPuntoInteres;
    private UsuarioDtoGet usuarioByIdUsuario;
}