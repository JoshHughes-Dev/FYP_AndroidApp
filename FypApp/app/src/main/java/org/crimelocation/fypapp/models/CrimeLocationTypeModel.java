package org.crimelocation.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by joshuahughes on 21/02/16.
 */
public class CrimeLocationTypeModel implements Parcelable {

    @SerializedName("name")
    public String Name;

    @SerializedName("id")
    public int Id;

    @SerializedName("locationType")
    public String LocationType;

    @SerializedName("crimeTypes")
    public ArrayList<String> CrimeTypes;



    //COMMAND + N -> Parcelable plugin generated code

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeInt(this.Id);
        dest.writeString(this.LocationType);
        dest.writeStringList(this.CrimeTypes);
    }

    public CrimeLocationTypeModel() {
    }

    protected CrimeLocationTypeModel(Parcel in) {
        this.Name = in.readString();
        this.Id = in.readInt();
        this.LocationType = in.readString();
        this.CrimeTypes = in.createStringArrayList();
    }

    public static final Creator<CrimeLocationTypeModel> CREATOR = new Creator<CrimeLocationTypeModel>() {
        public CrimeLocationTypeModel createFromParcel(Parcel source) {
            return new CrimeLocationTypeModel(source);
        }

        public CrimeLocationTypeModel[] newArray(int size) {
            return new CrimeLocationTypeModel[size];
        }
    };
}
