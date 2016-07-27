package org.crimelocation.fypapp.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.crimelocation.fypapp.R;
import org.crimelocation.fypapp.VolleyQueue;
import org.crimelocation.fypapp.helpers.StorageHelper;

import org.json.JSONArray;


public class StartUpActivity extends BaseActivity {

    protected Intent intent;
    private static final String TAG = "StartUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets content view for activity
        setContentView(R.layout.activity_start_up);

        //Prepare Intent destination
        intent = new Intent(this, DashBoardActivity.class);

        //Get network connection status
        boolean isConnected = isConnected();

        if (isConnected) {
            GetCrimeLocationTypesJson();
        } else if(StorageHelper.isClTypesDataInSharedPref(StartUpActivity.this)) {
            FinishStartup(false);
        } else{
            UnableToLaunchDialog();
        }
    }

    //SETS no options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    private void GetCrimeLocationTypesJson() {

        final String url =  getString(R.string.baseUrl) + getString(R.string.crimeLocationTypesCall);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    //Log.d("StartUp_log", response.toString());
                    StorageHelper.StoreCrimeLocationTypesJSON(StartUpActivity.this, response);
                    FinishStartup(true);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("StartUp_log", error.toString());
                    FinishStartup(error);
                }
            }
        );

        //set request queue policy
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
            6000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleyQueue.getInstance(this).addToRequestQueue(getRequest);
    }


    private void FinishStartup(boolean updateOccured){

        intent.putExtra(getString(R.string.intent_start_up_update), updateOccured);
        startActivity(intent);
        finish();
    }

    private void FinishStartup(VolleyError error){
        intent.putExtra(getString(R.string.intent_start_up_error), error);
        FinishStartup(false);
    }


    private void UnableToLaunchDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.unable_to_launch_dialog_title);
        builder.setMessage(R.string.unable_to_launch_dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(
                getString(R.string.unable_to_launch_dialog_positive),
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
