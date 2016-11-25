package com.example.adam.nutrition_label_recongizer.nutrient.comparator;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

import java.util.Comparator;

/**
 * Created by Adam on 10/16/2016.
 */

public class NutrientComparatorType implements Comparator<Nutrient> {
    @Override
    public int compare(Nutrient lhs, Nutrient rhs) {
        return lhs.getType().compareTo(rhs.getType());
    }
}
