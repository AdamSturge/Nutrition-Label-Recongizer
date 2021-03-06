package com.example.adam.nutrition_label_recongizer.ocr;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.adam.nutrition_label_recongizer.ocr.camera.GraphicOverlay;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;

/**
 * Created by Adam on 9/27/2016.
 */

public class OcrGraphic extends GraphicOverlay.Graphic {
    private int mId;

    private static final int TEXT_COLOR = Color.BLACK;
    private static final int BOUNDING_BOX_COLOR = Color.argb(125,255,255,255);
    private static final float BOUNDING_BOX_PADDING = 10.0f;
    private static final boolean DRAW_BOUNDING_BOX = false;
    private static final boolean DRAW_TEXT = false;

    private static Paint sRectPaint;
    private static Paint sTextPaint;
    private final TextBlock mText;

    OcrGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);

        mText = text;

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(BOUNDING_BOX_COLOR);
            sRectPaint.setStrokeWidth(4.0f);
            sRectPaint.setStyle(Paint.Style.FILL);


        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(54.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public TextBlock getTextBlock() {
        return mText;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        if (mText == null) {
            return false;
        }
        RectF rect = new RectF(mText.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (mText == null) {
            return;
        }

        if(DRAW_BOUNDING_BOX){
            // Draws the bounding box around the TextBlock.
            RectF rect = new RectF(mText.getBoundingBox());
            rect.left = translateX(rect.left - BOUNDING_BOX_PADDING);
            rect.top = translateY(rect.top - BOUNDING_BOX_PADDING);
            rect.right = translateX(rect.right + BOUNDING_BOX_PADDING);
            rect.bottom = translateY(rect.bottom + BOUNDING_BOX_PADDING);
            canvas.drawRoundRect(rect,12,12,sRectPaint);
        }

        if(DRAW_TEXT){
            // Break the text into multiple lines and draw each one according to its own bounding box.
            List<? extends Text> textComponents = mText.getComponents();
            for(Text currentText : textComponents) {
                float left = translateX(currentText.getBoundingBox().left);
                float bottom = translateY(currentText.getBoundingBox().bottom);
                canvas.drawText(currentText.getValue(), left, bottom, sTextPaint);
            }
        }
    }
}

