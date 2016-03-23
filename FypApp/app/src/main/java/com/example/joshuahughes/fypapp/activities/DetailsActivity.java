package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.YearMonthComparator;
import com.example.joshuahughes.fypapp.adapters.CrimesAdapter;
import com.example.joshuahughes.fypapp.fragments.CrimeGraphFragment;
import com.example.joshuahughes.fypapp.fragments.CrimesListFragment;
import com.example.joshuahughes.fypapp.helpers.myHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DetailsActivity extends BaseActivity implements OnMapReadyCallback, CrimesListFragment.OnFragmentInteractionListener, CrimeGraphFragment.OnFragmentInteractionListener{

    private CrimeLocationModel crimeLocationModel;
    private GoogleMap mMap;
    private TableLayout detailsTable;

    private CrimesListFragment crimesListFragment;
    private CrimeGraphFragment crimesGraphFragment;

    private int numberOfLocationResults = 0;
    private Boolean listOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24px);

        Intent intent = getIntent();

        crimeLocationModel = intent.getExtras().getParcelable("selectedCrimeLocationModel");
        numberOfLocationResults = intent.getIntExtra("numberOfLocationResults", 1);

        if (savedInstanceState != null) {
            crimeLocationModel = savedInstanceState.getParcelable("selectedCrimeLocationModel");
            numberOfLocationResults = savedInstanceState.getInt("numberOfLocationResults");
            Log.d("DetailsActivity", "loaded data from save instance");
        }

        setTitle(crimeLocationModel.Location.Name);


        //sort crimeLocationModel
        Collections.sort(crimeLocationModel.Crimes, new YearMonthComparator());

        //Init map fragment
        MapFragment liteMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.details_lite_map);
        liteMapFragment.getMapAsync(this);

        //populate details table
        detailsTable = (TableLayout) findViewById(R.id.tableLayout_details);
        createRankDetailTableRow();
        createCrimeDetailTableRow();
        createBadgeDetailTableRow();
        createDistanceDetailTableRow();

        //create crimes list fragment
        crimesListFragment = (CrimesListFragment) getSupportFragmentManager().findFragmentById(R.id.crimes_list_fragment);
        crimesGraphFragment = (CrimeGraphFragment) getSupportFragmentManager().findFragmentById(R.id.crimes_graph_fragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(listOpen){
            ft.hide(crimesGraphFragment);
        }
        else{
            ft.hide(crimesListFragment);
        }
        ft.commit();


        Button toggleButton = (Button) findViewById(R.id.listGraph_toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleGraphListView();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable("selectedCrimeLocationModel", crimeLocationModel);
        savedInstanceState.putInt("numberOfLocationResults", numberOfLocationResults);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng locationCoords = new LatLng(crimeLocationModel.Location.Latitude, crimeLocationModel.Location.Longitude);
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCoords, 16));
        mMap.addMarker(new MarkerOptions().position(locationCoords));
    }



    @Override
    public void onListLoadSavedInstance(){

        if(crimeLocationModel != null){
            if(crimesListFragment != null){
                crimesListFragment.CreateCrimesListView(crimeLocationModel);
            }
        }
    }

    @Override
    public void onGraphLoadSavedInstance(){

        if(crimeLocationModel != null){
            if(crimesListFragment != null) {
                crimesGraphFragment.CreateGraph(crimeLocationModel);
            }
        }
    }



    private void createRankDetailTableRow(){

        View tr = getLayoutInflater().inflate(R.layout.tablerow_details_item, null, false);

        TextView detailView = (TextView)tr.findViewById(R.id.table_row_detail);
        detailView.setText("Rank " + Integer.toString(crimeLocationModel.Rank));

        TextView detailSubView = (TextView)tr.findViewById(R.id.table_row_subDetail);
        detailSubView.setText("...of " + numberOfLocationResults + " results");

        detailsTable.addView(tr);
    }

    private void createCrimeDetailTableRow(){

        View tr = getLayoutInflater().inflate(R.layout.tablerow_details_item, null, false);

        TextView detailView = (TextView)tr.findViewById(R.id.table_row_detail);
        detailView.setText(Integer.toString(crimeLocationModel.Crimes.size())  + " crime(s) recorded");

        TextView detailSubView = (TextView)tr.findViewById(R.id.table_row_subDetail);
        detailSubView.setText("..in the past 12 months");

        detailsTable.addView(tr);
    }

    private void createBadgeDetailTableRow(){

        View tr = getLayoutInflater().inflate(R.layout.tablerow_details_item, null, false);

        ImageView iconView = (ImageView) tr.findViewById(R.id.table_row_icon);
        myHelper.SetBadgeIcon(iconView, crimeLocationModel.Badge);

        ArrayList<String> arrayList = myHelper.GetBadgeTitleAndDescription(crimeLocationModel.Badge);

        TextView detailView = (TextView)tr.findViewById(R.id.table_row_detail);
        detailView.setText(arrayList.get(0));

        TextView detailSubView = (TextView)tr.findViewById(R.id.table_row_subDetail);
        detailSubView.setText(arrayList.get(1));

        detailsTable.addView(tr);
    }

    private void createDistanceDetailTableRow(){

        View tr = getLayoutInflater().inflate(R.layout.tablerow_details_item, null, false);

        ImageView iconView = (ImageView) tr.findViewById(R.id.table_row_icon);
        iconView.setImageResource(R.drawable.ic_pin_drop_24dp);

        TextView detailView = (TextView)tr.findViewById(R.id.table_row_detail);
        detailView.setText(Integer.toString(crimeLocationModel.Distance) + " meters");

        TextView detailSubView = (TextView)tr.findViewById(R.id.table_row_subDetail);
        detailSubView.setText("...from starting search point");

        detailsTable.addView(tr);
    }


    private void ToggleGraphListView(){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(listOpen){
            //change to graph
            ft.show(crimesGraphFragment);
            ft.hide(crimesListFragment);
            listOpen = false;
        }
        else{
            //change to map view
            ft.hide(crimesGraphFragment);
            ft.show(crimesListFragment);
            listOpen = true;
        }

        ft.commit();
    }

}
