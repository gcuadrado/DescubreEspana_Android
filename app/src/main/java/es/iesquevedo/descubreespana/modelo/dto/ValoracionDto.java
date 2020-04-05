package es.iesquevedo.descubreespana.modelo.dto;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValoracionDto implements Parcelable {
    private int idValoracion;
    private Integer puntuacion;
    private String comentario;
    private UsuarioDtoGet usuarioByIdUsuario;
    private int idPuntoInteres;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idValoracion);
        dest.writeValue(this.puntuacion);
        dest.writeString(this.comentario);
        dest.writeParcelable(this.usuarioByIdUsuario, flags);
        dest.writeInt(this.idPuntoInteres);
    }

    protected ValoracionDto(Parcel in) {
        this.idValoracion = in.readInt();
        this.puntuacion = (Integer) in.readValue(Integer.class.getClassLoader());
        this.comentario = in.readString();
        this.usuarioByIdUsuario = in.readParcelable(UsuarioDtoGet.class.getClassLoader());
        this.idPuntoInteres = in.readInt();
    }

    public static final Parcelable.Creator<ValoracionDto> CREATOR = new Parcelable.Creator<ValoracionDto>() {
        @Override
        public ValoracionDto createFromParcel(Parcel source) {
            return new ValoracionDto(source);
        }

        @Override
        public ValoracionDto[] newArray(int size) {
            return new ValoracionDto[size];
        }
    };
}
