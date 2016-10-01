package com.example.adam.nutrition_label_recongizer.nutrients;

/**
 * Created by Adam on 9/30/2016.
 */


public abstract class Nutrient {
    public enum NType {PROTEIN,CARBOHYDRATE};

    protected NType mName;
    protected double mThreshold;
    protected boolean mIsGood;

    public String toString(){
        return "{ name: " + mName + ", threshold : " + mThreshold + ", isGood : " + mIsGood + "}";
    }

    public NType getName() {
        return mName;
    }

    public double getThreshold() {
        return mThreshold;
    }

    public boolean isGood() {
        return mIsGood;
    }
}
