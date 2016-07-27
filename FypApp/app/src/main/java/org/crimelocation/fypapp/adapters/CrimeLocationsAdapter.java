package org.crimelocation.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.crimelocation.fypapp.R;
import org.crimelocation.fypapp.fragments.ResultsListFragment;
import org.crimelocation.fypapp.helpers.myHelper;
import org.crimelocation.fypapp.models.CrimeLocationModel;

import java.util.ArrayList;


/**
 * Created by joshuahughes on 13/03/2016.
 */
public class CrimeLocationsAdapter extends ArrayAdapter<CrimeLocationModel> {

    private final Context context;
    private ArrayList<CrimeLocationModel> clArray;
    private ResultsListFragment resultsListFragment;

    public CrimeLocationsAdapter(Context c, ArrayList<CrimeLocationModel> array, ResultsListFragment fragment){
        super(c, -1, array);
        this.context = c;
        this.clArray = array;
        this.resultsListFragment = fragment; //need this so adapter can call method form fragment
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflator.inflate(R.layout.list_row_cl, parent, false);

        final CrimeLocationModel model = clArray.get(position);

        TextView rankView = (TextView) rowView.findViewById(R.id.clRank);
        rankView.setText(Integer.toString(model.Rank));

        ImageView badgeImageView = (ImageView) rowView.findViewById(R.id.badgeImageView);

        badgeImageView.setImageResource(myHelper.GetBadgeIconDrawableId(model.Badge));

        TextView textView = (TextView) rowView.findViewById(R.id.clName);
        textView.setText(model.Location.Name);

        TextView distanceView = (TextView) rowView.findViewById(R.id.clDistance);
        distanceView.setText(Integer.toString(model.Distance) + " meters away");

        TextView crimesView = (TextView) rowView.findViewById(R.id.clCrimes);
        crimesView.setText(Integer.toString(model.Crimes.size()) + " crime(s)");



        return rowView;

    }



}
