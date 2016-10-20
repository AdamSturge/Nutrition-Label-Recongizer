package com.example.adam.nutrition_label_recongizer.food;

/**
 * Created by Adam on 10/20/2016.
 */

public class FoodItemException extends Exception {

    public FoodItemException(){
        super();
    }

    public FoodItemException(String message){
        super(message);
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
