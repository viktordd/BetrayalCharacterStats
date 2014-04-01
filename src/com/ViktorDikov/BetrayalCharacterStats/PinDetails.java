package com.ViktorDikov.BetrayalCharacterStats;

import android.graphics.Bitmap;
import android.graphics.Point;

public class PinDetails {

	private Bitmap speedPinImg = null;
	private Bitmap mightPinImg = null;
	private Bitmap sanityPinImg = null;
	private Bitmap knowledgePinImg = null;
	private int ImageOrientation = -1;
	private Point[][] PinPos = null;

	public PinDetails() {
	}
	
	public boolean isSet() {
		return speedPinImg != null && mightPinImg != null && sanityPinImg != null && knowledgePinImg != null;
	}
	
	public boolean isSet(int orientation) {
		return isSet() && ImageOrientation == orientation;
	}
	
	public void Clear(){
		if (isSet()){
			speedPinImg.recycle();
			mightPinImg.recycle();
			sanityPinImg.recycle();
			knowledgePinImg.recycle();
		}
		speedPinImg = null;
		mightPinImg = null;
		sanityPinImg = null;
		knowledgePinImg = null;
	}

	public Bitmap getSpeedPinImg() {
		return speedPinImg;
	}

	public void setSpeedPinImg(Bitmap speedPinImg) {
		this.speedPinImg = speedPinImg;
	}

	public Bitmap getMightPinImg() {
		return mightPinImg;
	}

	public void setMightPinImg(Bitmap mightPinImg) {
		this.mightPinImg = mightPinImg;
	}

	public Bitmap getSanityPinImg() {
		return sanityPinImg;
	}

	public void setSanityPinImg(Bitmap sanityPinImg) {
		this.sanityPinImg = sanityPinImg;
	}

	public Bitmap getKnowledgePinImg() {
		return knowledgePinImg;
	}

	public void setKnowledgePinImg(Bitmap knowledgePinImg) {
		this.knowledgePinImg = knowledgePinImg;
	}

	public int getImageOrientation() {
		return ImageOrientation;
	}

	public void setImageOrientation(int imageOrientation) {
		ImageOrientation = imageOrientation;
	}

	public Point getPinPos(int i, int j) {
		return PinPos[i][j];
	}

	public void FillScaledPinPos(int numPositions, int displayAreaWidth, int displayAreaHeight, double density, double scale, int[] lowerPinOffsets, int[] upperPinOffsets) {
		PinPos = new Point[2][];
		for (int i = 0; i < 2; i++) {
			PinPos[i] = new Point[numPositions];
			for (int j = 0; j < PinPos[i].length; j++) {
				PinPos[i][j] = new Point();
			}
		}
		
		double densityScale = (density / 320.0) * scale;
		fillScaledPinPos(0, displayAreaWidth, displayAreaHeight, speedPinImg.getWidth(), speedPinImg.getHeight(), densityScale, lowerPinOffsets);
		fillScaledPinPos(1, displayAreaWidth, displayAreaHeight, mightPinImg.getWidth(), mightPinImg.getHeight(), densityScale, upperPinOffsets);
	}
	private void fillScaledPinPos(int i, int displayAreaWidth, int displayAreaHeight, int imageWidth, int imageHeight, double densityScale, int[] pinOffsets) {

		double x = (displayAreaWidth / 2.0) - (imageWidth / 2.0);
		double y = (displayAreaHeight / 2.0) - (imageHeight / 2.0);
		
		double xMin = x - (pinOffsets[0] * densityScale);
		double yMin = y - (pinOffsets[1] * densityScale);
		
		double xMax = x - (pinOffsets[2] * densityScale);
		double yMax = y - (pinOffsets[3] * densityScale);

		int lastPos = PinPos[i].length - 1;
		double xDelta = (xMax - xMin) / lastPos;
		double yDelta = (yMax - yMin) / lastPos;
		
		for (int j = 0; j < lastPos; j++) {
			PinPos[i][j].set((int) (xMin + (xDelta * j)), (int) (yMin + (yDelta * j)));
		}
		PinPos[i][lastPos].set((int) xMax, (int) yMax);
	}
}
