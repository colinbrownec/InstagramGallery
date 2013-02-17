package com.example.instagramgallery;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.instagramgallery.network.WebInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	private JSONObject imageData;
	private GridView gridView;
	
	private static int TILE_WIDTH = 220;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// fetch the the 
		gridView = (GridView) findViewById(R.id.image_grid_view);

		// fetch the JSON download in a different thread that will then set the layout of the view
		RequestImagesTask request = new RequestImagesTask("http://pages.cs.wisc.edu/~griepent/instagram.json", this);
		request.execute();

		// fit as many pictures as possible depending on screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		gridView.setNumColumns(metrics.widthPixels / TILE_WIDTH);
		
		// when an image is tapped, open in a fullscreen view
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				Intent i = new Intent(MainActivity.this, ImageActivity.class);
				
				// Send image position to the ImageActivity
				try {
					String url = imageData.getJSONArray("data").getJSONObject(position).getJSONObject("images").getJSONObject("standard_resolution").getString("url");
					i.putExtra("url", url);
				} catch (JSONException e) {
					i.putExtra("url", "");
				}
				
				startActivity(i);
			}
		});
		
	}

	private class RequestImagesTask extends AsyncTask<Void, Void, Void> {
		private String url;
		private Context c;

		public RequestImagesTask(String url, Context c) {
			super();
			this.url = url;
			this.c = c;
		}

		@Override
		protected Void doInBackground(Void... params) {
			imageData = WebInterface.requestWebService(url);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			gridView.setAdapter(new ImageStreamAdapter(c, imageData));
		}

	}


}