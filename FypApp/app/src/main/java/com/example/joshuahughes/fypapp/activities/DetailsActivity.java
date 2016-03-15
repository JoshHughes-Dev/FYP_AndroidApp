package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.helpers.myHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends BaseActivity implements OnMapReadyCallback {

    private CrimeLocationModel crimeLocationModel;
    private GoogleMap mMap;
    private TableLayout detailsTable;
    private int numberOfLocationResults = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        crimeLocationModel = intent.getExtras().getParcelable("selectedCrimeLocationModel");
        numberOfLocationResults = intent.getIntExtra("numberOfLocationResults", 1);

        setTitle(crimeLocationModel.Location.Name);

        //Init map fragment
        MapFragment liteMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.details_lite_map);
        liteMapFragment.getMapAsync(this);

        detailsTable = (TableLayout) findViewById(R.id.tableLayout_details);

        createRankDetailTableRow();
        createCrimeDetailTableRow();
        createBadgeDetailTableRow();
        createDistanceDetailTableRow();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng locationCoords = new LatLng(crimeLocationModel.Location.Latitude, crimeLocationModel.Location.Longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCoords, 15));

        mMap.addMarker(new MarkerOptions().position(locationCoords));

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

}
