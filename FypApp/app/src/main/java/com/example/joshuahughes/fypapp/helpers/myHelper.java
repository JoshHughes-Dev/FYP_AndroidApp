package com.example.joshuahughes.fypapp.helpers;

import android.widget.ImageView;

import com.example.joshuahughes.fypapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joshuahughes on 13/03/2016.
 */
public class myHelper {

//    public static void SetBadgeIcon(ImageView imageView, int badge){
//
//        switch(badge){
//            case 0:
//                //star
//                imageView.setImageResource(R.drawable.ic_star_black_36px);
//                break;
//            case 1:
//                //constant improvement
//                imageView.setImageResource(R.drawable.ic_arrow_upward_black_36px);
//                break;
//            case 2:
//                //improvement
//                imageView.setImageResource(R.drawable.ic_trending_up_black_36px);
//                break;
//            case 3:
//                //no change
//                imageView.setImageResource(R.drawable.ic_trending_flat_black_36px);
//                break;
//            case 4:
//                //worsening
//                imageView.setImageResource(R.drawable.ic_trending_down_black_36px);
//                break;
//            case 5:
//                //constantly worsening
//                imageView.setImageResource(R.drawable.ic_arrow_downward_black_36px);
//                break;
//        }
//    }

    public static int GetBadgeIconDrawableId(int badge) {

        int toReturn = 0;

        switch (badge) {
            case 0:
                //star
                return R.drawable.ic_star_black_36px;
            case 1:
                //constant improvement
                return R.drawable.ic_arrow_upward_black_36px;
            case 2:
                //improvement
                return R.drawable.ic_trending_up_black_36px;
            case 3:
                //no change
                return R.drawable.ic_trending_flat_black_36px;
            case 4:
                //worsening
                return R.drawable.ic_trending_down_black_36px;
            case 5:
                //constantly worsening
                return R.drawable.ic_arrow_downward_black_36px;
            default:
                return 0;
        }


    }

    public static ArrayList<String> GetBadgeTitleAndDescription(int badge){

        ArrayList<String> arrayList = new ArrayList<>();

        switch(badge){
            case 0:
                //star
                arrayList.add("Star");
                arrayList.add("No crime recorded in the last 12 months");
                break;
            case 1:
                //constant improvement
                arrayList.add("Consistent Improvement");
                arrayList.add("Crime levels consistently decreasing every 3 months over past 12 months");
                break;
            case 2:
                //improvement
                arrayList.add("Improvement");
                arrayList.add("Less crime in the last 3 months than the previous 3 months");
                break;
            case 3:
                //no change
                arrayList.add("No change");
                arrayList.add("Crime levels have not changed over last 3 months from previous 3");
                break;
            case 4:
                //worsening
                arrayList.add("Worsening");
                arrayList.add("More crime in the last 3 months than the previous 3 months");
                break;
            case 5:
                //constantly worsening
                arrayList.add("Consistent Worsening");
                arrayList.add("Crime levels consistently increasing every 3 months over past 12 months");
                break;
        }

        return arrayList;
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
