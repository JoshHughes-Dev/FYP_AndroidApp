package com.example.joshuahughes.fypapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.adapters.CrimesAdapter;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrimesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrimesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimesListFragment extends Fragment {


    private ListView crimesListView;
    private CrimesAdapter crimesAdapter;
    private CrimeLocationModel crimeLocationModel;
    private OnFragmentInteractionListener mListener;

    public CrimesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CrimesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimesListFragment newInstance() {
        CrimesListFragment fragment = new CrimesListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crimes_list, container, false);

        crimesListView = (ListView) v.findViewById(R.id.crimes_listView);

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

    //have to wait until activity is create before getting results model from parent activity
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mListener.onListLoadSavedInstance();
    }


    public void CreateCrimesListView(CrimeLocationModel model){

        crimeLocationModel = model;
        crimesAdapter = new CrimesAdapter(getActivity(), crimeLocationModel.Crimes);
        crimesListView.setAdapter(crimesAdapter);
    }
}
