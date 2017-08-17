package com.bruno.desafiomuxi.webrequest;

import android.graphics.Bitmap;

/**
 * Created by Bruno on 16/08/2017.
 */

public interface WebRequestListener {
    public void fruitsReceived(Fruit[] fruits, int requestId);
    public void fruitImageReceived(Bitmap fruitImage, int requestId);
    public void imageReceived(Bitmap image, int requestId);
    public void requestError(String errorMessage, int requestId);
}
