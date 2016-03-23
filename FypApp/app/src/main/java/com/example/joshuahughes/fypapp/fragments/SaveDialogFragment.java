package com.example.joshuahughes.fypapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.models.CrimeLocationModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;
import com.example.joshuahughes.fypapp.models.CrimeLocationsRequestModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaveDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaveDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveDialogFragment extends DialogFragment{

    private CrimeLocationTypeModel crimeLocationTypeModel;
    private CrimeLocationsRequestModel crimeLocationsRequestModel;
    private Boolean closeDialog = false;

    private OnFragmentInteractionListener mListener;

    public SaveDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SaveDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveDialogFragment newInstance(CrimeLocationTypeModel cltm, CrimeLocationsRequestModel clrm) {
        SaveDialogFragment fragment = new SaveDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("crimeLocationTypeModel", cltm);
        args.putParcelable("crimeLocationsRequestModel", clrm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            crimeLocationTypeModel = getArguments().getParcelable("crimeLocationTypeModel");
            crimeLocationsRequestModel = getArguments().getParcelable("crimeLocationsRequestModel");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Save Search Request");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_save_dialog, null);
        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    //this set in on start override
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                }
            });

        TextView resultsDetailsView = (TextView) view.findViewById(R.id.save_dialog_results_details);
        resultsDetailsView.setText("Type: " +Integer.toString(crimeLocationsRequestModel.CrimeLocations.size()) + " crime(s)");

        TextView typeDetailsView = (TextView) view.findViewById(R.id.save_dialog_type_details);
        typeDetailsView.setText("Results: " + crimeLocationTypeModel.Name);

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //have to set positive button click here to override on start super.
    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Save
                    EditText edit= (EditText) getDialog().findViewById(R.id.save_dialog_save_name);
                    String text = edit.getText().toString();

                    if(text.length() > 0){
                        mListener.onNewSave(text);
                        dismiss();
                    }
                    else{
                        Toast toast = Toast.makeText(getActivity(), "Please enter a name for the request you want to save", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
                    }

                }
            });
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNewSave(String saveName);
    }
}
