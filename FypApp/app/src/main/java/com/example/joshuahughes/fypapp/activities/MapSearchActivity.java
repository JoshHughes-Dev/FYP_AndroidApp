package com.example.joshuahughes.fypapp.activities;

import android.Manifest;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.VolleyQueue;


import com.example.joshuahughes.fypapp.fragments.MapInputFragment;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;



import org.json.JSONObject;


public class MapSearchActivity extends BaseActivity implements MapInputFragment.OnFragmentInteractionListener {


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

        CrimeLocationTypeModel crimeLocationType = intent.getExtras().getParcelable("selectedCrimeLocationType");

        setTitle(crimeLocationType.Name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Will open 'save to favourites' dialog", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final String testUrl = getString(R.string.baseUrl) + getString(R.string.crimeLocationTypesCall) + "/" + String.valueOf(crimeLocationType.Id) + "/" + "51.510343/-0.073214/50";

        TextView textview = (TextView) findViewById(R.id.textView3);
        textview.setText(testUrl);


        Button testButton = (Button) findViewById(R.id.button);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                GetCrimeLocationJson(testUrl, progressBar);
            }
        });


    }

    private void GetCrimeLocationJson(String url, final ProgressBar progressBar) {

        final TextView responseTextView = (TextView) findViewById(R.id.textView4);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("StartUp_log", response.toString());
                        responseTextView.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("StartUp_log", error.toString());
                        responseTextView.setText(error.toString());
                    }
                }
        );

        VolleyQueue.getInstance(this).addToRequestQueue(getRequest);
    }

    @Override
    public void onMapInputInteraction(Uri uri){

    }


}
