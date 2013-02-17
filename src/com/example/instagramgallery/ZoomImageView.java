package com.example.instagramgallery;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {
	private static float minZoom = 1f, maxZoom = 5f;
	
	private float scale = 1f;
	private float lastDistance = 0;
	private ScaleGestureDetector detector;
	
	public ZoomImageView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);
		setupPinchZoom();
	}
	
	public ZoomImageView(Context context, AttributeSet set) {
		super(context, set);
		setupPinchZoom();
	}
	
	public ZoomImageView(Context context) {
		super(context);
		setupPinchZoom();
	}
	
	public void setupPinchZoom() {
		detector = new ScaleGestureDetector(getContext(), new OnScaleGestureListener() {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				scale *= detector.getScaleFactor();
				scale = Math.max(minZoom, Math.min(scale, maxZoom));
				Log.i("crb", "scale = " + scale);
				invalidate();
				return true;
			}

			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				if (lastDistance == 0) {
					lastDistance = detector.getCurrentSpan();
					
				} else {
					float delta = detector.getCurrentSpan() - lastDistance;
					scale += Math.signum(delta) * scale * 0.01;
					
					Log.i("crb", "distance = " + delta);
					Log.i("crb", "scale = " + scale);
		
					lastDistance = detector.getCurrentSpan();
				}
		
				return false;
			}

			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return true;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.scale(scale, scale);
		super.onDraw(canvas);
	}
}
