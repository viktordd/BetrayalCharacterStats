package com.viktordikov.betrayalcharacterstats;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout.LayoutParams;

abstract class PinTouchListener implements OnTouchListener {
	
	private int CurrPos;
	private Point[] PinPos;
	private Point delta;

	public PinTouchListener(int currPos, Point[] pinPos) {
		CurrPos = currPos;
		PinPos = pinPos;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		if (delta != null) {
			x -= delta.x;
			y -= delta.y;
		}
		int closestPos = -1;

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				Rect rect = new Rect();
				v.getHitRect(rect);
				delta = new Point(x - rect.left, y - rect.top);
				break;

			case MotionEvent.ACTION_MOVE:
				closestPos = getClosestPin(x, y);
				if (closestPos > -1) {
					SetPoint(v, closestPos);
					SetStats(closestPos, false);
				}
				break;

			case MotionEvent.ACTION_UP:
				closestPos = getClosestPin(x, y);
				if (closestPos > -1) {
					SetPoint(v, closestPos);
					SetStats(closestPos, true);
				}
				
				delta = null;
				break;
				
			case MotionEvent.ACTION_CANCEL:
				SetPoint(v, CurrPos);
				SetStats(CurrPos, false);
				
				delta = null;
				break;
		}
		return true;
	}

	private int getClosestPin(int x, int y) {
		int closestPos = -1;
		double distance = Integer.MAX_VALUE;
		for (int i = 0; i < PinPos.length; i++) {
			double currDistance = Math.pow(x - PinPos[i].x, 2) + Math.pow(y - PinPos[i].y, 2);
			if (currDistance < distance) {
				distance = currDistance;
				closestPos = i;
			}
		}
		return closestPos;
	}

	private void SetPoint(View v, int position) {
		SetPoint(v, PinPos[position].x, PinPos[position].y);
	}

	private void SetPoint(View v, int x, int y) {
		LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
		layoutParams.leftMargin = x;
		layoutParams.topMargin = y;
		v.setLayoutParams(layoutParams);
	}

	protected abstract void SetStats(int pos, boolean touchEnd);
}