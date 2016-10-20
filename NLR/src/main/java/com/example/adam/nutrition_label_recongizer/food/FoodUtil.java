package com.example.adam.nutrition_label_recongizer.food;

import java.util.ArrayList;

/**
 * Created by Adam on 10/20/2016.
 */

public class FoodUtil {

    private FoodItem mFoodItem;

    public FoodUtil(FoodItem item){
        mFoodItem = item;
    }

    /**
     * Adjusts nutritional values of the provided food item to reflect a serving size
     * specified by the provided serving size
     * @param newServingSize new serving size to adjust nutrients to
     * @return a new food item where all values have been scaled by the ratio of the old serving size
     * to the new serving size
     */
    public FoodItem adjustFoodItemServingSize(float newServingSize){
        ArrayList<NutrientVal> foodVals = mFoodItem.getNutrients();
        float oldServingSize = mFoodItem.getServing().getAmount();

        float ratio = newServingSize/oldServingSize;

        ArrayList<NutrientVal> adjustedFoodVals = new ArrayList<NutrientVal>();
        for(NutrientVal val : foodVals){
            adjustedFoodVals.add(new NutrientVal(val.getType(),ratio*val.getVal()));
        }

        Serving adjustedServing = new Serving(newServingSize,mFoodItem.getServing().getUnit());
        int adjustedCalories = (int)ratio*mFoodItem.getCalories();

        return  new FoodItem(adjustedFoodVals,adjustedServing,adjustedCalories);
    }

    /**
     * Matches user nutrient values to food item nutrient values based on nutrient type
     * @param userNutrient
     * @return food nutrient value that matches the type of the provided nutrient value.
     * null if no match
     */
    public NutrientVal matchFoodNutrientToUserNutrient(NutrientVal userNutrient){
        if(mFoodItem == null){
            return null;
        }
        if(userNutrient == null){
            return null;
        }

        ArrayList<NutrientVal> foodVals = mFoodItem.getNutrients();
        for(NutrientVal nv : foodVals){
            if(nv.getType() == userNutrient.getType()){
                return nv;
            }
        }
        return null;
    }

}
