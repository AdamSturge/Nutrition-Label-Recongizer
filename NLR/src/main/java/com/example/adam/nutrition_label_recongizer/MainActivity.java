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
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.adam.nutrition_label_recongizer.charting.NutrientChartBuilder;
import com.example.adam.nutrition_label_recongizer.food.FoodItem;
import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.food.Serving;
import com.example.adam.nutrition_label_recongizer.nutrients.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrients.NutrientFactory;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] mDrawerLabels;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button mConsumeButton;
    private CombinedChart mChart;

    private static final String FOOD_INTENT_KEY = "food"; // Maybe should tie these keys to same source instead of relying on source and dest agreeing
    private static final String USER_NUTRIENT_STORAGE_DAY_KEY = "NUTRIENTS LAST UPDATED ON DAY";


    private FoodItem mFoodItem;
    private ArrayList<NutrientVal> mNutrientVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Nutrition Label Recognizer");
        getSupportActionBar().setShowHideAnimationEnabled(true);

        mConsumeButton = (Button)findViewById(R.id.consume_food_button);
        mConsumeButton.setOnClickListener(new OnConsumeClickListener());

        if(getIntent().hasExtra(FOOD_INTENT_KEY)){
            mFoodItem = getIntent().getParcelableExtra(FOOD_INTENT_KEY);
            mConsumeButton.setVisibility(View.VISIBLE);
        }

/*        ArrayList<NutrientVal> nv = new ArrayList<NutrientVal>();
        nv.add(new NutrientVal(Nutrient.NType.SODIUM,125, NutrientVal.unit.MILLIGRAM));
        nv.add(new NutrientVal(Nutrient.NType.FAT,6, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.SAT_FAT,1, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.TRANS_FAT,0, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.CHOLESTEROL,15, NutrientVal.unit.MILLIGRAM));
        nv.add(new NutrientVal(Nutrient.NType.CARBOHYDRATE,23, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.SUGAR,8, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.PROTEIN,2, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.FIBRE,3, NutrientVal.unit.GRAM));
        mFoodItem = new FoodItem(nv,new Serving(1,"pouch"),150);*/

        initializeDrawer();

        //displayTestChart();
        loadUserNutrients();
        drawChart();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //loadUserNutrients(); NEED TO DO SOME TESTING TO SEE IF THIS IS REQUIRED

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

    /**
     * Loads consumed nutrients out of persistent storage
     */
    private void loadUserNutrients(){
        mNutrientVals = new ArrayList<NutrientVal>();
        SharedPreferences preferences = getPreferences(getApplicationContext().MODE_PRIVATE);

        int lastStoredDay = preferences.getInt(USER_NUTRIENT_STORAGE_DAY_KEY ,1);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(lastStoredDay != today){
            // Clear stored values
            resetUserNutrients();
        }

        for(Nutrient.NType nType : Nutrient.NType.values()){
            float val = preferences.getFloat(nType.name(),0.0f);
            mNutrientVals.add(new NutrientVal(nType,val, NutrientVal.unit.GRAM));
        }

    }

    /**
     * Builds and draws a nutrient chart based on current nutrient values
     * in persistent storage and any food items under consideration
     */
    private void drawChart(){
        NutrientChartBuilder chartBuilder = new NutrientChartBuilder(getApplicationContext());
        NutrientFactory nutrientFactory = new NutrientFactory();

        sortNutrientVals(mNutrientVals);

        for(NutrientVal nutrientVal : mNutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            if(mFoodItem != null){
                NutrientVal foodVal = matchFoodNutrientToUserNutrient(nutrientVal);
                if(foodVal != null){
                    chartBuilder.addNutrient(new Pair<NutrientVal, NutrientVal>(nutrientVal,foodVal),nutrient.isGood());
                }else{
                    chartBuilder.addNutrient(nutrientVal,nutrient.isGood());
                    // TO DO : ADD CODE TO GREY OUT MISSING DATA
                }
            }else{
                chartBuilder.addNutrient(nutrientVal,nutrient.isGood());
            }
        }
        CombinedChart chart = chartBuilder.build();

        mChart = chart;

        // get a layout defined in xml
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        frameLayout.addView(chart); // add the programmatically created chart

        chart.invalidate();
    }

    /**
     * Matches user nutrient values to food item nutrient values
     * based on nutrient type
     * @param userNutrient
     * @return
     */
    private NutrientVal matchFoodNutrientToUserNutrient(NutrientVal userNutrient){
        if(mFoodItem == null){
            return null;
        }

        ArrayList<NutrientVal> foodVals = mFoodItem.getNutrients();
        for(NutrientVal nv : foodVals){
            if(nv.getType() == userNutrient.getType()){
                return nv;
            }
        }

        return null;
    }

    /**
     * Updates nutrient values in persistent storage.
     * Removes food item under consideration
     * Updates barchart to reflect consumed nutrients
     * Hides consume button
     */
    private class OnConsumeClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            updateBarColors();
            saveUserNutrients();
            mConsumeButton.setVisibility(View.GONE);
            mFoodItem = null;
        }

        private void updateBarColors(){
            BarData barData = mChart.getBarData();
            for(int i = 0; i < barData.getDataSetCount(); ++i){
                BarDataSet set = (BarDataSet)barData.getDataSetByIndex(i);

                List<Integer> oldColors = set.getColors();
                set.setColor(oldColors.get(0)); // updates reference data in chart, no need to reassign to chart
            }

            mChart.invalidate();
        }
    }

    /**
     * Saves the current nutrient values to persistent storage
     */
    private void saveUserNutrients(){
        SharedPreferences preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        BarData barData = mChart.getBarData();
        for(int i = 0; i < barData.getDataSetCount(); ++i){
            BarDataSet set = (BarDataSet)barData.getDataSetByIndex(i);
            editor.putFloat(set.getLabel(),set.getEntryForIndex(0).getY());
        }
        editor.commit();
    }

    /**
     * Resets the nutrient values in persistent storage to zero. Updates last reset day
     */
    private void resetUserNutrients(){
        SharedPreferences preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for(Nutrient.NType nType : Nutrient.NType.values()){
            editor.putFloat(nType.name(),0.0f);
        }
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        editor.putInt(USER_NUTRIENT_STORAGE_DAY_KEY ,today);
        Log.e("ResetUserVals",String.valueOf(today));
        editor.commit();
    }

    /**
     * sorts a list of nutrient vals by nutrient type
     * @param vals
     */
    private void sortNutrientVals(ArrayList<NutrientVal> vals){
        Collections.sort(vals,new Comparator<NutrientVal>(){

            private NutrientFactory nutrientFactory = new NutrientFactory();
            @Override
            public int compare(NutrientVal lhs, NutrientVal rhs) {
                Nutrient nlhs = nutrientFactory.buildNutrient(lhs.getType());
                Nutrient nrhs = nutrientFactory.buildNutrient(rhs.getType());
                return nrhs.compareTo(nlhs);
            }
        });
    }

}
