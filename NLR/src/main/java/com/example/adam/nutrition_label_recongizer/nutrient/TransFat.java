package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 10/2/2016.
 */

public class TransFat extends Nutrient {
    protected TransFat(){
        mType = NType.TRANS;
        mThreshold = 2.0f;
        mIsGood = false;
        mHealthFactor = mThreshold/THRESHOLD_SUM;
    }
}
