package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 9/30/2016.
 */


public abstract class Nutrient {
    public enum NType {PROTEIN,CARBOHYDRATE,FAT,SAT_FAT,SUGAR,SODIUM,CHOLESTEROL,TRANS_FAT, FIBRE};

    protected NType mType;
    protected float mThreshold;
    protected boolean mIsGood;

    public String toString(){
        return "{ type: " + mType + ", threshold : " + mThreshold + ", isGood : " + mIsGood + "}";
    }

    public NType getType() {
        return mType;
    }

    public float getThreshold() {
        return mThreshold;
    }

    public boolean isGood() {
        return mIsGood;
    }

}
