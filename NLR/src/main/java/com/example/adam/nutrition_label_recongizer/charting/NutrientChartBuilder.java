package com.example.adam.nutrition_label_recongizer.charting;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrient.NutrientFactory;
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
    private static final String DISABLED_NUTRIENT_COLOR = "#d3d3d3";
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
    public void addNutrient(NutrientVal nutrientVal, boolean isGood, boolean enabled){
        if(isGood){
            addNutrientToGoodGroup(nutrientVal,enabled);
        }else{
            addNutrientToBadGroup(nutrientVal,enabled);
        }
        AddThreshold(nutrientVal.getType(), Color.BLACK);

        mXAxisLabels.add(nutrientVal.getName().toLowerCase());
        mNextBarCenter += mXAxisGranulatirty;
    }

    /**
     * Adds a stacked bar representing already consumed and to be consumed nutrients
     * @param nutrientVals
     * @param isGood
     */
    public void addNutrient(Pair<NutrientVal,NutrientVal> nutrientVals, boolean isGood,boolean enabled){
        if(isGood){
            addNutrientToGoodGroup(nutrientVals,enabled);
        }else{
            addNutrientToBadGroup(nutrientVals,enabled);
        }
        AddThreshold(nutrientVals.first.getType(), Color.BLACK);

        mXAxisLabels.add(nutrientVals.first.getName().toLowerCase());
        mNextBarCenter += mXAxisGranulatirty;
    }

    /**
     * Adds a nutrient value to the group of nutrients that are bad for you
     * @param nutrientVal
     */
    private void addNutrientToBadGroup(NutrientVal nutrientVal,boolean enabled){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter,new float[]{nutrientVal.getVal()},nutrientVal.getName()));
        BarDataSet badBarDataSet = buildBadNutrientDataSet(entries, nutrientVal.getName(),enabled);

        mBadBarDataSets.add(badBarDataSet);
    }

    /**
     * Adds a stacked bar representing already consumed and to be consumed good nutrients
     * @param nutrientVal
     */
    private void addNutrientToGoodGroup(NutrientVal nutrientVal,boolean enabled){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter,new float[]{nutrientVal.getVal()},nutrientVal.getName()));
        BarDataSet goodBarDataSet = buildGoodNutrientDataSet(entries, nutrientVal.getName(),enabled);
        mGoodBarDataSets.add(goodBarDataSet);
    }

    /**
     * Adds a stacked bar representing already consumed and to be consumed bad nutrients
     * @param nutrientVals
     */
    private void addNutrientToBadGroup(Pair<NutrientVal,NutrientVal> nutrientVals,boolean enabled){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter, new float[]{nutrientVals.first.getVal(),nutrientVals.second.getVal()},nutrientVals.first.getName()));
        BarDataSet badBarDataSet = buildBadNutrientDataSet(entries, nutrientVals.first.getName(),enabled);
        mBadBarDataSets.add(badBarDataSet);
    }
    /**
     * Adds a stacked bar to the chart representing already consumed and two be consumed good nutrients
     * @param nutrientVals
     */
    private void addNutrientToGoodGroup(Pair<NutrientVal,NutrientVal> nutrientVals,boolean enabled){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(mNextBarCenter, new float[]{nutrientVals.first.getVal(),nutrientVals.second.getVal()},nutrientVals.first.getName()));
        BarDataSet goodBarDataSet = buildGoodNutrientDataSet(entries, nutrientVals.first.getName(),enabled);
        mGoodBarDataSets.add(goodBarDataSet);
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
    private BarDataSet buildBadNutrientDataSet(ArrayList<BarEntry> entries, String label, boolean enabled){
        BarDataSet badBarDataSet = new BarDataSet(entries,label);
        badBarDataSet.setDrawValues(false);
        if(enabled){
            badBarDataSet.setColors(new int[] {Color.parseColor(BAD_NUTRIENT_COLOR), Color.TRANSPARENT});
            badBarDataSet.setBarBorderColor(Color.parseColor(BAD_NUTRIENT_COLOR));
        }else{
            badBarDataSet.setColors(new int[] {Color.parseColor(DISABLED_NUTRIENT_COLOR), Color.TRANSPARENT});
            badBarDataSet.setBarBorderColor(Color.parseColor(DISABLED_NUTRIENT_COLOR));
        }

        badBarDataSet.setBarBorderWidth(1.0f);

        return badBarDataSet;
    }

    /**
     * Builds a bar data set styled to conform with good nutrients
     * @param entries array of bar entries to belong to this data set
     * @param label label for the data set
     * @return BarDataSet
     */
    private BarDataSet buildGoodNutrientDataSet(ArrayList<BarEntry> entries, String label, boolean enabled){
        BarDataSet goodBarDataSet = new BarDataSet(entries,label);
        goodBarDataSet.setDrawValues(false);
        if(enabled){
            goodBarDataSet.setColors(new int[] {Color.parseColor(GOOD_NUTRIENT_COLOR), Color.TRANSPARENT});
            goodBarDataSet.setBarBorderColor(Color.parseColor(GOOD_NUTRIENT_COLOR));
        }else{
            goodBarDataSet.setColors(new int[] {Color.parseColor(DISABLED_NUTRIENT_COLOR), Color.TRANSPARENT});
            goodBarDataSet.setBarBorderColor(Color.parseColor(DISABLED_NUTRIENT_COLOR));
        }
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

    /**
     * Builds a combined bar and line chart representing the data stored in the builder
     * Values are divided by their threshold values to create a percent bar chart
     * @return
     */
    public CombinedChart buildPercentChart(){
        CombinedChart chart = build();

        for(int i = 0; i < mLineData.getDataSetCount(); ++i){
            LineDataSet absoluteLineDataSet = (LineDataSet)mLineData.getDataSets().get(i);
            absoluteLineDataSet.getEntryForIndex(0).setY(1.0f);
            absoluteLineDataSet.getEntryForIndex(1).setY(1.0f);
        }


        NutrientFactory nutrientFactory = new NutrientFactory();
        for(int i = 0; i < mBarData.getDataSetCount(); ++i){
            BarDataSet absoluteDataSet = (BarDataSet)mBarData.getDataSets().get(i);
            BarEntry barEntry = absoluteDataSet.getEntryForIndex(0);

            String label = barEntry.getData().toString();
            Nutrient.NType ntype = Nutrient.NType.valueOf(label);
            Nutrient nutrient = nutrientFactory.buildNutrient(ntype);
            float threshold = nutrient.getThreshold();

            float[] oldYVals = barEntry.getYVals();
            float[] newYVals = new float[oldYVals.length];
            for(int j = 0; j < oldYVals.length; ++j){
                newYVals[j] = oldYVals[j]/threshold;
            }

            barEntry.setVals(newYVals);

        }

        chart.getAxisLeft().setAxisMaxValue(1.2f);


        chart.notifyDataSetChanged();


        return chart;
    }
}
