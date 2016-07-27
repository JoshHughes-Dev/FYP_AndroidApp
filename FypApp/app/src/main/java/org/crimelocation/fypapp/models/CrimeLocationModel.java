package org.crimelocation.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class CrimeLocationModel implements Parcelable {

    @SerializedName("location")
    public LocationModel Location;

    @SerializedName("crimes")
    public ArrayList<CrimeModel> Crimes;

    @SerializedName("badge")
    public int Badge;


    //-----------------------------------//

    public int Rank;

    //meters
    public int Distance;


    //-----------------------------------//

    //COMMAND + N -> Parcelable plugin generated code
    //new parceable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.Location, 0);
        dest.writeTypedList(Crimes);
        dest.writeInt(this.Badge);
        dest.writeInt(this.Rank);
        dest.writeInt(this.Distance);
    }

    public CrimeLocationModel() {
    }

    protected CrimeLocationModel(Parcel in) {
        this.Location = in.readParcelable(LocationModel.class.getClassLoader());
        this.Crimes = in.createTypedArrayList(CrimeModel.CREATOR);
        this.Badge = in.readInt();
        this.Rank = in.readInt();
        this.Distance = in.readInt();
    }

    public static final Creator<CrimeLocationModel> CREATOR = new Creator<CrimeLocationModel>() {
        public CrimeLocationModel createFromParcel(Parcel source) {
            return new CrimeLocationModel(source);
        }

        public CrimeLocationModel[] newArray(int size) {
            return new CrimeLocationModel[size];
        }
    };



    //COMMAND + N -> Parcelable plugin generated code
    //old parceable

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(this.Location, 0);
//        dest.writeTypedList(Crimes);
//        dest.writeInt(this.Badge);
//    }
//
//    public CrimeLocationModel() {
//    }
//
//    protected CrimeLocationModel(Parcel in) {
//        this.Location = in.readParcelable(LocationModel.class.getClassLoader());
//        this.Crimes = in.createTypedArrayList(CrimeModel.CREATOR);
//        this.Badge = in.readInt();
//    }
//
//    public static final Parcelable.Creator<CrimeLocationModel> CREATOR = new Parcelable.Creator<CrimeLocationModel>() {
//        public CrimeLocationModel createFromParcel(Parcel source) {
//            return new CrimeLocationModel(source);
//        }
//
//        public CrimeLocationModel[] newArray(int size) {
//            return new CrimeLocationModel[size];
//        }
//    };



}
