package com.example.fluteapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class MultitouchView extends View {

	private static final int SIZE = 60;

	private SparseArray<PointF> mActivePointers;
	private Paint mPaint;
	private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
			Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
			Color.LTGRAY, Color.YELLOW };

	private Paint textPaint;

	public MultitouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mActivePointers = new SparseArray<PointF>();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// set painter color to a color you like
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(20);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("Test", event.toString());
		

		// get pointer index from the event object
		int pointerIndex = event.getActionIndex();

		// get pointer ID
		int pointerId = event.getPointerId(pointerIndex);

		// get masked (not specific to a pointer) action
		int maskedAction = event.getActionMasked();

		switch (maskedAction) {

		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN: {
			// We have a new pointer. Lets add it to the list of pointers

			PointF f = new PointF();
			f.x = event.getX(pointerIndex);
			f.y = event.getY(pointerIndex);
			mActivePointers.put(pointerId, f);
			break;
		}
		case MotionEvent.ACTION_MOVE: { // a pointer was moved
			for (int size = event.getPointerCount(), i = 0; i < size; i++) {
				PointF point = mActivePointers.get(event.getPointerId(i));
				if (point != null) {
					point.x = event.getX(i);
					point.y = event.getY(i);
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL: {
			mActivePointers.remove(pointerId);
			break;
		}
		}
		invalidate();

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// draw all pointers
		for (int size = mActivePointers.size(), i = 0; i < size; i++) {
			PointF point = mActivePointers.valueAt(i);
			if (point != null) {
                mPaint.setColor(colors[i % 9]);
            }

            int marginY = 60;

            if(point.x < this.getWidth()/2) {
                if(point.y < this.getHeight()/3) {
                    canvas.drawRect(0, 0+marginY, this.getWidth()/2, this.getHeight()/3-marginY, mPaint);
                }
                else if (point.y > this.getHeight()/3 && point.y < this.getHeight()/3*2) {
                    canvas.drawRect(0, this.getHeight()/3+marginY, this.getWidth()/2, this.getHeight()/3*2-marginY, mPaint);
                }
                else {
                    canvas.drawRect(0, this.getHeight()/3*2+marginY, this.getWidth()/2, this.getHeight()-marginY, mPaint);
                }
            }
            else {
                if(point.y < this.getHeight()/3) {
                    canvas.drawRect(this.getWidth()/2, 0+marginY, this.getWidth(), this.getHeight()/3-marginY, mPaint);
                }
                else if (point.y > this.getHeight()/3 && point.y < this.getHeight()/3*2) {
                    canvas.drawRect(this.getWidth()/2, this.getHeight()/3+marginY, this.getWidth(), this.getHeight()/3*2-marginY, mPaint);
                }
                else {
                    canvas.drawRect(this.getWidth()/2, this.getHeight()/3*2+marginY, this.getWidth(), this.getHeight()-marginY, mPaint);
                }
            }
		}
		canvas.drawText("Total pointers: " + mActivePointers.size(), 10, 40, textPaint);
		
	}

}
