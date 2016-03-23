package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter2;
import com.example.joshuahughes.fypapp.adapters.SaveModelsAdapter;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.example.joshuahughes.fypapp.models.SaveArrayModel;
import com.example.joshuahughes.fypapp.models.SaveModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SavedRequestsActivity extends BaseActivity {


    protected ListView saveRequestsListView;
    protected SaveArrayModel saveArrayModel;
    protected SaveModelsAdapter saveModelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            saveArrayModel = savedInstanceState.getParcelable("crimeLocationRequestModel");
            Log.d("SavedRequestsActivity", "loaded data from save instance");
        }
        else{
            String jsonString = StorageHelper.RetrieveSaveArrayModelJSON(this).toString();
            saveArrayModel = createSaveArrayModelObject(jsonString);
        }

        CreateListView(saveArrayModel.SaveModels);

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


    private SaveArrayModel createSaveArrayModelObject (String jsonObject){

        Gson gson = new Gson();
        SaveArrayModel saveArrayModel = gson.fromJson(jsonObject, SaveArrayModel.class);
        return saveArrayModel;
    }

    protected void StartIntentToDetailsActivity(SaveModel saveModel){

        Intent intent = new Intent(this, MapSearchActivity.class);
        intent.putExtra("selectedCrimeLocationType", saveModel.CrimeLocationTypeModel);
        intent.putExtra("crimeLocationRequestModel", saveModel.CrimeLocationsRequestModel);
        startActivity(intent);
        finish();
    }
}
