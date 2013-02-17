package com.example.instagramgallery;

import com.example.instagramgallery.network.BitmapDownloaderTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	ZoomImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);

		// Get intent data 
		Intent i = getIntent();
		String url = i.getExtras().getString("url");

		if (url.length() > 0) {

			imageView = (ZoomImageView) findViewById(R.id.full_image_view);

			// start a task to download the image
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			if (!task.searchCache(url))
				task.execute(url);

		} else {

			// display error message

		}
	}
}
