package com.bruno.desafiomuxi.core;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.bruno.desafiomuxi.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Bruno on 20/08/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FruitDetailsActivityTest {
    @Rule
    public ActivityTestRule<FruitDetailsActivity> fruitDetailsTestRule = new ActivityTestRule(FruitDetailsActivity.class);

    @Test
    public void brlFieldUpdating() throws Throwable {
        fruitDetailsTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FruitDetailsActivity fruitDetailsActivity = (FruitDetailsActivity)fruitDetailsTestRule.getActivity();
                TextView brlTextView = (TextView)fruitDetailsActivity.findViewById(R.id.brlTextView);

                fruitDetailsActivity.conversionCallback(10);
                assertEquals("Wrong UI output", "R$ 10,00", brlTextView.getText().toString());

                fruitDetailsActivity.conversionCallback(0.01);
                assertEquals("Wrong UI output", "R$ 0,01", brlTextView.getText().toString());

                fruitDetailsActivity.conversionCallback(9.999);
                assertEquals("Wrong UI output", "R$ 10,00", brlTextView.getText().toString());

                fruitDetailsActivity.conversionCallback(0.001);
                assertEquals("Wrong UI output", "R$ 0,00", brlTextView.getText().toString());
            }
        });
    }
}