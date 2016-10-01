package com.example.adam.nutrition_label_recongizer.nutrients;

/**
 * Created by Adam on 9/30/2016.
 */

public class NutrientFactory {
    NutrientFactory(){};

    Nutrient buildNutrient(Nutrient.NType name){
        Nutrient nutrient = null;
        switch (name){
            case PROTEIN:
                nutrient = new Protein();
                break;
            case CARBOHYDRATE:
                nutrient = new Carbohydrate();
                break;
        }

        return  nutrient;
    }
}
