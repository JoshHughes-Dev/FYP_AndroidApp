package com.example.joshuahughes.fypapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private TextView textview;
    private CrimeLocationTypeModel crimeLocationType;
    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


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

        LatLng testLatLng = new LatLng(51.510343,-0.073214);
        Integer testRadius = 50;

        textview = (TextView) findViewById(R.id.textView3);

        drawText(testLatLng, testRadius);



        Button testButton = (Button) findViewById(R.id.button);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                GetCrimeLocationJson(urlString, progressBar);
            }
        });


    }

    private void GetCrimeLocationJson(String url, final ProgressBar progressBar) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        createTempResultsDialog(response.toString());
                        CrimeLocationsRequestModel model = ParseCrimeLocationsRequest(response.toString());
                        SendMapInputResults(model);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        createTempResultsDialog(error.toString());
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyQueue.getInstance(this).addToRequestQueue(getRequest);
    }

    @Override
    public void onMapInputInteraction(LatLng position, Integer radius) {
        drawText(position, radius);
    }

    private void drawText(LatLng position, Integer radius){

        urlString = getString(R.string.baseUrl) +
                        getString(R.string.crimeLocationTypesCall) + "/" +
                        String.valueOf(crimeLocationType.Id) + "/" +
                        position.latitude + "/"+
                        position.longitude + "/" +
                        radius;

        textview.setText(urlString);
    }


    private void createTempResultsDialog(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Results");
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
        MapInputFragment mapInputFragment =(MapInputFragment) getSupportFragmentManager().findFragmentById(R.id.map_input_fragment);
        if(mapInputFragment !=null){
            mapInputFragment.drawResultsMarkersTest(model);
        }
    }

}
