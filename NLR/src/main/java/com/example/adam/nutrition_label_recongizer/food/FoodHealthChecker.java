package com.example.adam.nutrition_label_recongizer.food;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrient.NutrientFactory;

import java.util.ArrayList;

/**
 * Created by Adam on 10/21/2016.
 */

public class FoodHealthChecker {

    /**
     * Computes how healthy the food provided would be to consume relative to
     * the already consumed nutrients
     * @param alreadyConsumed List of already consumed nutrients
     * @param foodItem Food item under consideration
     * @return float. The more positive the more healthy, the more negative the more unhealthy
     */
    public float relativeHealth(ArrayList<NutrientVal> alreadyConsumed,FoodItem foodItem){
        NutrientFactory nutrientFactory = new NutrientFactory();
        FoodUtil foodUtil = new FoodUtil(foodItem);
        float healthiness = 0.0f;
        for(NutrientVal nutrientVal : alreadyConsumed) {
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            NutrientVal foodVal = foodUtil.matchFoodNutrientToUserNutrient(nutrientVal);
            /*
             * If a food value is zero it should not contribute to the decision to eat the item.
             * If the already consumed value of a good nutrient is more than the threshold it does
             * not contribute to decision to eat item
             * If a food value is non-zero then the new nutritional value for the user if they chose
             * to eat it is used to calculate "healthiness".
             */
            if (foodVal.getVal() != 0.0f) {
                float val = foodVal.getVal() + nutrientVal.getVal();
                float threshold = nutrient.getThreshold();
                if (val == threshold) {
                    val += 0.0001f; // add a milligram to avoid division by zero
                }
                float diff = 0.0f;
                float arg = threshold / Math.abs(threshold - val);
                if (nutrient.isGood()) {
                    if (nutrientVal.getVal() < threshold) {
                        diff = (float) Math.exp(arg);
                    }
                } else {
                    diff = -(float) Math.exp(arg);

                }
                healthiness += diff;
            }
        }

        return healthiness;
    }

    /**
     * Computes the "healtiness" of a food item, irrespective of any nutrients the user may have consumed
     * @param foodItem
     * @return "healtiness" value. The higher the better. Range is (-Inf,Inf)
     */
    public float absoluteHealth(FoodItem foodItem){
        NutrientFactory nutrientFactory = new NutrientFactory();
        float healthiness = 0.0f;
        ArrayList<NutrientVal> foodVals = foodItem.getNutrients();
        for(NutrientVal foodVal : foodVals) {
            Nutrient nutrient = nutrientFactory.buildNutrient(foodVal.getType());
            /*
             * If a food value is zero it should not contribute to the decision to eat the item.
             */
            if (foodVal.getVal() != 0.0f) {
                float val = foodVal.getVal();
                float threshold = nutrient.getThreshold();
                if (val == threshold) {
                    val += 0.0001f; // add a milligram to avoid division by zero
                }
                float diff = 0.0f;
                float arg = threshold / Math.abs(threshold - val);
                if (nutrient.isGood()) {
                    diff = (float) Math.exp(arg);
                } else {
                    diff = -(float) Math.exp(arg);

                }
                healthiness += diff;
            }
        }
        return healthiness;
    }

}
