package com.example.adam.nutrition_label_recongizer.food;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Adam on 10/20/2016.
 */
public class FoodUtilTest {
    @Test
    public void adjustFoodItemServingSize() throws Exception {
        final float adjustedServingSize = 2.0f;

        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();
        nutrientVals.add(new NutrientVal(Nutrient.NType.PROTEIN,3.0f));
        nutrientVals.add(new NutrientVal(Nutrient.NType.SUGAR,5.0f));
        Serving serving = new Serving(1.0f,"bar");
        FoodItem foodItem1 = new FoodItem(nutrientVals,serving,100);
        FoodUtil foodUtil = new FoodUtil(foodItem1);
        FoodItem foodItem2 = foodUtil.adjustFoodItemServingSize(adjustedServingSize);

        assertEquals(foodItem1.getNutrients().size(),foodItem2.getNutrients().size());

        for(int i = 0; i < nutrientVals.size(); ++i){
            assertEquals(foodItem1.getNutrients().get(i).getName(),foodItem2.getNutrients().get(i).getName());
            assertEquals(foodItem2.getNutrients().get(i).getVal(),adjustedServingSize*foodItem1.getNutrients().get(i).getVal(),0.0001);
        }

        assertEquals(foodItem2.getServing().getUnit(),foodItem1.getServing().getUnit());
        assertEquals(foodItem2.getServing().getAmount(),adjustedServingSize*foodItem1.getServing().getAmount(),0.0001);
        assertEquals(foodItem2.getCalories(),adjustedServingSize*foodItem1.getCalories(),0.0001);
    }

    @Test
    public void matchFoodNutrientToUserNutrient1() throws Exception {
        NutrientVal userNutrient = new NutrientVal(Nutrient.NType.PROTEIN,3.0f);

        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();
        nutrientVals.add(new NutrientVal(Nutrient.NType.PROTEIN,3.0f));
        nutrientVals.add(new NutrientVal(Nutrient.NType.SUGAR,5.0f));
        Serving serving = new Serving(1.0f,"bar");
        FoodItem foodItem1 = new FoodItem(nutrientVals,serving,100);
        FoodUtil foodUtil = new FoodUtil(foodItem1);

        NutrientVal matchedNutrient = foodUtil.matchFoodNutrientToUserNutrient(userNutrient);

        assertEquals(userNutrient.getName(),matchedNutrient.getName());

    }

    @Test
    public void matchFoodNutrientToUserNutrient2() throws Exception {
        NutrientVal userNutrient = new NutrientVal(Nutrient.NType.PROTEIN,3.0f);

        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();
        nutrientVals.add(new NutrientVal(Nutrient.NType.CARBOHYDRATE,3.0f));
        nutrientVals.add(new NutrientVal(Nutrient.NType.SUGAR,5.0f));
        Serving serving = new Serving(1.0f,"bar");
        FoodItem foodItem1 = new FoodItem(nutrientVals,serving,100);
        FoodUtil foodUtil = new FoodUtil(foodItem1);

        NutrientVal matchedNutrient = foodUtil.matchFoodNutrientToUserNutrient(userNutrient);

        assertNull(matchedNutrient);

    }

    @Test
    public void matchFoodNutrientToUserNutrient3() throws Exception {
        NutrientVal userNutrient = new NutrientVal(Nutrient.NType.PROTEIN,3.0f);

        FoodItem foodItem1 = null;
        FoodUtil foodUtil = new FoodUtil(foodItem1);

        NutrientVal matchedNutrient = foodUtil.matchFoodNutrientToUserNutrient(userNutrient);

        assertNull(matchedNutrient);

    }

    @Test
    public void matchFoodNutrientToUserNutrient4() throws Exception {
        NutrientVal userNutrient = null;

        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();
        nutrientVals.add(new NutrientVal(Nutrient.NType.CARBOHYDRATE,3.0f));
        nutrientVals.add(new NutrientVal(Nutrient.NType.SUGAR,5.0f));
        Serving serving = new Serving(1.0f,"bar");
        FoodItem foodItem1 = new FoodItem(nutrientVals,serving,100);
        FoodUtil foodUtil = new FoodUtil(foodItem1);

        NutrientVal matchedNutrient = foodUtil.matchFoodNutrientToUserNutrient(userNutrient);

        assertNull(matchedNutrient);

    }

}