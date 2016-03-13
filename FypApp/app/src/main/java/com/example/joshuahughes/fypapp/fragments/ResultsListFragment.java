package com.example.joshuahughes.fypapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.activities.MapSearchActivity;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationTypesAdapter2;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationsAdapter;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsListFragment extends Fragment {

    private ListView resultsListView;
    private CrimeLocationsAdapter clAdapter;

    private OnFragmentInteractionListener mListener;

    public ResultsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResultsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultsListFragment newInstance() {
        ResultsListFragment fragment = new ResultsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
        Log.d("ResultsListFragement", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =   inflater.inflate(R.layout.fragment_results_list, container, false);

        resultsListView = (ListView) v.findViewById(R.id.results_list_View);

        return v;
    }

    //have to wait until activity is create before getting results model from parent activity
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mListener.onListLoadSavedInstance();
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
        void onListLoadSavedInstance();
    }

    //Public to allow parent activity to call
    public void ProcessNewRequestResults(CrimeLocationsRequestModel model){

        if(model != null) {
            CreateListView(model);
        }
        else{
            //TODO no model?
            Log.d("asdf", "no model");
        }

    }

    private void CreateListView(final CrimeLocationsRequestModel model){

        Context c = getActivity();
        ArrayList<CrimeLocationModel> asdf = model.CrimeLocations;

        clAdapter = new CrimeLocationsAdapter(c, asdf);
        resultsListView.setAdapter(clAdapter);

    }
}
