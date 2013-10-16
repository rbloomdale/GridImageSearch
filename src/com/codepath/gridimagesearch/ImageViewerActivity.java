package com.codepath.gridimagesearch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.loopj.android.image.SmartImageView;

public class ImageViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		
		String imageResult = getIntent().getExtras().getString("imageURL");
	    SmartImageView smartImageView = (SmartImageView) findViewById(R.id.ivLargeImage);
		smartImageView.setImageUrl(imageResult);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_viewer, menu);
		return true;
	}

}
