package com.example.adam.nutrition_label_recongizer.food;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 9/30/2016.
 */

public class FoodItem implements Parcelable{
    private ArrayList<NutrientVal> mNutrients;
    private Serving mServing;
    private int mCalories;

    private static final String NUTRIENT_BUNDLE_KEY = "nutrients";
    private static final String SERVING_BUNDLE_KEY = "serving";
    private static final String CALORIES_BUNDLE_KEY = "calories";


    public FoodItem(ArrayList<NutrientVal> nutrients, Serving serving, int calories) {
        mNutrients = nutrients;
        mServing = serving;
        mCalories = calories;
    }

    public List<NutrientVal> getNutrients() {
        return mNutrients;
    }

    public Serving getServing() {
        return mServing;
    }

    public int getCalories() {
        return mCalories;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{ serving : ");
        sb.append(mServing.toString());
        sb.append(", calories : ");
        sb.append(mCalories);
        sb.append(", nutrients : {");
        for(NutrientVal nutrientVal : mNutrients){
            sb.append(nutrientVal.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() -1);
        sb.append("}}");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(NUTRIENT_BUNDLE_KEY,mNutrients);
        bundle.putParcelable(SERVING_BUNDLE_KEY,mServing);
        bundle.putInt(CALORIES_BUNDLE_KEY,mCalories);

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>(){

        @Override
        public FoodItem createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle(NutrientVal.class.getClassLoader()); // Need to supply the class loader for NutrientVal for some reason. Otherwise bundle can't find it
            ArrayList<NutrientVal> nutrientVals = bundle.getParcelableArrayList(NUTRIENT_BUNDLE_KEY);
            Serving serving = bundle.getParcelable(SERVING_BUNDLE_KEY);
            int calories = bundle.getInt(CALORIES_BUNDLE_KEY);

            return new FoodItem(nutrientVals,serving,calories);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}
