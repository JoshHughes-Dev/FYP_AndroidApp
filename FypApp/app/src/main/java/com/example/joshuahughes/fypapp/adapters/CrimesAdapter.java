package com.example.joshuahughes.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.models.CrimeModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;


/**
 * Created by joshuahughes on 15/03/2016.
 */
public class CrimesAdapter extends ArrayAdapter<CrimeModel> {

    private final Context context;
    private ArrayList<CrimeModel> crimeArray;


    public CrimesAdapter(Context c, ArrayList<CrimeModel> array){
        super(c, -1, array);
        this.context = c;
        this.crimeArray = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflator.inflate(R.layout.list_row_crime, parent, false);

        final CrimeModel model = crimeArray.get(position);

        TextView categoryView = (TextView) rowView.findViewById(R.id.list_row_crime_category);
        categoryView.setText(model.Category);

        TextView dateView = (TextView) rowView.findViewById(R.id.list_row_crime_date);
        dateView.setText(GetFriendlyDateString(model.Year, model.Month));


        return rowView;

    }


    private String GetFriendlyDateString(int year, int month){

        String dateStr = new DateFormatSymbols().getMonths()[month-1] + " " + Integer.toString(year);
        return dateStr;
    }

}
