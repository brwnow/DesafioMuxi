package com.bruno.desafiomuxi.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruno.desafiomuxi.R;
import com.bruno.desafiomuxi.currency.Currency;
import com.bruno.desafiomuxi.webrequest.Fruit;
import com.bruno.desafiomuxi.webrequest.WebRequestListener;
import com.bruno.desafiomuxi.webrequest.WebRequester;

import java.text.NumberFormat;
import java.util.Locale;

public class FruitDetailsActivity extends AppCompatActivity implements WebRequestListener {
    // Loading the native lib for currency conversion
    static {
        System.loadLibrary("currency-converter");
    }

    private ImageView fruitImageView;
    private TextView fruitNameTextView, usdPriceTextView, brlPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_details);

        // Initialize the web requester with the context of the application
        // and the JSON url
        WebRequester.init(this.getApplicationContext());

        fruitImageView = (ImageView)findViewById(R.id.fruitImageView);

        fruitNameTextView = (TextView)findViewById(R.id.fruitNameTextView);
        usdPriceTextView = (TextView)findViewById(R.id.usdTextView);
        brlPriceTextView = (TextView)findViewById(R.id.brlTextView);

        // Filling up view components with the extra information
        // passed from the main activity
        fruitNameTextView.setText(getIntent().getStringExtra("FRUIT_NAME"));

        // Currency util class. Will be used to get the conversion ratio and formating currency
        Currency currency = new Currency();

        // Fill up dollar price text view with the formated price to the US Locale
        usdPriceTextView.setText(currency.format(getIntent().getDoubleExtra("FRUIT_PRICE", 0.0), Currency.CurrencyTag.USD));

        // Get the conversion ratio from USD to BRL
        double usdToBrlConversionRatio = currency.getConversionRatio(Currency.CurrencyTag.USD, Currency.CurrencyTag.BRL);

        // Calls the asyncronous convertion from USD to BRL
        asyncConvertCurrency(getIntent().getDoubleExtra("FRUIT_PRICE", 0.0), usdToBrlConversionRatio);

        // Fill up image view
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        (new WebRequester()).imageGetRequest(imageUrl, this, 0, fruitImageView);
    }

    public native void asyncConvertCurrency(double baseValue, double conversionRatio);

    public void conversionCallback(double valueConvertedToBrl) {
        Currency currency = new Currency();

        brlPriceTextView.setText(currency.format(valueConvertedToBrl, Currency.CurrencyTag.BRL));
    }

    @Override
    public void fruitsReceived(Fruit[] fruits, int requestId) {
        // Not used
    }

    @Override
    public void imageReceived(int requestId) {
        Log.v("REQUEST", "Image " + getIntent().getStringExtra("IMAGE_URL") + " loaded");
    }

    @Override
    public void requestError(String errorMessage, int requestId) {
        Log.e("REQUEST", errorMessage);
    }
}
