package com.example.joshuahughes.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class RequestErrorModel implements Parcelable {

    @SerializedName("locationRequestIndex")
    public int LocationRequestIndex;

    @SerializedName("crimeRequestIndex")
    public int CrimeRequestIndex;

    @SerializedName("requestErrorTypeEnum")
    public String RequestErrorType;

    @SerializedName("message")
    public String RequestErrorMessage;


    //COMMAND + N -> Parcelable plugin generated code


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.LocationRequestIndex);
        dest.writeInt(this.CrimeRequestIndex);
        dest.writeString(this.RequestErrorType);
        dest.writeString(this.RequestErrorMessage);
    }

    public RequestErrorModel() {
    }

    protected RequestErrorModel(Parcel in) {
        this.LocationRequestIndex = in.readInt();
        this.CrimeRequestIndex = in.readInt();
        this.RequestErrorType = in.readString();
        this.RequestErrorMessage = in.readString();
    }

    public static final Parcelable.Creator<RequestErrorModel> CREATOR = new Parcelable.Creator<RequestErrorModel>() {
        public RequestErrorModel createFromParcel(Parcel source) {
            return new RequestErrorModel(source);
        }

        public RequestErrorModel[] newArray(int size) {
            return new RequestErrorModel[size];
        }
    };
}
