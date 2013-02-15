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

    public BitmapDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        
        if (cache == null) {
        	cache = new HashMap<String, Bitmap>();
        }
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
    	url = params[0];
    	
    	// check for cached copy
    	if (cache.containsKey(url)) {
    		//this.cancel(false);
    		return cache.get(url);
    	}
    	
    	// params comes from the execute() call: params[0] is the url.
    	return WebInterface.downloadBitmap(url);
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) { 
    	
        if (isCancelled()) {
            return;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {          	
            	
            	// add to cache
            	cache.put(url, bitmap); 
            	
            	// set bitmap on image view
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}