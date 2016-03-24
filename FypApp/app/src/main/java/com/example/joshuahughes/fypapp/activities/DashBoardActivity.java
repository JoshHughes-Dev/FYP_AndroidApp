package com.example.joshuahughes.fypapp.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class DashBoardActivity extends BaseActivity {


    protected ListView clTypesListView;
    protected CrimeLocationTypesAdapter clTypesListAdapter;
    protected ArrayList<CrimeLocationTypeModel> clTypesArrayList;

    private static final String TAG = "DashBoardActivity";

    private static final String STATE_CL_TYPES = "clTypesArrayList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets content view for activity
        setContentView(R.layout.activity_dash_board);

        //sets toolbar view for activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        //get crime location types data either from saved instance or from storage
        if (savedInstanceState != null) {
            clTypesArrayList = savedInstanceState.getParcelableArrayList(STATE_CL_TYPES);
        }
        else {
            String jsonString = StorageHelper.RetrieveCrimeLocationTypesJSON(this).toString();
            clTypesArrayList = CreateClTypeArrayList(jsonString);
        }

        //Create and populate list view
        CreateListView(clTypesArrayList);


        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.intent_start_up_update))){
            StartUpUpdateToast(intent.getExtras().getBoolean(getString(R.string.intent_start_up_update)));

            if(intent.hasExtra(getString(R.string.intent_start_up_error))){
                VolleyError error = intent.getExtras().getParcelable(getString(R.string.intent_start_up_error));
                //TODO use for log
            }
        }

        if(!isConnected()){
            CreateOfflineModeDialog();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //save crime location types array list
        savedInstanceState.putParcelableArrayList(STATE_CL_TYPES, clTypesArrayList);
    }


    /**
     * Creates adapter and populates list view with crime location types data.
     * @param clTypesArray
     */
    private void CreateListView(final ArrayList<CrimeLocationTypeModel> clTypesArray){

        clTypesListView = (ListView)findViewById(R.id.dash_board_cl_types_list_view);
        clTypesListAdapter = new CrimeLocationTypesAdapter(this,clTypesArray);
        clTypesListView.setAdapter(clTypesListAdapter);
        clTypesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CrimeLocationTypeModel selectedClTypeModel = clTypesArray.get(position);
                //Log.d("LIST_ITEM_CLICKED", selectedClTypeModel.Name.toString());
                InitialiseForwardIntent(selectedClTypeModel);
            }
        });
    }


    /**
     * Converts "JSON" string into Array list using GSON
     * @param jsonArray
     * @return
     */
    private ArrayList<CrimeLocationTypeModel> CreateClTypeArrayList(String jsonArray){

        Gson gson = new Gson();
        Type arrayType = new TypeToken<ArrayList<CrimeLocationTypeModel>>() {}.getType();
        return gson.fromJson(jsonArray, arrayType);
    }

    /**
     * Creates Intent to next activity, passes selected crime-location type with it
     * @param crimeLocationTypeModel
     */
    private void InitialiseForwardIntent(CrimeLocationTypeModel crimeLocationTypeModel){

        Intent intent = new Intent(DashBoardActivity.this, MapSearchActivity.class);
        intent.putExtra(getString(R.string.intent_selected_crimeLocationType), crimeLocationTypeModel);
        startActivity(intent);
    }

    /**
     * Creates and shows "Offline" Dialog View
     */
    private void CreateOfflineModeDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.offline_dialog_title);
        builder.setMessage(R.string.offline_dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(
                getString(R.string.offline_dialog_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates toast to show crime location types were successfully retrieved and stored in memory
     */
    private void StartUpUpdateToast(boolean successfulUpdate){

        Toast toast;

        if(successfulUpdate) {
            toast = Toast.makeText(this, getString(R.string.start_up_update_toast_message_success), Toast.LENGTH_SHORT);
        }
        else{
            toast = Toast.makeText(this, getString(R.string.start_up_update_toast_message_fail), Toast.LENGTH_SHORT);
        }

        toast.show();
    }
}

