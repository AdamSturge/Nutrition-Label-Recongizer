package com.example.adam.nutrition_label_recongizer.food;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Adam on 10/20/2016.
 */
public class ServingFactoryTest {
    @Test
    public void buildFromTextEmpty() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("");
        assertNull(serving);
    }

    @Test
    public void buildFromText1() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("per 1");
        assertNull(serving);
    }

    @Test
    public void buildFromText2() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("per 1 bottle");
        Serving bottle = new Serving(1.0f,"bottle");
        assertEquals(serving,bottle);
    }

    @Test
    public void buildFromText3() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("per 10 bottle");
        Serving bottle = new Serving(10.0f,"bottle");
        assertEquals(serving,bottle);
    }

    @Test
    public void buildFromText4() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("aaaaaaaaaaaaa");
        assertNull(serving);
    }

    @Test
    public void buildFromText5() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("per bottle");
        assertNull(serving);
    }

    @Test
    public void buildFromText6() throws Exception {
        ServingFactory servingFactory = new ServingFactory();
        Serving serving = servingFactory.buildFromText("per     10    bottle");
        Serving bottle = new Serving(10.0f,"bottle");
        assertEquals(serving,bottle);
    }



}