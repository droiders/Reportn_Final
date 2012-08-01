package com.android.orange;


import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.android.orange.R;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class LocationViewers extends LinearLayout {

	private MapLocationOverlay overlay;
	
    //  Known lat/long coordinates that we'll be using.
    private List<MapLocation> mapLocations;
    
    public static MapView mapView;
    
	public LocationViewers(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LocationViewers(Context context) {
		super(context);
		init();
	}
	
	public void init() {		

		setOrientation(VERTICAL);
		setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
		String api = getResources().getString(R.string.map_api_key);
		mapView = new MapView(getContext(),api);
		mapView.setEnabled(true);
		mapView.setClickable(true);
		addView(mapView);
		overlay = new MapLocationOverlay(this);
		mapView.getOverlays().add(overlay);
    	mapView.getController().setZoom(5);
    	mapView.getController().setCenter(getMapLocations().get(0).getPoint());
	}
	
	
	public List<MapLocation> getMapLocations() {
		if (mapLocations == null) {
			mapLocations = new ArrayList<MapLocation>();
			mapLocations.add(new MapLocation("Avinashi road, Coimbatore",11.0138,76.9871));
			//mapLocations.add(new MapLocation("Marina Beach, Chennai",13.0548,80.2830));
			//mapLocations.add(new MapLocation("Taj Mahal, New Delhi",28.6353,77.2250));
			//mapLocations.add(new MapLocation("Meenakshi Temple, Madurai",9.9195,78.1208));
		}
		return mapLocations;
	}

	public MapView getMapView() {
		return mapView;
	}
}
