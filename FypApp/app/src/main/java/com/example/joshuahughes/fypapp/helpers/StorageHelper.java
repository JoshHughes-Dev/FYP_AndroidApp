package com.example.joshuahughes.fypapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.joshuahughes.fypapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joshuahughes on 22/02/16.
 */
public class StorageHelper {

    private static final String storageLogIdString = "STORAGE_EXCEPTION";

    // -------------------------------------------------------------------------------------------//

    private static final String clTypesPrefFileName = "CrimeLocationTypesJSON";
    private static final String clTypesKey = "crimeLocationTypesJson";
    private static final String clTypesObjWrapper = "ClTypesArray";

    public static void StoreCrimeLocationTypesJSON(Context context, JSONArray jsonArray){
        StoreJsonArrayInSharedPrefs(context, clTypesPrefFileName, clTypesKey, jsonArray, clTypesObjWrapper);
    }

    public static JSONArray RetrieveCrimeLocationTypesJSON(Context context){
        return RetrieveJsonArrayFromSharedPrefs(context, clTypesPrefFileName, clTypesKey, clTypesObjWrapper);
    }

    public static Boolean isClTypesDataInSharedPref(Context context){
        return isDataInSharedPrefs(context, clTypesPrefFileName, clTypesKey);
    }

    // -------------------------------------------------------------------------------------------//

    private static final String saveArrayPrefFileName = "SaveArrayModelJSON";
    private static final String saveArrayKey = "saveArrayModelJSON";


    public static Boolean isSaveArrayDataInSharedPref(Context context){
        return isDataInSharedPrefs(context, saveArrayPrefFileName, saveArrayKey);
    }

    public static JSONObject RetrieveSaveArrayModelJSON(Context context){
        return RetrieveJsonObjectFromSharedPrefs(context, saveArrayPrefFileName, saveArrayKey);
    }

    public static void StoreSaveArrayModelJSON(Context context, String jsonString){

        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            StoreJsonObjectInSharedPrefs(context, saveArrayPrefFileName, saveArrayKey,jsonObject);

        } catch(JSONException e){
            Log.d("StorageHelper", "bad json object conversion for saveArrayModel");
        }

    }


    // BASE METHODS -----------------------------------------------------------------------------//

    public static void StoreJsonArrayInSharedPrefs(Context context, String prefFileName, String key, JSONArray jsonArray, String objWrapperName){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(objWrapperName, jsonArray);

        } catch (JSONException e){
            Log.d(storageLogIdString, e.toString());
        }

        StoreJsonObjectInSharedPrefs(context, prefFileName,key,jsonObject);

    }

    private static JSONArray RetrieveJsonArrayFromSharedPrefs(Context context, String prefFileName, String key, String objWrapperName){

        JSONObject jsonObject = RetrieveJsonObjectFromSharedPrefs(context, prefFileName, key);
        JSONArray jsonArray = new JSONArray();

        if(jsonObject != null){

            try{
                jsonArray = jsonObject.getJSONArray(objWrapperName);

            } catch (JSONException e){
                Log.d(storageLogIdString, e.toString());
            }

        }

        return jsonArray;
    }

    public static void StoreJsonObjectInSharedPrefs(Context context, String prefFileName, String key, JSONObject jsonObject){

        SharedPreferences settings = context.getSharedPreferences(prefFileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, jsonObject.toString());
        editor.commit();

    }

    public static JSONObject RetrieveJsonObjectFromSharedPrefs(Context context, String prefFileName, String key){
        SharedPreferences settings = context.getSharedPreferences(prefFileName, 0);
        String strJson = settings.getString(key, "0");
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject = new JSONObject(strJson);
        }catch(JSONException e){
            Log.d(storageLogIdString, e.toString());
        }

        return jsonObject;
    }

    public static Boolean isDataInSharedPrefs(Context context, String prefFileName, String key){
        SharedPreferences settings = context.getSharedPreferences(prefFileName, 0);
        return settings.contains(key);
    }
}
