package com.example.joshuahughes.fypapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.joshuahughes.fypapp.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //SETS options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    //event listener for menu (and whole toolbar?)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        Intent intent;

        switch(item.getItemId()){
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
                break;
            case R.id.savedRequests:
                intent = new Intent(this, SavedRequestsActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        return true;

    }
}
