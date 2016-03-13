package com.example.joshuahughes.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.helpers.myHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;

import java.util.ArrayList;


/**
 * Created by joshuahughes on 13/03/2016.
 */
public class CrimeLocationsAdapter extends ArrayAdapter<CrimeLocationModel> {

    private final Context context;
    private ArrayList<CrimeLocationModel> clArray;

    public CrimeLocationsAdapter(Context c, ArrayList<CrimeLocationModel> array){
        super(c, -1, array);
        this.context = c;
        this.clArray = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflator.inflate(R.layout.cl_row, parent, false);

        final CrimeLocationModel model = clArray.get(position);

        ImageView badgeImageView = (ImageView) rowView.findViewById(R.id.badgeImageView);
        myHelper.SetBadgeIcon(badgeImageView, model.Badge);

        TextView textView = (TextView) rowView.findViewById(R.id.clName);
        textView.setText(model.Location.Name);

        Button button = (Button) rowView.findViewById(R.id.DetailsButton);

        return rowView;

    }



}
