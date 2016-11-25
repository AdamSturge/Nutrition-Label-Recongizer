package com.example.adam.nutrition_label_recongizer.food;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adam on 10/20/2016.
 */

class ServingFactory {

    private final static String SERVING_REGEX = "(^|\\s)(\\w*)(per\\s+)(\\d+)(/\\d+)?(\\s*)(\\w+)($|\\s*)";

    /**
     * Attempts to extract the serving information out the the string contained in a textblock
     * @param text
     * @return serving information, or null if failed
     */
    public Serving buildFromText(String text){
        try{
            Pattern pattern = Pattern.compile(SERVING_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            if(matcher.find() && matcher.groupCount() >= 7){
                String numer =  numer = matcher.group(4);
                String denom = "1.0";
                if(matcher.group(5) != null){
                    denom = matcher.group(5).substring(1); //remove division operator;
                }

                String unit = matcher.group(7).trim();
                float amount = Float.parseFloat(numer)/Float.parseFloat(denom);
                return new Serving(amount,unit);
            }else {
                return null;
            }
        }
        catch (NumberFormatException e){
            return null;
        }
    }
}
