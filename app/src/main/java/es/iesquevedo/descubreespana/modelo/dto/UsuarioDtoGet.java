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
public class UsuarioDtoGet implements Parcelable {
    private int idUsuario;
    private String email;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idUsuario);
        dest.writeString(this.email);
    }

    protected UsuarioDtoGet(Parcel in) {
        this.idUsuario = in.readInt();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<UsuarioDtoGet> CREATOR = new Parcelable.Creator<UsuarioDtoGet>() {
        @Override
        public UsuarioDtoGet createFromParcel(Parcel source) {
            return new UsuarioDtoGet(source);
        }

        @Override
        public UsuarioDtoGet[] newArray(int size) {
            return new UsuarioDtoGet[size];
        }
    };
}
