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
import com.example.adam.nutrition_label_recongizer.food.FoodUtil;
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


        ArrayList<NutrientVal> nv = new ArrayList<NutrientVal>();
        nv.add(new NutrientVal(Nutrient.NType.SODIUM,0));
        nv.add(new NutrientVal(Nutrient.NType.FAT,0));
        nv.add(new NutrientVal(Nutrient.NType.SATURATED,0));
        nv.add(new NutrientVal(Nutrient.NType.TRANS,0));
        nv.add(new NutrientVal(Nutrient.NType.CHOLESTEROL,2.9f));
        nv.add(new NutrientVal(Nutrient.NType.CARBOHYDRATE,123));
        nv.add(new NutrientVal(Nutrient.NType.SUGAR,0));
        nv.add(new NutrientVal(Nutrient.NType.PROTEIN,2));
        nv.add(new NutrientVal(Nutrient.NType.FIBRE,3));
        mFoodItem = new FoodItem(nv,new Serving(1,"pouch"),150);

        mConsumeButton.setBackgroundColor(computeConsumeButtonColor(mUserNutrientVals));
        mConsumeButton.setVisibility(View.VISIBLE);
        drawChart(mFoodItem);

        /*
        if(getIntent().hasExtra(FOOD_INTENT_KEY)){
            Intent intent = getIntent();
            mFoodItem = intent.getBundleExtra(FOOD_INTENT_KEY).getParcelable(FOOD_INTENT_KEY);
            mConsumeButton.setBackgroundColor(computeConsumeButtonColor(mUserNutrientVals));
            mConsumeButton.setVisibility(View.VISIBLE);
            drawChart(mFoodItem);

            // TO DO: SOME KIND OF ALERT IF SERVING SIZE OR CALORIE INFO WAS NOT EXTRACTED

        }else{
            drawChart();
        }
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
        FoodUtil foodUtil = new FoodUtil(foodItem);

        Collections.sort(mUserNutrientVals,new NutrientValComparatorIsGood());

        for(NutrientVal nutrientVal : mUserNutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            NutrientVal foodVal = foodUtil.matchFoodNutrientToUserNutrient(nutrientVal);
            if(foodVal.getVal() != -1.0f){
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
     * Called when the user selects a new serving size from the serving size dialog
     * @param dialog
     */
    @Override
    public void onServingSizeSet(ServingSizeDialog dialog) {
        float servingSize = dialog.getServingSize();
        FoodUtil foodUtil = new FoodUtil(mFoodItem);
        mFoodItem = foodUtil.adjustFoodItemServingSize(servingSize);
        drawChart(mFoodItem);
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
     * Computes the color of the consume button so as to reflect how "healthy"
     * the food under consideration is at this moment
     * @param nutrientVals
     * @return color
     */
    private int computeConsumeButtonColor(ArrayList<NutrientVal> nutrientVals){
        if(mFoodItem == null){
            return 0;
        }

        NutrientFactory nutrientFactory = new NutrientFactory();
        FoodUtil foodUtil = new FoodUtil(mFoodItem);
        float healthiness = 0.0f;
        for(NutrientVal nutrientVal : nutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            NutrientVal foodVal = foodUtil.matchFoodNutrientToUserNutrient(nutrientVal);
            /*
             * If a food value is zero it should not contribute to the decision to eat the item.
             * If a food value is non-zero then the new nutritional value for the user if they chose
             * to eat it is used to calculate "healthiness".
             */
            float val = foodVal.getVal() != 0.0f ?  foodVal.getVal() + nutrientVal.getVal() : 0.0f;
            float threshold = nutrient.getThreshold();
            if(nutrient.isGood()){
                float diff = 0.0f;
                if(val < threshold){
                   diff = Math.abs(threshold - val)/threshold;
                }
                healthiness += diff;
            }else{
                float diff = (threshold - val)/threshold;
               healthiness += diff;
            }
        }

        // cap to avoid overflow
        if(healthiness > 600){
            healthiness = 600;
        }
        if(healthiness < -600){
            healthiness = -600;
        }

        // User a modified hyperbolic tangent to map the real line onto [badColor,goodColor]
        int goodColor = getResources().getColor(R.color.goodNutrient, getTheme());
        int badColor = getResources().getColor(R.color.badNutrient, getTheme());
        int grad = (int)((goodColor*Math.exp(healthiness) + badColor*Math.exp(-healthiness))/(Math.exp(healthiness) + Math.exp(-healthiness)));

        return grad;
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
            FoodUtil foodUtil = new FoodUtil(mFoodItem);
            ArrayList<NutrientVal> updatedNutrients = new ArrayList<NutrientVal>();
            for(NutrientVal nutrientVal : mUserNutrientVals){
                NutrientVal foodVal = foodUtil.matchFoodNutrientToUserNutrient(nutrientVal);
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
