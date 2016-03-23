package com.example.joshuahughes.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by joshuahughes on 23/03/2016.
 */
public class SaveArrayModel implements Parcelable {

    @SerializedName("saveModels")
    public ArrayList<SaveModel> SaveModels;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(SaveModels);
    }

    public SaveArrayModel() {
    }

    protected SaveArrayModel(Parcel in) {
        this.SaveModels = in.createTypedArrayList(SaveModel.CREATOR);
    }

    public static final Parcelable.Creator<SaveArrayModel> CREATOR = new Parcelable.Creator<SaveArrayModel>() {
        public SaveArrayModel createFromParcel(Parcel source) {
            return new SaveArrayModel(source);
        }

        public SaveArrayModel[] newArray(int size) {
            return new SaveArrayModel[size];
        }
    };
}
