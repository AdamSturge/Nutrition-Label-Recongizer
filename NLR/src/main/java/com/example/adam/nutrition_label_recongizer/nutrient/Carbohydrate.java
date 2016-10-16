package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 9/30/2016.
 */

class Carbohydrate extends Nutrient {
    protected Carbohydrate(){
        mType = NType.CARBOHYDRATE;
        mThreshold = 310f;
        mIsGood = true;
    }

}
