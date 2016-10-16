package com.example.adam.nutrition_label_recongizer.user;

import android.content.SharedPreferences;

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
            nutrientVals.add(new NutrientVal(nType,val, NutrientVal.unit.GRAM));
        }

        return nutrientVals;

    }
    /**
     * Saves the current nutrient values to persistent storage
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
}
