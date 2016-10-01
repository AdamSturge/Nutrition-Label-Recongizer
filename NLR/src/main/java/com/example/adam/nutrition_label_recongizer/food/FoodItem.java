package com.example.adam.nutrition_label_recongizer.food;

import java.util.List;

/**
 * Created by Adam on 9/30/2016.
 */

public class FoodItem {
    List<NutrientVal> mNutrients;
    int mServingSize;
    String mServingUnit;
    int mCalories;

    public FoodItem(List<NutrientVal> nutrients, int servingSize, String servingUnit, int calories) {
        mNutrients = nutrients;
        mServingSize = servingSize;
        mServingUnit = servingUnit;
        mCalories = calories;
    }

    public List<NutrientVal> getNutrients() {
        return mNutrients;
    }

    public int getServingSize() {
        return mServingSize;
    }

    public String getServingUnit() {
        return mServingUnit;
    }

    public int getCalories() {
        return mCalories;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{ serving size : ");
        sb.append(mServingSize);
        sb.append(", serving unit : ");
        sb.append(mServingUnit);
        sb.append(", calories : ");
        sb.append(mCalories);
        sb.append(", nutrients : {");
        for(NutrientVal nutrientVal : mNutrients){
            sb.append(nutrientVal.toString());
            sb.append(",");
        }
        sb.append("}}");
        return sb.toString();
    }
}
