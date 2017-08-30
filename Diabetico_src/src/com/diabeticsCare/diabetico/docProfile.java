package com.diabeticsCare.diabetico;



import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.diabeticsCare.diabetico.ScrollViewX.OnScrollViewListener;

public class docProfile extends Activity implements OnClickListener{
	private ImageView 	docPP;
	private TextView  	docLocation;
	private TextView  	docNumber;
	private TextView  	docName;
	private TextView  	docDegree;
	private TextView  	docExper;
	private TextView	fees;
	private Button    	CallDoc;
	private ListView 	list;
	testListAdapter 	lviewAdapter;
	private static int DOC_PRACTICE_IDENTIFY = 3;
	
	private String		docClinics[] 	= new String[]{};
	private String      clinicLocs[] 	= new String[]{};
	private String		clinicTmings[] 	= new String[]{};
	
	public static int 	position;
	public static ArrayList< HashMap< String, String > > docWishList = new ArrayList<HashMap<String, String>>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.doc_profile);

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
		docLocation = (TextView)findViewById(R.id.labLocation);
		docName     = (TextView)findViewById(R.id.Doctor);
		docDegree	= (TextView)findViewById(R.id.Degree);
		docExper	= (TextView)findViewById(R.id.experience);
		fees		= (TextView)findViewById(R.id.fees);

		CallDoc.setOnClickListener(this);
		
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

		docClinics 		= searchResults.docList.get(position).get("clinic").split(",");
		clinicLocs    	= searchResults.docList.get(position).get("loc").split("/");
		clinicTmings 	= searchResults.docList.get(position).get("Timings").split("/");
		
		/*StringBuilder allClinincs 		= new StringBuilder();
		StringBuilder allClinicLocs		= new StringBuilder();
		StringBuilder allClinicTimings 	= new StringBuilder();*/
		
		if( null == searchResults.bitMaps.get( position ) )
			docPP.setImageResource(R.drawable.blank_pp);
		else
			docPP.setImageBitmap(searchResults.bitMaps.get( position ) ) ;
		
		docExper.setText( searchResults.docList.get(position).get("Exp") + "	Years Experience"); /* Pass actlly is YOex */
		
		/*if( searchResults.isAvl.get(position ).contains("Available"))
		{
			docNumber.setTextColor( Color.parseColor("#4CAF50") );
		}
		else
		{
			
		}*/
		docNumber.setTextColor( Color.parseColor("#87CEFA") );
		docNumber.setText( searchResults.docList.get(position).get("Day"));
		fees.setText(searchResults.docList.get(position).get("Fees"));
		docName.setText( searchResults.docList.get(position).get("name") );
		docDegree.setText( searchResults.docList.get(position).get("qual") );
		
		/*for( int numTests = 0; numTests < docClinics.length;++numTests )
		{
			allClinincs.append( docClinics[ numTests ]  );
			allClinicLocs.append(clinicLocs[numTests ]  );
			allClinicTimings.append(clinicTmings[numTests] );
		}*/
		
		lviewAdapter = new testListAdapter(this, docClinics, clinicLocs, clinicTmings,DOC_PRACTICE_IDENTIFY );
		
		String primaryLoc[] = new String[]{};
		primaryLoc = searchResults.docList.get(position).get("loc").split("/");
		
		docLocation.setText(primaryLoc[0] +"\n"+
				searchResults.docList.get(position).get("distance") );

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
			HashMap<String, String> docWish = new HashMap<String, String>();

			docWish.put("name",		searchResults.docList.get(position).get("name") ) ;
			docWish.put("location", 	searchResults.docList.get(position).get("loc") );
			docWish.put("Availablity", searchResults.docList.get(position).get("Day") );
			docWish.put("qual", searchResults.docList.get(position).get("qual") );
			docWish.put("clinics", searchResults.docList.get(position).get("clinic") );
			docWish.put("clinicLoc", searchResults.docList.get(position).get("loc") );
			docWish.put("clinicTime", searchResults.docList.get(position).get("Timings") );
			docWish.put("Distance", searchResults.docList.get(position).get("distance") );
			docWish.put("Experience", searchResults.docList.get(position).get("Exp") );
			docWish.put("Fees", searchResults.docList.get(position).get("Fees") );
			docWish.put("pos", 	""+position);
			docWish.put("status", "wish");
			if( !docWishList.contains(docWish))
			{
				docWishList.add(docWish);	
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
		if ( CallDoc == v )
		{
			try
			{
				Intent CallIntent = new Intent( Intent.ACTION_CALL );
				CallIntent.setData(Uri.parse("tel:"+ 
				searchResults.docList.get(position).get("number").toString().trim() ) );
				startActivity( CallIntent );
				
				try
				{
					Thread.sleep(3000);
				}
				catch( Exception e )
				{
					
				}

				new AlertDialog.Builder(docProfile.this)  
				.setMessage("Have You Got Your Appointment? Help Us Remind You..")  
				.setCancelable(false) 
				.setIcon(R.id.icon)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialog, int which)   
				    {   
				    	Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointment.class ) ;
				    	addAppointmentIntent.putExtra("DocName", searchResults.docList.get(position).get("name") );
				    	addAppointmentIntent.putExtra("DocLocation",searchResults.docList.get(position).get("loc") );
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
