package com.viktordikov.betrayalcharacterstats;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.viktordikov.betrayalcharacterstats.Data.AsyncResult;

import java.lang.ref.WeakReference;

public class TokenWorkerTask extends AsyncTask<Integer, Void, AsyncResult> {
    final WeakReference<CharacterFragment> CharFragment;
    final int CharId;
    final int ImgResId;
    final int Orientation;
    final int Width;
    final int Height;
    final Resources r;

    TokenWorkerTask(WeakReference<CharacterFragment> fragment, int charID, int imgResId, int orientation, int width, int height, Resources r) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        CharId = charID;
        CharFragment = fragment;
        ImgResId = imgResId;
        Orientation = orientation;
        Width = width;
        Height = height;
        this.r = r;
    }

    // Decode image in background.
    @Override
    protected AsyncResult doInBackground(Integer... params) {

        // Load image
        Bitmap tempImg = BitmapFactory.decodeResource(r, ImgResId);

        double Scale = getScale(tempImg);
        int Density = tempImg.getDensity();

        Bitmap bitmap = scaleImage(Scale, tempImg);

        // Load pins
        PinDetails pins = new PinDetails();
        pins.setSpeedPinImg(scaleImage(r, R.mipmap.pin_speed, Scale));
        pins.setMightPinImg(scaleImage(r, R.mipmap.pin_might, Scale));
        pins.setSanityPinImg(scaleImage(r, R.mipmap.pin_sanity, Scale));
        pins.setKnowledgePinImg(scaleImage(r, R.mipmap.pin_knowledge, Scale));

        pins.FillScaledPinPos(r, CharId, bitmap, Width, Height, (Density / 320.0) * Scale, Orientation);

        return new AsyncResult(bitmap, pins);
    }

    private double getScale(Bitmap b) {
        if (Orientation == Configuration.ORIENTATION_PORTRAIT)
            return Width / (float) b.getWidth();
        else
            // Configuration.ORIENTATION_LANDSCAPE
            return Height / (float) b.getHeight();
    }

    private Bitmap scaleImage(double Scale, Bitmap tempImg) {
        if (Scale == 1.0)
            return tempImg;
        return Bitmap.createScaledBitmap(tempImg, (int) (tempImg.getWidth() * Scale), (int) (tempImg.getHeight() * Scale), true);
    }

    private Bitmap scaleImage(Resources r, int resource, double Scale) {
        Bitmap tempImg = BitmapFactory.decodeResource(r, resource);
        return scaleImage(Scale, tempImg);
    }

    // Once complete, see if CharFragment is still around and set bitmaps.
    @Override
    protected void onPostExecute(AsyncResult result) {
        if (result == null || result.Bitmap == null || result.Pins == null || CharFragment == null)
            return;

        CharacterFragment fragment = CharFragment.get();
        if (fragment != null) {
            fragment.SetCharacterImages(result);
        }
    }
}
