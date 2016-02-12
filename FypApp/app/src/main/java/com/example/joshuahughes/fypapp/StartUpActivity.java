package com.example.joshuahughes.fypapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StartUpActivity extends AppCompatActivity {

    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        TextView loadingText = (TextView) findViewById(R.id.loadingProgressText);

        loadingText.setText(R.string.loadingProgress_InternetConnection);
        boolean isConnected = isConnected();

        intent = new Intent(this, DashBoardActivity.class);

        if(isConnected) {
            startActivity(intent);
        }
        else {
            createAlertDialog();
        }

        finish(); //finish so this cant be returned to
    }

    private void createAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.loadingProgress_NoInternetMessage);

        builder.setCancelable(false);
        builder.setPositiveButton(
                "Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(intent);
                    }
                }
        );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private boolean isConnected(){

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }
}
