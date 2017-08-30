package com.diabeticsCare.diabetico;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import com.diabeticsCare.diabetico.R.menu;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class MainActivity extends Activity  {


	public static 	String 			resultLoc 			= "";
	private		 	String 			mPhoneNumber;
	private 		String 			imei; 
	private 		String 			simSerialNumber;

	AutoCompleteTextView 		mSearch;
	String[] 						languages	= 
		{"Doc ","Urine Test","Blood Sugar","Excercise","Diabetes","Clinic","Services","Doctor"};
	private Tracker tracker;
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker =EasyTracker.getTracker();

		Intent i = getIntent();


		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_TITLE);
		addTabs(actionBar);

		final GetMyCurrentLocation getMyLoc = new GetMyCurrentLocation( resultLoc );
		
		/* if (!startService()) {
			 Toast.makeText(MainActivity.this, "Cannot Find Location ",
                     Toast.LENGTH_LONG).show();
         } else {
             Toast.makeText(MainActivity.this, "Service Started",
                     Toast.LENGTH_LONG).show();
         }*/

		TelephonyManager tMgr 	= (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		mPhoneNumber 			= tMgr.getLine1Number();
		imei 					= tMgr.getDeviceId();
		simSerialNumber 		= tMgr.getSimSerialNumber();      
		mSearch					= (AutoCompleteTextView)findViewById(R.id.search);
		mSearch.setThreshold(1);

		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,android.R.layout.simple_list_item_1,languages);

		mSearch.setAdapter(adapter);
		//mSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		GridView gridview = (GridView) findViewById(R.id.mygrid);   
		gridview.setAdapter(new MyAdapter(this)); 
		gridview.setBackgroundColor(Color.WHITE);

		if( resultLoc.isEmpty() )
		{
			if( isOnline() )
				getMyLoc.execute();
			else
				Toast.makeText(getApplicationContext(), " You are Offline ", 
						Toast.LENGTH_SHORT).show();
		}

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				tracker.trackEvent("Buttons Category", ""+position, "", 0L);
				if( isOnline() )
				{
					getMyLoc.cancel(true);
					if( 2 == position )
					{
						Intent iDietician = new Intent( getApplicationContext(), SearchDietician.class );
						iDietician.putExtra("loc", 2);
						startActivity( iDietician );
					}
					else if( 1 == position )
					{
						Intent iAlternative = new Intent( getApplicationContext(), Alternative_Search.class );
						iAlternative.putExtra("loc", 2);
						startActivity( iAlternative );
					}
					else if( 0 == position )
					{
						Intent i = new Intent(getApplicationContext(), searchResults.class);
						i.putExtra("id", position);
						i.putExtra("loc", 2); /* 2 is to Use Current Location */
						startActivity(i);
					}
					else if( 3 == position )
					{
						Intent i = new Intent(getApplicationContext(), searchResults.class);
						i.putExtra("id", position);
						i.putExtra("loc", 2);
						startActivity(i);
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), 
							"NetWork Not Connected !!", Toast.LENGTH_LONG).show();
				}
			}
		});	
	}

	public String getMyData()
	{
		return simSerialNumber;
	}

	public void addTabs( ActionBar bar )
	{
		bar.addTab(bar.newTab()
				.setText("Home")			
				.setTabListener(new TabListener<Home>(
						this, "Home", Home.class)));

		bar.addTab(bar.newTab()
				.setText("Profile")			
				.setTabListener(new TabListener<UserProfile>(
						this, "Profile", UserProfile.class)));


		bar.addTab(bar.newTab()
				.setText("Bookings")
				.setTabListener(new TabListener<Bookings>(
						this, "Bookings", Bookings.class)));


	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Analytics
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method
	}

	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}
		public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state.  If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
				ft.hide(mFragment);
				ft.commit();
			}
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.show(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.hide(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isOnline() 
	{
		//Getting the ConnectivityManager.
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		//Getting NetworkInfo from the Connectivity manager.
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		//If I received an info and isConnectedOrConnecting return true then there is an Internet connection.
		if (netInfo != null && netInfo.isConnectedOrConnecting()) 
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		/*MenuItem item = menu.findItem(R.id.action_search);
		 SearchView search = (SearchView) item.getActionView();
		 search.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));*/

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
		case R.id.item5:

			String shareBody = "Download From : www.diabetco.com";	
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);		
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, " I have found my diabetico Specialist in secs:");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			sharingIntent.setType("text/plain");
			startActivity( Intent.createChooser(sharingIntent, "Share via") );
			return true;
		case R.id.item6:

			Intent marketIntent = new Intent();
			marketIntent.setAction(Intent.ACTION_VIEW);
			marketIntent.setData(Uri.parse("market://search?q=foo"));
			PackageManager pm = getPackageManager();
			List<ResolveInfo> list = pm.queryIntentActivities(marketIntent, 0);
			if( list.isEmpty() )
				Toast.makeText(getApplicationContext(),"Not Yet in The Market !", 
						Toast.LENGTH_SHORT).show();
			try
			{
				marketIntent.setData(Uri.parse("market://details?id=" + getPackageName()));
				startActivity( marketIntent );
			}
			catch( ActivityNotFoundException e)
			{
				Toast.makeText(getApplicationContext(),"No such app found !", 
						Toast.LENGTH_SHORT).show();
			}
			return true;	
		default:
			return super.onOptionsItemSelected( menu );
		}
	}

	class GetMyCurrentLocation extends AsyncTask< String, String, String >
	{
		String locs = "";
		private ProgressDialog pDialog;
		public GetMyCurrentLocation( String resL)
		{
			this.locs = resL;
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			
			Looper.prepare();
			try
			{
				AppLocationService appLocationService = new AppLocationService( getApplicationContext() );

				Location gpsLocation = appLocationService
						.getLocation(LocationManager.PASSIVE_PROVIDER); /****TODO : Why ?? **/
				if (gpsLocation != null) {
					double latitude  = gpsLocation.getLatitude();
					double longitude = gpsLocation.getLongitude();

					Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
					List<Address> addresses = null;
					try {
						addresses = gcd.getFromLocation(latitude, longitude, 2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (addresses.size() > 0) 
						resultLoc = (addresses.get(0).getSubLocality()) +","+(addresses.get(0).getLocality());

				} else {
					showSettingsAlert();
				} 
			}
			catch( Exception exp)
			{
				System.out.println(" Could not locate U...*****"+exp);
				pDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Could Not Locate You", Toast.LENGTH_SHORT).show();
				showSettingsAlert();
			}
			Looper.loop();
			return resultLoc;

		}

		@Override
		protected void onPostExecute( String Result)
		{
			super.onPostExecute(Result);
			Result = " Address Detected";
			Log.v("RESULT", "Location Detected");
		}

		public void showSettingsAlert() {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MainActivity.this);
			alertDialog.setTitle("SETTINGS");
			alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
			alertDialog.setPositiveButton("Settings",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					MainActivity.this.startActivity(intent);
				}
			});
			alertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alertDialog.show();
		}

	}
}



