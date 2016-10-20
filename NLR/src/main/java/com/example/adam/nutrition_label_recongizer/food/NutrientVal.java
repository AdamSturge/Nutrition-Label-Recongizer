package com.example.adam.nutrition_label_recongizer.food;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

/**
 * Created by Adam on 9/30/2016.
 */

public class NutrientVal implements Parcelable {
    Nutrient.NType mType;
    float mVal;

    private static final String NUTRIENT_TYPE_BUNDLE_KEY = "nutrient type";
    private static final String VALUE_BUNDLE_KEY = "value";


    public NutrientVal(Nutrient.NType name, float val) {
        mType = name;
        mVal = val;
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

    @Override
    public String toString(){
        return "{ type : " + mType.name() +", val : " + mVal+" }";
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

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<NutrientVal> CREATOR = new Parcelable.Creator<NutrientVal>(){

        @Override
        public NutrientVal createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            Nutrient.NType nType = (Nutrient.NType)bundle.getSerializable(NUTRIENT_TYPE_BUNDLE_KEY);
            float val = bundle.getFloat(VALUE_BUNDLE_KEY);

            return new NutrientVal(nType,val);
        }

        @Override
        public NutrientVal[] newArray(int size) {
            return new NutrientVal[size];
        }
    };

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(!NutrientVal.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        final NutrientVal other = (NutrientVal)obj;
        if(this.mType != other.getType()){
            return false;
        }
        if(this.mVal != other.getVal()){
            return false;
        }
        return true;
    }

}
