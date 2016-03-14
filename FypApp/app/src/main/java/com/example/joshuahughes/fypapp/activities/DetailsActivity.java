package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;

public class DetailsActivity extends BaseActivity {

    private CrimeLocationModel crimeLocationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        crimeLocationModel = intent.getExtras().getParcelable("selectedCrimeLocationModel");

        setTitle(crimeLocationModel.Location.Name);
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//
//        return true;
//
//    }

}
