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
public class FotoPuntoInteresDtoGet implements Parcelable {
    private int idFoto;
    private String path;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idFoto);
        dest.writeString(this.path);
    }

    protected FotoPuntoInteresDtoGet(Parcel in) {
        this.idFoto = in.readInt();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<FotoPuntoInteresDtoGet> CREATOR = new Parcelable.Creator<FotoPuntoInteresDtoGet>() {
        @Override
        public FotoPuntoInteresDtoGet createFromParcel(Parcel source) {
            return new FotoPuntoInteresDtoGet(source);
        }

        @Override
        public FotoPuntoInteresDtoGet[] newArray(int size) {
            return new FotoPuntoInteresDtoGet[size];
        }
    };
}
