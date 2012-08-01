package com.android.orange;

import java.io.File;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	
	// Keep all Images in array
	public File[] mlistFiles = new File("/sdcard/dcim/camera").listFiles();
	int lng = mlistFiles.length;
	
	
	// Constructor
	public ImageAdapter(Context c){
		mContext = c;
	}

	public int getCount() {
		return mlistFiles.length;
	}

	
	public Object getItem(int position) {
		return mlistFiles[position];
	}

	
	public long getItemId(int position) {
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {			
		ImageView imageView = new ImageView(mContext);
		
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        return imageView;
	}

}
