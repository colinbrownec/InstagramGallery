package com.example.instagramgallery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {
	private static float minZoom = 0.5f, maxZoom = 3f;

	private float scale = 1f;
	private ScaleGestureDetector detector;
	
	private int width, height, minSize;
	private Matrix transform;

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
		// get the size of the screen
		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		
		// set the size of the picture to fit the smallest orientation of the screen
		minSize = Math.min(width, height);
		scale = (float) minSize / 612;
		
		// adjust scaling limits if needed
		if (scale > maxZoom)
			maxZoom = scale;
		if (scale < minZoom)
			minZoom = scale;
		
		// create a transformation matrix and set the scale and translation based on the initial scale
		transform = new Matrix();
		transform.setScale(scale, scale);
		transform.postTranslate(-306 * scale, -306 * scale);
		
		// set up a listener for pinch and zoom
		detector = new ScaleGestureDetector(getContext(), new ScaleListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		BitmapDrawable draw = (BitmapDrawable) this.getDrawable();
		if (draw != null) {		
			// draw a bitmap with the desired scaling and translate the canvas to pan
			canvas.translate(width / 2, height / 2);
			canvas.drawBitmap(draw.getBitmap(), transform, null);
		}
	}
	
	/**
	 * An extension of the android scaled gesture listener that implements adjusting the 
	 * image scale based on how the user pinches the screen
	 * @author Colin
	 */
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// adjust our scaling based on the gesture
			scale *= detector.getScaleFactor();
			scale = Math.max(minZoom, Math.min(scale, maxZoom));
			
			// adjust matrix
			transform.setScale(scale, scale);
			transform.postTranslate(-306 * scale, -306 * scale);
			
			// tell the canvas to redraw
			invalidate();
			return true;
		}
	}
}
