package com.example.adam.nutrition_label_recongizer.ocr;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adam.nutrition_label_recongizer.R;
import com.example.adam.nutrition_label_recongizer.food.NutrientInfoFromText;
import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.food.Serving;
import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.example.adam.nutrition_label_recongizer.ocr.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


/**
 * Created by Adam on 9/27/2016.
 */

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private SparseArray<TextBlock> mDetectionItems;
    private NutrientInfoFromText mNutrientInfoFromText;
    private ArrayList<Nutrient.NType> mFilter;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
        mDetectionItems = new SparseArray<TextBlock>();
        mNutrientInfoFromText = new NutrientInfoFromText();
        mFilter = new ArrayList<Nutrient.NType>();
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear(false);
        mDetectionItems = detections.getDetectedItems();
        Resources resources = mGraphicOverlay.getContext().getResources();
        for (int i = 0; i < mDetectionItems.size(); ++i) {
            TextBlock item = mDetectionItems.valueAt(i);
            if (item != null && item.getValue() != null) {
                //mNutrientInfoFromText.scrubTextBlock(item,mFilter,false);

                ArrayList<NutrientVal> nutrientVals = mNutrientInfoFromText.extractNutrientVals(item);
                for(NutrientVal nutrientVal : nutrientVals){
                    final String key = nutrientVal.getName().toUpperCase();
                    if(mGraphicOverlay.getPersistentGraphic(key) == null){ // Not actually needed due to scrub, but nice safety check
                        OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                        mGraphicOverlay.addPersistent(key,graphic);
                        mFilter.add(nutrientVal.getType());
                    }
                }

                Serving serving = mNutrientInfoFromText.extractServing(item);
                if(mGraphicOverlay.getPersistentGraphic(resources.getString(R.string.PERSISTENT_GRAPHIC_SERVING)) == null && serving != null){
                    OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                    mGraphicOverlay.addPersistent(resources.getString(R.string.PERSISTENT_GRAPHIC_SERVING),graphic);
                }

                int calories = mNutrientInfoFromText.extractCalories(item);
                if(mGraphicOverlay.getPersistentGraphic(resources.getString(R.string.PERSISTENT_GRAPHIC_CALORIES)) == null && calories != -1){
                    OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                    mGraphicOverlay.addPersistent(resources.getString(R.string.PERSISTENT_GRAPHIC_CALORIES),graphic);
                }
            }

        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear(false);
    }

    public SparseArray<TextBlock> getDetectionItems() {
        return mDetectionItems;
    }

    private boolean checkItemAlreadyPersisted(TextBlock item){
        Collection<OcrGraphic> values = mGraphicOverlay.getPersistentGraphics().values();
        for(OcrGraphic graphic : values){
            if(graphic.getTextBlock().equals(item)){
                return true;
            }
        }
        return  false;
    }

}

