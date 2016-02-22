package com.example.joshuahughes.fypapp.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.RecyclerItemClickListener;
import com.example.joshuahughes.fypapp.SimpleDividerItemDecoration;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter2;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.lang.reflect.Type;
import java.util.ArrayList;


public class DashBoardActivity extends BaseActivity {

    //protected RecyclerView mRecyclerView;
    //protected CrimeLocationTypesAdapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;

    protected ListView mListView;
    protected CrimeLocationTypesAdapter2 mAdapter2;


    protected ArrayList<CrimeLocationTypeModel> clTypesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets content view for activity
        setContentView(R.layout.activity_dash_board);
        //sets toolbar view for activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        Intent intent = getIntent();

        String jsonString = StorageHelper.RetrieveCrimeLocationTypesJSON(this).toString();
        TextView textview = (TextView)findViewById(R.id.textView2);
        textview.setText(jsonString);

        clTypesArray = createCLTarray(jsonString);

        CreateListView(clTypesArray);
        //CreateRecyclerView(clTypesArray);


        Boolean isConnected = intent.getExtras().getBoolean("isConnected");
        if(!isConnected){
            createOfflineModeDialog();
        }

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


//    private void CreateRecyclerView(final ArrayList<CrimeLocationTypeModel> clTypesArray){
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(DashBoardActivity.this));
//        mRecyclerView.setHasFixedSize(true);
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        // specify an adapter (see also next example)
//        mAdapter = new CrimeLocationTypesAdapter(clTypesArray);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener(){
//                @Override
//                public void onItemClick(View view, int position){
//                    Log.d("RECYCLER_ITEM_CLICKED", clTypesArray.get(position).Name.toString());
//                }
//            })
//        );
//    }


    private void CreateListView(final ArrayList<CrimeLocationTypeModel> clTypesArray){

        mListView = (ListView)findViewById(R.id.listView);
        mAdapter2 = new CrimeLocationTypesAdapter2(this,clTypesArray);
        mListView.setAdapter(mAdapter2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("LIST_ITEM_CLICKED", clTypesArray.get(position).Name.toString());
            }
        });
    }


    private ArrayList<CrimeLocationTypeModel> createCLTarray(String jsonArray){

        Gson gson = new Gson();

        Type arrayType = new TypeToken<ArrayList<CrimeLocationTypeModel>>() {}.getType();
        ArrayList<CrimeLocationTypeModel> crimeLocationTypeModelArrayList = gson.fromJson(jsonArray, arrayType);

        return crimeLocationTypeModelArrayList;
    }


}

