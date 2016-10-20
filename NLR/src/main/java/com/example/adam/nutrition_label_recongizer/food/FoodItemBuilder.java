package com.example.adam.nutrition_label_recongizer.food;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

import java.util.ArrayList;

/**
 * Created by Adam on 10/20/2016.
 */

public class FoodItemBuilder {
    private NutrientVal mProtein;
    private NutrientVal mCarbohydrates;
    private NutrientVal mFat;
    private NutrientVal mSaturated;
    private NutrientVal mTrans;
    private NutrientVal mSugar;
    private NutrientVal mFibre;
    private NutrientVal mCholesterol;
    private NutrientVal mSodium;
    private Serving mServing;
    private int mCalories;

    public FoodItemBuilder(){
        mProtein = new NutrientVal(Nutrient.NType.PROTEIN,-1.0f);
        mCarbohydrates = new NutrientVal(Nutrient.NType.CARBOHYDRATE,-1.0f);
        mFat = new NutrientVal(Nutrient.NType.FAT,-1.0f);
        mSaturated = new NutrientVal(Nutrient.NType.SATURATED,-1.0f);
        mTrans = new NutrientVal(Nutrient.NType.TRANS,-1.0f);
        mSugar = new NutrientVal(Nutrient.NType.SUGAR,-1.0f);
        mFibre = new NutrientVal(Nutrient.NType.FIBRE,-1.0f);
        mCholesterol = new NutrientVal(Nutrient.NType.CHOLESTEROL,-1.0f);
        mSodium = new NutrientVal(Nutrient.NType.SODIUM,-1.0f);
        mServing = new Serving(-1.0f,"");
        mCalories = -1;
    }

    public NutrientVal getProtein() {
        return mProtein;
    }

    public void setProtein(NutrientVal mProtein) {
        this.mProtein = mProtein;
    }

    public NutrientVal getCarbohydrates() {
        return mCarbohydrates;
    }

    public void setCarbohydrates(NutrientVal mCarbohydrates) {
        this.mCarbohydrates = mCarbohydrates;
    }

    public NutrientVal getFat() {
        return mFat;
    }

    public void setFat(NutrientVal mFat) {
        this.mFat = mFat;
    }

    public NutrientVal getSaturated() {
        return mSaturated;
    }

    public void setSaturated(NutrientVal mSaturated) {
        this.mSaturated = mSaturated;
    }

    public NutrientVal getTrans() {
        return mTrans;
    }

    public void setTrans(NutrientVal mTrans) {
        this.mTrans = mTrans;
    }

    public NutrientVal getSugar() {
        return mSugar;
    }

    public void setSugar(NutrientVal mSugar) {
        this.mSugar = mSugar;
    }

    public NutrientVal getFibre() {
        return mFibre;
    }

    public void setFibre(NutrientVal mFibre) {
        this.mFibre = mFibre;
    }

    public NutrientVal getCholesterol() {
        return mCholesterol;
    }

    public void setCholesterol(NutrientVal mCholesterol) {
        this.mCholesterol = mCholesterol;
    }

    public NutrientVal getSodium() {
        return mSodium;
    }

    public void setSodium(NutrientVal mSodium) {
        this.mSodium = mSodium;
    }

    public Serving getServing() {
        return mServing;
    }

    public void setServing(Serving mServing) {
        this.mServing = mServing;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int mCalories) {
        this.mCalories = mCalories;
    }

    public FoodItem build() throws FoodItemException{
        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();
        nutrientVals.add(mProtein);
        nutrientVals.add(mCarbohydrates);
        nutrientVals.add(mCholesterol);
        nutrientVals.add(mFat);
        nutrientVals.add(mFibre);
        nutrientVals.add(mSaturated);
        nutrientVals.add(mSugar);
        nutrientVals.add(mTrans);
        nutrientVals.add(mSodium);

        if(mServing == null){
            throw new FoodItemException("Serving cannot be null");
        }

        if(mCalories == -1){
            throw new FoodItemException("Calories cannot be null");
        }

        return new FoodItem(nutrientVals,mServing,mCalories);
    }

}
