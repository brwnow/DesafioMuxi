package com.bruno.desafiomuxi.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bruno.desafiomuxi.R;
import com.bruno.desafiomuxi.webrequest.Fruit;
import com.bruno.desafiomuxi.webrequest.WebRequestListener;
import com.bruno.desafiomuxi.webrequest.WebRequester;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements WebRequestListener, AdapterView.OnItemClickListener {
    private ListView fruitsListView;

    // Stores the information of the fruit list got
    // from the json
    Fruit[] fruitsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the web requester with the context of the application
        // and the JSON url
        WebRequester.init(this.getApplicationContext());

        fruitsListView = (ListView)findViewById(R.id.fruitsListView);

        fruitsListView.setOnItemClickListener(this);

        // Create a WebRequester and request the list of fruits
        // Pass this activity as the listener of the request
        // and the ID is always zero because there is no other
        // requests simultaneously
        (new WebRequester()).fruitsGetRequest(this, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent fruitDetailsIntent = new Intent(this, FruitDetailsActivity.class);

        // Let's pass extra information for the fruit name, image url and price
        // Since we have the position of the clicked item it must match the
        // position of this item in the fruits array
        fruitDetailsIntent.putExtra("FRUIT_NAME", fruitsArray[position].getName());
        fruitDetailsIntent.putExtra("IMAGE_URL", fruitsArray[position].getImageUrl());
        fruitDetailsIntent.putExtra("FRUIT_PRICE", fruitsArray[position].getPrice());

        startActivity(fruitDetailsIntent);
    }

    @Override
    public void fruitsReceived(Fruit[] fruits, int requestId) {
        // The data to insert in the ListView of fruits
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        // Store the fruits data in the array of fruits for further access
        fruitsArray = fruits;

        // Builds the data to be shown in the list of fruits
        // out of the result of the get request
        for (Fruit fruit : fruits) {
            Map<String, String> datum = new HashMap<String, String>(3);

            String formattedPrice = NumberFormat.getCurrencyInstance(Locale.US).format(fruit.getPrice());

            datum.put("name", fruit.getName());
            datum.put("price", formattedPrice);

            data.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "price"},
                new int[]{android.R.id.text1, android.R.id.text2});

        fruitsListView.setAdapter(adapter);
    }

    @Override
    public void imageReceived(Bitmap bitmap, int requestId) {
        // Not used
    }

    @Override
    public void requestError(String errorMessage, int requestId) {
        Log.e("REQUEST", errorMessage);

        Toast errorToast = Toast.makeText(this, "Fruits couldn't be loaded", Toast.LENGTH_LONG);
        errorToast.setGravity(Gravity.CENTER, 0, 0);

        errorToast.show();
    }
}
