package com.example.adam.nutrition_label_recongizer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.adam.nutrition_label_recongizer.food.FoodHealthChecker;
import com.example.adam.nutrition_label_recongizer.food.FoodItem;

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
        FoodHealthChecker healthChecker = new FoodHealthChecker();
        float healthiness = healthChecker.absoluteHealth(foodItem);

        if (healthiness > 600) {
            healthiness = 600;
        } else if (healthiness < -600) {
            healthiness = -600;
        }

        int goodColor = getResources().getColor(R.color.goodNutrient, theme);
        int badColor = getResources().getColor(R.color.badNutrient, theme);

        int goodRed = Color.red(goodColor);
        int badRed = Color.red(badColor);
        int buttonRed = (int) cappedColor(healthiness, goodRed, badRed, 1);

        int goodGreen = Color.green(goodColor);
        int badGreen = Color.green(badColor);
        int buttonGreen = (int) cappedColor(healthiness, goodGreen, badGreen, 1);

        int goodBlue = Color.blue(goodColor);
        int badBlue = Color.blue(badColor);
        int buttonBlue = (int) cappedColor(healthiness, goodBlue, badBlue, 1);

        int grad = Color.rgb(buttonRed, buttonGreen, buttonBlue);

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


