package com.example.adam.nutrition_label_recongizer.food;

import android.view.ViewDebug;

import com.example.adam.nutrition_label_recongizer.nutrients.Nutrient;

/**
 * Created by Adam on 9/30/2016.
 */

public class NutrientVal {
    public enum unit {GRAM,MILLIGRAM};

    Nutrient.NType mName;
    int mVal;
    unit mUnit;


    public NutrientVal(Nutrient.NType name, int val,unit unit) {
        mName = name;
        mVal = val;
        mUnit = unit;
    }

    public Nutrient.NType getName() {
        return mName;
    }

    public int getVal() {
        return mVal;
    }

    public unit getmUnit() {
        return mUnit;
    }

    @Override
    public String toString(){
        return "{name : " +mName.name() +", val : " + mVal+", unit : " + mUnit+"}";

    }
}
