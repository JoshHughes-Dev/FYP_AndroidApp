package org.crimelocation.fypapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.crimelocation.fypapp.R;
import org.crimelocation.fypapp.models.CrimeLocationTypeModel;

import java.util.ArrayList;

/**
 * Created by joshuahughes on 22/02/16.
 * Adapter for list view using Crime-Location Types
 */
public class CrimeLocationTypesAdapter extends ArrayAdapter<CrimeLocationTypeModel> {

    private final Context context;
    private ArrayList<CrimeLocationTypeModel> clTypesArray;

    public CrimeLocationTypesAdapter(Context c, ArrayList<CrimeLocationTypeModel> array){
        super(c, -1, array);
        this.context = c;
        this.clTypesArray = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflator.inflate(R.layout.list_row_cl_types, parent, false);

        final CrimeLocationTypeModel model = clTypesArray.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.clTypeName);
        textView.setText(model.Name);


        ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.clTypeInfoButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cltInfoDialog(view.getContext(), model);
            }
        });

        return rowView;

    }

    private void cltInfoDialog(Context context, CrimeLocationTypeModel model){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String LocationInfo = "Location type: "  + model.LocationType;
        String CrimeInfo = "Crime Types: ";

        for(int i = 0; i < model.CrimeTypes.size(); i++){
            CrimeInfo += model.CrimeTypes.get(i);
            if(i+1 != model.CrimeTypes.size()){
                CrimeInfo += ", ";
            }
        }

        builder.setTitle("Crime-location type information");
        builder.setMessage(LocationInfo + "\n" + CrimeInfo);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}