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
	private ViewPager ViewPager;
	private Point delta;
	private int PrevPos;

	public PinTouchListener(int currPos, Point[] pinPos, ViewPager viewPager) {
		CurrPos = currPos;
		PinPos = pinPos;
		ViewPager = viewPager;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		if (delta != null) {
			x -= delta.x;
			y -= delta.y;
		}
		int closestPos;

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				if (ViewPager != null)
					ViewPager.requestDisallowInterceptTouchEvent(true);
				Rect rect = new Rect();
				v.getHitRect(rect);
				delta = new Point(x - rect.left, y - rect.top);
				PrevPos = CurrPos;
				break;

			case MotionEvent.ACTION_MOVE:
				closestPos = getClosestPin(x, y);
				if (closestPos > -1 && closestPos != PrevPos) {
					SetPoint(v, closestPos);
					SetStats(closestPos, false);
					PrevPos = closestPos;
				}
				break;

			case MotionEvent.ACTION_UP:
				closestPos = getClosestPin(x, y);
				if (closestPos > -1) {
					SetPoint(v, closestPos);
					SetStats(closestPos, true);
					CurrPos = closestPos;
				}

				delta = null;
				if (ViewPager != null)
					ViewPager.requestDisallowInterceptTouchEvent(false);
				break;
				
			case MotionEvent.ACTION_CANCEL:
				SetPoint(v, CurrPos);
				SetStats(CurrPos, false);
				
				delta = null;
				if (ViewPager != null)
					ViewPager.requestDisallowInterceptTouchEvent(false);
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