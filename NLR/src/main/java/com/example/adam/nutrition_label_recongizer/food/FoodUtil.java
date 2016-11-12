package com.example.adam.nutrition_label_recongizer.food;

import android.util.Log;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrient.NutrientFactory;

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

    /**
     * Comptues the Berry-Index for the provided food item based on its nutritional contents
     * @return float, Berry-Index
     */
    private float BerryIndex(){
        ArrayList<NutrientVal> nutrientVals = mFoodItem.getNutrients();

        float sum = 0.0f;
        float numNonZero = 0.0f;
        for(NutrientVal val : nutrientVals){
            sum += val.getVal();
            if(val.getVal() != 0){
                numNonZero++;
            }
        }

        float BI = 1.0f;
        for(NutrientVal val : nutrientVals){
            BI -= (val.getVal()/sum)*(1/numNonZero);
        }

        //Log.e("ADAM","BI:"+BI);
        return BI;
    }

    /**
     * Returns the HealthValue for the provided food item
     * @return float, health value in range [0,1]
     */
    private float HealthValue(){
        ArrayList<NutrientVal> nutrientVals = mFoodItem.getNutrients();

        float sum = 0.0f;
        float numNonZero = 0.0f;
        for(NutrientVal val : nutrientVals){
            sum += val.getVal();
            if(val.getVal() != 0){
                numNonZero++;
            }
        }

        float HV = 0.0f;
        NutrientFactory nutrientFactory = new NutrientFactory();
        for(NutrientVal val : nutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(val.getType());
            float s = (val.getVal()/Nutrient.THRESHOLD_SUM)*(1/numNonZero);
            HV += s*nutrient.getHealthFactor();

        }


        HV = HV/0.2963f; // Max HV based on foods that maximize their nutrient thresholds
        // Log.e("ADAM","HF:"+HF);
        return HV;
    }

    /**
     * Returns the health index for the provided food item
     * @return float health index in [0,1]
     */
    public float HealthIndex(){
        return (BerryIndex()*HealthValue())/0.2711f; // Max HI based on foods that maximize their nutrient thresholds
    }

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
