package com.android.orange;

import com.android.orange.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyEmailDemo extends Activity  {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String[] recipients = new String[]{"mymail@email.com", "",};
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Votre ami vous inviter à tuiliser l'app" +
				"bla bla blou bla" +
				"bliii");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail client :"));
		finish();*/
		Intent emailIntent2 = new Intent(android.content.Intent.ACTION_SEND);

		String[] recipients2 = new String[]{"...", "",};
		emailIntent2.putExtra(android.content.Intent.EXTRA_EMAIL, recipients2);
		emailIntent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation personnelle");
		emailIntent2.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml("<b><i>"+"Bonjour,"+"</i></b><br><br><b><i>"+"J'ai le" +
				"plaisir de vous inviter à télécharger l'application" +
				""+"</i></b><br><br><b><i>"+"reportn"));
		emailIntent2.setType("text/html");
		startActivity(Intent.createChooser(emailIntent2, "Applications d'envoie :"));
		finish();
		
	

	
	}
}