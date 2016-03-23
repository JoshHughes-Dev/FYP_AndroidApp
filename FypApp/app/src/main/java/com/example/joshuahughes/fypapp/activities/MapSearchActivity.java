package com.example.joshuahughes.fypapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.VolleyQueue;


import com.example.joshuahughes.fypapp.fragments.MapInputFragment;
import com.example.joshuahughes.fypapp.fragments.ResultsListFragment;
import com.example.joshuahughes.fypapp.fragments.SaveDialogFragment;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;
import com.example.joshuahughes.fypapp.models.SaveArrayModel;
import com.example.joshuahughes.fypapp.models.SaveModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class MapSearchActivity extends BaseActivity implements MapInputFragment.OnFragmentInteractionListener, ResultsListFragment.OnFragmentInteractionListener, SaveDialogFragment.OnFragmentInteractionListener {


    private CrimeLocationTypeModel crimeLocationType;
    private CrimeLocationsRequestModel crimeLocationsRequestModel;
    public ProgressDialog progressDialog;
    private AlertDialog clearResultsDialog;

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24px);

        Intent intent = getIntent();
        Log.d("MapSearchActivity", "create");

        crimeLocationType = intent.getExtras().getParcelable("selectedCrimeLocationType");

        if(intent.hasExtra("crimeLocationRequestModel")){
            crimeLocationsRequestModel = intent.getExtras().getParcelable("crimeLocationRequestModel");
        }


        if (savedInstanceState != null) {
            crimeLocationsRequestModel = savedInstanceState.getParcelable("crimeLocationRequestModel");
            mapViewOpen = savedInstanceState.getBoolean("mapViewOpen");
            Log.d("MapSearchActivity", "loaded data from save instance");
        }

        //set action bar title
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

        //creates config for clear results dialog
        CreateClearResultsDialog();

        FloatingActionButton saveFab = (FloatingActionButton) findViewById(R.id.saveFab);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(crimeLocationsRequestModel != null) {
                    CreateSaveDialogFragment();
                }
                else{
                    CreateNothingToDoDialog("save");
                }

            }
        });
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
                    item.setIcon(R.drawable.ic_map_white_24px);
                    ft.show(resultsListFragment);
                    ft.hide(mapInputFragment);
                    mapViewOpen = false;
                }
                else{
                    //change to map view
                    item.setIcon(R.drawable.ic_list_white_24px);
                    ft.hide(resultsListFragment);
                    ft.show(mapInputFragment);
                    mapViewOpen = true;
                }

                ft.commit();
                break;

            case R.id.clearResults:
                if(crimeLocationsRequestModel != null){
                    clearResultsDialog.show();
                }
                else{
                    CreateNothingToDoDialog("clear");
                }
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

    // Save Dialog fragment listener implements --------------------------------------------------//

    //TODO
    @Override
    public void onNewSave(String saveName){
        Log.d("MapSearchInput", saveName);

        SaveModel saveModel = new SaveModel();
        saveModel.Name = saveName;
        saveModel.SaveDate = new Date();
        saveModel.CrimeLocationsRequestModel = crimeLocationsRequestModel;
        saveModel.CrimeLocationTypeModel = crimeLocationType;
        saveModel.SelectedLocation = mapInputFragment.selectedLocation;
        saveModel.SelectedRadius = mapInputFragment.selectedRadius;

        SaveArrayModel saveArrayModel;

        Gson gson = new Gson();

        if(StorageHelper.isSaveArrayDataInSharedPref(this)){
            Log.d("MapSearchActivity", "existing array");
            String saveArrayJSON = StorageHelper.RetrieveSaveArrayModelJSON(this).toString();
            saveArrayModel = gson.fromJson(saveArrayJSON, SaveArrayModel.class);
        }
        else{
            Log.d("MapSearchActivity", "new array");
            saveArrayModel = new SaveArrayModel();
            saveArrayModel.SaveModels = new ArrayList<>();
        }

        saveArrayModel.SaveModels.add(saveModel);

        String jsonString = gson.toJson(saveArrayModel);
        StorageHelper.StoreSaveArrayModelJSON(this, jsonString);
        Log.d("MapSearchActivity", jsonString);
    }

    //--------------------------------------------------------------------------------------------//

    private void GetCrimeLocationJson(final LatLng position, final Integer radius) {

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
                        if(progressDialog != null){
                            progressDialog.cancel();
                        }
                        CreateErrorDialog(error.toString());
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
            mapInputFragment.ProcessRequestResults(crimeLocationsRequestModel);
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

    protected void StartIntentToDetailsActivity(CrimeLocationModel crimeLocationModel){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("selectedCrimeLocationModel", crimeLocationModel);
        intent.putExtra("numberOfLocationResults", crimeLocationsRequestModel.CrimeLocations.size());
        startActivity(intent);
    }

    //TODO
    private void ClearResults(){
        mapInputFragment.ClearResults();
        resultsListFragment.ClearResults();
        crimeLocationsRequestModel = null;
    }


    // DIALOGS -----------------------------------------------------------------------------------//

    private void CreateErrorDialog(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage(str);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void CreateRequestProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching for locations and associated crime...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void CreateClearResultsDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MapSearchActivity.this);

        builder.setMessage("Are you sure you want to clear search results");

        // Add the buttons
        builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ClearResults();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        clearResultsDialog = builder.create();
    }

    private void CreateSaveDialogFragment(){

        //null check happens when calling this from outside, should change TODO
        SaveDialogFragment saveDialogFragment = SaveDialogFragment.newInstance(crimeLocationType, crimeLocationsRequestModel);

        saveDialogFragment.show(getFragmentManager(), "Save Dialog Fragment");

    }

    private void CreateNothingToDoDialog(String todo){

        AlertDialog.Builder builder = new AlertDialog.Builder(MapSearchActivity.this);

        builder.setMessage("Nothing to " + todo + ". Use map to start a search");
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    //--------------------------------------------------------------------------------------------//
}
