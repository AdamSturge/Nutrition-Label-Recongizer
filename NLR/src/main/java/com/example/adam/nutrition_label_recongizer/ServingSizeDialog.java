package com.example.adam.nutrition_label_recongizer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adam.nutrition_label_recongizer.food.FoodItem;

/**
 * Created by Adam on 10/16/2016.
 */

public class ServingSizeDialog extends DialogFragment {

    private View mLayout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        mLayout = inflater.inflate(R.layout.serving_size_dialog, null);
        builder.setView(mLayout);
        builder.setPositiveButton(R.string.action_serving_size_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // sign in the user ...
            }
        });
        builder.setNegativeButton(R.string.action_serving_size_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setTitle("Serving size");

        Dialog dialog = builder.create();
        dialog.getWindow().setLayout(800, RelativeLayout.LayoutParams.WRAP_CONTENT); // TO DO : FIGURE OUT HOW TO AUTOMATICALLY SET WIDTH

        NumberPicker np = (NumberPicker)mLayout.findViewById(R.id.serving_size_num_picker);
        np.setMinValue(0);
        np.setMaxValue(100);

        TextView tv = (TextView)mLayout.findViewById(R.id.serving_size_unit);
        tv.setText(getArguments().getString("unit","unit not found"));

        return dialog;
    }


}