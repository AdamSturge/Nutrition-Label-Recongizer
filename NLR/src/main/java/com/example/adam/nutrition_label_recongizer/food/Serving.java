package com.example.adam.nutrition_label_recongizer.food;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adam on 10/1/2016.
 */

public class Serving implements Parcelable{
    private float mAmount;
    private String mUnit;

    private static final String AMOUNT_BUNDLE_KEY = "amount";
    private static final String UNIT_BUNDLE_KEY = "unit";

    public Serving(float amount, String unit) {
        mAmount = amount;
        mUnit = unit;
    }

    public float getAmount() {
        return mAmount;
    }

    public String getUnit() {
        return mUnit;
    }

    @Override
    public String toString(){
        return "{ amount : " + mAmount + ", unit : " + mUnit + " }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putFloat(AMOUNT_BUNDLE_KEY,mAmount);
        bundle.putString(UNIT_BUNDLE_KEY,mUnit);

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<Serving> CREATOR = new Parcelable.Creator<Serving>(){

        @Override
        public Serving createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            float amount = bundle.getFloat(AMOUNT_BUNDLE_KEY);
            String unit = bundle.getString(UNIT_BUNDLE_KEY);
            return new Serving(amount,unit);
        }

        @Override
        public Serving[] newArray(int size) {
            return new Serving[size];
        }
    };

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(!Serving.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        Serving other = (Serving)obj;
        if(this.mAmount != other.getAmount()){
            return  false;
        }
        if(this.mUnit.compareTo(other.getUnit()) != 0){
            return false;
        }
        return true;

    }
}
