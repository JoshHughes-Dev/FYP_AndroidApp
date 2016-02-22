package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;

public class MapSearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        CrimeLocationTypeModel crimeLocationType = intent.getExtras().getParcelable("selectedCrimeLocationType");

        setTitle(crimeLocationType.Name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Will open 'save to favourites' dialog", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String testUrl = getString(R.string.baseUrl) + getString(R.string.crimeLocationTypesCall) + "/" + String.valueOf(crimeLocationType.Id) + "/" + "51.510343/-0.073214/50";

        TextView textview = (TextView) findViewById(R.id.textView3);
        textview.setText(testUrl);
    }

}
