package com.android.orange;

import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;

import com.android.orange.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class MapLocationOverlay  extends Overlay {

	/**
	 * Stored as global instances as one time initialization is enough
	 */
    private Bitmap mBubbleIcon, mShadowIcon;
    
    private LocationViewers mLocationViewers;
    
	private Paint	mInnerPaint, mBorderPaint, mTextPaint;
    
	/**
	 * It is used to track the visibility of information window and clicked location is known location or not 
	 * of the currently selected Map Location
	 */
    private MapLocation mSelectedMapLocation;  
    
	public MapLocationOverlay(LocationViewers mLocationViewers) {
		
		this.mLocationViewers = mLocationViewers;
		
		mBubbleIcon = BitmapFactory.decodeResource(mLocationViewers.getResources(),R.drawable.bubble);
		mShadowIcon = BitmapFactory.decodeResource(mLocationViewers.getResources(),R.drawable.shadow);
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  {
		
		/**
		 * Track the popup display
		 */
		boolean isRemovePriorPopup = mSelectedMapLocation != null;  

		/**
		 * Test whether a new popup should display
		 */
		mSelectedMapLocation = getHitMapLocation(mapView,p);
		if ( isRemovePriorPopup || mSelectedMapLocation != null) {
			mapView.invalidate();
		}		
		
		/**
		 *   Return true if we handled this onTap()
		 */
		return mSelectedMapLocation != null;
	}
	
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
    	
   		drawMapLocations(canvas, mapView, shadow);
   		drawInfoWindow(canvas, mapView, shadow);
    }

    /**
     * Test whether an information balloon should be displayed or a prior balloon hidden.
     */
    private MapLocation getHitMapLocation(MapView	mapView, GeoPoint	tapPoint) {
    	
    	/**
    	 *   Tracking the clicks on MapLocation
    	 */
    	MapLocation hitMapLocation = null;
		
    	RectF hitTestRecr = new RectF();
		Point screenCoords = new Point();
    	Iterator<MapLocation> iterator = mLocationViewers.getMapLocations().iterator();
    	while(iterator.hasNext()) {
    		MapLocation testLocation = iterator.next();
    		
    		/**
    		 * This is used to translate the map's lat/long coordinates to screen's coordinates
    		 */
    		mapView.getProjection().toPixels(testLocation.getPoint(), screenCoords);

	    	// Create a testing Rectangle with the size and coordinates of our icon
	    	// Set the testing Rectangle with the size and coordinates of our on screen icon
    		hitTestRecr.set(-mBubbleIcon.getWidth()/2,-mBubbleIcon.getHeight(),mBubbleIcon.getWidth()/2,0);
    		hitTestRecr.offset(screenCoords.x,screenCoords.y);

	    	//  At last test for a match between our Rectangle and the location clicked by the user
    		mapView.getProjection().toPixels(tapPoint, screenCoords);
    		if (hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
    			hitMapLocation = testLocation;
    			break;
    		}
    	}
    	
    	//  Finally clear the new MouseSelection as its process finished
    	tapPoint = null;
    	
    	return hitMapLocation; 
    }
    
    private void drawMapLocations(Canvas canvas, MapView	mapView, boolean shadow) {
    	
		Iterator<MapLocation> iterator = mLocationViewers.getMapLocations().iterator();
		Point screenCoords = new Point();
    	while(iterator.hasNext()) {	   
    		MapLocation location = iterator.next();
    		mapView.getProjection().toPixels(location.getPoint(), screenCoords);
			
	    	if (shadow) {
	    		// Offset the shadow in the y axis as it is angled, so the base is at x=0
	    		canvas.drawBitmap(mShadowIcon, screenCoords.x, screenCoords.y - mShadowIcon.getHeight(),null);
	    	} else {
    			canvas.drawBitmap(mBubbleIcon, screenCoords.x - mBubbleIcon.getWidth()/2, screenCoords.y - mBubbleIcon.getHeight(),null);
	    	}
    	}
    }

    private void drawInfoWindow(Canvas canvas, MapView	mapView, boolean shadow) {
    	
    	if ( mSelectedMapLocation != null) {
    		if ( shadow) {
    			//  Skip painting a shadow
    		} else {
				//  First we need to determine the screen coordinates of the selected MapLocation
				Point selDestinationOffset = new Point();
				mapView.getProjection().toPixels(mSelectedMapLocation.getPoint(), selDestinationOffset);
		    	
		    	//  Setup the info window
				int INFO_WINDOW_WIDTH = 175;
				int INFO_WINDOW_HEIGHT = 40;
				RectF infoWindowRect = new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);				
				int infoWindowOffsetX = selDestinationOffset.x-INFO_WINDOW_WIDTH/2;
				int infoWindowOffsetY = selDestinationOffset.y-INFO_WINDOW_HEIGHT-mBubbleIcon.getHeight();
				infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);

				//  Drawing the inner info window
				canvas.drawRoundRect(infoWindowRect, 5, 5, getmInnerPaint());
				
				//  Drawing the border for info window
				canvas.drawRoundRect(infoWindowRect, 5, 5, getmBorderPaint());
					
				//  Draw the MapLocation's name
				int TEXT_OFFSET_X = 10;
				int TEXT_OFFSET_Y = 15;
				canvas.drawText(mSelectedMapLocation.getName(),infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getmTextPaint());
			}
    	}
    }

	public Paint getmInnerPaint() {
		if ( mInnerPaint == null) {
			mInnerPaint = new Paint();
			mInnerPaint.setARGB(225, 50, 50, 50); //inner color
			mInnerPaint.setAntiAlias(true);
		}
		return mInnerPaint;
	}

	public Paint getmBorderPaint() {
		if ( mBorderPaint == null) {
			mBorderPaint = new Paint();
			mBorderPaint.setARGB(255, 255, 255, 255);
			mBorderPaint.setAntiAlias(true);
			mBorderPaint.setStyle(Style.STROKE);
			mBorderPaint.setStrokeWidth(2);
		}
		return mBorderPaint;
	}

	public Paint getmTextPaint() {
		if ( mTextPaint == null) {
			mTextPaint = new Paint();
			mTextPaint.setARGB(255, 255, 255, 255);
			mTextPaint.setAntiAlias(true);
		}
		return mTextPaint;
	}
}