package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {
	
	EditText etSearchField;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);
		setupViews();
		imageAdapter = new ImageResultAdapter(this,imageResults);
		gvResults.setAdapter(imageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search, menu);
		return true;
	}
	
	public void setupViews()
	{
		etSearchField = (EditText) findViewById(R.id.etSearchField);
		gvResults = (GridView) findViewById(R.id.gvSearchResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		
	}
	
	public void onImageSearch(View v){
		final String query = etSearchField.getText().toString();
		Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
		Log.d("MYDEBUG", "query entered: " + query);
		AsyncHttpClient client = new AsyncHttpClient();
		Log.d("MYDEBUG", "client created: " + query);
		client.get(
			"https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "start=" + 0 + "&v=1.0&q=" + Uri.encode(query), 
			new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response){
					Log.d("MYDEBUG", "query success" + query);
					JSONArray imageJsonResults = null;
					try{
						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
						imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
						Log.d("MYDEBUG", imageResults.toString());
					} catch (JSONException e) {
						e.printStackTrace();
						Log.d("MYDEBUG", "failed to connect" + e.getMessage());
					}
				}
				
				@Override
				public void onFailure(Throwable e, JSONObject errorResponse){
					Log.d("MYDEBUG", "async internet get failed\n" + e.getMessage());
				}
		});
		Log.d("MYDEBUG", "get call made: " + query);
	}

}
