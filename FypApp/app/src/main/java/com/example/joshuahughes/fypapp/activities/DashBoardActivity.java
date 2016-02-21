package com.example.joshuahughes.fypapp.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class DashBoardActivity extends AppCompatActivity {


    protected RecyclerView mRecyclerView;
    protected CrimeLocationTypesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        String jsonString = GetCrimeLocationTypesFromStorage().toString();
        TextView textview = (TextView)findViewById(R.id.textView2);
        textview.setText(jsonString);

        ArrayList<CrimeLocationTypeModel> clTarray = createCLTarray(jsonString);
        
        CreateRecyclerView(clTarray);


        Boolean isConnected = intent.getExtras().getBoolean("isConnected");
        if(!isConnected){
            createOfflineModeDialog();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    private void createOfflineModeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.loadingProgress_NoInternetMessage);
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Continue",
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

    private JSONArray GetCrimeLocationTypesFromStorage(){

        SharedPreferences settings = getSharedPreferences("CrimeLocationTypesJSON", 0);
        String strJson = settings.getString("crimeLocationTypesJson", "0");

        JSONArray jsonArray = new JSONArray();
        JSONObject tempObject;

        if(strJson != null){
            try {
                tempObject = new JSONObject(strJson);

                try{
                    jsonArray = tempObject.getJSONArray(getString(R.string.ClTypesStorage));

                } catch (JSONException e){
                    Log.d("JSONArray", e.toString());
                }

            } catch (JSONException e){
                Log.d("JSONobject", e.toString());
            }

        }

        return jsonArray;
    }


    private void CreateRecyclerView(ArrayList<CrimeLocationTypeModel> clTypesArray){

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CrimeLocationTypesAdapter(clTypesArray);
        mRecyclerView.setAdapter(mAdapter);
    }


    private ArrayList<CrimeLocationTypeModel> createCLTarray(String jsonArray){
        ArrayList<CrimeLocationTypeModel> crimeLocationTypeModelArrayList = new ArrayList<>();

        Gson gson = new Gson();

        Type arrayType = new TypeToken<ArrayList<CrimeLocationTypeModel>>() {}.getType();
        crimeLocationTypeModelArrayList = gson.fromJson(jsonArray,arrayType);
        

        return crimeLocationTypeModelArrayList;
    }


}
