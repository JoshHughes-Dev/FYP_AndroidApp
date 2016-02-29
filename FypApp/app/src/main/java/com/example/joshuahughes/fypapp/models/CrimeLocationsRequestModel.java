package com.example.joshuahughes.fypapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by joshuahughes on 29/02/16.
 */
public class CrimeLocationsRequestModel {

    @SerializedName("status")
    public RequestStatusModel Status;

    @SerializedName("crimeLocations")
    public ArrayList<CrimeLocationModel> CrimeLocations;

}
