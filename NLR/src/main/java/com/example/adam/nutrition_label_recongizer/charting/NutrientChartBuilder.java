package com.example.adam.nutrition_label_recongizer.charting;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;

import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.nutrients.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrients.NutrientFactory;
import com.example.adam.nutrition_label_recongizer.nutrients.Sugar;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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
    private ArrayList<BarDataSet> mGoodBarDataSets;
    private ArrayList<BarDataSet> mBadBarDataSets;

    private float mThresholdLineWidth;
    private LineData mLineData;

    private ArrayList<String> mXAxisLabels;

    private static final String GOOD_NUTRIENT_COLOR = "#269fca";
    private static final String BAD_NUTRIENT_COLOR = "#e76182";
    private static final String GOOD_NUTRIENT_LABEL = "Eat more of these";
    private static final String BAD_NUTRIENT_LABEL = "Limit these";

    public NutrientChartBuilder(Context context){
        mContext = context;

        mNextBarCenter = 0.0f;
        mXAxisGranulatirty = 1.0f;
        mLabelSize = 10.0f;

        mBarWidth = 0.9f;
        mBarData = new BarData();
        mBarData.setBarWidth(mBarWidth);

        mGoodBarDataSets = new ArrayList<BarDataSet>();
        mBadBarDataSets = new ArrayList<BarDataSet>();

        mThresholdLineWidth = 1.0f;
        mLineData = new LineData();

        mXAxisLabels = new ArrayList<String>();
    }

    /**
     * Adds a nutrient to the chart (bar plus threshold)
     * @param nutrientVal
     * @param isGood is nutrient good for you
     */
    public void addNutrient(NutrientVal nutrientVal, boolean isGood){
        if(isGood){
            addNutrientToGoodGroup(nutrientVal);
        }else{
            addNutrientToBadGroup(nutrientVal);
        }
        AddThreshold(nutrientVal.getType(), Color.BLACK);

        mXAxisLabels.add(nutrientVal.getName().toLowerCase());
        mNextBarCenter += mXAxisGranulatirty;
    }

    public void addNutrient(Pair<NutrientVal,NutrientVal> nutrientVals, boolean isGood){
        if(isGood){
            addNutrientToGoodGroup(nutrientVals);
        }else{
            addNutrientToBadGroup(nutrientVals);
        }
        AddThreshold(nutrientVals.first.getType(), Color.BLACK);

        mXAxisLabels.add(nutrientVals.first.getName().toLowerCase());
        mNextBarCenter += mXAxisGranulatirty;
    }

    /**
     * Adds a nutrient value to the group of nutrients that are bad for you
     * @param nutrientVal
     */
    private void addNutrientToBadGroup(NutrientVal nutrientVal){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter,nutrientVal.getVal(),nutrientVal.getName()));
        BarDataSet goodBarDataSet = buildBadNutrientDataSet(entries, nutrientVal.getName());
        mBadBarDataSets.add(goodBarDataSet);
    }

    /**
     * Adds a nutrient value to the group of nutrients that are good for you
     * @param nutrientVal
     */
    private void addNutrientToGoodGroup(NutrientVal nutrientVal){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter,nutrientVal.getVal(),nutrientVal.getName()));
        BarDataSet goodBarDataSet = buildGoodNutrientDataSet(entries, nutrientVal.getName());
        mGoodBarDataSets.add(goodBarDataSet);
    }

    /**
     * Adds a stacked bar to the chart representing already consumed and two be consumed bad nutrients
     * @param nutrientVals
     */
    private void addNutrientToBadGroup(Pair<NutrientVal,NutrientVal> nutrientVals){
        float firstVal = nutrientVals.first.getUnit() == NutrientVal.unit.GRAM ? nutrientVals.first.getVal() : nutrientVals.first.getVal()/1000.0f;
        float secondVal = nutrientVals.second.getUnit() == NutrientVal.unit.GRAM ? nutrientVals.second.getVal() : nutrientVals.second.getVal()/1000.0f;
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter, new float[]{firstVal,secondVal},nutrientVals.first.getName()));
        BarDataSet badBarDataSet = buildBadNutrientDataSet(entries, nutrientVals.first.getName());
        mBadBarDataSets.add(badBarDataSet);
    }
    /**
     * Adds a stacked bar to the chart representing already consumed and two be consumed good nutrients
     * @param nutrientVals
     */
    private void addNutrientToGoodGroup(Pair<NutrientVal,NutrientVal> nutrientVals){
        float firstVal = nutrientVals.first.getUnit() == NutrientVal.unit.GRAM ? nutrientVals.first.getVal() : nutrientVals.first.getVal()/1000.0f;
        float secondVal = nutrientVals.second.getUnit() == NutrientVal.unit.GRAM ? nutrientVals.second.getVal() : nutrientVals.second.getVal()/1000.0f;
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter, new float[]{firstVal,secondVal},nutrientVals.first.getName()));
        BarDataSet goodBarDataSet = buildGoodNutrientDataSet(entries, nutrientVals.first.getName());
        mGoodBarDataSets.add(goodBarDataSet);
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

    /**
     * Builds a bar data set styled to conform with bad nutrients
     * @param entries array of bar entries to belong to this data set
     * @param label label for the data set
     * @return BarDataSet
     */
    private BarDataSet buildBadNutrientDataSet(ArrayList<BarEntry> entries, String label){
        BarDataSet badBarDataSet = new BarDataSet(entries,label);
        badBarDataSet.setDrawValues(false);
        badBarDataSet.setColors(new int[] {Color.parseColor(BAD_NUTRIENT_COLOR), Color.TRANSPARENT});
        badBarDataSet.setBarBorderColor(Color.parseColor(BAD_NUTRIENT_COLOR));
        badBarDataSet.setBarBorderWidth(1.0f);

        return badBarDataSet;
    }

    /**
     * Builds a bar data set styled to conform with good nutrients
     * @param entries array of bar entries to belong to this data set
     * @param label label for the data set
     * @return BarDataSet
     */
    private BarDataSet buildGoodNutrientDataSet(ArrayList<BarEntry> entries, String label){
        BarDataSet goodBarDataSet = new BarDataSet(entries,label);
        goodBarDataSet.setDrawValues(false);
        goodBarDataSet.setColors(new int[] {Color.parseColor(GOOD_NUTRIENT_COLOR), Color.TRANSPARENT});
        goodBarDataSet.setBarBorderColor(Color.parseColor(GOOD_NUTRIENT_COLOR));
        goodBarDataSet.setBarBorderWidth(1.0f);

        return goodBarDataSet;
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

        chart.zoom(1.6f,1.0f,0f,0f);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        // TO DO : Figure out how to set maximum "zoom out level"

        chart.setMaxVisibleValueCount(60);

        chart.setDrawGridBackground(false);

        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(false);

        chart.setHighlightFullBarEnabled(false);

        chart.setDescription("");

        chart.animateY(1250, Easing.EasingOption.EaseInCubic);

        chart.setHighlightFullBarEnabled(false);

        chart.setBorderWidth(0);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);
        l.setCustom(new int[]{Color.parseColor(GOOD_NUTRIENT_COLOR),Color.parseColor(BAD_NUTRIENT_COLOR)}, new String[]{GOOD_NUTRIENT_LABEL,BAD_NUTRIENT_LABEL});

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(mXAxisGranulatirty);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMinValue(-mBarWidth);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setTextSize(mLabelSize);
        xAxis.setAxisLineWidth(0.0f);
        xAxis.setAxisLineColor(Color.TRANSPARENT);
        xAxis.setValueFormatter(new AxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)(value/mXAxisGranulatirty);
                String rawLabel = index < mXAxisLabels.size() && index > -1 ? mXAxisLabels.get(index) : "";
                String label = "";
                switch (rawLabel){
                    case "carbohydrate" : label = "carbs"; break;
                    case "sat_fat"      : label = "sat fat"; break;
                    case "trans_fat"    : label = "trans fat"; break;
                    case "cholesterol"  : label = "chol"; break;
                    default             : label = rawLabel; break;
                }

                return label;
            }
            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        });


        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisMinValue(0.0f);
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisLineWidth(0.0f);
        axisLeft.setAxisLineColor(Color.TRANSPARENT);
        axisLeft.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });


        YAxis axisRight = chart.getAxisRight();
        axisRight.setEnabled(false);

        for(BarDataSet set : mBadBarDataSets){
            mBarData.addDataSet(set);
        }

        for(BarDataSet set : mGoodBarDataSets){
            mBarData.addDataSet(set);
        }

        CombinedData data = new CombinedData();
        data.setData(mBarData);
        data.setData(mLineData);
        chart.setData(data);

        return chart;
    }
}
