package com.example.adam.nutrition_label_recongizer.nutrients;

/**
 * Created by Adam on 9/30/2016.
 */

class Protein extends Nutrient {
    protected Protein(){
        mName = NType.PROTEIN;
        mThreshold = 10;
        mIsGood = true;
    }

}
