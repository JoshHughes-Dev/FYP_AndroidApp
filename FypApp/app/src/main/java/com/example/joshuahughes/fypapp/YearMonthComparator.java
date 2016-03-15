package com.example.joshuahughes.fypapp;

import com.example.joshuahughes.fypapp.models.CrimeModel;

import java.util.Comparator;

/**
 * Created by joshuahughes on 15/03/2016.
 */
public class YearMonthComparator implements Comparator<CrimeModel> {

    public int compare(CrimeModel c1, CrimeModel c2) {

        //first sort by crime size (less the better)
        int yearResult = c2.Year - c1.Year;
        if(yearResult != 0){
            return yearResult;
        }

        //finally sort by distance (closer the better)
        return c2.Month - c1.Month;
    }
}
