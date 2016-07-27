package org.crimelocation.fypapp;

import org.crimelocation.fypapp.models.CrimeLocationModel;

import java.util.Comparator;

/**
 * Created by joshuahughes on 14/03/2016.
 */
public class DistanceComparator  implements Comparator<CrimeLocationModel> {

    public int compare(CrimeLocationModel clm1, CrimeLocationModel clm2) {

        return clm1.Distance - clm2.Distance;
    }

}

