package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 10/2/2016.
 */

public class Sugar extends Nutrient {
    protected Sugar(){
        mType = NType.SUGAR;
        mThreshold = 90f;
        mIsGood = false;
        mHealthFactor = mThreshold/THRESHOLD_SUM;
    }
}
