package com.diabeticsCare.diabetico;

import java.util.ArrayList;
import java.util.HashMap;

import com.diabeticsCare.diabetico.ScrollViewX.OnScrollViewListener;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DieticianProfile extends Activity implements OnClickListener{
	
	private ImageView 	dieticianPP;
	private TextView  	dieticianLocation;
	private TextView  	dieticianNumber;
	private TextView  	dieticianName;
	private TextView  	dieticianDegree;
	private TextView  	dieticianExper;
	private TextView	dieticianfees;
	private Button    	Calldietician;
	private ListView 	list;
	testListAdapter 	lviewAdapter;
	private static int 	DIETICIAN_PRACTICE_IDENTIFY = 3;
	
	private String		dieticianClinics[] 			= new String[]{};
	private String      dieticianclinicLocs[] 		= new String[]{};
	private String		dieticianclinicTmings[] 	= new String[]{};
	
	public static int 	position;
	//public static ArrayList< HashMap< String, String > > docWishList = new ArrayList<HashMap<String, String>>();
	
	public void onCreate( Bundle savedInstanceState )
	{

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.dieticianprofile);

		ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.docProfile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		final ColorDrawable cd = new ColorDrawable( getResources()
                .getColor(R.color.theme) );
		getActionBar().setBackgroundDrawable(cd);
		cd.setAlpha(0);
		
		dieticianPP 		= (ImageView)findViewById( R.id.fullimage );
		dieticianNumber 	= (TextView)findViewById(R.id.phone);
		Calldietician   	= ( Button )findViewById(R.id.Call);
		list 				= (ListView)findViewById(R.id.listViewTest);
		dieticianLocation 	= (TextView)findViewById(R.id.labLocation);
		dieticianName     	= (TextView)findViewById(R.id.Doctor);
		dieticianDegree		= (TextView)findViewById(R.id.Degree);
		dieticianExper		= (TextView)findViewById(R.id.experience);
		dieticianfees		= (TextView)findViewById(R.id.fees);

		Calldietician.setOnClickListener(this);
		
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

		Intent i = getIntent();


		position = i.getExtras().getInt("position");
		--position;

		getActionBar().setTitle("");
		//getActionBar().setSubtitle("	"+searchResults.docList.get(position).get("name"));
		
		scrollView.setOnScrollViewListener(new OnScrollViewListener() {

			@Override
			public void onScrollChanged(ScrollViewX v, int l, int t, int oldl, int oldt) {

				cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
			}

			private int getAlphaforActionBar(int scrollY) {
				int minDist = 0,maxDist = 650;
				if(scrollY>maxDist){ 
					return 255;
				}
				else if(scrollY<minDist){
					return 0;
				}
				else {
					int alpha = 0;
					alpha = (int)  ((255.0/maxDist)*scrollY);
					return alpha;
				}
			}
		});

		dieticianClinics 		= SearchDietician.dieticianList.get(position).get("clinic").split(",");
		dieticianclinicLocs    	= SearchDietician.dieticianList.get(position).get("loc").split("/");
		dieticianclinicTmings 	= SearchDietician.dieticianList.get(position).get("Timings").split("/");
		
		if( null == SearchDietician.bitMapsOfDieticin.get( position ) )
			dieticianPP.setImageResource(R.drawable.blank_pp);
		else
			dieticianPP.setImageBitmap(SearchDietician.bitMapsOfDieticin.get( position ) ) ;
		
		dieticianExper.setText( SearchDietician.dieticianList.get(position).get("Exp") + "	Years Experience"); /* Pass actlly is YOex */
		
		/*if( searchResults.isAvl.get(position ).contains("Available"))
		{
			docNumber.setTextColor( Color.parseColor("#4CAF50") );
		}
		else
		{
			
		}*/
		dieticianNumber.setTextColor( Color.parseColor("#87CEFA") );
		dieticianNumber.setText( SearchDietician.dieticianList.get(position).get("Day"));
		dieticianfees.setText(SearchDietician.dieticianList.get(position).get("Fees"));
		dieticianName.setText( SearchDietician.dieticianList.get(position).get("name") );
		dieticianDegree.setText( SearchDietician.dieticianList.get(position).get("qual") );
		
		/*for( int numTests = 0; numTests < docClinics.length;++numTests )
		{
			allClinincs.append( docClinics[ numTests ]  );
			allClinicLocs.append(clinicLocs[numTests ]  );
			allClinicTimings.append(clinicTmings[numTests] );
		}*/
		
		lviewAdapter = new testListAdapter(this, dieticianClinics, dieticianclinicLocs, 
				dieticianclinicTmings,DIETICIAN_PRACTICE_IDENTIFY );
		
		dieticianLocation.setText( SearchDietician.dieticianList.get(position).get("loc") +"\n"+
				SearchDietician.dieticianList.get(position).get("distance") );

		list.setAdapter(lviewAdapter);

		setListViewHeightBasedOnChildren(list);	
	}
	/* We could not use two scrolling simulteniuosly.We will have get total 
	 * length of ListView and expand listview with the total height
	 * @arindam 
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	    
	    int totalHeight = 0;
	    int c = listAdapter.getCount();
	    int measuredWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY); 
	    int measuredHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
	    listView.measure(measuredWidth, measuredHeight); 
	    for (int i = 0; i < c; i++) {
	        
	    	listView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		    		MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	    	listView.measure(0, 0);
	        totalHeight += listView.getMeasuredHeight();
	    }

	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight
	            + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	      }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.wishlist, menu);
		return true;
	}

	public boolean onOptionsItemSelected( MenuItem menu )
	{
		switch ( menu.getItemId() ) 
		{
		case android.R.id.home:
			finish();
			return true;
		case R.id.wish_btn:
			HashMap<String, String> dieticianWish = new HashMap<String, String>();

			dieticianWish.put("name",		SearchDietician.dieticianList.get(position).get("name") ) ;
			dieticianWish.put("location", 	SearchDietician.dieticianList.get(position).get("loc") );
			dieticianWish.put("Availablity", SearchDietician.dieticianList.get(position).get("Day") );
			dieticianWish.put("qual", SearchDietician.dieticianList.get(position).get("qual") );
			dieticianWish.put("clinics", SearchDietician.dieticianList.get(position).get("clinic") );
			dieticianWish.put("clinicLoc", SearchDietician.dieticianList.get(position).get("loc") );
			dieticianWish.put("clinicTime", SearchDietician.dieticianList.get(position).get("Timings") );
			dieticianWish.put("Distance", SearchDietician.dieticianList.get(position).get("distance") );
			dieticianWish.put("Experience", SearchDietician.dieticianList.get(position).get("Exp") );
			dieticianWish.put("Fees", SearchDietician.dieticianList.get(position).get("Fees") );
			dieticianWish.put("pos", 	""+position);
			dieticianWish.put("status", "wish");
			if( !docProfile.docWishList.contains(dieticianWish))
			{
				docProfile.docWishList.add(dieticianWish);	
				Toast.makeText(getApplicationContext(), "Added To Wish List", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "AlreadyAdded", Toast.LENGTH_SHORT).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected( menu );

		}
	}
	
	@Override
	public void onClick(View v) {
		if ( Calldietician == v )
		{
			try
			{
				Intent CallIntent = new Intent( Intent.ACTION_CALL );
				CallIntent.setData(Uri.parse("tel:"+ 
						SearchDietician.dieticianList.get(position).get("number").toString().trim() ) );
				startActivity( CallIntent );
				
				try
				{
					Thread.sleep(3000);
				}
				catch( Exception e )
				{
					
				}

				new AlertDialog.Builder(DieticianProfile.this)  
				.setMessage("Have You Got Your Appointment? Help Us Remind You..")  
				.setCancelable(false) 
				.setIcon(R.id.icon)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialog, int which)   
				    {   
				    	Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointmentDietician.class ) ;
				    	addAppointmentIntent.putExtra("DocName", SearchDietician.dieticianList.get(position).get("name") );
				    	addAppointmentIntent.putExtra("DocLocation",SearchDietician.dieticianList.get(position).get("loc") );
				    	addAppointmentIntent.putExtra("position",position );
				    	startActivity( addAppointmentIntent );
				        dialog.cancel();  
				    }  
				})    
				.setNegativeButton("No", new DialogInterface.OnClickListener() {  
				      public void onClick(DialogInterface dialog, int which)   
				      {    
				         dialog.cancel();  
				      }  
				}).show();	
			}
			catch( android.content.ActivityNotFoundException ex )
			{
				Toast.makeText( this,
						"Call failed, please try again later!"+ex, Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class PhoneCallListener extends PhoneStateListener {

		private boolean isPhoneCalling = false;

		String LOG_TAG = "LOGGING 123";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				Log.i(LOG_TAG, "OFFHOOK");
				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {
				if (isPhoneCalling) {
					/* Asks For FeedBack ***********************************************/

					isPhoneCalling = false;
				}

			}
		}
	}

}
