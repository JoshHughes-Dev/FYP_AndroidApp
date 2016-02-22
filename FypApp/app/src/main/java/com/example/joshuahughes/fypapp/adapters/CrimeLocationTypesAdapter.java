package com.example.joshuahughes.fypapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.activities.DashBoardActivity;
import com.example.joshuahughes.fypapp.models.CrimeLocationTypeModel;



import java.util.ArrayList;

/**
 * Created by joshuahughes on 21/02/16.
 */
public class CrimeLocationTypesAdapter extends RecyclerView.Adapter<CrimeLocationTypesAdapter.ViewHolder> {
    private ArrayList<CrimeLocationTypeModel> clTypesArray;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageButton imageButton;

        public ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.clTypeName);
            imageButton = (ImageButton) v.findViewById(R.id.clTypeInfoButton);

        }

        public TextView getTextView() {
            return textView;
        }
        public ImageButton getImageButton(){
            return imageButton;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CrimeLocationTypesAdapter(ArrayList<CrimeLocationTypeModel> array) {
        clTypesArray = array;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CrimeLocationTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cl_type_row, parent, false);
        // set the view's size, margins, paddings and layout parameters ...

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CrimeLocationTypeModel model = clTypesArray.get(position);

        holder.getTextView().setText(model.Name);

        final ImageButton imageButton = holder.getImageButton();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cltInfoDialog(view.getContext(), model);
            }
        });


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return clTypesArray.size();
    }


    private void cltInfoDialog(Context context, CrimeLocationTypeModel model){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String LocationInfo = "Location type: "  + model.LocationType;
        String CrimeInfo = "Crime Types: ";

        for(int i = 0; i < model.CrimeTypes.size(); i++){
            CrimeInfo += model.CrimeTypes.get(i);
            if(i+1 != model.CrimeTypes.size()){
                CrimeInfo += ", ";
            }
        }

        builder.setTitle("Crime-location type information");
        builder.setMessage(LocationInfo + "\n" + CrimeInfo);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}
