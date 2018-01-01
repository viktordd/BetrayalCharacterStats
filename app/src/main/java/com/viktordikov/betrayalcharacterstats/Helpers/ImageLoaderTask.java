package com.viktordikov.betrayalcharacterstats.Helpers;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.viktordikov.betrayalcharacterstats.CharacterFragment;
import com.viktordikov.betrayalcharacterstats.Data.AsyncResult;
import com.viktordikov.betrayalcharacterstats.Data.ImageResources;
import com.viktordikov.betrayalcharacterstats.R;

import java.lang.ref.WeakReference;

public class ImageLoaderTask extends AsyncTask<Integer, Void, AsyncResult> {
    final WeakReference<CharacterFragment> CharFragment;
    final int CharId;
    final int ImgResId;
    final int Orientation;
    final int Width;
    final int Height;
    final Resources r;

    public ImageLoaderTask(CharacterFragment fragment, int width, int height, Resources r) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        CharFragment = new WeakReference(fragment);
        CharId = fragment.getCharID();
        ImgResId = ImageResources.GetChar(fragment.getCharID());
        Orientation = r.getConfiguration().orientation;
        Width = width;
        Height = height;
        this.r = r;
    }

    // Decode image in background.
    @Override
    protected AsyncResult doInBackground(Integer... params) {

        // Load image
        Bitmap tempImg = BitmapFactory.decodeResource(r, ImgResId);

        double scale = getScale(tempImg);
        int density = tempImg.getDensity();

        Bitmap bitmap = scaleImage(scale, tempImg);

        // Load pins
        PinDetails pins = new PinDetails();
        pins.setSpeedPinImg(scaleImage(r, R.drawable.pin_speed, scale));
        pins.setMightPinImg(scaleImage(r, R.drawable.pin_might, scale));
        pins.setSanityPinImg(scaleImage(r, R.drawable.pin_sanity, scale));
        pins.setKnowledgePinImg(scaleImage(r, R.drawable.pin_knowledge, scale));

        pins.FillScaledPinPos(r, CharId, bitmap, Width, Height, (density / 320.0) * scale, Orientation);

        return new AsyncResult(bitmap, pins);
    }

    private double getScale(Bitmap b) {
        if (Orientation == Configuration.ORIENTATION_PORTRAIT)
            return Width / (float) b.getWidth();
        else // Configuration.ORIENTATION_LANDSCAPE
            return Height / (float) b.getHeight();
    }

    private Bitmap scaleImage(Resources r, int resource, double Scale) {
        Bitmap tempImg = BitmapFactory.decodeResource(r, resource);
        return scaleImage(Scale, tempImg);
    }

    private Bitmap scaleImage(double Scale, Bitmap tempImg) {
        if (Scale == 1.0)
            return tempImg;
        int newWidth = (int) (tempImg.getWidth() * Scale);
        int newHeight = (int) (tempImg.getHeight() * Scale);
        try {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempImg, newWidth, newHeight, true);
            return scaledBitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("ImageLoaderTask ", "Exception while scaling image.", e);
            return tempImg;
        }
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
