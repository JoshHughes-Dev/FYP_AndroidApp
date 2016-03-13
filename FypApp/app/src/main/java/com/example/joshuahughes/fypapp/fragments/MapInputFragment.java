package com.example.joshuahughes.fypapp.fragments;

import java.util.ArrayList;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapInputFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapInputFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SeekBar.OnSeekBarChangeListener, ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {

    CrimeLocationsRequestModel resultsModel;
    private ArrayList<Marker> resultsMarkers;
    private LatLng selectedLocation;
    private Integer selectedRadius = 400; //400 is default value;
    private TextView radiusValueCounter;

    private OnFragmentInteractionListener mListener;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    protected ImageButton myLocationButton;

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
        void onMapLoadSavedInstance();
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

        // Restoring the markers on configuration changes
        if(savedInstanceState!=null){
            selectedLocation = savedInstanceState.getParcelable("selectedLocation");
            selectedRadius = savedInstanceState.getInt("selectedRadius");
        }
        else{
            selectedRadius = 400;
        }

        //build api client for location services
        buildGoogleApiClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map_input, container, false);

        //Init radiusValueCounter TextView
        radiusValueCounter = (TextView) v.findViewById(R.id.radiusValueCounter);
        radiusValueCounter.setText(Integer.toString(selectedRadius) + 'm');

        //Init radiusSeekBar
        radiusSeekBar = (SeekBar) v.findViewById(R.id.radiusSeekBar);
        radiusSeekBar.setMax((radiusMax - radiusMin) / radiusStep);
        radiusSeekBar.setProgress(selectedRadius);
        radiusSeekBar.setOnSeekBarChangeListener(this);

        //Init map view
        mMapView = (MapView) v.findViewById(R.id.googleMap);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);


        //Init my location button
        myLocationButton = (ImageButton) v.findViewById(R.id.myLocationButton);
        myLocationButton.setColorFilter(Color.parseColor("#212121"));
        myLocationButton.setOnClickListener(this);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("selectedLocation", selectedLocation);
        outState.putInt("selectedRadius", selectedRadius);

    }

    //have to wait until activity is create before getting results model from parent activity
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mListener.onMapLoadSavedInstance();
    }


    // MAP VIEW implements -----------------------------------------------------------------------//

    private MapView mMapView;
    private Marker locationMarker;
    private Circle locationRadius;
    private GoogleMap mMap;


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        //set click event listener
        mMap.setOnMapClickListener(this);


        if(selectedLocation != null && selectedRadius != null){
            SetNewLocationMarker(selectedLocation, selectedRadius);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, GetZoomLevel(selectedRadius)));
            //results processed onto map from local pointer when map is ready
            // (rather than old method of requesting from parent when map ready)
            ProcessResultsOntoMap();

        }
        else{
            //default camera view
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.769261, -1.2272118), 14.0f));
        }

    }

    @Override
    public void onMapClick(LatLng point){
        selectedLocation = point;
        RemoveCurrentResultsMarkers();
        SetNewLocationMarker(selectedLocation,selectedRadius);
        NewSearchRequest();
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
    final private Integer radiusMin = 50;
    final private Integer radiusMax = 5000;
    final private Integer radiusStep = 1;


    @Override
    public void onStopTrackingTouch(SeekBar seekBar){
        UpdateLocationRadiusSize(locationMarker, selectedRadius, false);
        RemoveCurrentResultsMarkers();
        if(locationMarker != null) {
            NewSearchRequest();
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar){}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

        selectedRadius = radiusMin + (progress * radiusStep);
        radiusValueCounter.setText(Integer.toString(selectedRadius) + 'm');
        UpdateLocationRadiusSize(locationMarker, selectedRadius, true);

    }

    // MAP DRAWING & MANIPULATION ----------------------------------------//

    private void SetNewLocationMarker(LatLng point, int circleRadius){

        if(locationMarker == null) {

            locationMarker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            );

            locationRadius = mMap.addCircle(new CircleOptions()
                    .center(locationMarker.getPosition())
                    .radius(circleRadius)
                    .fillColor(0x3033FFFF)
                    .strokeWidth(2)
            );
        }
        else{
            UpdateLocationMarkerPosition(point);
        }

    }

    private void UpdateLocationMarkerPosition(LatLng newPoint){
        locationMarker.setPosition(newPoint);
        locationRadius.setCenter(newPoint);
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
        }

    }

    private int GetZoomLevel(double r){

        double radius = r + r / 2;
        double scale = radius / 500;
        int zoomLevel = (int) (16 - Math.log(scale)/Math.log(2));

        return zoomLevel;
    }

    private void DrawResultsMarkers(CrimeLocationsRequestModel model){

        int suggestedLocations = 0;

        for(int j = 0; j<model.CrimeLocations.size(); j++){

            CrimeLocationModel clm = model.CrimeLocations.get(j);

            LatLng latLng = new LatLng(clm.Location.Latitude, clm.Location.Longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(clm.Location.Name);
            markerOptions.snippet(clm.Crimes.size() + " crimes. Lat: " + clm.Location.Latitude + "  Lng: " + clm.Location.Longitude);

            float[] distance = new float[2];
            Location.distanceBetween(latLng.latitude, latLng.longitude, selectedLocation.latitude, selectedLocation.longitude, distance);

            if( distance[0] > selectedRadius ){
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                suggestedLocations++;
            }

            Marker marker = mMap.addMarker(markerOptions);

            resultsMarkers.add(marker);

        }

        CreateResultsNotification(suggestedLocations, model.CrimeLocations.size());

    }

    private void RemoveCurrentResultsMarkers(){
        for(int i = 0; i< resultsMarkers.size(); i++){
            resultsMarkers.get(i).remove();
        }
        resultsMarkers.clear();
    }

    private void CreateResultsNotification(int suggestedLocations, int numberOfResults){

        String message = Integer.toString(numberOfResults - suggestedLocations) + " found";
        if(suggestedLocations > 0){
            message += ", " + Integer.toString(suggestedLocations) + " suggested";
        }

        Toast.makeText(getActivity(), message , Toast.LENGTH_SHORT).show();
    }

    // Request / Response Methods -------------------------------------------------------//

    /**
     * check connected to internet than make new request through interface
     */
    private void NewSearchRequest(){

        if(mListener.isConnectedToInternet()){
            mListener.onMapInputInteraction(selectedLocation, selectedRadius);
        }
        else{
            //TODO better no internet message
            Toast toast = Toast.makeText(getActivity(), "Can't perform new search without internet connection", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * publicly called from parent activity
     * saves passed model to fragment for local access
     * if map is ready and working, process results straight away
     * (this can get called when fragment ready to receive data from parent but map not ready yet - lifecycle stuff)
     * @param model
     */
    public void ProcessNewRequestResults(CrimeLocationsRequestModel model){

        if(model != null){
            resultsModel = model;
            if(mMap != null){
                ProcessResultsOntoMap();
            }
        }
        else{
            //TODO: null error with results passed to fragment
            // or could be no results avaiable yet
        }
    }

    /**
     * take local results and apply them to map
     */
    private void ProcessResultsOntoMap(){

        if(resultsModel != null){
            RemoveCurrentResultsMarkers();
            DrawResultsMarkers(resultsModel);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMarker.getPosition(), GetZoomLevel(locationRadius.getRadius())));
        }
        else{
            //TODO: null error with results passed to fragment
            // or could be no results avaiable yet
        }
    }


    // My location handling methods -----------------------------------------------------//

    /**
     * Starts google api client connection when custom button clicked
     *
     * @param view
     */
    @Override
    public void onClick(View view){
        mGoogleApiClient.connect();
    }

    /**
     * Builds a GoogleApiClient configuration.  The addApi() method used to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * makes sure google client disconnects when fragment stops
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * when google api clinet connected, check if location permission granted,
     * gets last known location and uses this to start a new request and map marker(s) draw
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Provides a simple way of getting a device's location and is well suited for
            // applications that do not require a fine-grained location and that do not need location
            // updates. Gets the best and most recent location currently available, which may be null
            // in rare cases when a location is not available.
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                //assign last location to do something
                Toast.makeText(getActivity(), "location FOUND", Toast.LENGTH_LONG).show();
                selectedLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                RemoveCurrentResultsMarkers();
                SetNewLocationMarker(selectedLocation,selectedRadius);
                NewSearchRequest();

                //disconnect when finished
                mGoogleApiClient.disconnect();
            } else {
                Toast.makeText(getActivity(), "no location detected", Toast.LENGTH_LONG).show();
                //TODO show message with some kind of problem occured
            }
        } else {
            // Show rationale and request permission.
            //TODO request permission here
        }


    }

    /**
     * google api client connection failed handler
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("MainInputFragment", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        //TODO connection failed message
    }

    /**
     * google api client connection suspended handler
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("MainInputFragment", "Connection suspended");
        mGoogleApiClient.connect();
    }


}
