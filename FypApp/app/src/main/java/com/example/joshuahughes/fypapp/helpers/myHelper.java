package com.example.joshuahughes.fypapp.helpers;

import android.widget.ImageView;

import com.example.joshuahughes.fypapp.R;

import java.util.Map;

/**
 * Created by joshuahughes on 13/03/2016.
 */
public class myHelper {

    public static void SetBadgeIcon(ImageView imageView, int badge){

        switch(badge){
            case 0:
                //star
                imageView.setImageResource(R.drawable.ic_star_black_36px);
                break;
            case 1:
                //constant improvement
                imageView.setImageResource(R.drawable.ic_arrow_upward_black_36px);
                break;
            case 2:
                //improvement
                imageView.setImageResource(R.drawable.ic_trending_up_black_36px);
                break;
            case 3:
                //no change
                imageView.setImageResource(R.drawable.ic_trending_flat_black_36px);
                break;
            case 4:
                //worsening
                imageView.setImageResource(R.drawable.ic_trending_down_black_36px);
                break;
            case 5:
                //constantly worsening
                imageView.setImageResource(R.drawable.ic_arrow_downward_black_36px);
                break;

        }
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
