package com.example.instagramgallery.network;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {	
	private static HashMap<String, Bitmap> cache;

	private final WeakReference<ImageView> imageViewReference;

	private String url;

	/**
	 * Creates a new task to download a bitmap and set to display on the
	 * provided ImageView
	 * @param imageView The ImageView to place the downloaded bitmap
	 */
	public BitmapDownloaderTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);

		// create a new static cache if there is none
		if (cache == null) {
			cache = new HashMap<String, Bitmap>();
		}
	}

	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		url = params[0];

		// check for cached copy
		if (cache.containsKey(url)) {
			return cache.get(url);
		}

		// params comes from the execute() call: params[0] is the url.
		return WebInterface.downloadBitmap(url);
	}

	@Override
	// Once the image is downloaded, associates it to the imageView
	protected void onPostExecute(Bitmap bitmap) { 
		// add to cache
		cache.put(url, bitmap); 
		
		if (isCancelled()) {
			return;
		}
		
		
		// check image imageView is still valid
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {          	

				// set bitmap on image view
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	/**
	 * Checks the cache and if found it will finish it's task immediately
	 * Do not call execute to save on the creation of a new thread
	 * @param url The url to search for in the cache
	 * @return True iff the url was found in the cache
	 */
	public boolean searchCache(String url) {
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {          	

				// check for cached copy
				if (cache.containsKey(url)) {

					// set bitmap on image view
					imageView.setImageBitmap(cache.get(url));

					return true;
				}
			}
		}
		
		return false;
	}
	
}