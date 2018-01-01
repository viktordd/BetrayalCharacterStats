package com.viktordikov.betrayalcharacterstats.Helpers;

import android.util.Log;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;


public class CastChannel implements Cast.MessageReceivedCallback {
    private static final String TAG = "CastChannel";

    public String getNamespace(){
        return "urn:x-cast:com.google.cast.betrayalCharacterStats";
    }
    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
        Log.d(TAG, "onMessageReceived: " + message);
    }
}
