package com.example.adam.nutrition_label_recongizer.food;

import com.example.adam.nutrition_label_recongizer.nutrient.Nutrient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Adam on 10/20/2016.
 */
public class NutrientValFactoryTest {
    @Test
    public void buildFromTextEmpty() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("");
        assertNull(nutrientVal);
    }

    @Test
    public void buildFromTextProtein1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("protein 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.PROTEIN,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextProtein2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("protein 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.PROTEIN,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextProtein3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("protein 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.PROTEIN,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextCarb1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("carbohydrate 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.CARBOHYDRATE,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextCarb2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("carbohydrate 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.CARBOHYDRATE,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextCarb3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("carbohydrate 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.CARBOHYDRATE,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextCholesterol1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("cholesterol 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.CHOLESTEROL,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextCholesterol2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("cholesterol 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.CHOLESTEROL,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextCholesterol3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("cholesterol 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.CHOLESTEROL,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextFat1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("fat 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.FAT,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextFat2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("fat 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.FAT,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextFat3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("fat 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.FAT,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSatFat1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("saturated 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SATURATED,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSatFat2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("saturated 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SATURATED,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSatFat3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("saturated 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SATURATED,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSatFat4() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("saturated 1.5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SATURATED,1.5f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextTransFat1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("trans 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.TRANS,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextTransFat2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("trans 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.TRANS,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextTransFat3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("trans 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.TRANS,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextFibre1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("fibre 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.FIBRE,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextFibre2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("fibre 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.FIBRE,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextFibre3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("fibre 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.FIBRE,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSugar1() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("sugar 5g");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SUGAR,5);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSugar2() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("sugar 5mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SUGAR,5.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }

    @Test
    public void buildFromTextSugar3() throws Exception {
        NutrientValFactory factory = new NutrientValFactory();
        NutrientVal nutrientVal = factory.buildFromText("sugar 500mg");
        NutrientVal protein = new NutrientVal(Nutrient.NType.SUGAR,500.0f/1000.0f);
        assertEquals(nutrientVal,protein);
    }


}