package com.example.adam.nutrition_label_recongizer.nutrient;

/**
 * Created by Adam on 9/30/2016.
 */

public class NutrientFactory {
    public NutrientFactory(){};

    public Nutrient buildNutrient(Nutrient.NType name){
        Nutrient nutrient = null;
        switch (name){
            case PROTEIN:
                nutrient = new Protein();
                break;
            case CARBOHYDRATE:
                nutrient = new Carbohydrate();
                break;
            case FAT:
                nutrient = new Fat();
                break;
            case SATURATED:
                nutrient = new SaturatedFat();
                break;
            case SUGAR:
                nutrient = new Sugar();
                break;
            case SODIUM:
                nutrient = new Sodium();
                break;
            case CHOLESTEROL:
                nutrient = new Cholesterol();
                break;
            case TRANS:
                nutrient = new TransFat();
                break;
            case FIBRE:
                nutrient = new Fibre();
                break;
        }

        return  nutrient;
    }
}
