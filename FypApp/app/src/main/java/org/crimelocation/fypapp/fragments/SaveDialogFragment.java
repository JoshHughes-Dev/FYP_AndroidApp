package org.crimelocation.fypapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.crimelocation.fypapp.R;

import org.crimelocation.fypapp.models.CrimeLocationTypeModel;
import org.crimelocation.fypapp.models.CrimeLocationsRequestModel;

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

    private OnFragmentInteractionListener mListener;

    private final static String TAG = "SaveDialogFragment";

    private final static String STATE_CL_TYPE = "crimeLocationTypeModel";
    private final static String STATE_CL_REQUEST_MODEL = "crimeLocationsRequestModel";

    //--------------------------------------------------------------------------------------------//

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
        args.putParcelable(STATE_CL_TYPE, cltm);
        args.putParcelable(STATE_CL_REQUEST_MODEL, clrm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            crimeLocationTypeModel = getArguments().getParcelable(STATE_CL_TYPE);
            crimeLocationsRequestModel = getArguments().getParcelable(STATE_CL_REQUEST_MODEL);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.save_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_save_dialog, null);
        builder.setView(view);

        builder.setPositiveButton(R.string.save_dialog_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //this set in on start override
            }
        });
        builder.setNegativeButton(R.string.save_dialog_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        TextView resultsDetailsView = (TextView) view.findViewById(R.id.save_dialog_results_details);
        String resultsText = "Results: " +  Integer.toString(crimeLocationsRequestModel.CrimeLocations.size()) + " crime(s)";
        resultsDetailsView.setText(resultsText);

        TextView typeDetailsView = (TextView) view.findViewById(R.id.save_dialog_type_details);
        String typeText= "Type: " + crimeLocationTypeModel.Name;
        typeDetailsView.setText(typeText);

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
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
        //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        super.onStart();
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
                        Toast toast = Toast.makeText(getActivity(), R.string.save_validation_toast_message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
                    }
                }
            });
        }
    }


    public interface OnFragmentInteractionListener {
        void onNewSave(String saveName);
    }
}
