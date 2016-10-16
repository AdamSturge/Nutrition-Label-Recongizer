package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 10/2/2016.
 */

public class Sodium extends Nutrient {
    protected Sodium(){
        mType = NType.SODIUM;
        mThreshold = 2.3f;
        mIsGood = false;
    }
}
