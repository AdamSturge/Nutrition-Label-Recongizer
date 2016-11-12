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
        Log.e("ADAM","HI:"+foodUtil.HealthIndex());

        if (healthiness > 600) {
            healthiness = 600;
        } else if (healthiness < -600) {
            healthiness = -600;
        }

        int goodColor = getResources().getColor(R.color.goodNutrient, theme);
        int badColor = getResources().getColor(R.color.badNutrient, theme);

        int grad = (int) (healthiness*badColor + (1-healthiness)*goodColor); //convex combination of bad and good color

        this.setBackgroundColor(grad);

        return grad;
    }

    /**
     * Modified hyperbolic tangent function
     *
     * @param x     arg for tanh
     * @param a     limit as x -> infinity
     * @param b     limit as x -> -infinity
     * @param scale scale factor to adjust arg
     * @return double in (a,b)
     */
    private double cappedColor(double x, double a, double b, double scale) {
        return ((a * Math.exp(scale * x) + b * Math.exp(-scale * x)) / (Math.exp(scale * x) + Math.exp(-scale * x)));
    }
}


