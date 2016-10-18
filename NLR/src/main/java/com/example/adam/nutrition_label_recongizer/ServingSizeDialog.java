package com.example.adam.nutrition_label_recongizer;

import android.app.Activity;
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
import com.example.adam.nutrition_label_recongizer.food.Serving;

/**
 * Created by Adam on 10/16/2016.
 */

public class ServingSizeDialog extends DialogFragment {

    private ServingSizeDialogListener mListener;
    private float mServingSize;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View layout = inflater.inflate(R.layout.serving_size_dialog, null);
        builder.setView(layout);
        builder.setPositiveButton(R.string.action_serving_size_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mListener.onServingSizeSet(ServingSizeDialog.this);
            }
        });
        builder.setNegativeButton(R.string.action_serving_size_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setTitle("Serving size");

        Dialog dialog = builder.create();
        dialog.getWindow().setLayout(800, RelativeLayout.LayoutParams.WRAP_CONTENT); // TO DO : FIGURE OUT HOW TO AUTOMATICALLY SET WIDTH

        mServingSize = getArguments().getFloat("serving size",1.0f);

        NumberPicker np = (NumberPicker)layout.findViewById(R.id.serving_size_num_picker);
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setValue((int)mServingSize);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mServingSize = newVal;
            }
        });

        TextView tv = (TextView)layout.findViewById(R.id.serving_size_unit);
        tv.setText(getArguments().getString("unit","unit not found"));

        return dialog;
    }

    public interface ServingSizeDialogListener{
        public void onServingSizeSet(ServingSizeDialog dialog);
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ServingSizeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ServingSizeDialogListener");
        }
    }

    public float getServingSize() {
        return mServingSize;
    }
}