package com.viktordikov.betrayalcharacterstats.Data;

import android.graphics.Bitmap;

import com.viktordikov.betrayalcharacterstats.Helpers.PinDetails;

public class AsyncResult {
    public final android.graphics.Bitmap Bitmap;
    public final PinDetails Pins;

    public AsyncResult(Bitmap bitmap, PinDetails pins) {
        this.Bitmap = bitmap;
        this.Pins = pins;
    }
}