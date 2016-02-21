package com.example.joshuahughes.fypapp.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuahughes on 21/02/16.
 */
public class CrimeLocationTypeModel {

    public String Name;
    public int Id;
    public String LocationType;
    public ArrayList<String> CrimeTypes;

    public CrimeLocationTypeModel(){

    }

    public CrimeLocationTypeModel(JSONObject jsonObject){

            try{
                this.Name =jsonObject.getString("name");
                this.Id = jsonObject.getInt("id");
                this.LocationType = jsonObject.getString("locationType");

                JSONArray crimeTypesJson = jsonObject.getJSONArray("crimeTypes");
                this.CrimeTypes = new ArrayList<String>();

                for(int i = 0; i < crimeTypesJson.length(); i++){
                    this.CrimeTypes.add(crimeTypesJson.getString(i));
                }

            }
            catch(JSONException e){
                Log.d("JH: JSON binding error", "couldnt create CrimeLocationTypeModel from jsonObject");
            }


    }
}
