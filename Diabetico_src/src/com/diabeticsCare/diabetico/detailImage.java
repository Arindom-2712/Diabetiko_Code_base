package com.diabeticsCare.diabetico;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class detailImage extends Activity implements OnClickListener{
	
	private ImageView setLoc;
	private Button    useLoc;
	static int 		  position;
	static int 		  search;
	public static MultiAutoCompleteTextView   myLocation;
	
	String[] 						locations	= 
		{"Bellandur ","HSR Layout","SilkBoard","Konunkuntey",
			"Bomanahalli","Marathahalli","Domlur","Koramangala"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		setLoc = (ImageView)findViewById(R.id.myloc);
		useLoc = (Button)findViewById(R.id.use_current_Location);
		useLoc.setVisibility(View.GONE);
		myLocation = (MultiAutoCompleteTextView)findViewById(R.id.mylocation);
		myLocation.setThreshold( 1 );
		
		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,android.R.layout.simple_list_item_1,locations);

		myLocation.setAdapter(adapter);
		myLocation.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		setLoc.setOnClickListener(this);
		useLoc.setOnClickListener(this);

		Intent i = getIntent();

		position = i.getExtras().getInt("id");
		search   = i.getExtras().getInt("SearchFor");
		
		MyAdapter imageAdapter = new MyAdapter(this);

		ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
		
		imageView.setImageResource( imageAdapter.mItems.get(position).drawableId );    
		
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		imageView.startAnimation(myFadeInAnimation);
		
		TextView cat = (TextView) findViewById(R.id.category);
		cat.setText(imageAdapter.mItems.get(position).name);
		  
		getActionBar().setTitle("	Your Location");
	}
	public boolean onCreateOptionsMenu(Menu menu) {
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
	public  int location = 0;
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( v == setLoc )
		{
			if( 1 == search)
			{
				Intent i = new Intent(getApplicationContext(), Alternative_Search.class);
				i.putExtra("loc", 1);
				startActivity(i);
			}
			else if( 2 == search)
			{
				Intent i = new Intent(getApplicationContext(), SearchDietician.class);
				i.putExtra("loc", 1);
				startActivity(i);
			}
			else if( 0 == search )
			{
				Intent i = new Intent(getApplicationContext(), searchResults.class);
				i.putExtra("id", 0);
				i.putExtra("loc", 1);
				startActivity(i);
			}
			else if( 3 == search )
			{
				Intent i = new Intent(getApplicationContext(), searchResults.class);
				i.putExtra("id", 3);
				i.putExtra("loc", 1);
				startActivity(i);
			}
			
		}
		else if( v == useLoc )
		{
			if( MainActivity.resultLoc.isEmpty() )
			{
				Toast.makeText(getApplicationContext(), " Could Not Detect Your Location.. Tell Us Where You Are",
						Toast.LENGTH_LONG).show();
				return;
			}
			Intent i = new Intent(getApplicationContext(), searchResults.class);
			i.putExtra("id", position);
			i.putExtra("loc", 2);
			startActivity(i);
		}
	}

}
