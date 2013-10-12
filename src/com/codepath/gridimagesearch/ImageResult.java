package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult {
	private String fullUrl;
	private String thumbUrl;
	
	public ImageResult(JSONObject json){
		try{
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ImageResult(String url, String thumbnail){
		this.fullUrl = url;
		this.thumbUrl = thumbnail;
	}
	
	public String getFullUrl(){
		return fullUrl;
	}
	
	public String getThumbUrl(){
		return thumbUrl;
	}
	
	public String toString(){
		return thumbUrl;
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
		ArrayList<ImageResult> result = new ArrayList<ImageResult>();
		for (int x = 0; x<array.length(); x++){
			try{
				result.add(new ImageResult(array.getJSONObject(x)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
