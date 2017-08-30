package com.diabeticsCare.diabetico;

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
import android.view.Window;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlternativeProfile extends Activity implements OnClickListener{

	private ImageView 	AlternativePP;
	private TextView  	AlternativeLocation;
	private TextView  	AlternativeNumber;
	private TextView  	AlternativeName;
	private TextView  	AlternativeDegree;
	private TextView  	AlternativeExper;
	private TextView	Alternativefees;
	private Button    	CallAlternative;
	private ListView 	list;
	private ListView 	labdoclist;
	private TextView    practice;
	private TextView    Test;
	testListAdapterAlterative 	lviewAdapter;
	testListAdapterAlterative 	lviewAdapter1;
	


	private String		AlternativeCenters[] 			= new String[]{};
	private String		AlternativeDocs[] 				= new String[]{};
	private String		AlternativeServices[] 			= new String[]{};


	public static int 	position;

	public void onCreate( Bundle savedInstanceState )
	{

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.alternativeprofile);

		ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.docProfile);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		final ColorDrawable cd = new ColorDrawable( getResources()
				.getColor(R.color.theme) );
		getActionBar().setBackgroundDrawable(cd);
		cd.setAlpha(0);

		AlternativePP 			= (ImageView)findViewById( R.id.fullimage );
		AlternativeNumber 		= (TextView)findViewById(R.id.phone);
		CallAlternative   		= ( Button )findViewById(R.id.Call);
		list 					= (ListView)findViewById(R.id.listViewTest);
		AlternativeLocation 	= (TextView)findViewById(R.id.labLocation);
		AlternativeName     	= (TextView)findViewById(R.id.Doctor);
		AlternativeDegree		= (TextView)findViewById(R.id.Degree);
		AlternativeExper		= (TextView)findViewById(R.id.experience);
		Alternativefees			= (TextView)findViewById(R.id.fees);
		labdoclist  			= (ListView) findViewById(R.id.listViewTest1);
		practice				= (TextView)findViewById(R.id.Practice);
		Test					= (TextView)findViewById(R.id.test);

		CallAlternative.setOnClickListener(this);

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





		//AlternativeDocFees      = Alternative_Search.alternativeList.get(position).get("DocFees").split(",");

		if( null == Alternative_Search.bitMapsOfAlternatives.get( position ) )
			AlternativePP.setImageResource(R.drawable.blank_pp);
		else
			AlternativePP.setImageBitmap(Alternative_Search.bitMapsOfAlternatives.get( position ) ) ;

		AlternativeExper.setText( Alternative_Search.alternativeList.get(position).get("Exp") + "	Years Experience"); /* Pass actlly is YOex */

		/*if( searchResults.isAvl.get(position ).contains("Available"))
		{
			docNumber.setTextColor( Color.parseColor("#4CAF50") );
		}
		else
		{

		}*/
		AlternativeNumber.setTextColor( Color.parseColor("#87CEFA") );
		AlternativeNumber.setText( Alternative_Search.alternativeList.get(position).get("Day"));
		Alternativefees.setText(Alternative_Search.alternativeList.get(position).get("Fees"));
		AlternativeName.setText( Alternative_Search.alternativeList.get(position).get("name") );
		AlternativeDegree.setText( Alternative_Search.alternativeList.get(position).get("qual") );

		/*for( int numTests = 0; numTests < docClinics.length;++numTests )
		{
			allClinincs.append( docClinics[ numTests ]  );
			allClinicLocs.append(clinicLocs[numTests ]  );
			allClinicTimings.append(clinicTmings[numTests] );
		}*/

		if(  Alternative_Search.alternativeList.get(position).get("Type").toString().contains("Doc") )
		{
			practice.setText("Also practices at:");
			Test.setVisibility(View.GONE);
			
			AlternativeCenters 		= Alternative_Search.alternativeList.get(position).get("clinic").split(",");
			String      CenterLocs[]					= new String[ AlternativeCenters.length ];
			String      AlternativeLocs[]						= new String[ AlternativeCenters.length ];

			lviewAdapter = new testListAdapterAlterative(this, AlternativeCenters,CenterLocs,
					AlternativeLocs,3);			
			list.setAdapter(lviewAdapter);
			setListViewHeightBasedOnChildren(list);	
		}
		else if( Alternative_Search.alternativeList.get(position).get("Type").toString().contains("Center") )
		{
			practice.setText("Consulting Doctors:");
			Test.setVisibility(View.VISIBLE);
			Test.setText("Services And Packages:");
			
			AlternativeDocs    				= Alternative_Search.alternativeList.get(position).get("docs").split(",");
			String AlternativeTmings[] 		= new String[ AlternativeDocs.length];
			String AlternativeDocFees[]		= new String[ AlternativeDocs.length];

			String vv = Alternative_Search.alternativeList.get(position).get("services");
			AlternativeServices  				= Alternative_Search.alternativeList.get(position).get("services").split(",");
			String AlternativeServicesFees[] 	= new String[ AlternativeServices.length ];
			String Packages[] 					= new String[ AlternativeServices.length ];

			lviewAdapter = new testListAdapterAlterative(this, AlternativeServices,AlternativeServicesFees,
					Packages,2);			
			list.setAdapter(lviewAdapter);
			setListViewHeightBasedOnChildren(list);	

			lviewAdapter1 = new testListAdapterAlterative(this, AlternativeDocs, AlternativeTmings,AlternativeDocFees,1);
			labdoclist.setAdapter(lviewAdapter1);	
			setListViewHeightBasedOnChildren(labdoclist);
		}

		AlternativeLocation.setText( Alternative_Search.alternativeList.get(position).get("loc") +"\n"+
				Alternative_Search.alternativeList.get(position).get("distance") );
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
			HashMap<String, String> AlteranteWish = new HashMap<String, String>();

			AlteranteWish.put("name",Alternative_Search.alternativeList.get(position).get("name") ) ;
			AlteranteWish.put("phone", Alternative_Search.alternativeList.get(position).get("phone") );
			AlteranteWish.put("Type", Alternative_Search.alternativeList.get(position).get("Type") );
			AlteranteWish.put("loc", Alternative_Search.alternativeList.get(position).get("loc") );
			AlteranteWish.put("clinic", Alternative_Search.alternativeList.get(position).get("clinic") );
			AlteranteWish.put("number", Alternative_Search.alternativeList.get(position).get("number") );
			AlteranteWish.put("qual", Alternative_Search.alternativeList.get(position).get("qual") );
			AlteranteWish.put("docs", Alternative_Search.alternativeList.get(position).get("Docs") );
			AlteranteWish.put("services", Alternative_Search.alternativeList.get(position).get("services") );
			AlteranteWish.put("Fees", Alternative_Search.alternativeList.get(position).get("Fees") );
			AlteranteWish.put("Exp", Alternative_Search.alternativeList.get(position).get("Exp") ); 
			AlteranteWish.put("Timings",Alternative_Search.alternativeList.get(position).get("Timings") );
			AlteranteWish.put("Confirmation", Alternative_Search.alternativeList.get(position).get("Confirmation") );
			AlteranteWish.put("Day",Alternative_Search.alternativeList.get(position).get("Day") );
			AlteranteWish.put("Time",Alternative_Search.alternativeList.get(position).get("Time") );
			
			AlteranteWish.put("pos", 	""+position);
			AlteranteWish.put("status", "wish");
			if( !docProfile.docWishList.contains(AlteranteWish))
			{
				docProfile.docWishList.add(AlteranteWish);	
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
		if ( CallAlternative == v )
		{
			try
			{
				Intent CallIntent = new Intent( Intent.ACTION_CALL );
				CallIntent.setData(Uri.parse("tel:"+ 
						Alternative_Search.alternativeList.get(position).get("phone").toString().trim() ) );
				startActivity( CallIntent );

				try
				{
					Thread.sleep(3000);
				}
				catch( Exception e )
				{

				}

				new AlertDialog.Builder(AlternativeProfile.this)  
				.setMessage("Have You Got Your Appointment? Help Us Remind You..")  
				.setCancelable(false) 
				.setIcon(R.id.icon)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int which)   
					{   
						Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointmentAlternative.class ) ;
						addAppointmentIntent.putExtra("DocName", Alternative_Search.alternativeList.get(position).get("name") );
						addAppointmentIntent.putExtra("DocLocation",Alternative_Search.alternativeList.get(position).get("loc") );
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
