package com.example.joshuahughes.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class CrimeLocationsRequestModel implements Parcelable {

    @SerializedName("status")
    public RequestStatusModel Status;

    @SerializedName("crimeLocations")
    public ArrayList<CrimeLocationModel> CrimeLocations;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.Status, 0);
        dest.writeTypedList(CrimeLocations);
    }

    public CrimeLocationsRequestModel() {
    }

    protected CrimeLocationsRequestModel(Parcel in) {
        this.Status = in.readParcelable(RequestStatusModel.class.getClassLoader());
        this.CrimeLocations = in.createTypedArrayList(CrimeLocationModel.CREATOR);
    }

    public static final Parcelable.Creator<CrimeLocationsRequestModel> CREATOR = new Parcelable.Creator<CrimeLocationsRequestModel>() {
        public CrimeLocationsRequestModel createFromParcel(Parcel source) {
            return new CrimeLocationsRequestModel(source);
        }

        public CrimeLocationsRequestModel[] newArray(int size) {
            return new CrimeLocationsRequestModel[size];
        }
    };
}
