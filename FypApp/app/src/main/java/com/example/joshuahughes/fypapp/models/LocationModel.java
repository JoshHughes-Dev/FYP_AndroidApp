package com.example.joshuahughes.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class LocationModel implements Parcelable {

    @SerializedName("name")
    public String Name;

    @SerializedName("latitude")
    public Double Latitude;

    @SerializedName("longitude")
    public Double Longitude;

    //COMMAND + N -> Parcelable plugin generated code

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeValue(this.Latitude);
        dest.writeValue(this.Longitude);
    }

    public LocationModel() {
    }

    protected LocationModel(Parcel in) {
        this.Name = in.readString();
        this.Latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.Longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<LocationModel> CREATOR = new Parcelable.Creator<LocationModel>() {
        public LocationModel createFromParcel(Parcel source) {
            return new LocationModel(source);
        }

        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };
}
