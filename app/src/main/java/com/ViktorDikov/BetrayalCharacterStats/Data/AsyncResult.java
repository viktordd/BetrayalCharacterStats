package com.ViktorDikov.BetrayalCharacterStats.Data;

import android.graphics.Bitmap;

public class AsyncResult {
    public final android.graphics.Bitmap Bitmap;
    public final PinDetails Pins;

    public AsyncResult(Bitmap bitmap, PinDetails pins) {
        this.Bitmap = bitmap;
        this.Pins = pins;
    }
}