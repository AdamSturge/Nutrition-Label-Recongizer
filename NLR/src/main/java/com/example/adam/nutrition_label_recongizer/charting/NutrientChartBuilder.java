package com.example.adam.nutrition_label_recongizer.charting;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.nutrients.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrients.NutrientFactory;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by Adam on 10/1/2016.
 */

public class NutrientChartBuilder {

    private Context mContext;

    private float mNextBarCenter;
    private float mXAxisGranulatirty;
    private float mLabelSize;

    private float mBarWidth;
    private BarData mBarData;

    private float mThresholdLineWidth;
    private LineData mLineData;

    private ArrayList<String> mXAxisLabels;

    public NutrientChartBuilder(Context context){
        mContext = context;

        mNextBarCenter = 0.0f;
        mXAxisGranulatirty = 0.3f;
        mLabelSize = 10.0f;

        mBarWidth = 0.2f;
        mBarData = new BarData();
        mBarData.setBarWidth(mBarWidth);

        mThresholdLineWidth = 6.0f;
        mLineData = new LineData();

        mXAxisLabels = new ArrayList<String>();
    }

    /**
     * Adds a nutrient to the chart (bar plus threshold)
     * @param nutrientVal
     * @param color
     */
    public void addNutrient(NutrientVal nutrientVal, int color){
        AddBar(nutrientVal,color);
        AddThreshold(nutrientVal.getType(), Color.YELLOW);

        mXAxisLabels.add(nutrientVal.getName().toLowerCase());
        mNextBarCenter += mXAxisGranulatirty;
    }

    /**
     * Builds a combined bar and line chart representing the data stored in the builder
     * @return Combined chart
     */
    public CombinedChart build(){
        CombinedChart chart = new CombinedChart(mContext);

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        chart.setMaxVisibleValueCount(60);

        chart.setPinchZoom(true);

        chart.setDrawGridBackground(true);

        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(true);

        chart.setHighlightFullBarEnabled(false);

        chart.setDescription("");

        chart.animateY(1250, Easing.EasingOption.EaseInCubic);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(mXAxisGranulatirty);
        xAxis.setAxisMinValue(-mBarWidth - 0.1f);
        xAxis.setAxisMaxValue(mNextBarCenter);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(mLabelSize);
        //xAxis.setCenterAxisLabels(true); // Causes crash!
        xAxis.setValueFormatter(new AxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)(value/mXAxisGranulatirty);
                String label = index < mXAxisLabels.size() && index > -1 ? mXAxisLabels.get(index) : "";
                return label;
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        });
        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisMinValue(0.0f);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setEnabled(false);

        CombinedData data = new CombinedData();
        data.setData(mBarData);
        data.setData(mLineData);
        chart.setData(data);

        return chart;
    }

    /**
     * Adds a bar to the chart representing the passed nutrient value
     * Expected to be called alongside addThreshold()
     * @param nutrientVal
     * @param color bar color
     */
    private void AddBar(NutrientVal nutrientVal, int color){
        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        barEntries.add(new BarEntry(mNextBarCenter,(float)nutrientVal.getVal(),nutrientVal.getName()));
        BarDataSet dataSet = new BarDataSet(barEntries,"");
        dataSet.setColor(color);
        dataSet.setDrawValues(false);
        mBarData.addDataSet(dataSet);
    }

    /**
     * Adds a nutrient threshold for the provided nutrient type
     * Expected to be called alongside addBar()
     * @param nType type of nutrient to add threshold for
     * @param color line color
     */
    private void AddThreshold(Nutrient.NType nType, int color){
        NutrientFactory nutrientFactory = new NutrientFactory();
        Nutrient nutrient = nutrientFactory.buildNutrient(nType);

        ArrayList<Entry> lineEntries = new ArrayList<Entry>();
        lineEntries.add(new Entry(mNextBarCenter - mBarWidth/2,nutrient.getThreshold(),0f));
        lineEntries.add(new Entry(mNextBarCenter + mBarWidth/2,nutrient.getThreshold(),0f));
        LineDataSet lineDataSet = new LineDataSet(lineEntries,"");
        lineDataSet.setLineWidth(mThresholdLineWidth);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(color);
        lineDataSet.setDrawCircles(false);
        mLineData.addDataSet(lineDataSet);
    }
}
