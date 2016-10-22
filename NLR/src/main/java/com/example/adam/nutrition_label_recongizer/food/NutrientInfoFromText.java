package com.example.adam.nutrition_label_recongizer.food;

import android.util.Log;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Adam on 10/20/2016.
 */

public class NutrientInfoFromText {

    /**
     * Extracts nutrient values out of a textblock
     * @param textBlock
     * @return List of nutrient values, or empty array if no data can be extracted
     */
    public ArrayList<NutrientVal> extractNutrientVals(TextBlock textBlock){
        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();
        ArrayList<Line> lines = (ArrayList<Line>)textBlock.getComponents();
        for(Line line : lines){
            NutrientVal nutrientVal = extractNutrientVals(line.getValue());
            if(nutrientVal != null){
                nutrientVals.add(nutrientVal);
            }
        }
        return nutrientVals;
    }

    /**
     * Attempts to extract the number of calories out of the string contained in a textblock
     * @param textBlock
     * @return number of calories, or -1 if failed to extract the info
     */
    public int extractCalories(TextBlock textBlock){
        try{
            ArrayList<Line> lines = (ArrayList<Line>)textBlock.getComponents();
            int calories = -1;
            for(Line line : lines){
                calories = extractCalories(line.getValue());
                if(calories != -1){
                    return calories;
                }
            }
            return calories;
        }
        catch (NumberFormatException e){
            Log.e("extractCalories",e.toString());
            return -1;
        }
    }

    /**
     * Attempts to extract the serving information out the the string contained in a textblock
     * @param textBlock
     * @return serving information, or null if failed
     */
    public Serving extractServing(TextBlock textBlock){
        ArrayList<Line> lines = (ArrayList<Line>)textBlock.getComponents();
        Serving serving = null;
        for(Line line : lines){
            serving = extractServing(line.getValue());
            if(serving != null){
                return serving;
            }
        }
        return serving;
    }

    /**
     * Extracts nutrient value out of a string
     * @param text
     * @return nutrient value represented by string, or null if no data could be retrieved
     */
    public NutrientVal extractNutrientVals(String text){
        NutrientValFactory valFactory = new NutrientValFactory();
        ArrayList<NutrientVal> nutrientVals = new ArrayList<NutrientVal>();

        return valFactory.buildFromText(text);

    }

    /**
     * Attempts to extract the number of calories out of a string
     * @param text
     * @return number of calories, or -1 if failed to extract the info
     */
    public int extractCalories(String text){
        try{
            int calories = -1;

            if(text.toLowerCase().contains("calories")){
                String numbers = text.replaceAll("[^0-9]","");
                calories = numbers.isEmpty() ? -1 : Integer.parseInt(numbers);
            }

            return calories;
        }
        catch (NumberFormatException e){
            Log.e("extractCalories",e.toString());
            return -1;
        }
    }

    /**
     * Attempts to extract the serving information out of a string
     * @param text
     * @return serving information, or null if failed
     */
    public Serving extractServing(String text){
        ServingFactory servingFactory = new ServingFactory();
        return servingFactory.buildFromText(text);
    }

    public void scrubTextBlock(TextBlock textBlock, ArrayList<Nutrient.NType> filter, boolean keepLinesWithNoData){
        ArrayList<Line> lines = (ArrayList<Line>)textBlock.getComponents();
        for(int i = 0; i < lines.size(); ++i){
            NutrientVal nutrientVal = extractNutrientVals(lines.get(i).getValue());
            if(nutrientVal != null){
                if(filter.contains(nutrientVal.getType())){
                   lines.remove(i);
                    i--; //decrement to stay in same place since array to the right shifted down one
                }
            }else if(!keepLinesWithNoData){
                lines.remove(i);
                i--;//decrement to stay in same place since array to the right shifted down one
            }
        }
    }
}
