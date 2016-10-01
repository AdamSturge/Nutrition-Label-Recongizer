package com.example.adam.nutrition_label_recongizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.adam.nutrition_label_recongizer.charting.NutrientChartBuilder;
import com.example.adam.nutrition_label_recongizer.food.FoodItem;
import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.nutrients.Nutrient;
import com.example.adam.nutrition_label_recongizer.ocr.CameraActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] mDrawerLabels;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CombinedChart mChart;

    private static final String FOOD_INTENT_KEY = "food"; // Maybe should tie these keys to same source instead of relying on source and dest agreeing


    private FoodItem mFoodItem;
    private ArrayList<NutrientVal> mNutrientVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Nutrition Label Recognizer");
        getSupportActionBar().setShowHideAnimationEnabled(true);

        if(getIntent().hasExtra(FOOD_INTENT_KEY)){
            mFoodItem = getIntent().getParcelableExtra(FOOD_INTENT_KEY);
        }

        initializeDrawer();

        //displayTestChart();
        loadUserNutrients();
        drawChart();
    }

    /**
     * Initializes the action drawer
     *
     * @author Adam Sturge
     * @since 2016-09-30
     */
    private void initializeDrawer() {

        mDrawerLabels = getResources().getStringArray(R.array.drawer_labels);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerLabels));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        //mDrawerToggle.setDrawerArrowDrawable(new DrawerArrowDrawable(this.getApplicationContext()));
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Controls flow of application after a user selects an item from the drawer
     * @param position index of the selected item in the list of drawer items
     */
    private void selectItem(int position){
        switch(position){
            case 0 :
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(this,CameraActivity.class);
                startActivity(intent);
                break;
            default:break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        mDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
        //selectItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void displayTestChart(){
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
        FrameLayout rl = (FrameLayout) findViewById(R.id.content_frame);

        rl.addView(mChart); // add the programmatically created chart


        mChart.invalidate(); // refresh*/
    }

    private void loadUserNutrients(){
        mNutrientVals = new ArrayList<NutrientVal>();

        SharedPreferences preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        int userProtein = preferences.getInt(Nutrient.NType.PROTEIN.name(),10);
        int userCarbs = preferences.getInt(Nutrient.NType.CARBOHYDRATE.name(),30);

        mNutrientVals.add(new NutrientVal(Nutrient.NType.PROTEIN,userProtein, NutrientVal.unit.GRAM));
        mNutrientVals.add(new NutrientVal(Nutrient.NType.CARBOHYDRATE,userCarbs, NutrientVal.unit.GRAM));

    }

    private void drawChart(){
        NutrientChartBuilder chartBuilder = new NutrientChartBuilder(getApplicationContext());
        for(NutrientVal nutrientVal : mNutrientVals){
            chartBuilder.addNutrient(nutrientVal,Color.RED);
        }
        CombinedChart chart = chartBuilder.build();
        // get a layout defined in xml
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        frameLayout.addView(chart); // add the programmatically created chart

        chart.invalidate();
    }

}
