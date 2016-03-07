package com.example.joshuahughes.fypapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.VolleyQueue;


import com.example.joshuahughes.fypapp.fragments.MapInputFragment;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;



import org.json.JSONObject;




public class MapSearchActivity extends BaseActivity implements MapInputFragment.OnFragmentInteractionListener {


    private CrimeLocationTypeModel crimeLocationType;
    private String urlString;
    private CrimeLocationsRequestModel crimeLocationsRequestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        crimeLocationType = intent.getExtras().getParcelable("selectedCrimeLocationType");

        if (savedInstanceState != null) {
            crimeLocationsRequestModel = savedInstanceState.getParcelable("crimeLocationRequestModel");
        }

        setTitle(crimeLocationType.Name);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Will open 'save to favourites' dialog", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //save requestModel
        savedInstanceState.putParcelable("crimeLocationRequestModel", crimeLocationsRequestModel);

    }
//



    private void GetCrimeLocationJson(String url) {


        final String requestTag = "CRIME_LOCATIONS_REQUEST";
        //cancel any requests queued
        VolleyQueue.getInstance(this).cancelAllRequests(requestTag);


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        crimeLocationsRequestModel = ParseCrimeLocationsRequest(response.toString());
                        SendMapInputResults();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        createErrorDialog(error.toString());
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequest.setTag(requestTag);

        VolleyQueue.getInstance(this).addToRequestQueue(getRequest);
    }

    //MapInputFragment calls this
    @Override
    public void onMapInputInteraction(LatLng position, Integer radius) {
        setUrlString(position, radius);
        Log.d("Request", "getting new");
        GetCrimeLocationJson(urlString);


    }
    //MapInput Fragment calls this
    @Override
    public void onMapLoadSavedInstance(){
        Log.d("Request", "sending existing");
        SendMapInputResults();
    }

    // MapInputFragment calls this
    @Override
    public Boolean isConnectedToInternet(){
        return isConnected();
    }



    private void setUrlString(LatLng position, Integer radius){
        urlString = getString(R.string.baseUrl) +
                getString(R.string.crimeLocationTypesCall) + "/" +
                String.valueOf(crimeLocationType.Id) + "/" +
                position.latitude + "/"+
                position.longitude + "/" +
                radius;
    }

    private void createErrorDialog(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage(str);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private CrimeLocationsRequestModel ParseCrimeLocationsRequest(String jsonObject){
        Gson gson = new Gson();
        CrimeLocationsRequestModel model = gson.fromJson(jsonObject, CrimeLocationsRequestModel.class);
        return model;
    }

    public void SendMapInputResults(){
        MapInputFragment mapInputFragment = (MapInputFragment) getSupportFragmentManager().findFragmentById(R.id.map_input_fragment);

        if(mapInputFragment !=null){

            if(crimeLocationsRequestModel == null){
                mapInputFragment.progressDialog.cancel();
            } else {
                mapInputFragment.DrawResultsMarkersTest(crimeLocationsRequestModel);
            }
        }


    }

}
