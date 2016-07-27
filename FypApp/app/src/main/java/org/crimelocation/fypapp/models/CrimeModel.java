package org.crimelocation.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class CrimeModel implements Parcelable {

    @SerializedName("category")
    public String Category;

    @SerializedName("year")
    public int Year;

    @SerializedName("month")
    public int Month;


    //COMMAND + N -> Parcelable plugin generated code

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Category);
        dest.writeInt(this.Year);
        dest.writeInt(this.Month);
    }

    public CrimeModel() {
    }

    protected CrimeModel(Parcel in) {
        this.Category = in.readString();
        this.Year = in.readInt();
        this.Month = in.readInt();
    }

    public static final Parcelable.Creator<CrimeModel> CREATOR = new Parcelable.Creator<CrimeModel>() {
        public CrimeModel createFromParcel(Parcel source) {
            return new CrimeModel(source);
        }

        public CrimeModel[] newArray(int size) {
            return new CrimeModel[size];
        }
    };
}
