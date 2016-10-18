package com.example.adam.nutrition_label_recongizer;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.adam.nutrition_label_recongizer.charting.NutrientChartBuilder;
import com.example.adam.nutrition_label_recongizer.food.FoodItem;
import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
import com.example.adam.nutrition_label_recongizer.food.Serving;
import com.example.adam.nutrition_label_recongizer.food.comparator.NutrientValComparatorIsGood;
import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;
import com.example.adam.nutrition_label_recongizer.nutrient.NutrientFactory;
import com.example.adam.nutrition_label_recongizer.ocr.CameraActivity;
import com.example.adam.nutrition_label_recongizer.user.UserManager;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ServingSizeDialog.ServingSizeDialogListener {

    private Button mConsumeButton;
    private CombinedChart mChart;
    private Menu mMenu;

    private UserManager mUserManager;

    private FoodItem mFoodItem;
    private ArrayList<NutrientVal> mUserNutrientVals;

    private boolean mPercentView;

    private static final String FOOD_INTENT_KEY = "food"; // Maybe should tie these keys to same source instead of relying on source and dest agreeing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Nutrition Label Recognizer");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeDrawer(toolbar);

        mConsumeButton = (Button)findViewById(R.id.consume_food_button);
        mConsumeButton.setOnClickListener(new OnConsumeClickListener());

        mUserManager = new UserManager(getPreferences(getApplicationContext().MODE_PRIVATE));
        //mUserManager.resetUserNutrients();
        mUserNutrientVals = mUserManager.loadUserNutrients();
        mPercentView = mUserManager.loadPercentViewPreference();

        if(getIntent().hasExtra(FOOD_INTENT_KEY)){
            Intent intent = getIntent();
            mFoodItem = intent.getBundleExtra(FOOD_INTENT_KEY).getParcelable(FOOD_INTENT_KEY);
            mConsumeButton.setVisibility(View.VISIBLE);
            drawChart(mFoodItem);

            // TO DO: SOME KIND OF ALERT IF SERVING SIZE OR CALORIE INFO WAS NOT EXTRACTED

        }else{
            drawChart();
        }

        /*
        ArrayList<NutrientVal> nv = new ArrayList<NutrientVal>();
        nv.add(new NutrientVal(Nutrient.NType.SODIUM,125, NutrientVal.unit.MILLIGRAM));
        nv.add(new NutrientVal(Nutrient.NType.FAT,6, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.SAT_FAT,1, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.TRANS_FAT,0, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.CHOLESTEROL,15, NutrientVal.unit.MILLIGRAM));
        nv.add(new NutrientVal(Nutrient.NType.CARBOHYDRATE,23, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.SUGAR,8, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.PROTEIN,2, NutrientVal.unit.GRAM));
        nv.add(new NutrientVal(Nutrient.NType.FIBRE,3, NutrientVal.unit.GRAM));
        mFoodItem = new FoodItem(nv,new Serving(1,"pouch"),150);
        */

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
    private void initializeDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, mMenu);
        if(mFoodItem != null){
            menu.findItem(R.id.action_serving_size).setEnabled(true);
        }
        if(mPercentView){
            // hide percent view button
            mMenu.findItem(R.id.action_percent_view).setVisible(false);
            mMenu.findItem(R.id.action_absolute_view).setVisible(true);
        }else{
            // hide absolute view button
            mMenu.findItem(R.id.action_percent_view).setVisible(true);
            mMenu.findItem(R.id.action_absolute_view).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_serving_size) {
            showServingSizeDialog();
            return true;
        }else if(id == R.id.action_percent_view){
            percentView();
            return true;
        } else if(id == R.id.action_absolute_view){
            absoluteView();
            return true;
        }

        return super.onOptionsItemSelected(item); //handles drawer actions
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_capture) {
            Intent intent = new Intent(this,CameraActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Builds and draws a nutrient chart based on current nutrient values from persistent storage
     */
    private void drawChart(){
        NutrientChartBuilder chartBuilder = new NutrientChartBuilder(getApplicationContext());
        NutrientFactory nutrientFactory = new NutrientFactory();

        Collections.sort(mUserNutrientVals,new NutrientValComparatorIsGood());

        for(NutrientVal nutrientVal : mUserNutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            chartBuilder.addNutrient(nutrientVal,nutrient.isGood(),true);

        }


        CombinedChart chart = mPercentView ? chartBuilder.buildPercentChart() : chartBuilder.build();

        mChart = chart;

        // get a layout defined in xml
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        frameLayout.removeAllViews(); //ensure area is empty

        frameLayout.addView(chart); // add the programmatically created chart

        chart.invalidate();
    }

    /**
     * Builds and draws a nutrient chart based on current nutrient values from persistent storage and
     * the nutrient values from the provided food item
     * @param foodItem food item to be added to user nutrient values
     */
    private void drawChart(FoodItem foodItem){
        NutrientChartBuilder chartBuilder = new NutrientChartBuilder(getApplicationContext());
        NutrientFactory nutrientFactory = new NutrientFactory();

        Collections.sort(mUserNutrientVals,new NutrientValComparatorIsGood());

        for(NutrientVal nutrientVal : mUserNutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            NutrientVal foodVal = matchFoodNutrientToUserNutrient(foodItem,nutrientVal);
            if(foodVal != null){
                chartBuilder.addNutrient(new Pair<NutrientVal, NutrientVal>(nutrientVal,foodVal),nutrient.isGood(),true);
            }else{
                chartBuilder.addNutrient(nutrientVal,nutrient.isGood(),false);
            }
        }
        CombinedChart chart = mPercentView ? chartBuilder.buildPercentChart() : chartBuilder.build();

        mChart = chart;

        // get a layout defined in xml
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        frameLayout.removeAllViews(); //ensure area is empty

        frameLayout.addView(chart); // add the programmatically created chart

        chart.invalidate();
    }

    /**
     * Matches user nutrient values to food item nutrient values based on nutrient type
     * TO DO : Maybe move this method somewhere else
     * @param userNutrient
     * @return
     */
    private NutrientVal matchFoodNutrientToUserNutrient(FoodItem foodItem, NutrientVal userNutrient){
        if(mFoodItem == null){
            return null;
        }
        ArrayList<NutrientVal> foodVals = foodItem.getNutrients();
        for(NutrientVal nv : foodVals){
            if(nv.getType() == userNutrient.getType()){
                return nv;
            }
        }
        return null;
    }

    /**
     * Called when the user selects a new serving size from the serving size dialog
     * @param dialog
     */
    @Override
    public void onServingSizeSet(ServingSizeDialog dialog) {
        float servingSize = dialog.getServingSize();
        adjustFoodItemServingSize(servingSize);
        drawChart(mFoodItem);
    }

    /**
     * Adjusts nutritional values of the provided food item to reflect a serving size
     * specified by the provided serving size
     * TO DO: MOVE THIS SOMEWHERE ELSE
     * @param newServingSize new serving size to adjust nutrients to
     * @return a new food item where all values have been scaled by the ratio of the old serving size
     * to the new serving size
     */
    private void adjustFoodItemServingSize(float newServingSize){
        ArrayList<NutrientVal> foodVals = mFoodItem.getNutrients();
        float oldServingSize = mFoodItem.getServing().getAmount();

        float ratio = newServingSize/oldServingSize;

        ArrayList<NutrientVal> adjustedFoodVals = new ArrayList<NutrientVal>();
        for(NutrientVal val : foodVals){
            adjustedFoodVals.add(new NutrientVal(val.getType(),ratio*val.getVal()));
        }

        Serving adjustedServing = new Serving(newServingSize,mFoodItem.getServing().getUnit());
        int adjustedCalories = (int)ratio*mFoodItem.getCalories();

        mFoodItem = new FoodItem(adjustedFoodVals,adjustedServing,adjustedCalories);

    }

    /**
     * Creates and shows a serving size dialog
     */
    private void showServingSizeDialog(){
        ServingSizeDialog dialog = new ServingSizeDialog();
        Bundle args = new Bundle();
        args.putFloat("serving size",mFoodItem.getServing().getAmount());
        args.putString("unit",mFoodItem.getServing().getUnit());
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(),"ServingSizeDialog");
    }

    /**
     * Toggles percent view on chart data
     */
    private void percentView(){
        mPercentView = true;
        mMenu.findItem(R.id.action_percent_view).setVisible(false);
        mMenu.findItem(R.id.action_absolute_view).setVisible(true);
        mUserManager.savePercentViewPreference(mPercentView); // TO DO: Can probably put this off until OnPause
        if(mFoodItem != null){
            drawChart(mFoodItem);
        }else{
            drawChart();
        }
    }

    /**
     * Toggles abosulte view on chart data
     */
    private void absoluteView(){
        mPercentView = false;
        mMenu.findItem(R.id.action_percent_view).setVisible(true);
        mMenu.findItem(R.id.action_absolute_view).setVisible(false);
        mUserManager.savePercentViewPreference(mPercentView); // TO DO: Can probably put this off until OnPause
        if(mFoodItem != null){
            drawChart(mFoodItem);
        }else{
            drawChart();
        }
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
            mConsumeButton.setVisibility(View.GONE);
            mMenu.findItem(R.id.action_serving_size).setEnabled(false);

            for(NutrientVal x : mUserNutrientVals){
                Log.e("BEFORE UPDATE",x.toString());
            }
            updateUserNutrientVals();
            for(NutrientVal x : mUserNutrientVals){
                Log.e("AFTER UPDATE",x.toString());
            }
            updateBarColors();

            mFoodItem = null;

            mUserManager.saveUserNutrients(mUserNutrientVals);

        }

        /**
         * updates bar colors reflected newly consumed food item
         */
        private void updateBarColors(){
            BarData barData = mChart.getBarData();
            for(int i = 0; i < barData.getDataSetCount(); ++i){
                BarDataSet set = (BarDataSet)barData.getDataSetByIndex(i);

                List<Integer> oldColors = set.getColors();
                set.setColor(oldColors.get(0)); // updates reference data in chart, no need to reassign to chart
            }

            mChart.invalidate();
        }

        /**
         * Updates member variable of user nutrients to add consumed food values
         */
        private void updateUserNutrientVals(){
            ArrayList<NutrientVal> updatedNutrients = new ArrayList<NutrientVal>();
            for(NutrientVal nutrientVal : mUserNutrientVals){
                NutrientVal foodVal = matchFoodNutrientToUserNutrient(mFoodItem,nutrientVal);
                if(foodVal != null){
                    updatedNutrients.add(new NutrientVal(nutrientVal.getType(),nutrientVal.getVal() + foodVal.getVal()));
                }else{
                    updatedNutrients.add(nutrientVal);
                }
            }

            mUserNutrientVals = updatedNutrients;
        }
    }



}
