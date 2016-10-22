package com.example.adam.nutrition_label_recongizer.food.comparator;

import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrient.NutrientFactory;
import com.example.adam.nutrition_label_recongizer.nutrient.comparator.NutrientComparatorIsGood;

import java.util.Comparator;

/**
 * Created by Adam on 10/16/2016.
 */

public class NutrientValComparatorIsGood implements Comparator<NutrientVal> {
    @Override
    public int compare(NutrientVal lhs, NutrientVal rhs) {
        NutrientFactory nutrientFactory = new NutrientFactory();
        Nutrient nlhs = nutrientFactory.buildNutrient(lhs.getType());
        Nutrient nrhs = nutrientFactory.buildNutrient(rhs.getType());
        return new NutrientComparatorIsGood().compare(nlhs,nrhs);
    }
}
