package com.example.joshuahughes.fypapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.fragments.MapInputFragment;
import com.example.joshuahughes.fypapp.helpers.myHelper;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import java.util.HashMap;


/**
 * Created by joshuahughes on 13/03/2016.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


    private View view;
    private HashMap<CrimeLocationModel,Marker> resultModelMarkerMap;
    private MapInputFragment mapInputFragment;

    public CustomInfoWindowAdapter(HashMap<CrimeLocationModel,Marker> hashMap, MapInputFragment fragment) {

        LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        view =  inflater.inflate(R.layout.custom_info_window, null);
        resultModelMarkerMap = hashMap;
        mapInputFragment = fragment;
    }


    // Use default InfoWindow frame
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    // Defines the contents of the InfoWindow
    @Override
    public View getInfoContents(Marker marker) {

        CrimeLocationModel clm = (CrimeLocationModel)myHelper.getKeyFromValue(resultModelMarkerMap, marker);

        if(clm != null){
            TextView textView = (TextView) view.findViewById(R.id.locationTitle);
            textView.setText(clm.Location.Name);

            ImageView badgeView = (ImageView) view.findViewById(R.id.info_window_badge_view);
            badgeView.setImageResource(myHelper.GetBadgeIconDrawableId(clm.Badge));


            TextView badgeTextView = (TextView) view.findViewById(R.id.info_window_badge_text);
            String title = myHelper.GetBadgeTitleAndDescription(clm.Badge).get(0);
            badgeTextView.setText(title);

            TextView crimesView = (TextView) view.findViewById(R.id.info_window_crime_number);
            crimesView.setText(Integer.toString(clm.Crimes.size()) + " crime(s)");

            TextView distanceView = (TextView) view.findViewById(R.id.info_window_distance_view);
            distanceView.setText(Integer.toString(clm.Distance) + " meters away");

            //used to track last marker opened
            mapInputFragment.lastMarkerClicked = marker;

        }
        else {
            Log.d("CustomInfoWindowAdapter", "no crime Location model found in hashmap");
            return null; //note: stop user location marker temp
        }


        // Returning the view containing InfoWindow contents
        return view;
    }




}
