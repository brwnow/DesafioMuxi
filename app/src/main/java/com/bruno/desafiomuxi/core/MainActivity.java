package com.bruno.desafiomuxi.core;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bruno.desafiomuxi.R;
import com.bruno.desafiomuxi.webrequest.Fruit;
import com.bruno.desafiomuxi.webrequest.WebRequestListener;
import com.bruno.desafiomuxi.webrequest.WebRequester;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WebRequestListener {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the web requester
        WebRequester.init(this, "https://raw.githubusercontent.com/muxidev/desafio-android/master/fruits.json");

        WebRequester wr = new WebRequester();

        wr.fruitsGetRequest(this, 0);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void fruitsReceived(Fruit[] fruits, int requestId) {

    }

    @Override
    public void fruitImageReceived(Bitmap fruitImage, int requestId) {

    }

    @Override
    public void imageReceived(Bitmap image, int requestId) {

    }

    @Override
    public void requestError(String errorMessage, int requestId) {

    }
}
