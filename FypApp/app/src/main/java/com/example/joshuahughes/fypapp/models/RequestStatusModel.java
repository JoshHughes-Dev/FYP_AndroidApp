package com.example.joshuahughes.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class RequestStatusModel implements Parcelable {

    @SerializedName("errors")
    public ArrayList<RequestErrorModel> Errors;

    //COMMAND + N -> Parcelable plugin generated code


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.Errors);
    }

    public RequestStatusModel() {
    }

    protected RequestStatusModel(Parcel in) {
        this.Errors = new ArrayList<RequestErrorModel>();
        in.readList(this.Errors, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<RequestStatusModel> CREATOR = new Parcelable.Creator<RequestStatusModel>() {
        public RequestStatusModel createFromParcel(Parcel source) {
            return new RequestStatusModel(source);
        }

        public RequestStatusModel[] newArray(int size) {
            return new RequestStatusModel[size];
        }
    };
}
