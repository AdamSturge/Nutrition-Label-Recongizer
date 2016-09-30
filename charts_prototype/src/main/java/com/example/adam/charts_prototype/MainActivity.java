package com.example.adam.charts_prototype;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;

import android.widget.RelativeLayout;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CombinedChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* // create chart
        LineChart chart = new LineChart(this.getApplicationContext());

        // create data
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 1));
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);

        // add data to chart
        chart.setData(lineData);

        // Set chart view layout parameters
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(params);

        // get a layout defined in xml
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);

        rl.addView(chart); // add the programmatically created chart

        chart.invalidate(); // refresh*/

        mChart = new CombinedChart(this.getApplicationContext());

        // draw bars behind lines
        mChart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });

        mChart.setMaxVisibleValueCount(60);

        mChart.setPinchZoom(true);

        mChart.setDrawGridBackground(true);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.setHighlightFullBarEnabled(false);

        mChart.setDescription("");

        mChart.animateY(1250, Easing.EasingOption.EaseInCubic);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1.0f);
        xAxis.setAxisMinValue(-1.0f);
        xAxis.setAxisMaxValue(3.0f);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(20f);
        //xAxis.setCenterAxisLabels(true); // Causes crash!
        xAxis.setValueFormatter(new AxisValueFormatter() {

            private String[] mValues = new String[]{"protein","carbs"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                switch((int)value) {
                    case 0 : return "protein";
                    case 1 :return "carbs";
                    default : return "";
                }
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        });
        YAxis axisLeft = mChart.getAxisLeft();
        axisLeft.setAxisMinValue(0.0f);

        YAxis axisRight = mChart.getAxisRight();
        axisRight.setEnabled(false);

        /**---------------------------------------------------Bar Chart Begin----------------------------------------------------------------**/
        BarData barData = new BarData();
        barData.setBarWidth(0.9f);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        barEntries.add(new BarEntry(0.0f,new float[]{3.0f,5.0f},"protein"));
        BarDataSet dataSet1 = new BarDataSet(barEntries,"xxxxx");
        dataSet1.setColors(new int[]{Color.GREEN,Color.GREEN});
        dataSet1.setDrawValues(false);
        barData.addDataSet(dataSet1);

        ArrayList<BarEntry> barEntries2 = new ArrayList<BarEntry>();
        barEntries2.add(new BarEntry(1.0f,new float[]{6.0f,1.0f},"carbs"));
        BarDataSet dataSet2 = new BarDataSet(barEntries2,"xxxxx");
        dataSet2.setColors(new int[]{Color.GREEN,Color.RED});
        dataSet2.setDrawValues(false);
        barData.addDataSet(dataSet2);

/*        ArrayList<BarEntry> entryList3 = new ArrayList<BarEntry>();
        entryList3.add(new BarEntry(0.0f,new float[]{7.0f},"protein threshold"));
        BarDataSet dataSet3 = new BarDataSet(entryList3,"xxxxx");
        dataSet3.setColors(new int[]{Color.TRANSPARENT});
        dataSet3.setBarBorderColor(Color.BLUE);
        dataSet3.setBarBorderWidth(1f);
        dataSet3.setDrawValues(false);
        barData.addDataSet(dataSet3);*/

        //mChart.setFitBars(true); // make the x-axis fit exactly all bars
        /**---------------------------------------------------Bar Chart End----------------------------------------------------------------**/
        /**---------------------------------------------------Line Charts Begin----------------------------------------------------------------**/
        LineData lineData = new LineData();

        ArrayList<Entry> lineEntries = new ArrayList<Entry>();
        lineEntries.add(new Entry(-0.45f,7,0f));
        lineEntries.add(new Entry(0.45f,7,0f));
        LineDataSet lineDataSet1 = new LineDataSet(lineEntries,"YYYYY");
        lineDataSet1.setLineWidth(3f);
        lineDataSet1.setDrawValues(false);
        lineDataSet1.setColor(Color.YELLOW);
        lineDataSet1.setDrawCircles(false);
        lineData.addDataSet(lineDataSet1);

        ArrayList<Entry> lineEntries2 = new ArrayList<Entry>();
        lineEntries2.add(new Entry(1.0f - 0.45f,6,0f));
        lineEntries2.add(new Entry(1.0f + 0.45f,6,0f));
        LineDataSet lineDataSet2 = new LineDataSet(lineEntries2,"YYYYY");
        lineDataSet2.setLineWidth(3f);
        lineDataSet2.setDrawValues(false);
        lineDataSet2.setColor(Color.YELLOW);
        lineDataSet2.setDrawCircles(false);
        lineData.addDataSet(lineDataSet2);

        /**---------------------------------------------------Line Charts End----------------------------------------------------------------**/

        // Set chart view layout parameters
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        mChart.setLayoutParams(params);

        CombinedData data = new CombinedData();
        data.setData(barData);
        data.setData(lineData);
        mChart.setData(data);

        // get a layout defined in xml
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);

        rl.addView(mChart); // add the programmatically created chart

        mChart.invalidate(); // refresh*/

    }
}
