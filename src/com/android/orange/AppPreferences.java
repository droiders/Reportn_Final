package com.android.orange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.orange.R;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextWatcher;
import android.util.Log;
 
public class AppPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener  {

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9+._%-+]{1,256}" +
	          "@" +
	          "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
	          "(" +
	          "." +
	          "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
	          ")+"
	      );
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.layout.preferences);
   }  


@Override
    protected void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

   @Override
   protected void onPause() {
       super.onPause();

       // Unregister the listener whenever a key changes

getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    

   }

   private boolean checkEmail(String email) {
       return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
   }
   
   boolean check(String inputMail) {   
	    Pattern pattern= Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
	    return pattern.matcher(inputMail).matches();
	}
   @Override
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       //This just calls a function to update the Pref Summary
       Preference pref = findPreference("email");
       
       EditTextPreference editTextPreference=(EditTextPreference)findPreference("email");  
       
     if(!check(editTextPreference.getText())){
    	 
    	 editTextPreference.setText("");
    	 AlertDialog alertDialog = new AlertDialog.Builder(AppPreferences.this).create();
        	alertDialog.setTitle("Erreur...");
        	alertDialog.setMessage("Email non valide");
        	alertDialog.show();
     }
       
     

   }


}
