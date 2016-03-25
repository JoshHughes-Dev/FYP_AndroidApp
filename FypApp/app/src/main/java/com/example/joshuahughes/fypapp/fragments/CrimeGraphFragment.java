package com.example.joshuahughes.fypapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrimeGraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrimeGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeGraphFragment extends Fragment {


    private CrimeLocationModel crimeLocationModel;
    private GraphView graphView;

    private OnFragmentInteractionListener mListener;

    public CrimeGraphFragment() {
        // Required empty public constructor
    }


    public static CrimeGraphFragment newInstance() {
        CrimeGraphFragment fragment = new CrimeGraphFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_graph, container, false);

        graphView = (GraphView) v.findViewById(R.id.crimes_graphView);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //have to wait until activity is create before getting results model from parent activity
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mListener.onGraphLoadSavedInstance();
    }

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
        // TODO: Update argument type and name
        void onGraphLoadSavedInstance();
    }

    /**
     * Public so parent can call it
     * Creates Graphs
     * @param model
     */
    public void CreateGraph(CrimeLocationModel model){
        crimeLocationModel = model;

        ArrayList<Integer> timePeriodList = GetTimePeriodIntList();
        String[] monthStr = ConvertIntToStringMonths(timePeriodList);

        setGraphSettings(monthStr);

        ArrayList<DataPoint> dataPoints = GetDataPointsFromCrimeLocationModel(timePeriodList);

        DataPoint[] dataPointsArray = new DataPoint[dataPoints.size()];
        dataPointsArray = dataPoints.toArray(dataPointsArray);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointsArray);

        graphView.addSeries(series);

    }

    /**
     * create datapoints for graph (position and value)
     * @param timePeriodList
     * @return
     */
    private ArrayList<DataPoint> GetDataPointsFromCrimeLocationModel(ArrayList<Integer> timePeriodList){
        ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

        //iterate through each timeperiod month
        for(int i = 0; i<timePeriodList.size(); i++){

            //count crime occurences
            int countOccurences = 0;
            for(int j = 0; j< crimeLocationModel.Crimes.size(); j++){
                if(crimeLocationModel.Crimes.get(j).Month == timePeriodList.get(i)){
                    countOccurences++;
                }
            }

            //add new data point at index i
            dataPoints.add(new DataPoint(i, countOccurences));

        }

        return dataPoints;
    }

    /**
     * Sets graph settings before creation
     * @param labels
     */
    private void setGraphSettings(String[] labels){

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(labels);

        graphView.getGridLabelRenderer().setNumHorizontalLabels(12);
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphView.setScaleY(1);
        graphView.setScaleX(1);

    }

    /**
     * creates int month in order of last 12 months
     * @return
     */
    private ArrayList<Integer> GetTimePeriodIntList(){

        ArrayList<Integer> timePeriodList = new ArrayList<>();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        int previousYear = currentYear - 1;

        for(int i = currentMonth; i<13; i++)
        {
            timePeriodList.add(i);
        }
        for(int j = 1; j<currentMonth; j++)
        {
            timePeriodList.add(j);
        }

        return timePeriodList;
    }

    /**
     * Converst int months to string months
     * @param timePeriodList
     * @return
     */
    private String[] ConvertIntToStringMonths(ArrayList<Integer> timePeriodList){

        ArrayList<String> monthStrs = new ArrayList<String>();

        for(int i=0; i< timePeriodList.size(); i++){
            String monthLong = new DateFormatSymbols().getMonths()[timePeriodList.get(i)-1];

            monthStrs.add(monthLong.substring(0, Math.min(monthLong.length(), 3)));
        }

        String [] monthStrsArray = new String[monthStrs.size()];
        monthStrsArray = monthStrs.toArray(monthStrsArray);

        return monthStrsArray;
    }
}


