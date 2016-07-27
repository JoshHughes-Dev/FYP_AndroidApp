package org.crimelocation.fypapp.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class CrimeLocationsRequestModel implements Parcelable {

    @SerializedName("status")
    public RequestStatusModel Status;

    @SerializedName("crimeLocations")
    public ArrayList<CrimeLocationModel> CrimeLocations;



    //COMMAND + N -> Parcelable plugin generated code

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


    //--------------------------------//

    public void SetDistanceFromForResults(LatLng point){

        for(int i=0; i <CrimeLocations.size(); i++){

            float[] distance = new float[2];
            Location.distanceBetween(point.latitude, point.longitude, CrimeLocations.get(i).Location.Latitude, CrimeLocations.get(i).Location.Longitude, distance);


            int distInt = Math.round(distance[0]);

            CrimeLocations.get(i).Distance = distInt;
        }
    }

    public void SetRankForResults(){

        //sort by rank algorithm
        Collections.sort(this.CrimeLocations, new Comparator<CrimeLocationModel>() {
            @Override
            public int compare(CrimeLocationModel clm1, CrimeLocationModel clm2) {
                //best to worst

                //first sort by crime size (less the better)
                int crimeSizeResult = clm1.Crimes.size() - clm2.Crimes.size();
                if(crimeSizeResult != 0){
                    return crimeSizeResult;
                }

                //next sort by badge (lower the better)
                int badgeResults = clm1.Badge - clm2.Badge;
                if(badgeResults != 0){
                    return badgeResults;
                }

                //finally sort by distance (closer the better)
                return clm1.Distance - clm2.Distance;
            }
        });

        //apply rank number now sorted
        for(int i = 0; i<this.CrimeLocations.size(); i++){
            this.CrimeLocations.get(i).Rank = i+1;
        }
    }


}
