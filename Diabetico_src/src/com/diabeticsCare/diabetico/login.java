package com.diabeticsCare.diabetico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class login extends Activity{

	public void onCreate( Bundle savedInstanceate )
	{
		super.onCreate(savedInstanceate);
		setContentView(R.layout.login);

		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("	Login");

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected( MenuItem menu )
	{
		switch ( menu.getItemId() ) 
		{
		case R.id.item1:
			startActivity(new Intent(getApplicationContext(), login.class));
			return true;
		case R.id.item4:
			startActivity(new Intent(getApplicationContext(), diagnosticsignup.class));
			return true;
		case R.id.item3:
			startActivity(new Intent(getApplicationContext(), docsignup.class));
			return true;
		case R.id.action_settings:
			Toast.makeText(getApplicationContext(),"Under Construction", 
					Toast.LENGTH_SHORT).show();	
			return true;
		default:
			return super.onOptionsItemSelected( menu );

		}
	}
}
