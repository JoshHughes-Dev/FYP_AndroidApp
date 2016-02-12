package com.example.joshuahughes.fypapp;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

public class StartUpActivity extends AppCompatActivity {

    protected Intent intent;
    protected boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isConnected = isConnected();

        intent = new Intent(this, DashBoardActivity.class);

        if (isConnected) {
            VolleyTest();
        } else {
            FinishStartup(null);
        }
    }


    private boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }

    private void VolleyTest() {
        //RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "http://co-web-1.lboro.ac.uk/cojh5web/api/crimelocation";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());

                        FinishStartup(response);
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }
            }
        );

        //queue.add(getRequest);
        MySingleton.getInstance(this).addToRequestQueue(getRequest);
    }


    private void FinishStartup(JSONArray jsonArray) {
        if(jsonArray != null){
            intent.putExtra("crimeLocationTypes", jsonArray.toString());
        }
        intent.putExtra("isConnected", isConnected);
        startActivity(intent);

        finish();
    }

}
