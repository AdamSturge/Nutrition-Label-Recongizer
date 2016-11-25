package com.example.adam.nutrition_label_recongizer;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.example.adam.nutrition_label_recongizer.food.FoodItem;
import com.example.adam.nutrition_label_recongizer.food.FoodUtil;

/**
 * Created by Adam on 10/21/2016.
 */

public class ConsumeButton extends Button {

    public ConsumeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Updates the color of the consume button so as to reflect how "healthy"
     * the food under consideration is at this moment
     *
     * @param foodItem
     * @param theme activity theme
     * @return updated color
     */
    public int updateColor(FoodItem foodItem, Resources.Theme theme) {
        if (foodItem == null) {
            return 0;
        }
        FoodUtil foodUtil = new FoodUtil(foodItem);
        float healthiness = foodUtil.HealthIndex();

        int goodColor = getResources().getColor(R.color.goodNutrient, theme);
        int badColor = getResources().getColor(R.color.badNutrient, theme);

        int grad = (int) (healthiness*goodColor + (1-healthiness)*badColor); //convex combination of bad and good color

        this.setBackgroundColor(grad);

        return grad;
    }

}


