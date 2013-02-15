package com.example.instagramgallery;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.instagramgallery.network.*;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ImageStreamAdapter extends BaseAdapter {
	private HashMap<ImageView, BitmapDownloaderTask> competitors;

	JSONObject imageData;
	Context c;

	public ImageStreamAdapter(Context c, JSONObject imageData) {
		Log.i("crb", "image data downloaded");

		this.c = c;
		this.imageData = imageData;

		competitors = new HashMap<ImageView, BitmapDownloaderTask>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try {
			return imageData.getJSONArray("data").length();
		} catch (JSONException e) {
			// simply return 0
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if(convertView == null)	// Recycled View
		{
			imageView = new ImageView(c);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
		}
		else	// Re-use the view
		{
			imageView = (ImageView) convertView;
		}

		// clear the displayed image
		imageView.setImageBitmap(null);

		try {
			//imageView.setImageResource(mThumbs[position]);
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);

	        // get competitor & add new task
	        BitmapDownloaderTask other = competitors.put(imageView, task);
	        
	        // cancel competitor if one existed
	        if (other != null)
	        	other.cancel(false);

			String url = imageData.getJSONArray("data").getJSONObject(position).getJSONObject("images").getJSONObject("thumbnail").getString("url");
			task.execute(url);
		} catch (JSONException e) {

		}

		return imageView;
	}



}
