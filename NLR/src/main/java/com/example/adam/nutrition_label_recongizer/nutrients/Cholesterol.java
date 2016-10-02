package com.example.adam.nutrition_label_recongizer.nutrients;

/**
 * Created by Adam on 10/2/2016.
 */

public class Cholesterol extends Nutrient {
    protected Cholesterol(){
        mType = NType.CHOLESTEROL;
        mThreshold = 0.2f;
        mIsGood = false;
    }
}
