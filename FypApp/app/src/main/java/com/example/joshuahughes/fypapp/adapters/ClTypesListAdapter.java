package com.example.joshuahughes.fypapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joshuahughes on 16/02/16.
 */
public class ClTypesListAdapter extends JSONAdapter {

    public ClTypesListAdapter(Activity activity, JSONArray jsonArray){
        super(activity,jsonArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.row, null);
        }

        final Context context = convertView.getContext();

        TextView text = (TextView) convertView.findViewById(R.id.label);
        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.cltInfoButton);

        final JSONObject json_data = getItem(position);

        if (null != json_data) {
            try {
                String cltName = json_data.getString("name");
                text.setText(cltName);

                imageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        cltInfoDialog(context, json_data);
                    }
                });




            } catch (JSONException e) {
                Log.d("JSON exception row", e.toString());
            }

        }
        return convertView;
    }

    private void cltInfoDialog(Context context, JSONObject jsonObject){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String dialogMessage = "";

        try{
            String LocationInfo = "Location type: "  + jsonObject.getString("locationType");
            String CrimeInfo = "Crime Types: ";
            JSONArray crimeTypes = jsonObject.getJSONArray("crimeTypes");

            for(int i = 0; i < crimeTypes.length(); i++){
                CrimeInfo += crimeTypes.getString(i);
                if(i+1 != crimeTypes.length()){
                    CrimeInfo += ", ";
                }
            }

            dialogMessage = LocationInfo + "\n" + CrimeInfo;

        } catch (JSONException e){
            Log.d("asdf", e.toString());
        }

        builder.setMessage(dialogMessage);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
