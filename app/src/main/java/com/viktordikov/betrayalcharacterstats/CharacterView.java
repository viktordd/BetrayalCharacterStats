//package com.viktordikov.betrayalcharacterstats;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.widget.ImageView;
//
//
//public class CharacterView extends ImageView {
//
//
//	/**
//	 * @param context
//	 */
//	public CharacterView(Context context)
//	{
//		super(context);
//		// TODO Auto-generated constructor stub
//		setBackgroundColor(0xFFFFFF);
//	}
//
//	/**
//	 * @param context
//	 * @param attrs
//	 */
//	public CharacterView(Context context, AttributeSet attrs)
//	{
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @param context
//	 * @param attrs
//	 * @param defStyle
//	 */
//	public CharacterView(Context context, AttributeSet attrs, int defStyle)
//	{
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas)
//	{
//		// TODO Auto-generated method stub
//		super.onDraw(canvas);
//		System.out.println("Painting content");
//		Paint paint  = new Paint(Paint.LINEAR_TEXT_FLAG);
//		paint.setColor(0x0);
//		paint.setTextSize(12.0F);
//		System.out.println("Drawing text");
//		canvas.drawText("Hello World in custom view", 100, 100, paint);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event)
//	{
//		// TODO Auto-generated method stub
//		return super.onTouchEvent(event);
//
//	}
//}
