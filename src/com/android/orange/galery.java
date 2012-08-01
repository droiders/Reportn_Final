package com.android.orange;

import java.io.File;

import com.android.orange.R;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class galery extends Activity {
	
	
	
private File[] mlistFiles;
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_galery);
                
                mlistFiles = new File("/sdcard/dcim/camera").listFiles();
                
                GridView gridview = (GridView) findViewById(R.id.gridview);
                gridview.setAdapter(new ImageAdapter(this));
                gridview.setOnItemClickListener(new OnItemClickListener() {
        			public void onItemClick(AdapterView<?> parent, View v,
        					int position, long id) {
        				
        				// Sending image id to FullScreenActivity
        				Intent i = new Intent(getApplicationContext(),FormActivity.class);
        				// passing array index
        				i.putExtra("flag", 1);
        				i.putExtra("id", position);
        				startActivity(i);
        			}
        		});
                
        }
        
        public class ImageAdapter extends BaseAdapter {
                private Context mContext;

                public ImageAdapter(Context c) {
                        mContext = c;
                }

                public int getCount() {
                        return mlistFiles.length;
                }

                public Object getItem(int position) {
                        return position;
                }

                public long getItemId(int position) {
                        return position;
                }

                public View getView(int position, View convertView, ViewGroup parent) {
                        ImageView imageView;
                        if (convertView == null) {
                                imageView = new ImageView(mContext);
                                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setPadding(8, 8, 8, 8);
                        } else {
                                imageView = (ImageView) convertView;
                        }
                        
                        BitmapFactory.Options options=new BitmapFactory.Options();
                        options.inSampleSize = 20;

                        imageView.setImageBitmap(BitmapFactory.decodeFile(mlistFiles[position].getPath(), options));
                        
                        return imageView;
                }

        
		
	}
}