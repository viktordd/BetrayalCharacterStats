package com.viktordikov.betrayalcharacterstats.Helpers;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.viktordikov.betrayalcharacterstats.R;

public class PinDetails {
	public static int SPEED_PINS = 0;
	public static int MIGHT_PINS = 1;
	public static int SANITY_PINS = 2;
	public static int KNOWLEDGE_PINS = 3;

	private Bitmap speedPinImg = null;
	private Bitmap mightPinImg = null;
	private Bitmap sanityPinImg = null;
	private Bitmap knowledgePinImg = null;
	private Point[][] PinPos = null;

	public PinDetails() {
	}
	
	public boolean isSet() {
		return speedPinImg != null && mightPinImg != null && sanityPinImg != null && knowledgePinImg != null;
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

	public Point[] getPinPos(int i) {
		return PinPos[i];
	}

	public Point getPinPos(int i, int j) {
		return PinPos[i][j];
	}
	public void FillScaledPinPos(Resources r, int charID, Bitmap charImg, int width, int height, double densityScale, int orientation) {
		int numPositions = r.getIntArray(R.array.char_constraints)[1] + 1;
		PinPos = new Point[4][];
		for (int i = 0; i < 4; i++) {
			PinPos[i] = new Point[numPositions];
			for (int j = 0; j < numPositions; j++) {
				PinPos[i][j] = new Point();
			}
		}
		int[] pinPoint = r.getIntArray(R.array.pin_pointer_offset);
		int[] array = r.getIntArray(getCharPinOffsetResoureID(charID));
		fillScaledPinPos(SPEED_PINS, width, height, orientation, charImg, densityScale, array, 0, pinPoint[0], pinPoint[1]);
		fillScaledPinPos(MIGHT_PINS, width, height, orientation, charImg, densityScale, array, 4, pinPoint[2], pinPoint[3]);
		fillScaledPinPos(SANITY_PINS, width, height, orientation, charImg, densityScale, array, 8, pinPoint[4], pinPoint[5]);
		fillScaledPinPos(KNOWLEDGE_PINS, width, height, orientation, charImg, densityScale, array, 12, pinPoint[6], pinPoint[7]);
	}
	private void fillScaledPinPos(int i, int width, int height, int orientation, Bitmap charImg, double densityScale, int[] pinOffsets, int pinOffsetsI, int pinX, int pinY) {
		int xOffset, yOffset;
		if (orientation == Configuration.ORIENTATION_PORTRAIT){
			xOffset = 0;
			yOffset = (height - charImg.getHeight()) / 2;
		} 
		else { // Configuration.ORIENTATION_LANDSCAPE
			xOffset = (width - charImg.getWidth()) / 2;
			yOffset = 0;
		}
		
		double xMin = xOffset + ((pinOffsets[pinOffsetsI + 0] - pinX) * densityScale);
		double yMin = yOffset + ((pinOffsets[pinOffsetsI + 1] - pinY) * densityScale);
		
		double xMax = xOffset + ((pinOffsets[pinOffsetsI + 2] - pinX) * densityScale);
		double yMax = yOffset + ((pinOffsets[pinOffsetsI + 3] - pinY) * densityScale);

		int lastPos = PinPos[i].length - 1;
		double xDelta = (xMax - xMin) / lastPos;
		double yDelta = (yMax - yMin) / lastPos;
		
		for (int j = 0; j < lastPos; j++) {
			PinPos[i][j].set((int) (xMin + (xDelta * j)), (int) (yMin + (yDelta * j)));
		}
		PinPos[i][lastPos].set((int) xMax, (int) yMax);
	}
	
	private int getCharPinOffsetResoureID(int m_id) {
		switch (m_id) {
			default:
			case 0:
				return R.array.pin_offset_blue_madame_zostra;
			case 1:
				return R.array.pin_offset_blue_vivian_lopez;
			case 2:
				return R.array.pin_offset_green_brandon_jaspers;
			case 3:
				return R.array.pin_offset_green_peter_akimoto;
			case 4:
				return R.array.pin_offset_purple_heather_granville;
			case 5:
				return R.array.pin_offset_purple_jenny_leclerc;
			case 6:
				return R.array.pin_offset_red_darrin_flash_williams;
			case 7:
				return R.array.pin_offset_red_ox_bellows;
			case 8:
				return R.array.pin_offset_white_father_rhinehardt;
			case 9:
				return R.array.pin_offset_white_professor_longfellow;
			case 10:
				return R.array.pin_offset_yellow_missy_dubourde;
			case 11:
				return R.array.pin_offset_yellow_zoe_ingstrom;
		}
	}
}
