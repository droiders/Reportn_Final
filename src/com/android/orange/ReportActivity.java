package com.android.orange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.android.orange.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class ReportActivity extends MapActivity implements
	LocationListener {

    private MapView mapView = null;
    private LocationManager lm = null;
    static double lat = 0;
    static double lng = 0;
    private MapController mc = null;
    private MyLocationOverlay myLocation = null;
    double lat0;
    double lng0;
    double tmp1,tmp2;
    GeoPoint pp = null;
    static GeoPoint pt;
    static TextView text;
    int CAMERA_PIC_REQUEST = 1337;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
//	setContentView(R.layout.activity_report);
	setContentView(R.layout.activity_report);
	mapView = (MapView) this.findViewById(R.id.map);
	//mapView.setStreetView(true);
	mapView.setBuiltInZoomControls(true);
	
	text= (TextView)findViewById(R.id.adresse);
	lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
	lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
		this);
	
	mc = mapView.getController();
	mc.setZoom(15);

	myLocation = new MyLocationOverlay(getApplicationContext(), mapView);
	myLocation.disableCompass();
	myLocation.runOnFirstFix(new Runnable() {
	    public void run() {
		mc.animateTo(myLocation.getMyLocation());
		mc.setZoom(17);
		handler.sendEmptyMessage(1);
	    }
	});
	mapView.getOverlays().add(myLocation);
	Log.i("mylocation","avant");


	 Drawable marker=getResources().getDrawable(R.drawable.pushpin);
	    
	    marker.setBounds(0, 0, marker.getIntrinsicWidth(),
	                            marker.getIntrinsicHeight());
	    
	    mapView.getOverlays().add(new SitesOverlay(marker));
	    
	    myLocation=new MyLocationOverlay(this, mapView);
	    mapView.getOverlays().add(myLocation);
	         ImageButton btn_cam = (ImageButton) findViewById(R.id.button_camera);

	   		 btn_cam.setOnClickListener(new View.OnClickListener() {


	   				public void onClick(View view) {
	   					// Launching News Feed Screen
	   					AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportActivity.this);

	   	                // Setting Dialog Title
	   	                alertDialog.setTitle("vous devez joindre une photo");

	   	                // Setting Dialog Message
	   	                alertDialog.setMessage("allez à l'emplacement de la photo");

	   	                // Setting Icon to Dialog
	   	                alertDialog.setIcon(R.drawable.gd_action_bar_slideshow);

	   	                // Setting Positive "Yes" Button
	   	                alertDialog.setPositiveButton("camera", new DialogInterface.OnClickListener() {
	   	                    public void onClick(DialogInterface dialog, int which) {
	   	                    	Intent i = new Intent(getApplicationContext(), takephoto.class);
	   	            			startActivity(i);
	   	                    }
	   	                });

	   	                // Setting Negative "NO" Button
	   	                alertDialog.setNegativeButton("galerie", new DialogInterface.OnClickListener() {
	   	                    public void onClick(DialogInterface dialog, int which) {
	   	                    	Intent i = new Intent(getApplicationContext(), galery.class);
	   	            			startActivity(i);// User pressed No button. Write Logic Here
	   	                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
	   	                    }
	   	                });

	   	                // Setting Netural "Cancel" Button
	   	                alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
	   	                    public void onClick(DialogInterface dialog, int which) {
	   	                    // User pressed Cancel button. Write Logic Here
	   	                    Toast.makeText(getApplicationContext(), "You clicked on Cancel",
	   	                                        Toast.LENGTH_SHORT).show();
	   	                    }
	   	                });

	   	                // Showing Alert Message
	   	                alertDialog.show();
	   				}
	   			});
	   		 
	   		
	       }

    

    @Override
    protected void onResume() {
	super.onResume();
	myLocation.enableMyLocation();
	myLocation.enableCompass();
    }

    @Override
    protected boolean isRouteDisplayed() {
	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_S) {
	    mapView.setSatellite(!mapView.isSatellite());
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }

    public void onLocationChanged(Location location) {
	lat = location.getLatitude();
	lng = location.getLongitude();
	pp=new GeoPoint ((int) (lat * 1E6), (int) (lng * 1E6));
	GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
	mc.animateTo(p);
	mc.setCenter(p);
	
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }


    
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    
  public static String getadresse(){
	  return text.getText().toString();
	  
  }
    
  public double getLatitude(){
	  
	  return lat;
  }
  
 public double getLongitude(){
	  
	  return lng;
  }
  
  
    private Handler handler = new Handler() {

    	  public void handleMessage(android.os.Message msg) {

    	  
    	   if(msg.what == 1) {

    	    Geocoder geoCoder1 = new Geocoder(
    	      getBaseContext(), Locale.getDefault());
    	    try {

    	     GeoPoint p1 = new GeoPoint(myLocation.getMyLocation().getLatitudeE6(), myLocation.getMyLocation().getLongitudeE6());
    	     List<Address> addresses1 = geoCoder1.getFromLocation(
    	       p1.getLatitudeE6()  / 1E6, 
    	       p1.getLongitudeE6() / 1E6, 1);


    	     String AdresseLocation;
			if (addresses1.size() > 0) 
    	     {
    	      AdresseLocation="";
    	      for (int i=0; i<3; 
    	        i++){

    	       AdresseLocation += addresses1.get(0).getAddressLine(i) + "  ";
    	      }
    	     }else{AdresseLocation="Adresse Actuelle inconnue";}

    	     text.setHint(AdresseLocation);
    	    }
    	    catch (IOException e) {  

    	     e.printStackTrace();
    	    } 


    	   }

    	  };

    	 };
    	 
    	 private GeoPoint getPoint(double lat, double lon) {
    		    return(new GeoPoint((int)(lat*1000000.0),
    		                          (int)(lon*1000000.0)));
    		  }
    		    
    		  private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
    		    private List<OverlayItem> items=new ArrayList<OverlayItem>();
    		    private Drawable marker=null;
    		    private OverlayItem inDrag=null;
    		    private ImageView dragImage=null;
    		    private int xDragImageOffset=0;
    		    private int yDragImageOffset=0;
    		    private int xDragTouchOffset=0;
    		    private int yDragTouchOffset=0;
    		    boolean first;
    		    public SitesOverlay(Drawable marker) {
    		      
    		    	super(marker);
    		      this.marker=marker;
    		      first=true;
    		      
    		      dragImage=(ImageView)findViewById(R.id.drag);
    		      xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
    		      yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();
    		      
    		/*      items.add(new OverlayItem(getPoint(40.748963847316034,
    		                                          -73.96807193756104),
    		                                "UN", "United Nations"));
    		      items.add(new OverlayItem(getPoint(40.76866299974387,
    		                                          -73.98268461227417),
    		                                "Lincoln Center",
    		                                "Home of Jazz at Lincoln Center"));*/
    		     

    		      
    		    

    		     // populate();
    		    }
    		    
    		    @Override
    		    protected OverlayItem createItem(int i) {
    		      return(items.get(i));
    		    }
    		    
    		    @Override
    		    public void draw(Canvas canvas, MapView mapView,
    		                      boolean shadow) {
    		      super.draw(canvas, mapView, shadow);
    		      
    		      boundCenterBottom(marker);
    		    }
    		    
    		    @Override
    		    public int size() {
    		      return(items.size());
    		    }
    		    
    		    @Override
    		    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
    		      final int action=event.getAction();
    		      final int x=(int)event.getX();
    		      final int y=(int)event.getY();
    		      boolean result=false;
    		      
    		      
    		      if (action==MotionEvent.ACTION_DOWN) {
    		    	  
    		        for (OverlayItem item : items) {
    		          Point p=new Point(0,0);
    		          
    		          mapView.getProjection().toPixels(item.getPoint(), p);
    		          
    		          if (hitTest(item, marker, x-p.x, y-p.y)) {
    		            result=true;
    		            inDrag=item;
    		            items.remove(inDrag);
    		            populate();

    		            xDragTouchOffset=0;
    		            yDragTouchOffset=0;
    		            
    		            setDragImagePosition(p.x, p.y);
    		            dragImage.setVisibility(View.VISIBLE);

    		            xDragTouchOffset=x-p.x;
    		            yDragTouchOffset=y-p.y;
    		            
    		            break;
    		          }
    		        }
    		      }
    		      else if (action==MotionEvent.ACTION_MOVE && inDrag!=null) {
    		        setDragImagePosition(x, y);
    		        result=true;
    		      }
    		      else if (action==MotionEvent.ACTION_UP && inDrag!=null) {
    		        dragImage.setVisibility(View.GONE);
    		        
    		        pt=mapView.getProjection().fromPixels(x-xDragTouchOffset,
    		                                                   y-yDragTouchOffset);
    		        OverlayItem toDrop=new OverlayItem(pt, inDrag.getTitle(),
    		                                           inDrag.getSnippet());
    		        
    		        
    		        Geocoder geoCoder = new Geocoder(
    	                    getBaseContext(), Locale.getDefault());
    	                try {
    	                    List<Address> addresses = geoCoder.getFromLocation(
    	                        pt.getLatitudeE6()  / 1E6, 
    	                        pt.getLongitudeE6() / 1E6, 1);
    	 
    	                    String add = "";
    	                    if (addresses.size() > 0) 
    	                    {
    	                        for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
    	                             i++)
    	                           add += addresses.get(0).getAddressLine(i) + "\n";
    	                    }
    	                    text.setText(add);
    	 
    	                    Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
    	                }
    	                catch (IOException e) {                
    	                    e.printStackTrace();
    	                }   
    		      
    	                    /*Toast.makeText(getBaseContext(), 
    	                        pt.getLatitudeE6() / 1E6 + "," + 
    	                        pt.getLongitudeE6() /1E6 , 
    	                        Toast.LENGTH_SHORT).show();*/
    		        
    		        items.add(toDrop);
    		        populate();
    		        
    		        inDrag=null;
    		        result=true;
    		      }
    		          		      
    		      if(first==true){
					  items.add(new OverlayItem(getPoint(lat,lng),"laaaaaac", "emchii")); 
					  populate();
					  first=false;
					  
				    		      }
    		          		      
    		      return(result || super.onTouchEvent(event, mapView));
    		    }
    		    
    		    
    		    
    		    
    		    private void setDragImagePosition(int x, int y) {
    		      RelativeLayout.LayoutParams lp=
    		        (RelativeLayout.LayoutParams)dragImage.getLayoutParams();
    		            
    		      lp.setMargins(x-xDragImageOffset-xDragTouchOffset,
    		                      y-yDragImageOffset-yDragTouchOffset, 0, 0);
    		      dragImage.setLayoutParams(lp);
    		    }
    		  }
    	
    	 
    	
    	 
}