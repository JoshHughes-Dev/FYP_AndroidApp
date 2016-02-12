package com.example.joshuahughes.fypapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        String jsonString = intent.getStringExtra("crimeLocationTypes");
        TextView textview = (TextView)findViewById(R.id.textView2);
        textview.setText(jsonString);

        Boolean isConnected = intent.getExtras().getBoolean("isConnected");

        if(!isConnected){
            TextView textview0 =  (TextView)findViewById(R.id.textView);
            textview0.setText("OFFLINE MODE");
            createOfflineModeDialog();
        }
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

}
