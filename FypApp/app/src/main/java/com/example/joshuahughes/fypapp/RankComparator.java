package com.example.joshuahughes.fypapp;

import com.example.joshuahughes.fypapp.models.CrimeLocationModel;

import java.util.Comparator;

/**
 * Created by joshuahughes on 14/03/2016.
 */
public class RankComparator implements Comparator<CrimeLocationModel> {

    public int compare(CrimeLocationModel clm1, CrimeLocationModel clm2){

        return clm1.Rank - clm2.Rank;
    }
}
