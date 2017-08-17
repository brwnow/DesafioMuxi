package com.bruno.desafiomuxi.webrequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Bruno on 16/08/2017.
 */

public class WebRequester {
    // Once initialized, the request queue will be used for all
    // instances of the WebRequester class
    private static RequestQueue requestQueue = null;

    private static String fruitsJsonUrl = "";

    // This method initializes the request queue, given a context
    // and sets the URL of the fruits json
    public static void init(Context context, String fruitsJsonUrl) {
        // Only initialize the request queue if no request queue were initialized before
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);

        WebRequester.fruitsJsonUrl = fruitsJsonUrl;
    }

    public WebRequester() {
    }

    private boolean canStartRequests() {
        return requestQueue != null && !fruitsJsonUrl.isEmpty();
    }

    // This method perform a get request for the json that contains the fruits informations
    public boolean fruitsGetRequest(WebRequestListener webRequestListener, int requestId) {
        if(canStartRequests()) {
            requestQueue.add(new StringRequest( Request.Method.GET,
                                                fruitsJsonUrl,
                                                new FruitsRequestHandler(webRequestListener, requestId),
                                                new ErrorResponseHandler(webRequestListener, requestId) ) );

            return true; // Request performed succesfully
        } else {
            return false; // A get request can't be started
        }
    }

    // This method perform a get request for the image of the given fruit
    public boolean fruitImageGetRequest(final Fruit fruit, WebRequestListener webRequestListener, int requestId) {
        if(canStartRequests()) {
            requestQueue.add(new ImageRequest(  fruit.getImageUrl(),
                                                new FruitImageRequestHandler(webRequestListener, requestId),
                                                0, 0, null,
                                                new ErrorResponseHandler(webRequestListener, requestId) ) );

            return true; // Request performed succesfully
        } else {
            return false; // A get request can't be started
        }
    }

    // This method perform a get request for the image, given the image URL
    public boolean imageGetRequest(final String imageUrl, WebRequestListener webRequestListener, int requestId) {
        if(canStartRequests()) {
            requestQueue.add(new ImageRequest(  imageUrl,
                                                new ImageRequestHandler(webRequestListener, requestId),
                                                0, 0, null,
                                                new ErrorResponseHandler(webRequestListener, requestId) ) );

            return true; // Request performed succesfully
        } else {
            return false; // A get request can't be started
        }
    }

    private abstract class BaseRequestHandler {
        protected final WebRequestListener webRequestListener;
        protected final int requestId;

        public BaseRequestHandler(final WebRequestListener webRequestListener, int requestId) {
            this.webRequestListener = webRequestListener;
            this.requestId = requestId;
        }
    }

    private class FruitsRequestHandler extends BaseRequestHandler implements Response.Listener<String> {
        public FruitsRequestHandler(final WebRequestListener webRequestListener, int requestId) {
            super(webRequestListener, requestId);
        }

        @Override
        public void onResponse(String response) {
            Gson gson = new Gson();

            FruitsDeserialized fruitsDeserialized = gson.fromJson(response, FruitsDeserialized.class);

            webRequestListener.fruitsReceived(fruitsDeserialized.getFruitsArray(), requestId);
        }
    }

    private class FruitImageRequestHandler extends BaseRequestHandler implements Response.Listener<Bitmap> {
        public FruitImageRequestHandler(final WebRequestListener webRequestListener, int requestId) {
            super(webRequestListener, requestId);
        }

        @Override
        public void onResponse(Bitmap response) {
            webRequestListener.fruitImageReceived(response, requestId);
        }
    }

    private class ImageRequestHandler extends BaseRequestHandler implements Response.Listener<Bitmap> {
        public ImageRequestHandler(final WebRequestListener webRequestListener, int requestId) {
            super(webRequestListener, requestId);
        }

        @Override
        public void onResponse(Bitmap response) {
            webRequestListener.imageReceived(response, requestId);
        }
    }

    private class ErrorResponseHandler extends BaseRequestHandler implements Response.ErrorListener {
        public ErrorResponseHandler(final WebRequestListener webRequestListener, int requestId) {
            super(webRequestListener, requestId);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            webRequestListener.requestError(error.getMessage(), requestId);
        }
    }
}
