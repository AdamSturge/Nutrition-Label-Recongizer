package com.example.adam.nutrition_label_recongizer.food;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.adam.nutrition_label_recongizer.nutrients.Nutrient;

/**
 * Created by Adam on 9/30/2016.
 */

public class NutrientVal implements Parcelable {
    public enum unit {GRAM,MILLIGRAM};

    Nutrient.NType mType;
    float mVal;
    unit mUnit;

    private static final String NUTRIENT_TYPE_BUNDLE_KEY = "nutrient type";
    private static final String VALUE_BUNDLE_KEY = "value";
    private static final String UNIT_BUNDLE_KEY = "unit";


    public NutrientVal(Nutrient.NType name, float val,unit unit) {
        mType = name;
        mVal = val;
        mUnit = unit;
    }

    public Nutrient.NType getType() {
        return mType;
    }

    public String getName() {
        return mType.name();
    }

    public float getVal() {
        return mVal;
    }

    public unit getUnit() {
        return mUnit;
    }

    @Override
    public String toString(){
        return "{ type : " + mType.name() +", val : " + mVal+", unit : " + mUnit+" }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NUTRIENT_TYPE_BUNDLE_KEY, mType);
        bundle.putFloat(VALUE_BUNDLE_KEY,mVal);
        bundle.putSerializable(UNIT_BUNDLE_KEY,mUnit);

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<NutrientVal> CREATOR = new Parcelable.Creator<NutrientVal>(){

        @Override
        public NutrientVal createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            Nutrient.NType nType = (Nutrient.NType)bundle.getSerializable(NUTRIENT_TYPE_BUNDLE_KEY);
            int val = bundle.getInt(VALUE_BUNDLE_KEY);
            unit unit = (unit)bundle.getSerializable(UNIT_BUNDLE_KEY);

            return new NutrientVal(nType,val,unit);
        }

        @Override
        public NutrientVal[] newArray(int size) {
            return new NutrientVal[size];
        }
    };
}
