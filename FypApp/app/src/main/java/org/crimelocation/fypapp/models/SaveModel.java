package org.crimelocation.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by joshuahughes on 23/03/2016.
 */
public class SaveModel implements Parcelable {

    @SerializedName("name")
    public String Name;

    @SerializedName("saveDate")
    public Date SaveDate;

    @SerializedName("crimeLocationTypeModel")
    public CrimeLocationTypeModel CrimeLocationTypeModel;

    @SerializedName("crimeLocationsRequestModel")
    public CrimeLocationsRequestModel CrimeLocationsRequestModel;

    @SerializedName("selectedLocation")
    public LatLng SelectedLocation;

    @SerializedName("selectedRadius")
    public int SelectedRadius;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeLong(SaveDate != null ? SaveDate.getTime() : -1);
        dest.writeParcelable(this.CrimeLocationTypeModel, 0);
        dest.writeParcelable(this.CrimeLocationsRequestModel, 0);
        dest.writeParcelable(this.SelectedLocation, 0);
        dest.writeInt(this.SelectedRadius);
    }

    public SaveModel() {
    }

    protected SaveModel(Parcel in) {
        this.Name = in.readString();
        long tmpSaveDate = in.readLong();
        this.SaveDate = tmpSaveDate == -1 ? null : new Date(tmpSaveDate);
        this.CrimeLocationTypeModel = in.readParcelable(org.crimelocation.fypapp.models.CrimeLocationTypeModel.class.getClassLoader());
        this.CrimeLocationsRequestModel = in.readParcelable(org.crimelocation.fypapp.models.CrimeLocationsRequestModel.class.getClassLoader());
        this.SelectedLocation = in.readParcelable(LatLng.class.getClassLoader());
        this.SelectedRadius = in.readInt();
    }

    public static final Creator<SaveModel> CREATOR = new Creator<SaveModel>() {
        public SaveModel createFromParcel(Parcel source) {
            return new SaveModel(source);
        }

        public SaveModel[] newArray(int size) {
            return new SaveModel[size];
        }
    };
}
