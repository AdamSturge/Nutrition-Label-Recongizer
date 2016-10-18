package com.example.adam.nutrition_label_recongizer.user;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Adam on 10/16/2016.
 */

public class UserManager {

    private SharedPreferences mPreferences;

    private static final String USER_NUTRIENT_STORAGE_DAY_KEY = "NUTRIENTS LAST UPDATED ON DAY";
    private static final String PERCENT_VIEW_KEY = "PERCENT VIEW";


    public UserManager(SharedPreferences preferences)
    {
        mPreferences = preferences;
    }

    /**
     * Loads consumed nutrients out of persistent storage into ArrayList
     */
    public ArrayList<NutrientVal>  loadUserNutrients(){
        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();

        int lastStoredDay = mPreferences.getInt(USER_NUTRIENT_STORAGE_DAY_KEY ,1);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(lastStoredDay != today){
            // Clear stored values
            resetUserNutrients();
        }

        for(Nutrient.NType nType : Nutrient.NType.values()){
            float val = mPreferences.getFloat(nType.name(),0.0f);
            NutrientVal nutrientVal = new NutrientVal(nType,val);
            nutrientVals.add(nutrientVal);
        }

        return nutrientVals;

    }

    /**
     * Saves the current nutrient values to persistent storage.
     */
    public void saveUserNutrients(ArrayList<NutrientVal> nutrientVals){
        SharedPreferences.Editor editor = mPreferences.edit();

        for(NutrientVal nutrientVal : nutrientVals){
            editor.putFloat(nutrientVal.getName(),nutrientVal.getVal());
        }
        editor.commit();
    }

    /**
     * Resets the nutrient values in persistent storage to zero. Updates last reset day
     */
    public void resetUserNutrients(){
        SharedPreferences.Editor editor = mPreferences.edit();

        for(Nutrient.NType nType : Nutrient.NType.values()){
            editor.putFloat(nType.name(),0.0f);
        }
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        editor.putInt(USER_NUTRIENT_STORAGE_DAY_KEY ,today);
        editor.commit();
    }

    /**
     * Loads the users preference for percent view or absolute view out of persistent storage
     * @return boolean, true for percent view, false for absolute view
     */
    public boolean loadPercentViewPreference(){
        return mPreferences.getBoolean(PERCENT_VIEW_KEY,false);
    }

    /**
     * Saves the users prefernce for percent view or absolute view in persistent storage
     * @param percentView
     */
    public void savePercentViewPreference(boolean percentView){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(PERCENT_VIEW_KEY,percentView);
        editor.commit();
    }
}
