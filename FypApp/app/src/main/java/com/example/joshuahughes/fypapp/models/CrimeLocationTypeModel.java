package com.example.joshuahughes.fypapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by joshuahughes on 21/02/16.
 */
public class CrimeLocationTypeModel {

    @SerializedName("name")
    public String Name;

    @SerializedName("id")
    public int Id;

    @SerializedName("locationType")
    public String LocationType;

    @SerializedName("crimeTypes")
    public ArrayList<String> CrimeTypes;



}
