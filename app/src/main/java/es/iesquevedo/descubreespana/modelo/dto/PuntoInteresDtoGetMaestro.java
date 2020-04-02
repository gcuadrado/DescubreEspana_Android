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
public class PuntoInteresDtoGetMaestro implements Parcelable {
    private int idPuntoInteres;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private Boolean activado;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idPuntoInteres);
        dest.writeString(this.nombre);
        dest.writeValue(this.latitud);
        dest.writeValue(this.longitud);
        dest.writeValue(this.activado);
    }

    protected PuntoInteresDtoGetMaestro(Parcel in) {
        this.idPuntoInteres = in.readInt();
        this.nombre = in.readString();
        this.latitud = (Double) in.readValue(Double.class.getClassLoader());
        this.longitud = (Double) in.readValue(Double.class.getClassLoader());
        this.activado=(Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PuntoInteresDtoGetMaestro> CREATOR = new Parcelable.Creator<PuntoInteresDtoGetMaestro>() {
        @Override
        public PuntoInteresDtoGetMaestro createFromParcel(Parcel source) {
            return new PuntoInteresDtoGetMaestro(source);
        }

        @Override
        public PuntoInteresDtoGetMaestro[] newArray(int size) {
            return new PuntoInteresDtoGetMaestro[size];
        }
    };
}
