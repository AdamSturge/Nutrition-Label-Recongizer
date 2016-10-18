package com.example.adam.nutrition_label_recongizer.food;

import android.util.Log;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adam on 10/1/2016.
 */

public class NutrientValFactory {
    /**
     * Attempts to parse a block of text as a nutrient value by comparing the text
     * to the Nutrient.Ntype enum.
     * @param text text to be parsed
     * @return the first matching nutrient type and value or null if none match
     */
    public NutrientVal buildFromText(String text){
        try{
            Pattern pattern = Pattern.compile("(^|\\s)(\\d+)(\\s*)(\\w{1,2})($|\\s)"); //matches some number of spaces, number, unit, then some number of spaces

            NutrientVal nutrientVal = null;
            text = text.toLowerCase();

            for(Nutrient.NType nType : Nutrient.NType.values()){
                String nTypeName = nType.name().toLowerCase();
                if(text.contains(nTypeName)){
                    Matcher matcher = pattern.matcher(text);
                    if(matcher.find()){
                        String stringVal = matcher.group(2);
                        String stringUnit = matcher.group(4);

                        float val = Float.parseFloat(stringVal);
                        switch (stringUnit){
                            case "g"  : nutrientVal = new NutrientVal(nType,val); break;
                            case "mg" : nutrientVal = new NutrientVal(nType,val/1000f); break;
                            default   : nutrientVal = new NutrientVal(nType,val); break;
                        }

                        break; // extracted info form this line, stop looking for more nutrient info
                    }
                }
            }

            return nutrientVal;
        }catch (NumberFormatException e){
            Log.e("NutrientValFactory",e.toString());
            return null;
        }

    }
}
