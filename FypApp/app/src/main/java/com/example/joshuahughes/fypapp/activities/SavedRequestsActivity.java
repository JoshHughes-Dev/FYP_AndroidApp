package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.adapters.SaveModelsAdapter;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;
import com.example.joshuahughes.fypapp.models.SaveArrayModel;
import com.example.joshuahughes.fypapp.models.SaveModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SavedRequestsActivity extends BaseActivity {


    protected ListView saveRequestsListView;
    protected SaveArrayModel saveArrayModel;
    protected SaveModelsAdapter saveModelsAdapter;

    private static final String TAG = "SavedRequestsActivity";
    private static final String STATE_SAVE_ARRAY = "saveArrayModel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Saved Requests");


        if (savedInstanceState != null) {
            saveArrayModel = savedInstanceState.getParcelable(STATE_SAVE_ARRAY);
            Log.d(TAG, "loaded data from save instance");
        }
        else{
            String jsonString = StorageHelper.RetrieveSaveArrayModelJSON(this).toString();
            saveArrayModel = createSaveArrayModelObject(jsonString);
        }


        if(saveArrayModel != null && saveArrayModel.SaveModels != null) {
            CreateListView(saveArrayModel.SaveModels);
        }

    }

    //SETS no options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    private void CreateListView(final ArrayList<SaveModel> saveModelsArray){

        saveRequestsListView = (ListView)findViewById(R.id.saved_requests_list_view);
        saveModelsAdapter = new SaveModelsAdapter(this,saveModelsArray);
        saveRequestsListView.setAdapter(saveModelsAdapter);
        saveRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SaveModel saveModel = (SaveModel) parent.getAdapter().getItem(position);
            StartIntentToDetailsActivity(saveModel);
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //save crime location types array list
        savedInstanceState.putParcelable(STATE_SAVE_ARRAY, saveArrayModel);
    }


    private SaveArrayModel createSaveArrayModelObject (String jsonObject){

        Gson gson = new Gson();
        return gson.fromJson(jsonObject, SaveArrayModel.class);
    }

    protected void StartIntentToDetailsActivity(SaveModel saveModel){

        Intent intent = new Intent(this, MapSearchActivity.class);

        intent.putExtra(getString(R.string.intent_selected_crimeLocationType), saveModel.CrimeLocationTypeModel);
        intent.putExtra(getString(R.string.intent_crimeLocationRequestModel), saveModel.CrimeLocationsRequestModel);
        intent.putExtra(getString(R.string.intent_location_from_save), saveModel.SelectedLocation);
        intent.putExtra(getString(R.string.intent_radius_from_save), saveModel.SelectedRadius);

        // this clears previous map search activity from stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    public void UpdateSavedModelsStorage(){

        Gson gson = new Gson();
        String jsonString = gson.toJson(saveArrayModel);
        StorageHelper.StoreSaveArrayModelJSON(this, jsonString);
    }
}
