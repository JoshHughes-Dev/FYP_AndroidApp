package com.example.joshuahughes.fypapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
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

    public ProgressDialog progressDialog;
    protected MapInputFragment mapInputFragment;
    protected ResultsListFragment resultsListFragment;

    private Intent intent;
    private CrimeLocationTypeModel crimeLocationType;
    private CrimeLocationsRequestModel crimeLocationsRequestModel;

    protected Boolean mapViewOpen = true;
    private LatLng locationFromSave;
    private Integer radiusFromSave;

    private static final String TAG = "MapSearchActivity";

    private static final String STATE_SELECTED_CL_TYPE = "selectedCrimeLocationType";
    private static final String STATE_CL_REQUEST_MODEL = "crimeLocationRequestModel";
    private static final String STATE_MAP_VIEW_OPEN = "mapViewOpen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24px);

        //gets intent
        intent = getIntent();

        //Checks intent for selected crime location type, CRITICAL for this activity
        if(intent.hasExtra(getString(R.string.intent_selected_crimeLocationType))){
            crimeLocationType = intent.getExtras().getParcelable(getString(R.string.intent_selected_crimeLocationType));
        }

        //Checks if activity was started by intent from Saved Requests by checking and loading data
        CheckIntentIsLoadingFromSave();

        //gets any data from saved instance if exists
        if (savedInstanceState != null) {
            crimeLocationType = savedInstanceState.getParcelable(STATE_SELECTED_CL_TYPE);
            crimeLocationsRequestModel = savedInstanceState.getParcelable(STATE_CL_REQUEST_MODEL);
            mapViewOpen = savedInstanceState.getBoolean(STATE_MAP_VIEW_OPEN);
            Log.d(TAG, "loaded data from save instance");
        }

        //set action bar title
        setTitle(crimeLocationType.Name);

        //Init fragments
        InitFragments(savedInstanceState);

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

    /**
     * Overrides base method inflating special menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_extra, menu);
        return true;
    }


    /**
     * Overrides base method adding additional menu handlers on top of base functionality
     * @param item
     * @return
     */
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
                    CreateClearResultsDialog();
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

        //save current crime location type
        savedInstanceState.putParcelable(STATE_SELECTED_CL_TYPE, crimeLocationType);
        //save requestModel
        savedInstanceState.putParcelable(STATE_CL_REQUEST_MODEL, crimeLocationsRequestModel);
        //save mapViewOpen
        savedInstanceState.putBoolean(STATE_MAP_VIEW_OPEN, mapViewOpen);

    }

    //-------------------------------------------------------------------------------------------//

    /**
     * Prepares child fragments including which one displayed first
     * @param savedInstance
     */
    protected void InitFragments(Bundle savedInstance){

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
    }


    //Map fragment listener implements ----------------------------------------------------------//

    /**
     * Handles map fragment interface listener.
     * Starts Request for crime locations
     * @param position
     * @param radius
     */
    @Override
    public void onMapInputInteraction(LatLng position, Integer radius) {
        CreateRequestProgressDialog();
        GetCrimeLocationJson(position, radius);
    }

    /**
     * Handles map fragment interface listener.
     * sends map fragment relevant data. (part of larger feature where map fragment requests data
     * from parent activity once its ready rather than during on create
     */
    @Override
    public void onMapLoadSavedInstance(){
        SendMapInputResults();
    }

    /**
     * Handles map fragment interface listerner.
     * Allows map fragment to have access to 'isConnected' base method
     * @return
     */
    @Override
    public Boolean isConnectedToInternet(){
        return isConnected();
    }

    /**
     * Handles map fragment interface listener.
     * allows map fragment to call this activity to start intent to details activity
     * @param crimeLocationModel
     */
    @Override
    public void InitDetailsActivityFromMapFragment(CrimeLocationModel crimeLocationModel) {
        StartIntentToDetailsActivity(crimeLocationModel);
    }

    // Results list fragment listener implements -------------------------------------------------//

    /**
     * Handles list  fragment interface listener.
     * sends list fragment relevant data. (part of larger feature where list fragment requests data
     * from parent activity once its ready rather than during on create
     */
    @Override
    public void onListLoadSavedInstance(){
        if(crimeLocationsRequestModel != null){
            SendListViewResults();
        }
    }

    /**
     * Handles list fragment interface listener.
     * allows list fragment to call this activity to start intent to details activity
     * @param crimeLocationModel
     */
    @Override
    public void InitDetailsActivityFromListFragment(CrimeLocationModel crimeLocationModel){
        StartIntentToDetailsActivity(crimeLocationModel);
    }

    // Save Dialog fragment listener implements --------------------------------------------------//

    /**
     * Creates new save request,
     * checks for existing array of saved requests
     * adds new saved request to array then stores in memory
     * @param saveName
     */
    @Override
    public void onNewSave(String saveName){

        try {
            SaveModel saveModel = new SaveModel();
            saveModel.Name = saveName;
            saveModel.SaveDate = new Date();
            saveModel.CrimeLocationsRequestModel = crimeLocationsRequestModel;
            saveModel.CrimeLocationTypeModel = crimeLocationType;
            saveModel.SelectedLocation = mapInputFragment.selectedLocation;
            saveModel.SelectedRadius = mapInputFragment.selectedRadius;

            SaveArrayModel saveArrayModel;

            Gson gson = new Gson();

            if (StorageHelper.isSaveArrayDataInSharedPref(this)) {
                Log.d(TAG, "existing array");
                String saveArrayJSON = StorageHelper.RetrieveSaveArrayModelJSON(this).toString();
                saveArrayModel = gson.fromJson(saveArrayJSON, SaveArrayModel.class);
            } else {
                Log.d(TAG, "new array");
                saveArrayModel = new SaveArrayModel();
                saveArrayModel.SaveModels = new ArrayList<>();
            }

            saveArrayModel.SaveModels.add(saveModel);

            String jsonString = gson.toJson(saveArrayModel);
            StorageHelper.StoreSaveArrayModelJSON(this, jsonString);

            CreateSaveCompletionDialog(true);
        }
        catch(Exception e){
            CreateSaveCompletionDialog(false);
            //TODO error somewhere
        }

    }

    //--------------------------------------------------------------------------------------------//

    /**
     * Builds and sends request for crime-location request model data
     * @param position
     * @param radius
     */
    private void GetCrimeLocationJson(final LatLng position, final Integer radius) {

        String url = getUrlString(position, radius);

        //define request tag (for queue purposes)
        final String requestTag = "CRIME_LOCATIONS_REQUEST";
        //cancel any requests queued
        VolleyQueue.getInstance(this).cancelAllRequests(requestTag);

        Log.d(TAG, "New Request: " + url);
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

    /**
     * Builds url string for request
     * @param position
     * @param radius
     * @return
     */
    private String getUrlString(LatLng position, Integer radius){
        String urlString = getString(R.string.baseUrl) +
                getString(R.string.crimeLocationTypesCall) + "/" +
                String.valueOf(crimeLocationType.Id) + "/" +
                position.latitude + "/"+
                position.longitude + "/" +
                radius;

        return urlString;
    }

    /**
     * Parses "JSON" string to object model using GSON.
     * @param jsonObject
     * @return
     */
    private CrimeLocationsRequestModel ParseCrimeLocationsRequest(String jsonObject){
        Gson gson = new Gson();
        CrimeLocationsRequestModel model = gson.fromJson(jsonObject, CrimeLocationsRequestModel.class);
        return model;
    }

    /**
     * Sends Map Input Fragment results data using fragment public method
     */
    private void SendMapInputResults(){

        if(mapInputFragment != null){
            if(progressDialog != null){
                progressDialog.cancel();
            }

            if(locationFromSave != null && radiusFromSave != null){
                mapInputFragment.ProcessRequestResults(crimeLocationsRequestModel, locationFromSave, radiusFromSave);
                locationFromSave = null;
                radiusFromSave = null;
            }else {
                mapInputFragment.ProcessRequestResults(crimeLocationsRequestModel);
            }
        }
    }

    /**
     * Sends List View Fragment results data using fragment public method
     */
    private void SendListViewResults(){

        if(resultsListFragment != null){
            if(progressDialog != null){
                progressDialog.cancel();
            }
            resultsListFragment.ProcessNewRequestResults(crimeLocationsRequestModel);
        }
    }

    /**
     * Prepares Intent to Details Activity
     * @param crimeLocationModel
     */
    protected void StartIntentToDetailsActivity(CrimeLocationModel crimeLocationModel){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.intent_selected_crimeLocationModel), crimeLocationModel);
        intent.putExtra(getString(R.string.intent_num_locations), crimeLocationsRequestModel.CrimeLocations.size());
        startActivity(intent);
    }

    /**
     * Clears results data from this activity and child fragments
     */
    private void ClearResults(){
        mapInputFragment.ClearResults();
        resultsListFragment.ClearResults();
        crimeLocationsRequestModel = null;
        //should be cleared but double check with these
        locationFromSave = null;
        radiusFromSave = null;
    }


    // DIALOGS -----------------------------------------------------------------------------------//

    /**
     * Creates Error dialog for failed requests
     * @param str
     */
    private void CreateErrorDialog(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.request_error_dialog_title);
        builder.setMessage(str);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates Progress Dialog during a request.
     * TODO improve it
     */
    private void CreateRequestProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.request_progress_dialog_message));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Creates Confirmation dialog for "clearing" results
     */
    private void CreateClearResultsDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MapSearchActivity.this);

        builder.setMessage(R.string.clear_results_dialog_message);

        builder.setPositiveButton(R.string.clear_results_dialog_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ClearResults();
            }
        });
        builder.setNegativeButton(R.string.clear_results_dialog_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog clearResultsDialog = builder.create();
        clearResultsDialog.show();
    }

    /**
     * Creates SaveDialog Fragment for saving request to memory
     */
    private void CreateSaveDialogFragment(){
        if(crimeLocationsRequestModel != null){
            SaveDialogFragment saveDialogFragment = SaveDialogFragment.newInstance(crimeLocationType, crimeLocationsRequestModel);
            saveDialogFragment.show(getFragmentManager(), "Save Dialog Fragment");
        }
    }

    /**
     * Creates dialog to show user the action they want doesnt do anything
     * @param todo
     */
    private void CreateNothingToDoDialog(String todo){

        AlertDialog.Builder builder = new AlertDialog.Builder(MapSearchActivity.this);

        String message = "Nothing to " + todo + ". Use map to start a search";

        builder.setMessage(message);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     *
     * @param successfulSave
     */
    private void CreateSaveCompletionDialog(boolean successfulSave){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(successfulSave){
            builder.setMessage(R.string.save_completion_message_success);
        }
        else{
            builder.setTitle(R.string.save_completion_title_error);
            builder.setMessage(R.string.save_completion_message_error);
        }

        builder.setCancelable(true);

        AlertDialog saveCompletionDialog = builder.create();
        saveCompletionDialog.show();
    }

    //--------------------------------------------------------------------------------------------//

    /**
     * Checks if Intetn has data for loading a request (aka from saved request activity)
     */
    private void CheckIntentIsLoadingFromSave(){
        if(intent.hasExtra(getString(R.string.intent_crimeLocationRequestModel))
                && intent.hasExtra(getString(R.string.intent_location_from_save))
                && intent.hasExtra(getString(R.string.intent_radius_from_save))){

            Log.d(TAG, "loading save from intent");
            crimeLocationsRequestModel = intent.getExtras().getParcelable(getString(R.string.intent_crimeLocationRequestModel));
            locationFromSave = intent.getExtras().getParcelable(getString(R.string.intent_location_from_save));
            radiusFromSave = intent.getExtras().getInt(getString(R.string.intent_radius_from_save));
        }
    }
}

