package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {
	
	EditText etSearchField;
	GridView gvResults;
	Button btnSearch;
	Spinner spColorOptions;
	Spinner spSizeOptions;
	Spinner spTypeOptions;
	EditText etSiteOptionField;
	RelativeLayout rlAdvancedOptions;
	
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
		gvResults.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
	        	ImageResult image = imageAdapter.getItem(position);
	        	Intent i = new Intent(ImageSearchActivity.this, ImageViewerActivity.class);
	        	i.putExtra("imageURL", image.getFullUrl());
	        	startActivity(i);
	        }
	    });
		gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Whatever code is needed to append new items to your AdapterView
	                // probably sending out a network request and appending items to your adapter. 
	                // Use the page or the totalItemsCount to retrieve correct data.
		        performImageSearch(page); 
		    }
	        });
		btnSearch = (Button) findViewById(R.id.btnSearch);
		spColorOptions = (Spinner) findViewById(R.id.spColorOptions);
		spSizeOptions = (Spinner) findViewById(R.id.spSizeOptions);
		spTypeOptions = (Spinner) findViewById(R.id.spTypeOptions);
		etSiteOptionField = (EditText) findViewById(R.id.etSiteOptionField);
		rlAdvancedOptions = (RelativeLayout) findViewById(R.id.advancedOptionsContainer);
	}
	
	public boolean toggleAdvancedOptions(MenuItem item)
	{
		Boolean visible = rlAdvancedOptions.getVisibility() == View.VISIBLE;
		if(visible)
		{
			rlAdvancedOptions.setVisibility(View.GONE);
		} 
		else
		{
			rlAdvancedOptions.setVisibility(View.VISIBLE);
		}
		return true;
	}
	
	public void onImageSearch(View v){
		imageAdapter.clear();
		performImageSearch(0);
	}
	
	public void performImageSearch(int page){
		final String query = etSearchField.getText().toString();
		AsyncHttpClient client = new AsyncHttpClient();
		String options = getAdvanceOptionsString();
		client.get(
			"https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "start=" + page*8 + "&v=1.0&q=" + Uri.encode(query) + options, 
			new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response){
					JSONArray imageJsonResults = null;
					try{
						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
						ArrayList<ImageResult> imageResults = ImageResult.fromJSONArray(imageJsonResults);
						for (int i = 0; i < imageResults.size(); i++) {
							imageAdapter.add(imageResults.get(i));
					    }						
					} catch (JSONException e) {
						e.printStackTrace();
						Log.d("MYDEBUG", "failed to connect message=" + e.getMessage());
					}
				}
				
				@Override
				public void onFailure(Throwable e, JSONObject errorResponse){
					Log.d("MYDEBUG", "async internet get failed message=" + e.getMessage());
				}
		});
		Log.d("MYDEBUG", "get call made: " + query);
	}

	private String getAdvanceOptionsString(){
		String result = "";
		Boolean visible = rlAdvancedOptions.getVisibility() == View.VISIBLE;
		if(!visible) return result;
		Object selectedColorItem = spColorOptions.getSelectedItem();
		if(selectedColorItem != null && selectedColorItem != "All"){
			result+="&imgcolor=" + selectedColorItem;
		}
		Object selectedTypeItem = spTypeOptions.getSelectedItem();
		if(selectedTypeItem != null && selectedTypeItem != "All"){
			result+="&imgtype=" + selectedTypeItem;
		}
		Object selectedSizeItem = spSizeOptions.getSelectedItem();
		if(selectedSizeItem != null && selectedSizeItem != "All"){
			result+="&imgsz=" + selectedSizeItem;
		}
		String siteUrl = etSiteOptionField.getText().toString().toLowerCase();
		if(siteUrl != null && !siteUrl.isEmpty()){
			UrlValidator urlValidator = new UrlValidator();
			if(!siteUrl.contains("http://") || !siteUrl.contains("https://")){
				siteUrl="http://"+siteUrl;
			}
			if(urlValidator.isValid(siteUrl)){
				result+="&as_sitesearch="+Uri.encode(siteUrl);
			} else {
				Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
			}
		}
		return result.toLowerCase();
	}
}
