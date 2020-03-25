package es.iesquevedo.descubreespana.modelo.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoInteresDtoGetDetalle implements Parcelable {
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
    private List<FotoPuntoInteresDtoGet> fotoPuntoInteresByIdPuntoInteres;
    private UsuarioDtoGet usuarioByIdUsuario;


    protected PuntoInteresDtoGetDetalle(Parcel in) {
        idPuntoInteres = in.readInt();
        nombre = in.readString();
        resumen = in.readString();
        infoDetallada = in.readString();
        fechaInicio = in.readString();
        direccion = in.readString();
        horario = in.readString();
        coste = in.readByte() == 0x00 ? null : in.readDouble();
        byte accesibilidadVal = in.readByte();
        accesibilidad = accesibilidadVal == 0x02 ? null : accesibilidadVal != 0x00;
        puntuacion = in.readByte() == 0x00 ? null : in.readDouble();
        categoria = in.readString();
        latitud = in.readByte() == 0x00 ? null : in.readDouble();
        longitud = in.readByte() == 0x00 ? null : in.readDouble();
        enlaceInfo = in.readString();
        contacto = in.readString();
        if (in.readByte() == 0x01) {
            fotoPuntoInteresByIdPuntoInteres = new ArrayList<FotoPuntoInteresDtoGet>();
            in.readList(fotoPuntoInteresByIdPuntoInteres, FotoPuntoInteresDtoGet.class.getClassLoader());
        } else {
            fotoPuntoInteresByIdPuntoInteres = null;
        }
        usuarioByIdUsuario = (UsuarioDtoGet) in.readValue(UsuarioDtoGet.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idPuntoInteres);
        dest.writeString(nombre);
        dest.writeString(resumen);
        dest.writeString(infoDetallada);
        dest.writeString(fechaInicio);
        dest.writeString(direccion);
        dest.writeString(horario);
        if (coste == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(coste);
        }
        if (accesibilidad == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (accesibilidad ? 0x01 : 0x00));
        }
        if (puntuacion == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(puntuacion);
        }
        dest.writeString(categoria);
        if (latitud == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitud);
        }
        if (longitud == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitud);
        }
        dest.writeString(enlaceInfo);
        dest.writeString(contacto);
        if (fotoPuntoInteresByIdPuntoInteres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(fotoPuntoInteresByIdPuntoInteres);
        }
        dest.writeValue(usuarioByIdUsuario);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PuntoInteresDtoGetDetalle> CREATOR = new Parcelable.Creator<PuntoInteresDtoGetDetalle>() {
        @Override
        public PuntoInteresDtoGetDetalle createFromParcel(Parcel in) {
            return new PuntoInteresDtoGetDetalle(in);
        }

        @Override
        public PuntoInteresDtoGetDetalle[] newArray(int size) {
            return new PuntoInteresDtoGetDetalle[size];
        }
    };
}
