package com.example.joshuahughes.fypapp.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.SimpleDividerItemDecoration;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



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

        String jsonString = StorageHelper.RetrieveCrimeLocationTypesJSON(this).toString();
        TextView textview = (TextView)findViewById(R.id.textView2);
        textview.setText(jsonString);

        ArrayList<CrimeLocationTypeModel> clTarray = createCLTarray(jsonString);
        
        CreateRecyclerView(clTarray);


        Boolean isConnected = intent.getExtras().getBoolean("isConnected");
        if(!isConnected){
            createOfflineModeDialog();
        }

    }

    //SETS options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        Intent intent;

        switch(item.getItemId()){
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
                break;
            case R.id.savedRequests:
                //intent = new Intent(this, null);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        return true;

    }


    private void createOfflineModeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No internet Connection");
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


    private void CreateRecyclerView(ArrayList<CrimeLocationTypeModel> clTypesArray){

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(DashBoardActivity.this));
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CrimeLocationTypesAdapter(clTypesArray);
        mRecyclerView.setAdapter(mAdapter);
    }


    private ArrayList<CrimeLocationTypeModel> createCLTarray(String jsonArray){

        Gson gson = new Gson();

        Type arrayType = new TypeToken<ArrayList<CrimeLocationTypeModel>>() {}.getType();
        ArrayList<CrimeLocationTypeModel> crimeLocationTypeModelArrayList = gson.fromJson(jsonArray, arrayType);

        return crimeLocationTypeModelArrayList;
    }


}

