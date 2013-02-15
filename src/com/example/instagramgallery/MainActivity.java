package com.example.instagramgallery;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.instagramgallery.network.WebInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {

	private JSONObject imageData;
	private GridView gridView;
	
	private static int TILE_WIDTH = 220;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RequestImagesTask request = new RequestImagesTask("http://pages.cs.wisc.edu/~griepent/instagram.json", this);
		request.execute();

		gridView = (GridView) findViewById(R.id.image_grid_view);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		gridView.setNumColumns(metrics.widthPixels / TILE_WIDTH);
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
			try {
				JSONArray imageStream = imageData.getJSONArray("data");

				for (int i = 0; i < imageStream.length(); i++) {
					JSONObject thumbnail = imageStream.getJSONObject(i).getJSONObject("images").getJSONObject("thumbnail");
					Log.i("crb", thumbnail.getString("url") + "\n");
				}
			} catch (Exception e) {
				System.err.println("image stream failed to download");
			}

			gridView.setAdapter(new ImageStreamAdapter(c, imageData));
		}

	}


}