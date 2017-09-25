package com.bruno.desafiomuxi.core;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
    // The default ID of requests for fruit image
    private static final int FRUIT_IMAGE_REQUEST = 1;

    // Loading the native lib for currency conversion
    static {
        System.loadLibrary("currency-converter");
    }

    private ImageView fruitImageView;
    private TextView fruitNameTextView, usdPriceTextView, brlPriceTextView;

    // The bitmap loaded from the fruit image URI is stored in order
    // to save in the instance state in the case this activity is destroy
    // so the bitmap can be restored without redownloading it from web
    private Bitmap loadedBitmapFromWeb;

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

        // Try to restore a saved state
        if(!restoreState(savedInstanceState)) {
            // If the state can't be restored, setup the activity
            setupActivity();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("FRUIT_NAME", fruitNameTextView.getText().toString());
        savedInstanceState.putString("USD_PRICE", usdPriceTextView.getText().toString());
        savedInstanceState.putString("BRL_PRICE", brlPriceTextView.getText().toString());
        savedInstanceState.putParcelable("FRUIT_BITMAP", loadedBitmapFromWeb);
    }

    private boolean restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            String  fruitName = savedInstanceState.getString("FRUIT_NAME"),
                    usdPrice = savedInstanceState.getString("USD_PRICE"),
                    brlPrice = savedInstanceState.getString("BRL_PRICE");
            Bitmap fruitBitmap = savedInstanceState.getParcelable("FRUIT_BITMAP");

            // If any of the key is not mapped, the state restoring failed
            if(fruitName == null || usdPrice == null || brlPrice == null || fruitBitmap == null) {
                return false; // Failed at restoring state
            } else {
                fruitNameTextView.setText(fruitName);
                usdPriceTextView.setText(usdPrice);
                brlPriceTextView.setText(brlPrice);

                loadedBitmapFromWeb = fruitBitmap;

                fruitImageView.setImageBitmap(fruitBitmap);

                return true; // State succesfully restored
            }
        } else {
            return false; // There is no instance state saved, nothing to be restored
        }
    }

    private void setupActivity() {
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

        // Fill up image view with image get through a web request
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        (new WebRequester()).imageGetRequest(imageUrl, this, FruitDetailsActivity.FRUIT_IMAGE_REQUEST, fruitImageView);
    }

    // Calls the native lib for currency conversion
    // baseValue: value to be converted
    // conversionRatio: the ratio of the conversion
    // Example: From U$1,00 to R$ with ratio 3,5 the result is R$ 3,50
    public native void asyncConvertCurrency(double baseValue, double conversionRatio);

    // Called by native currency conversion lib
    public void conversionCallback(final double valueConvertedToBrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Currency currency = new Currency();

                brlPriceTextView.setText(currency.format(valueConvertedToBrl, Currency.CurrencyTag.BRL));

            }
        });
    }

    @Override
    public void fruitsReceived(Fruit[] fruits, int requestId) {
        // Not used
    }

    @Override
    public void imageReceived(int requestId) {
        // Verify if this is the request of the fruit image
        if(requestId == FRUIT_IMAGE_REQUEST) {
            // Stores the bitmap of the fruit image to save as instance state
            // In case the activity is destroy and try to restore state
            loadedBitmapFromWeb = ((BitmapDrawable)fruitImageView.getDrawable()).getBitmap();
        }
    }

    @Override
    public void requestError(String errorMessage, int requestId) {
        // Do nothing. WebRequester already log error messages
    }
}
