package com.example.adam.nutrition_label_recongizer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adam.nutrition_label_recongizer.charting.NutrientChartBuilder;
import com.example.adam.nutrition_label_recongizer.food.FoodItem;
import com.example.adam.nutrition_label_recongizer.food.NutrientVal;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button mConsumeButton;
    private CombinedChart mChart;
    private UserManager mUserManager;

    private FoodItem mFoodItem;
    private ArrayList<NutrientVal> mUserNutrientVals;

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

        if(getIntent().hasExtra(FOOD_INTENT_KEY)){
            mFoodItem = getIntent().getParcelableExtra(FOOD_INTENT_KEY);
            mConsumeButton.setVisibility(View.VISIBLE);
            //findViewById(R.id.action_serving_size).setEnabled(true);
        }

        mUserManager = new UserManager(getPreferences(getApplicationContext().MODE_PRIVATE));

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

        mUserNutrientVals = mUserManager.loadUserNutrients();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            ServingSizeDialog dialog = new ServingSizeDialog();
            Bundle args = new Bundle();
            args.putString("unit",mFoodItem.getServing().getUnit());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(),"ServingSizeDialog");
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
     * Builds and draws a nutrient chart based on current nutrient values
     * in persistent storage and any food items under consideration
     */
    private void drawChart(){
        NutrientChartBuilder chartBuilder = new NutrientChartBuilder(getApplicationContext());
        NutrientFactory nutrientFactory = new NutrientFactory();

        Collections.sort(mUserNutrientVals,new NutrientValComparatorIsGood());

        for(NutrientVal nutrientVal : mUserNutrientVals){
            Nutrient nutrient = nutrientFactory.buildNutrient(nutrientVal.getType());
            if(mFoodItem != null){
                NutrientVal foodVal = matchFoodNutrientToUserNutrient(nutrientVal);
                if(foodVal != null){
                    chartBuilder.addNutrient(new Pair<NutrientVal, NutrientVal>(nutrientVal,foodVal),nutrient.isGood(),true);
                }else{
                    chartBuilder.addNutrient(nutrientVal,nutrient.isGood(),false);
                    // TO DO : ADD CODE TO GREY OUT MISSING DATA
                }
            }else{
                chartBuilder.addNutrient(nutrientVal,nutrient.isGood(),true);
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
     * Matches user nutrient values to food item nutrient values based on nutrient type
     * TO DO : Maybe move this method somewhere else
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
            updateUserNutrientVals();

            updateBarColors();
            mConsumeButton.setVisibility(View.GONE);
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
            BarData barData = mChart.getBarData();
            ArrayList<NutrientVal> updatedNutrients = new ArrayList<NutrientVal>();
            for(NutrientVal nutrientVal : mUserNutrientVals){
                NutrientVal foodVal = matchFoodNutrientToUserNutrient(nutrientVal);
                if(foodVal != null){
                    updatedNutrients.add(new NutrientVal(nutrientVal.getType(),nutrientVal.getVal() + foodVal.getVal(),nutrientVal.getUnit()));
                }else{
                    updatedNutrients.add(nutrientVal);
                }
            }

            mUserNutrientVals = updatedNutrients;
        }
    }



}
