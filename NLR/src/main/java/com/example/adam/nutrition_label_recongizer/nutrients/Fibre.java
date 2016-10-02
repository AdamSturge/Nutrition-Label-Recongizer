package com.example.adam.nutrition_label_recongizer.nutrients;

/**
 * Created by Adam on 10/2/2016.
 */

public class Fibre extends Nutrient{
    protected Fibre(){
        mType = NType.FIBRE;
        mThreshold = 21f;
        mIsGood = true;
    }
}
