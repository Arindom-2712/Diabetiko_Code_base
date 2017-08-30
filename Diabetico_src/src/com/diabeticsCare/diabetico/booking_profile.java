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
import android.view.Window;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class booking_profile extends Activity implements OnClickListener{

	private ImageView 	docPP;
	private TextView  	docLocation;
	private TextView  	docNumber;
	private TextView  	docName;
	private TextView  	docDegree;
	private TextView  	docExper;
	private Button    	CallDoc;
	private ListView 	list;
	private ListView 	list1;
	private TextView	BookStatus;
	testListAdapter 	lviewAdapter;
	testListAdapter 	lviewAdapter1;
	private TextView	fees;
	private TextView	labtest;
	private TextView    docsList;

	private String		docClinics[] 			= new String[]{};
	private String      clinicLocs[] 			= new String[]{};
	private String		clinicTmings[] 			= new String[]{};
	
	private String		dietClinics[] 			= new String[]{};
	private String      dietClinicLocs[] 		= new String[]{};
	private String		dietClinicTmings[] 		= new String[]{};

	private String		labDocs[] 				= new String[]{};
	private String      labDocTimings[] 		= new String[]{};
	private String		labDocPrice[] 			= new String[]{};
	private String		labDocTests[] 			= new String[]{};
	private String		labDocTestPrice[] 		= new String[]{};
	private String 		labTmings[]				= new String[]{};

	public static int 	position;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.bookingprofile);

		ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.docProfile);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		final ColorDrawable cd = new ColorDrawable( getResources()
				.getColor(R.color.theme) );
		getActionBar().setBackgroundDrawable(cd);
		cd.setAlpha(0);

		docPP 		= (ImageView)findViewById( R.id.fullimage );
		docNumber 	= (TextView)findViewById(R.id.phone);
		CallDoc   	= ( Button )findViewById(R.id.Call);
		list 		= (ListView)findViewById(R.id.listViewTest);
		list1		= (ListView)findViewById(R.id.listViewTest1);
		docLocation = (TextView)findViewById(R.id.labLocation);
		docName     = (TextView)findViewById(R.id.Doctor);
		docDegree	= (TextView)findViewById(R.id.Degree);
		docExper	= (TextView)findViewById(R.id.experience);
		BookStatus  = (TextView)findViewById(R.id.bookStatus);
		fees		= (TextView)findViewById(R.id.fees);
		labtest		= (TextView)findViewById(R.id.labTests);
		docsList	= (TextView)findViewById(R.id.test);
		CallDoc.setOnClickListener(this);

		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

		Intent i = getIntent();


		position = i.getExtras().getInt("position");

		getActionBar().setTitle("");

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
		if( "doc" == docProfile.docWishList.get(position).get("type") )
		{
			labtest.setVisibility(View.GONE);
			docClinics 		= docProfile.docWishList.get(position).get("clinics").split(",");
			clinicLocs    	= docProfile.docWishList.get(position).get("clinicLoc").split("/");
			clinicTmings 	= docProfile.docWishList.get(position).get("clinicTime").split("/");

			docExper.setText( docProfile.docWishList.get(position).get("Experience") + "	Years Experience"); 
			fees.setText( docProfile.docWishList.get(position).get("Fees") );

			if( null == docProfile.docWishList.get(position).get("BookTime") )
			{
				BookStatus.setTextColor( Color.parseColor("#9E9E9E") );
				BookStatus.setText(" CALL AND BOOK NOW");
			}
			else
			{
				BookStatus.setTextColor( Color.parseColor("#87CEFA") );
				BookStatus.setText( docProfile.docWishList.get(position).get("BookTime") );
			}
			docNumber.setTextColor( Color.parseColor("#87CEFA") );
			docNumber.setText( docProfile.docWishList.get(position).get("Availablity"));	
			docName.setText( docProfile.docWishList.get(position).get("name") );
			docDegree.setText( docProfile.docWishList.get(position).get("qual") );


			lviewAdapter = new testListAdapter(this, docClinics, clinicLocs, clinicTmings,3 );		
			list.setAdapter(lviewAdapter);
			setListViewHeightBasedOnChildren(list);
			
			docLocation.setText(docProfile.docWishList.get(position).get("location") +"\n"+
					docProfile.docWishList.get(position).get("Distance") );
		}
		else if( "lab" == docProfile.docWishList.get(position).get("type") )
		{
			fees.setVisibility(View.GONE);
			docsList.setTextColor( Color.parseColor("#87CEFA") );
			docsList.setText("DOCTORS:");
			labDocs = docProfile.docWishList.get(position).get("labDos").split(",");
			labDocTimings = docProfile.docWishList.get(position).get("labDocTimings").split(",");
			labDocPrice = docProfile.docWishList.get(position).get("labDocFees").split(",");
			labDocTests = docProfile.docWishList.get(position).get("spel").split(",");
			labDocTestPrice = docProfile.docWishList.get(position).get("price").split(",");

			docExper.setText( docProfile.docWishList.get(position).get("Experience") + "	Years of Service"); 
			fees.setText( docProfile.docWishList.get(position).get("Fees") );

			if( null == docProfile.docWishList.get(position).get("BookTime") )
			{
				BookStatus.setTextColor( Color.parseColor("#9E9E9E") );
				BookStatus.setText(" CALL AND BOOK NOW");
			}
			else
			{
				BookStatus.setTextColor( Color.parseColor("#87CEFA") );
				BookStatus.setText( docProfile.docWishList.get(position).get("BookTime") );
			}
			docNumber.setTextColor( Color.parseColor("#9E9E9E") );
			docNumber.setText( docProfile.docWishList.get(position).get("Availablity"));	
			docName.setText( docProfile.docWishList.get(position).get("name") );
			docDegree.setText( docProfile.docWishList.get(position).get("qual") );


			lviewAdapter = new testListAdapter(this, labDocs, labDocTimings, labDocPrice,1 );
			list.setAdapter(lviewAdapter);	
			setListViewHeightBasedOnChildren(list);

			lviewAdapter1 = new testListAdapter(this, labDocTests, labDocTestPrice,labTmings,2);
			list1.setAdapter(lviewAdapter1);	
			setListViewHeightBasedOnChildren(list1);


			docLocation.setText(docProfile.docWishList.get(position).get("location") +"\n"+
					docProfile.docWishList.get(position).get("Distance") );

		}
		else if( "dietician" == docProfile.docWishList.get(position).get("type") )
		{
			labtest.setVisibility(View.GONE);
			dietClinics 		= docProfile.docWishList.get(position).get("clinics").split(",");
			dietClinicLocs    	= docProfile.docWishList.get(position).get("clinicLoc").split("/");
			dietClinicTmings 	= docProfile.docWishList.get(position).get("clinicTime").split("/");

			docExper.setText( docProfile.docWishList.get(position).get("Experience") + "	Years Experience"); 
			fees.setText( docProfile.docWishList.get(position).get("Fees") );

			if( null == docProfile.docWishList.get(position).get("BookTime") )
			{
				BookStatus.setTextColor( Color.parseColor("#9E9E9E") );
				BookStatus.setText(" CALL AND BOOK NOW");
			}
			else
			{
				BookStatus.setTextColor( Color.parseColor("#87CEFA") );
				BookStatus.setText( docProfile.docWishList.get(position).get("BookTime") );
			}
			docNumber.setTextColor( Color.parseColor("#87CEFA") );
			docNumber.setText( docProfile.docWishList.get(position).get("Availablity"));	
			docName.setText( docProfile.docWishList.get(position).get("name") );
			docDegree.setText( docProfile.docWishList.get(position).get("qual") );


			lviewAdapter = new testListAdapter(this, dietClinics, dietClinicLocs, dietClinicTmings,3 );		
			list.setAdapter(lviewAdapter);
			setListViewHeightBasedOnChildren(list);
			
			docLocation.setText(docProfile.docWishList.get(position).get("location") +"\n"+
					docProfile.docWishList.get(position).get("Distance") );
		}
		
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
		getMenuInflater().inflate(R.menu.remove, menu);
		return true;
	}

	public boolean onOptionsItemSelected( MenuItem menu )
	{
		switch ( menu.getItemId() ) 
		{
		case android.R.id.home:
			finish();
			return true;
		case R.id.delete_btn:
			HashMap<String, String> docWish = new HashMap<String, String>();

			if( !docProfile.docWishList.contains(docWish))
			{
				docProfile.docWishList.remove(position);	
				Intent intent = new Intent(this,MainActivity.class);
				intent.putExtra("fragmentNumber",1); //for example    
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "Removed..", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Already Removed", Toast.LENGTH_SHORT).show();
			}
			//finish();
			return true;
		default:
			return super.onOptionsItemSelected( menu );

		}
	}

	@Override
	public void onClick(View v) {
		if ( CallDoc == v )
		{
			try
			{
				Intent CallIntent = new Intent( Intent.ACTION_CALL );
				if( "lab" != docProfile.docWishList.get(position).get("type"))
					CallIntent.setData(Uri.parse("tel:"+ 
							searchResults.docList.get(position).get("number").toString().trim() ) );
				else
					CallIntent.setData(Uri.parse("tel:"+ 
							docProfile.docWishList.get(position).get("number").toString().trim() ) );
				startActivity( CallIntent );

				try
				{
					Thread.sleep(3000);
				}
				catch( Exception e )
				{

				}

				new AlertDialog.Builder(booking_profile.this)  
				.setMessage("Have You Got Your Appointment? Help Us Remind You..")  
				.setCancelable(false) 
				.setIcon(R.id.icon)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int which)   
					{  
						if( "doc" == docProfile.docWishList.get(position).get("type"))
						{
							Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointment.class ) ;
							addAppointmentIntent.putExtra("DocName", searchResults.docList.get(position).get("name") );
							addAppointmentIntent.putExtra("DocLocation",searchResults.docList.get(position).get("loc") );
							addAppointmentIntent.putExtra("position",position );
							startActivity( addAppointmentIntent );
						}
						else if( "lab" == docProfile.docWishList.get(position).get("type") ) /**** Rubbissssh *****/
						{
							Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointmentLab.class ) ;
							addAppointmentIntent.putExtra("DocName", searchResults.labList.get(position).get("name") );
							addAppointmentIntent.putExtra("DocLocation",searchResults.labList.get(position).get("loc") );
							addAppointmentIntent.putExtra("position",position );
							startActivity( addAppointmentIntent );
						}
						else if(  "dietician" == docProfile.docWishList.get(position).get("type") )
						{
							Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointmentDietician.class ) ;
							addAppointmentIntent.putExtra("DocName", SearchDietician.dieticianList.get(position).get("name") );
							addAppointmentIntent.putExtra("DocLocation",SearchDietician.dieticianList.get(position).get("loc") );
							addAppointmentIntent.putExtra("position",position );
							startActivity( addAppointmentIntent );
						}
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
