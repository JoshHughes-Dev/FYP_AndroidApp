package com.example.joshuahughes.fypapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshuahughes.fypapp.R;

import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapInputFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapInputFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SeekBar.OnSeekBarChangeListener {


    private ArrayList<Marker> resultsMarkers;
    private OnFragmentInteractionListener mListener;
    public ProgressDialog progressDialog;

    // MAP INPUT FRAGMENT ------------------------------------------------------------------------//

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onMapInputInteraction(LatLng position, Integer radius);
        Boolean isConnectedToInternet();
    }

    public MapInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapInputFragment newInstance() {
        MapInputFragment fragment = new MapInputFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultsMarkers = new ArrayList<Marker>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map_input, container, false);

        radiusSeekBar = (SeekBar) v.findViewById(R.id.radiusSeekBar);
        radiusSeekBar.setMax((radiusMax - radiusMin) / radiusStep);
        radiusSeekBar.setProgress(radiusValue);
        radiusSeekBar.setOnSeekBarChangeListener(this);

        mMapView = (MapView) v.findViewById(R.id.googleMap);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        try{
            MapsInitializer.initialize(getContext());
        } catch (Exception e){
            Log.d("MAP_INIT_ERROR",e.toString());
        }

        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    // MAP VIEW implements -----------------------------------------------------------------------//

    private MapView mMapView;
    private Marker locationMarker;
    private Circle locationRadius;
    private GoogleMap mMap;


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.769261, -1.2272118), 14.0f));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point){
        RemoveResultsMarkers();
        SetNewLocationMarker(point,radiusValue);
        AttemptSearchRequest();
    }

    @Override
    public void onResume(){
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause(){
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory(){
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    // SEEK BAR implements -----------------------------------------------------------------------//

    private SeekBar radiusSeekBar;
    private Integer radiusValue = 400;
    final private Integer radiusMin = 50;
    final private Integer radiusMax = 5000;
    final private Integer radiusStep = 1;


    @Override
    public void onStopTrackingTouch(SeekBar seekBar){
        UpdateLocationRadiusSize(locationMarker, radiusValue, false);
        RemoveResultsMarkers();
        AttemptSearchRequest();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar){}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

        radiusValue = radiusMin + (progress * radiusStep);

        UpdateLocationRadiusSize(locationMarker, radiusValue, true);
        Log.d("progress: " + Integer.toString(progress), "radiusValue: " + Integer.toString(radiusValue));
        Log.d("locationRadius", Double.toString(locationRadius.getRadius()));

    }

    // DRAW POSITION ----------------------------------------//

    private void SetNewLocationMarker(LatLng point, int circleRadius){

        if(locationMarker == null) {

            locationMarker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            locationRadius = mMap.addCircle(new CircleOptions()
                    .center(locationMarker.getPosition())
                    .radius(circleRadius)
                    .fillColor(0x3033FFFF)
                    .strokeWidth(2));

            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, GetZoomLevel(locationRadius)));
        }
        else{
            UpdateLocationMarkerPosition(point);
        }

    }


    private void UpdateLocationMarkerPosition(LatLng newPoint){
        locationMarker.setPosition(newPoint);
        locationRadius.setCenter(newPoint);

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPoint, GetZoomLevel(locationRadius)));
    }

    private void UpdateLocationRadiusSize(Marker locationMarker, int radius, Boolean inProgress){

        if(locationMarker != null){

            if(inProgress) {
                locationRadius.setFillColor(80000000);
            }else{
                locationRadius.setFillColor(0x4533FFFF);
            }

            locationRadius.setCenter(locationMarker.getPosition());
            locationRadius.setRadius(radius);

//            if(!inProgress){
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMarker.getPosition(), GetZoomLevel(locationRadius)));
//            }
        }

    }


    private int GetZoomLevel(Circle circle){
        int zoomLevel = 11;
        if(circle != null){
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale)/Math.log(2));
        }
        return zoomLevel;
    }

    public void DrawResultsMarkersTest(CrimeLocationsRequestModel model){

        progressDialog.cancel();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMarker.getPosition(), GetZoomLevel(locationRadius)));
        createResultsToast(model);
        RemoveResultsMarkers();

        for(int j = 0; j<model.CrimeLocations.size(); j++){

            CrimeLocationModel clm = model.CrimeLocations.get(j);

            LatLng latLng = new LatLng(clm.Location.Latitude, clm.Location.Longitude);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(clm.Location.Name)
                    .snippet(clm.Crimes.size() + " crimes. Lat: " + clm.Location.Latitude + "  Lng: " + clm.Location.Longitude));

            resultsMarkers.add(marker);
        }
    }

    private void RemoveResultsMarkers(){
        for(int i = 0; i< resultsMarkers.size(); i++){
            resultsMarkers.get(i).remove();
        }
        resultsMarkers.clear();
    }

    private void createResultsToast(CrimeLocationsRequestModel model){
        Toast toast = Toast.makeText(getActivity(), Integer.toString(model.CrimeLocations.size()) + " locations found", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void createOfflineToast(){
        Toast toast = Toast.makeText(getActivity(), "Can't perform new search without internet connection", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void AttemptSearchRequest(){

        if(mListener.isConnectedToInternet()){
            CreateProgressDialog();
            mListener.onMapInputInteraction(locationMarker.getPosition(), radiusValue);
        }
        else{
            createOfflineToast();
        }
    }

    private void CreateProgressDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Searching for Crime Locations...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
