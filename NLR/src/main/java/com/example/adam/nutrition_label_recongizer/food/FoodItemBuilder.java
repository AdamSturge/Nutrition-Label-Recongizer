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
    private int mPercentComplete;

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
        mPercentComplete = 0;
    }

    public NutrientVal getProtein() {
        return mProtein;
    }

    public void setProtein(NutrientVal mProtein) {
        this.mProtein = mProtein;
        mPercentComplete++;
    }

    public NutrientVal getCarbohydrates() {
        return mCarbohydrates;
    }

    public void setCarbohydrates(NutrientVal mCarbohydrates) {
        this.mCarbohydrates = mCarbohydrates;
        mPercentComplete++;
    }

    public NutrientVal getFat() {
        return mFat;
    }

    public void setFat(NutrientVal mFat) {
        this.mFat = mFat;
        mPercentComplete++;
    }

    public NutrientVal getSaturated() {
        return mSaturated;
    }

    public void setSaturated(NutrientVal mSaturated) {
        this.mSaturated = mSaturated;
        mPercentComplete++;
    }

    public NutrientVal getTrans() {
        return mTrans;
    }

    public void setTrans(NutrientVal mTrans) {
        this.mTrans = mTrans;
        mPercentComplete++;
    }

    public NutrientVal getSugar() {
        return mSugar;
    }

    public void setSugar(NutrientVal mSugar) {
        this.mSugar = mSugar;
        mPercentComplete++;
    }

    public NutrientVal getFibre() {
        return mFibre;
    }

    public void setFibre(NutrientVal mFibre) {
        this.mFibre = mFibre;
        mPercentComplete++;
    }

    public NutrientVal getCholesterol() {
        return mCholesterol;
    }

    public void setCholesterol(NutrientVal mCholesterol) {
        this.mCholesterol = mCholesterol;
        mPercentComplete++;
    }

    public NutrientVal getSodium() {
        return mSodium;
    }

    public void setSodium(NutrientVal mSodium) {
        this.mSodium = mSodium;
        mPercentComplete++;
    }

    public Serving getServing() {
        return mServing;
    }

    public void setServing(Serving mServing) {
        this.mServing = mServing;
        mPercentComplete++;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int mCalories) {
        this.mCalories = mCalories;
        mPercentComplete++;
    }

    /**
     * Gets how close the food item builder is to having all the info
     * needed to build a correct food item
     * @return percentage 0 <= p <= 100
     */
    public int getPercentComplete(){
        return (mPercentComplete/11)*100;
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
