package com.example.joshuahughes.fypapp.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

//    private TextView textview;
    private CrimeLocationTypeModel crimeLocationType;
    private String urlString;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);


        Intent intent = getIntent();

        crimeLocationType = intent.getExtras().getParcelable("selectedCrimeLocationType");

        setTitle(crimeLocationType.Name);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Will open 'save to favourites' dialog", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //LatLng testLatLng = new LatLng(51.510343,-0.073214);
        //Integer testRadius = 50;

        //textview = (TextView) findViewById(R.id.textView3);

        //drawText(testLatLng, testRadius);



    }

    private void GetCrimeLocationJson(String url) {

        //progressBar.setVisibility(View.VISIBLE);

        final String requestTag = "CRIME_LOCATIONS_REQUEST";
        //cancel any requests queued
        VolleyQueue.getInstance(this).cancelAllRequests(requestTag);


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CrimeLocationsRequestModel model = ParseCrimeLocationsRequest(response.toString());
                        SendMapInputResults(model);

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

    @Override
    public void onMapInputInteraction(LatLng position, Integer radius) {
        setUrlString(position,radius);
        GetCrimeLocationJson(urlString);
    }

    @Override
    public Boolean isConnectedToInternet(){
        return isConnected();
    }

//    private void drawText(LatLng position, Integer radius){
//
//        urlString = getString(R.string.baseUrl) +
//                        getString(R.string.crimeLocationTypesCall) + "/" +
//                        String.valueOf(crimeLocationType.Id) + "/" +
//                        position.latitude + "/"+
//                        position.longitude + "/" +
//                        radius;
//
//        textview.setText(urlString);
//    }

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

    public void SendMapInputResults(CrimeLocationsRequestModel model){
        MapInputFragment mapInputFragment = (MapInputFragment) getSupportFragmentManager().findFragmentById(R.id.map_input_fragment);

        if(mapInputFragment !=null){

            if(model == null){
                mapInputFragment.progressDialog.cancel();
            } else {
                mapInputFragment.DrawResultsMarkersTest(model);
            }
        }


    }

}
