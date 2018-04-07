package com.ViktorDikov.BetrayalCharacterStats.Helpers;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout.LayoutParams;

public abstract class PinTouchListener implements OnTouchListener {
	private int CurrPos;
	private Point[] PinPos;
	private ViewPager ViewPager;
	private Point delta;
	private static int touches = 0;

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
				touches++;
				break;

			case MotionEvent.ACTION_MOVE:
				setPos(v, x, y, false);
				break;

			case MotionEvent.ACTION_UP:
				setPos(v, x, y, true);
				if (touches > 0)
				    touches--;
				delta = null;
				if (ViewPager != null && touches == 0)
					ViewPager.requestDisallowInterceptTouchEvent(false);
				break;
				
			case MotionEvent.ACTION_CANCEL:
				delta = null;
                if (touches > 0)
                    touches--;
                if (ViewPager != null && touches == 0)
					ViewPager.requestDisallowInterceptTouchEvent(false);
				break;
		}
		return true;
	}

	private void setPos(View v, int x, int y, boolean touchEnd){
		int closestPos = getClosestPin(x, y);
		if (closestPos > -1 && closestPos != CurrPos) {
			SetPoint(v, closestPos);
			SetStats(closestPos, touchEnd);
			CurrPos = closestPos;
		}
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