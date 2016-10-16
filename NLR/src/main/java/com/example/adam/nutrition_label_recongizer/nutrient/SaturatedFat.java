package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 10/2/2016.
 */

public class SaturatedFat extends Nutrient {
    protected SaturatedFat(){
        mType = NType.SAT_FAT;
        mThreshold = 24f;
        mIsGood = false;
    }
}
