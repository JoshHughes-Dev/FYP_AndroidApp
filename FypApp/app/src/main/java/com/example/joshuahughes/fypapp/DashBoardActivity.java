package com.example.joshuahughes.fypapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        String jsonString = GetCrimeLocationTypesFromStorage().toString();
        TextView textview = (TextView)findViewById(R.id.textView2);
        textview.setText(jsonString);

        CreateListView(GetCrimeLocationTypesFromStorage());

        Boolean isConnected = intent.getExtras().getBoolean("isConnected");
        if(!isConnected){
            createOfflineModeDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
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

    private JSONArray GetCrimeLocationTypesFromStorage(){

        SharedPreferences settings = getSharedPreferences("CrimeLocationTypesJSON", 0);
        String strJson = settings.getString("crimeLocationTypesJson", "0");

        JSONArray jsonArray = new JSONArray();
        JSONObject tempObject;

        if(strJson != null){
            try {
                tempObject = new JSONObject(strJson);

                try{
                    jsonArray = tempObject.getJSONArray("ClrArray");

                } catch (JSONException e){
                    Log.d("JSONArray", e.toString());
                }

            } catch (JSONException e){
                Log.d("JSONobject", e.toString());
            }

        }

        return jsonArray;
    }


    private void CreateListView(JSONArray jsonArray){

        ListView listView = (ListView) findViewById(R.id.listView);
        CLAdapter adapter = new CLAdapter(DashBoardActivity.this, jsonArray);


        listView.setAdapter(adapter);
    }


    private class CLAdapter extends JSONAdapter {


        public CLAdapter(Activity activity, JSONArray jsonArray){
            super(activity,jsonArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = activity.getLayoutInflater().inflate(R.layout.row, null);

            TextView text = (TextView) convertView.findViewById(R.id.label);


            JSONObject json_data = getItem(position);
            if (null != json_data) {
                try {
                    String jj = json_data.getString("name");
                    text.setText(jj);

                } catch (JSONException e) {
                    Log.d("BLARDGGH", e.toString());
                }

            }
            return convertView;
        }

    }

}
