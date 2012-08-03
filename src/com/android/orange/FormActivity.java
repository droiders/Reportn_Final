
package com.android.orange;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class FormActivity extends MapActivity {
	static TextView txt;
	private MapView mapView = null;
	EditText name, email;
	takephoto tp;
	ReportActivity pa;
	private MapController mc = null;
	
	private MyLocationOverlay myLocation = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.form);
		//txt=(TextView) findViewById(R.id.adress);
		mapView = (MapView) this.findViewById(R.id.map);
		
		mapView.setSatellite(true);
		double lat = pa.getLatitude();
		double lng = pa.getLongitude();
		System.out.println(lat);
		GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		mc=mapView.getController();
		mc.animateTo(p);
		mc.setCenter(p);

		
	/*myLocation.runOnFirstFix(new Runnable() {
		    public void run() {
			mc.animateTo(myLocation.getMyLocation());
			mc.setZoom(17);
			handler.sendEmptyMessage(1);
		    }
		});*/
		int i;
		if ( (i=getIntent().getExtras().getInt("flag"))!=1){
	    ImageView report=(ImageView) findViewById(R.id.pictosend);
		tp=new takephoto();
		Bitmap bp=tp.getBitmap();
		report.setImageBitmap(bp);
		}else{
			ImageView report=(ImageView) findViewById(R.id.pictosend);
		   int j=getIntent().getExtras().getInt("id");
		   ImageAdapter imageAdapter = new ImageAdapter(this);
	        Bitmap myBitmap = BitmapFactory.decodeFile(imageAdapter.mlistFiles[j].getAbsolutePath());
			report.setImageBitmap(myBitmap);
		}
		
		
		 init();
	}

	private void init() {
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.Email);
	/*	age = (EditText) findViewById(R.id.editTextAge);
		age.setInputType(InputType.TYPE_CLASS_NUMBER);*/
		readPerson();
	}
	public void sendFeedback(View button) {
		
		final EditText nameField = (EditText) findViewById(R.id.name);
		String name = nameField.getText().toString();

		final EditText emailField = (EditText) findViewById(R.id.Email);
		String email = emailField.getText().toString();

		final EditText feedbackField = (EditText) findViewById(R.id.commentaire);
		String feedback = feedbackField.getText().toString();

		final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
		String feedbackType = feedbackSpinner.getSelectedItem().toString();


		final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
		boolean bRequiresResponse = responseCheckbox.isChecked();

		// Take the fields and format the message contents
		String subject = formatFeedbackSubject(feedbackType);

		String message = formatFeedbackMessage(feedbackType, name,
			 email, feedback, bRequiresResponse);

		// Create the message
		sendFeedbackMessage(subject, message);
	}


	protected String formatFeedbackSubject(String feedbackType) {

		String strFeedbackSubjectFormat = getResources().getString(
				R.string.feedbackmessagesubject_format);

		String strFeedbackSubject = String.format(strFeedbackSubjectFormat, feedbackType);

		return strFeedbackSubject;

	}

	protected String formatFeedbackMessage(String feedbackType, String name,
			String email, String feedback, boolean bRequiresResponse) {

		String strFeedbackFormatMsg = getResources().getString(
				R.string.feedbackmessagebody_format);

		String strRequiresResponse = getResponseString(bRequiresResponse);

		String strFeedbackMsg = String.format(strFeedbackFormatMsg,
				feedbackType, feedback, name, email, strRequiresResponse);

		return strFeedbackMsg;

	}


	protected String getResponseString(boolean bRequiresResponse)
	{
		if(bRequiresResponse==true)
		{
			return getResources().getString(R.string.feedbackmessagebody_responseyes);
		} else {
			return getResources().getString(R.string.feedbackmessagebody_responseno);
		}

	}

	public void sendFeedbackMessage(String subject, String message) {

		Intent messageIntent = new Intent(android.content.Intent.ACTION_SEND);

		String aEmailList[] = { "appfeedback@yourappwebsite.com" };
		messageIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);

		messageIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

		messageIntent.setType("plain/text");
		messageIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

		startActivity(messageIntent);
	}


	public void save(View view) {
		String nameText = name.getText().toString();
		String surnameText = email.getText().toString();
	//	String ageText = age.getText().toString();

		if (nameText != null)
			PreferenceConnector.writeString(this, PreferenceConnector.NAME,
					nameText);
		if (surnameText != null)
			PreferenceConnector.writeString(this, PreferenceConnector.SURNAME,
					surnameText);
	/*	if (ageText != null && !ageText.equals(""))
			PreferenceConnector.writeInteger(this, PreferenceConnector.AGE,
					Integer.parseInt(ageText));*/
	}

	public void reset(View view) {
		/* A better way to delete all is:
		 * PreferenceConnector.getEditor(this).clear().commit();
		 */
		PreferenceConnector.getEditor(this).remove(PreferenceConnector.NAME)
				.commit();
		PreferenceConnector.getEditor(this).remove(PreferenceConnector.SURNAME)
				.commit();
		/*PreferenceConnector.getEditor(this).remove(PreferenceConnector.AGE)
				.commit();*/
		readPerson();
	}

	/*
	 * Read the data refer to saved person and visualize them into Edittexts
	 */
	private void readPerson() {
		name.setText(PreferenceConnector.readString(this,
				PreferenceConnector.NAME, null));
		email.setText(PreferenceConnector.readString(this,
				PreferenceConnector.SURNAME, null));
		/*String agePref = ""
				+ PreferenceConnector.readInteger(this,
						PreferenceConnector.AGE, 0);
		age.setText((agePref.equals("0")) ? null : agePref);*/
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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

  	     txt.setHint(AdresseLocation);
  	    }
  	    catch (IOException e) {  

  	     e.printStackTrace();
  	    } 


  	   }

  	  };

  	 };
}