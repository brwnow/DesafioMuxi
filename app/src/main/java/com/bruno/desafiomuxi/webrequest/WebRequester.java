package com.bruno.desafiomuxi.webrequest;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bruno.desafiomuxi.R;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * Created by Bruno on 16/08/2017.
 */

public class WebRequester {
    // Once initialized, the request queue will be used for all
    // instances of the WebRequester class
    private static RequestQueue requestQueue = null;

    // Default max downloaded image resolution
    private static final int MAX_REQUESTED_IMG_WIDTH = 500, MAX_REQUESTED_IMG_HEIGHT = 500;

    // Application context for further use by Picasso lib
    private static Context appContext;

    private static Picasso picasso;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static String fruitsJsonUrl = "";

    // This method initializes the request queue, given a context
    // and sets the URL of the fruits json
    public static void init(Context context) {
        // Only initialize the request queue if no request queue were initialized before
        if(requestQueue == null || appContext == null) {
            requestQueue = Volley.newRequestQueue(context);

            appContext = context;

            picasso = new Picasso.Builder(appContext).memoryCache(new LruCache(appContext)).build();

            Log.v("WebRequest", "Request queue initialized");
        }

        // The fixed URI of the json file which contains information about
        // the list of fruits
        WebRequester.fruitsJsonUrl = appContext.getResources().getString(R.string.fruit_json_uri);
    }

    public WebRequester() {
    }

    private boolean canStartRequests() {
        return requestQueue != null && !fruitsJsonUrl.isEmpty();
    }

    // This method perform a get request for the json that contains the fruits informations
    public boolean fruitsGetRequest(WebRequestListener webRequestListener, int requestId) {
        if(canStartRequests()) {
            // The request callback handler, set up with the request listener and the request ID
            FruitsRequestHandler fruitsRequestHandler = new FruitsRequestHandler(webRequestListener, requestId);

            // Push the request into the request queue
            requestQueue.add(new StringRequest(Request.Method.GET, fruitsJsonUrl, fruitsRequestHandler, fruitsRequestHandler));

            return true; // Request performed succesfully
        } else {
            return false; // A get request can't be started
        }
    }

    // This method perform a get request for a image, given the image URI
    public boolean imageGetRequest(final String imageUrl,
                                   WebRequestListener webRequestListener,
                                   int requestId,
                                   ImageView intoView)
    {
        if(canStartRequests()) {
            // Calls picasso with the application context to start the image request with
            // ImageRequestListener as the target, request the image, resize it and centeralize it
            Picasso.with(appContext).load(imageUrl).resize(
                    MAX_REQUESTED_IMG_WIDTH, MAX_REQUESTED_IMG_HEIGHT).centerInside().into(
                    intoView, new ImageRequestHandler(webRequestListener, requestId));

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

    private class FruitsRequestHandler extends BaseRequestHandler implements Response.Listener<String>, Response.ErrorListener {
        public FruitsRequestHandler(final WebRequestListener webRequestListener, int requestId) {
            super(webRequestListener, requestId);
        }

        @Override
        public void onResponse(String response) {
            Log.v("JSON_REQUEST", "JSON succesfully loaded (id: " + requestId + ")");

            Gson gson = new Gson();
            FruitsDeserialized fruitsDeserialized = gson.fromJson(response, FruitsDeserialized.class);

            // Notify that fruits were received as json
            webRequestListener.fruitsReceived(fruitsDeserialized.getFruitsArray(), requestId);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if(error.getMessage() != null)
                // Notify a request error has ocurred and send the error message
                webRequestListener.requestError(error.getMessage(), requestId);
            else
                Log.e("JSON_REQUEST", error.getMessage() + " (id: " + requestId + ")");

                // // Notify a request error has ocurred, but there is no error message
                webRequestListener.requestError("", requestId);
        }
    }

    private class ImageRequestHandler extends BaseRequestHandler implements Callback {
        public ImageRequestHandler(final WebRequestListener webRequesteListener, int requestId) {
            super(webRequesteListener, requestId);
        }

        @Override
        public void onSuccess() {
            Log.v("IMAGE_REQUEST", "Image request succeed (id: " + requestId + ")");

            // Notify that image was received
            webRequestListener.imageReceived(requestId);
        }

        @Override
        public void onError() {
            Log.e("IMAGE_REQUEST", "Image request failed (id: " + requestId + ")");

            // Notify that the image request has failed
            webRequestListener.requestError("Couldn't load the image", requestId);
        }
    }
}
