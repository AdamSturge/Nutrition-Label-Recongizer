package com.example.adam.nutrition_label_recongizer.nutrients;

/**
 * Created by Adam on 9/30/2016.
 */

class Carbohydrate extends Nutrient {
    protected Carbohydrate(){
        mName = NType.CARBOHYDRATE;
        mThreshold = 10;
        mIsGood = true;
    }

}
