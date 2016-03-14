package com.example.joshuahughes.fypapp.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.VolleyQueue;
import com.example.joshuahughes.fypapp.helpers.StorageHelper;

import org.json.JSONArray;


public class StartUpActivity extends AppCompatActivity {

    protected Intent intent;
    protected boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(this, DashBoardActivity.class);

        isConnected = isConnected();

        if (isConnected) {
            GetCrimeLocationTypesJson();
        } else if(StorageHelper.isClTypesDataInSharedPref(StartUpActivity.this)) {
            FinishStartup();
        } else{
            UnableToLaunchDialog();
        }
    }


    private boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }

    private void GetCrimeLocationTypesJson() {

        final String url =  getString(R.string.baseUrl) + getString(R.string.crimeLocationTypesCall);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("StartUp_log", response.toString());
                    StorageHelper.StoreCrimeLocationTypesJSON(StartUpActivity.this, response);
                    FinishStartup();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("StartUp_log", error.toString());
                    FinishStartup();
                }
            }
        );

        //set request queue policy
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                        6000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        VolleyQueue.getInstance(this).addToRequestQueue(getRequest);
    }


    private void FinishStartup(){

        intent.putExtra("isConnected", isConnected);
        startActivity(intent);
        finish();
    }


    private void UnableToLaunchDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("App requires internet connection for first use, please relaunch app when connected to the internet");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Exit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        System.exit(0);
                    }
                }
        );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
