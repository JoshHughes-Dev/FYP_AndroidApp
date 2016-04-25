package com.example.joshuahughes.fypapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.joshuahughes.fypapp.R;
import com.example.joshuahughes.fypapp.helpers.myHelper;

import java.util.ArrayList;

public class AboutActivity extends BaseActivity {

    TableLayout badgeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About");


        badgeTable = (TableLayout) findViewById(R.id.about_view_badge_table);


        for(int i = 0; i<6; i++){
            ArrayList<String> arrayList = myHelper.GetBadgeTitleAndDescription(i);
            CreateDetailsRow(arrayList.get(0), arrayList.get(1), myHelper.GetBadgeIconDrawableId(i));
        }

    }

    //SETS no options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }



    private void CreateDetailsRow(String primaryText, String secondaryText, int iconId){

        View tr = getLayoutInflater().inflate(R.layout.tablerow_details_item, null, false);

        if(iconId > 0){
            ImageView iconView = (ImageView) tr.findViewById(R.id.table_row_icon);
            iconView.setImageResource(iconId);
        }

        TextView detailView = (TextView)tr.findViewById(R.id.table_row_detail);
        detailView.setText(primaryText);

        TextView detailSubView = (TextView)tr.findViewById(R.id.table_row_subDetail);
        detailSubView.setText(secondaryText);

        badgeTable.addView(tr);
    }

}
