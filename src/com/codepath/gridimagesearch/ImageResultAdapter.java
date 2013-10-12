package com.codepath.gridimagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultAdapter(Context context, List<ImageResult> list){
		super(context,0,0,list);
	}

	public View getView(int position, View convertView, ViewGroup parent){
		ImageResult imageResult = getItem(position);		
		SmartImageView smartImageView;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			smartImageView = (SmartImageView) inflater.inflate(R.layout.item_image_result, parent, false);
		}else {
			smartImageView = (SmartImageView) convertView;
			smartImageView.setImageResource(android.R.color.transparent);
		}
		smartImageView.setImageUrl(imageResult.getThumbUrl());
		return smartImageView;
	}
}
