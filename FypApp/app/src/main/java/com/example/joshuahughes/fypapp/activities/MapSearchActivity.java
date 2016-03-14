package com.example.joshuahughes.fypapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.VolleyQueue;


import com.example.joshuahughes.fypapp.fragments.MapInputFragment;
import com.example.joshuahughes.fypapp.fragments.ResultsListFragment;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;



import org.json.JSONObject;




public class MapSearchActivity extends BaseActivity implements MapInputFragment.OnFragmentInteractionListener, ResultsListFragment.OnFragmentInteractionListener {


    private CrimeLocationTypeModel crimeLocationType;
    private CrimeLocationsRequestModel crimeLocationsRequestModel;
    public ProgressDialog progressDialog;

    protected MapInputFragment mapInputFragment;
    protected ResultsListFragment resultsListFragment;
    protected Boolean mapViewOpen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Log.d("MapSearchActivity", "create");

        crimeLocationType = intent.getExtras().getParcelable("selectedCrimeLocationType");

        if (savedInstanceState != null) {
            crimeLocationsRequestModel = savedInstanceState.getParcelable("crimeLocationRequestModel");
            mapViewOpen = savedInstanceState.getBoolean("mapViewOpen");
            Log.d("MapSearchActivity", "loaded data from save instance");
        }


        setTitle(crimeLocationType.Name);


        mapInputFragment = (MapInputFragment) getSupportFragmentManager().findFragmentById(R.id.map_input_fragment);
        resultsListFragment = (ResultsListFragment) getSupportFragmentManager().findFragmentById(R.id.results_list_fragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mapViewOpen){
            ft.hide(resultsListFragment);
        }
        else{
            ft.hide(mapInputFragment);
        }
        ft.commit();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Will open 'save to favourites' dialog", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    //SETS options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_extra, menu);
        return true;
    }

    //Sets option menu handlers
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.toggleMapList:

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                if(mapViewOpen){
                    //change to list
                    item.setIcon(R.drawable.ic_map_white_36px);
                    ft.show(resultsListFragment);
                    ft.hide(mapInputFragment);
                    mapViewOpen = false;
                }
                else{
                    //change to map view
                    item.setIcon(R.drawable.ic_list_white_36px);
                    ft.hide(resultsListFragment);
                    ft.show(mapInputFragment);
                    mapViewOpen = true;
                }

                ft.commit();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //save requestModel
        savedInstanceState.putParcelable("crimeLocationRequestModel", crimeLocationsRequestModel);
        //save mapViewOpen
        savedInstanceState.putBoolean("mapViewOpen", mapViewOpen);

    }

    //Map fragment listener implements ----------------------------------------------------------//

    @Override
    public void onMapInputInteraction(LatLng position, Integer radius) {
        CreateRequestProgressDialog();
        GetCrimeLocationJson(position, radius);
    }

    @Override
    public void onMapLoadSavedInstance(){
        SendMapInputResults();
    }


    @Override
    public Boolean isConnectedToInternet(){
        return isConnected();
    }

    @Override
    public void InitDetailsActivityFromMapFragment(CrimeLocationModel crimeLocationModel){
        StartIntentToDetailsActivity(crimeLocationModel);
    }

    // Results list fragment listener implements -------------------------------------------------//


    @Override
    public void onListLoadSavedInstance(){

        if(crimeLocationsRequestModel != null){
            SendListViewResults();
        }
    }

    @Override
    public void InitDetailsActivityFromListFragment(CrimeLocationModel crimeLocationModel){
        StartIntentToDetailsActivity(crimeLocationModel);
    }

    //--------------------------------------------------------------------------------------------//

    private void GetCrimeLocationJson(final LatLng position, Integer radius) {

        String url = getUrlString(position, radius);

        //define request tag (for queue purposes)
        final String requestTag = "CRIME_LOCATIONS_REQUEST";
        //cancel any requests queued
        VolleyQueue.getInstance(this).cancelAllRequests(requestTag);

        Log.d("MapSearchActivity", "New Request: " + url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        crimeLocationsRequestModel = ParseCrimeLocationsRequest(response.toString());
                        crimeLocationsRequestModel.SetDistanceFromForResults(position);
                        crimeLocationsRequestModel.SetRankForResults();
                        SendMapInputResults();
                        SendListViewResults();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        createErrorDialog(error.toString());
                        //TODO better error response
                    }
                }
        );

        //set request queue policy
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        //set tag to this request
        getRequest.setTag(requestTag);
        //add this request to queue
        VolleyQueue.getInstance(this).addToRequestQueue(getRequest);
    }



    private String getUrlString(LatLng position, Integer radius){
        String urlString = getString(R.string.baseUrl) +
                getString(R.string.crimeLocationTypesCall) + "/" +
                String.valueOf(crimeLocationType.Id) + "/" +
                position.latitude + "/"+
                position.longitude + "/" +
                radius;

        return urlString;
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

    private void SendMapInputResults(){

        if(mapInputFragment != null){
            if(progressDialog != null){
                progressDialog.cancel();
            }
            mapInputFragment.ProcessNewRequestResults(crimeLocationsRequestModel);
        }
    }

    private void SendListViewResults(){

        if(resultsListFragment != null){
            if(progressDialog != null){
                progressDialog.cancel();
            }
            resultsListFragment.ProcessNewRequestResults(crimeLocationsRequestModel);
        }

    }


    private void CreateRequestProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching for Crime Locations...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    protected void StartIntentToDetailsActivity(CrimeLocationModel crimeLocationModel){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("selectedCrimeLocationModel",crimeLocationModel);
        startActivity(intent);
    }

}
