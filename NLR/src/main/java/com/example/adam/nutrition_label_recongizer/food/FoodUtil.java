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

        return new FoodItem(adjustedFoodVals,adjustedServing,adjustedCalories);
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
            float s = (val.getVal()/sum)*(1/numNonZero);
            HV += s*nutrient.getHealthFactor();

        }

        // Log.e("ADAM","HF:"+HF);
        return HV;
    }

    /**
     * Returns the health index for the provided food item
     * @return float health index in [0,1]
     */
    public float HealthIndex(){
        return (BerryIndex()*HealthValue())/0.1360f; // Max HI based on foods that maximize their nutrient thresholds
    }

}
