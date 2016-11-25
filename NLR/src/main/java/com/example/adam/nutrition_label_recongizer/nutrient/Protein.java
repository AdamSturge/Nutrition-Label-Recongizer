package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 9/30/2016.
 */

class Protein extends Nutrient {
    protected Protein(){
        mType = NType.PROTEIN;
        mThreshold = 50f;
        mIsGood = true;
        mHealthFactor = mThreshold/THRESHOLD_SUM;
    }

}
