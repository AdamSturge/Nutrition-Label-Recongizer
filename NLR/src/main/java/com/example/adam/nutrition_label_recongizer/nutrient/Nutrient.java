package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 9/30/2016.
 */


public abstract class Nutrient {
    public enum NType {PROTEIN,CARBOHYDRATE,FAT,SATURATED,SUGAR,SODIUM,CHOLESTEROL,TRANS,FIBRE};

    public static final float THRESHOLD_SUM = 569.5f;

    protected NType mType;
    protected float mThreshold;
    protected boolean mIsGood;
    protected float mHealthFactor;

    public String toString(){
        return "{ type: " + mType + ", threshold : " + mThreshold + ", isGood : " + mIsGood + ", HealthFactor : " + mHealthFactor + "}";
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

    public float getHealthFactor(){return mHealthFactor;}

}
