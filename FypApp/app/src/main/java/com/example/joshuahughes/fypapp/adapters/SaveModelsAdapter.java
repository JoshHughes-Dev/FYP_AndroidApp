package com.example.joshuahughes.fypapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.activities.DetailsActivity;
import com.example.joshuahughes.fypapp.models.SaveModel;



import java.util.ArrayList;

/**
 * Created by joshuahughes on 23/03/2016.
 */
public class SaveModelsAdapter extends ArrayAdapter<SaveModel> {

    private final Context context;
    private ArrayList<SaveModel> saveModelArray;

    public SaveModelsAdapter(Context c, ArrayList<SaveModel> array){
        super(c, -1, array);
        this.context = c;
        this.saveModelArray = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflator.inflate(R.layout.list_row_save_models, parent, false);

        final SaveModel model = saveModelArray.get(position);

        TextView nameView = (TextView) rowView.findViewById(R.id.list_row_save_model_name);
        nameView.setText(model.Name);

        TextView dateView = (TextView) rowView.findViewById(R.id.list_row_save_model_date);
        dateView.setText(model.SaveDate.toString());

        TextView crimeTypeView = (TextView) rowView.findViewById(R.id.list_row_save_model_crime_type);
        crimeTypeView.setText(model.CrimeLocationTypeModel.Name);

        TextView crimesView = (TextView) rowView.findViewById(R.id.list_row_save_model_crimes);
        crimesView.setText(Integer.toString(model.CrimeLocationsRequestModel.CrimeLocations.size()) + " crime(s)");

        Button button = (Button) rowView.findViewById(R.id.list_row_save_model_delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SaveModelAdapter", "Delete button click");
            }
        });

        return rowView;
    }

}
