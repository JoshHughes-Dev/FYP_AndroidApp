package com.example.joshuahughes.fypapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.DistanceComparator;
import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.RankComparator;
import com.example.joshuahughes.fypapp.adapters.CrimeLocationsAdapter;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;

import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsListFragment extends Fragment  {

    private ListView resultsListView;
    private CrimeLocationsAdapter clAdapter;
    private CrimeLocationsRequestModel requestModel;
    private TextView noResultsTextView;
    private Boolean ascendingFlag = true; //TODO
    private Boolean rankLastHit = true;

    private OnFragmentInteractionListener mListener;

    private final static String TAG = "ResultsListFragment";

    private final static String STATE_SORT_ASC = "ascendingFlag";
    private final static String STATE_RANK_SORT_LAST_HIT = "rankLastHit";

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

        if(savedInstanceState!=null){
            ascendingFlag = savedInstanceState.getBoolean(STATE_SORT_ASC);
            rankLastHit = savedInstanceState.getBoolean(STATE_RANK_SORT_LAST_HIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =   inflater.inflate(R.layout.fragment_results_list, container, false);

        resultsListView = (ListView) v.findViewById(R.id.results_list_View);

        noResultsTextView = (TextView) v.findViewById(R.id.noResultsTextView);

        Button sortRankButton = (Button) v.findViewById(R.id.sortByRankButton);
        sortRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSortByRank();
                rankLastHit = true;
            }
        });

        Button sortDistanceButton = (Button) v.findViewById(R.id.sortByDistanceButton);
        sortDistanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSortByDistance();
                rankLastHit = false;
            }
        });

        Button toggleSortButton = (Button) v.findViewById(R.id.toggleSortOrderButton);
        toggleSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleSortOrder();
            }
        });

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_SORT_ASC, ascendingFlag);
        outState.putBoolean(STATE_RANK_SORT_LAST_HIT, rankLastHit);

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
        void onListLoadSavedInstance();
        void InitDetailsActivityFromListFragment(CrimeLocationModel crimeLocationModel);
    }

    /**
     * Public to allow parent activity to call
     * Initiates creating list view
     * @param model
     */
    public void ProcessNewRequestResults(CrimeLocationsRequestModel model){

        if(model != null) {
            requestModel = model;
            CreateListView();
        }
        else{
            //TODO no model?
            Log.d(TAG, "no model");
        }

    }

    /**
     * populates and creates list view
     */
    private void CreateListView(){

        noResultsTextView.setVisibility(View.GONE);
        clAdapter = new CrimeLocationsAdapter(getActivity(), requestModel.CrimeLocations, this);
        resultsListView.setAdapter(clAdapter);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CrimeLocationModel crimeLocationModel = requestModel.CrimeLocations.get(position);
                mListener.InitDetailsActivityFromListFragment(crimeLocationModel);
            }
        });

        //set toggle and sort comparator (remember this is because of orientation change
        if(rankLastHit){
            UpdateSortByRank();
        } else{
            UpdateSortByDistance();
        }

    }

    /**
     * Applies comparator to sort by distance from starting point
     */
    private void UpdateSortByDistance(){
        if(ascendingFlag) {
            clAdapter.sort(new DistanceComparator());
        } else {
            clAdapter.sort(Collections.reverseOrder(new DistanceComparator()));
        }

        clAdapter.notifyDataSetChanged();
    }

    /**
     * Applies comparator to sort by rank
     */
    private void UpdateSortByRank(){
        if(ascendingFlag){
            clAdapter.sort(new RankComparator());
        }
        else{
            clAdapter.sort(Collections.reverseOrder(new RankComparator()));
        }

        clAdapter.notifyDataSetChanged();

    }

    /**
     * Toggles sort order of list dependent of current sort method being used
     */
    private void ToggleSortOrder(){
        if(ascendingFlag){
            ascendingFlag = false;
        }
        else{
            ascendingFlag = true;
        }

        if(rankLastHit){
            UpdateSortByRank();
        } else{
            UpdateSortByDistance();
        }
    }

    /**
     * Clears results from fragment.
     * Public to allow parent acitivty to call it
     */
    public void ClearResults(){
        requestModel.CrimeLocations.clear();
        clAdapter.notifyDataSetChanged();
        requestModel = null;
        noResultsTextView.setVisibility(View.VISIBLE);
    }
}
