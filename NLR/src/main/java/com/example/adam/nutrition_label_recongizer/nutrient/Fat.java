package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 10/2/2016.
 */

public class Fat extends Nutrient {
    protected Fat(){
        mType = NType.FAT;
        mThreshold = 70f;
        mIsGood = false;
    }

}
