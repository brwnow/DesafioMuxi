package com.bruno.desafiomuxi.webrequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by Bruno on 16/08/2017.
 */

public class WebRequester {
    // Once initialized, the request queue will be used for all
    // instances of the WebRequester class
    private static RequestQueue requestQueue = null;

    // Application context for further use by Picasso lib
    private static Context appContext;

    private static Picasso picasso;

    private static String fruitsJsonUrl = "";

    // This method initializes the request queue, given a context
    // and sets the URL of the fruits json
    public static void init(Context context, String fruitsJsonUrl) {
        // Only initialize the request queue if no request queue were initialized before
        if(requestQueue == null || appContext == null) {
            requestQueue = Volley.newRequestQueue(context);

            appContext = context;

            picasso = new Picasso.Builder(appContext).memoryCache(new LruCache(appContext)).build();

            Log.v("WebRequest", "Request queue initialized");
        }

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
    public boolean imageGetRequest(final String imageUrl, WebRequestListener webRequestListener, int requestId, ImageView intoView) {
        if(canStartRequests()) {
            Picasso.with(appContext).load(imageUrl).into(intoView, new ImageRequestHandler(webRequestListener, requestId));

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
            Gson gson = new Gson();

            FruitsDeserialized fruitsDeserialized = gson.fromJson(response, FruitsDeserialized.class);

            webRequestListener.fruitsReceived(fruitsDeserialized.getFruitsArray(), requestId);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if(error.getMessage() != null)
                webRequestListener.requestError(error.getMessage(), requestId);
            else
                webRequestListener.requestError("", requestId);
        }
    }

    private class ImageRequestHandler extends BaseRequestHandler implements Callback {
        public ImageRequestHandler(final WebRequestListener webRequesteListener, int requestId) {
            super(webRequesteListener, requestId);
        }

        @Override
        public void onSuccess() {
            webRequestListener.imageReceived(requestId);
        }

        @Override
        public void onError() {
            webRequestListener.requestError("Couldn't load the image", requestId);
        }
    }
}
