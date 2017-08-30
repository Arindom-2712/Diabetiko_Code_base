/**** Detail Information about one Particular Lab ************
 * @Arindam **********/
package com.diabeticsCare.diabetico;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.diabeticsCare.diabetico.ScrollViewX.OnScrollViewListener;


public class diagnotic_profile extends Activity implements OnClickListener{
	private ImageView 	docPP;
	private TextView  	docNumber;
	private TextView  	labTest;
	private TextView  	Fees;
	private Button   	CallDoc;
	private static int  LAB_DOC_IDENTIFY  = 1;
	private static int  LAB_TEST_IDENTIFY = 2;
	private TextView  	labLocation;
	private TextView	labName;
	private String		labTests[] 		= new String[]{};
	private String		labTmings[] 	= new String[]{"",};
	private String 		testPrices[]	= new String[]{};
	private String		labDoctors[]    = new String[]{};
	private String      labDoctorFees[]	= new String[]{};
	private String      labDocTime[]	= new String[]{};
	private TextView    labEstd;
	private static int position;

	private ListView list;
	private ListView labdoclist;
	testListAdapter lviewAdapter;
	testListAdapter lviewAdapterLabDocs;
	
	public static ArrayList< HashMap< String, String > > labWishList = new ArrayList<HashMap<String, String>>();
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.lab_profile);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		docPP 		= (ImageView)findViewById( R.id.fullimage );
		docNumber 	= (TextView)findViewById(R.id.phone);
		labTest     = (TextView)findViewById(R.id.special);
		Fees		= (TextView)findViewById(R.id.fees);
		labName     = (TextView)findViewById(R.id.Doctor);
		labLocation = (TextView)findViewById(R.id.labLocation);
		CallDoc   	= ( Button )findViewById(R.id.Call);
		list 		= (ListView) findViewById(R.id.listViewTest);
		labdoclist  = (ListView) findViewById(R.id.listViewTest1);
		labEstd		= (TextView)findViewById(R.id.experience);
		CallDoc.setOnClickListener(this);


		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

		Intent i = getIntent();


		position = i.getExtras().getInt("position");
		--position;


		ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.docProfile);
		//getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		final ColorDrawable cd = new ColorDrawable( getResources()
                .getColor(R.color.theme) );
		getActionBar().setBackgroundDrawable(cd);
		cd.setAlpha(0);

		
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
	
		labTests = searchResults.labList.get(position).get("spel").split(",");
		testPrices = searchResults.labList.get(position).get("price").split(",");
		labDoctors = searchResults.labList.get(position).get("labDos").split(",");
		labDoctorFees = searchResults.labList.get(position).get("labDocFees").split(",");
		labDocTime = searchResults.labList.get(position).get("labDocTimings").split(",");;
		
		StringBuilder allTests = new StringBuilder();
		
		if( null == searchResults.bitMaps.get( position ) )
			docPP.setImageResource(R.drawable.blank_pp);
		else
			docPP.setImageBitmap(searchResults.bitMaps.get( position ) ) ;
		labLocation.setText(searchResults.labList.get(position).get("loc")+"\n"+
		searchResults.labList.get(position).get("distance"));
		
		
		lviewAdapter = new testListAdapter(this, labTests, testPrices,labTmings,LAB_TEST_IDENTIFY);	
		list.setAdapter(lviewAdapter);	
		setListViewHeightBasedOnChildren(list);
		
		lviewAdapterLabDocs = new testListAdapter(this, labDoctors, labDocTime,labDoctorFees,LAB_DOC_IDENTIFY);
		labdoclist.setAdapter(lviewAdapterLabDocs);	
		setListViewHeightBasedOnChildren(labdoclist);
		
		labName.setText(searchResults.labList.get(position).get("name"));
		docNumber.setText(searchResults.labList.get(position).get("Day"));
		labEstd.setText( searchResults.labList.get(position).get("Exp")+" Years of Service");
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
			HashMap<String, String> labWish = new HashMap<String, String>();

			labWish.put("name",searchResults.labList.get(position).get("name") ) ;
			labWish.put("number", searchResults.labList.get(position).get("number") );
			labWish.put("spel", searchResults.labList.get(position).get("spel") );
			labWish.put("price", searchResults.labList.get(position).get("price"));
			labWish.put("Experience",searchResults.labList.get(position).get("Exp"));
			labWish.put("location", searchResults.labList.get(position).get("loc") );
			labWish.put("Day",searchResults.labList.get(position).get("Day") );
			labWish.put("Time",searchResults.labList.get(position).get("Time") );
			labWish.put("homecollection",searchResults.labList.get(position).get("homecollection") );
			labWish.put("labDos",searchResults.labList.get(position).get("labDos"));
			labWish.put("Distance", searchResults.labList.get(position).get("distance") );
			labWish.put("Availablity", searchResults.labList.get(position).get("Day") );
			labWish.put("labDocTimings",searchResults.labList.get(position).get("labDocTimings"));
			labWish.put("labDocFees",searchResults.labList.get(position).get("labDocFees"));
			labWish.put("pos", 	""+position);
			labWish.put("status", "wish");
			labWish.put("type", "lab");
			if( !docProfile.docWishList.contains(labWish))
			{
				docProfile.docWishList.add(labWish);	
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
						searchResults.labList.get(position).get("number").toString().trim() ) );
				startActivity( CallIntent );
				
				try
				{
					Thread.sleep(3000);
				}
				catch( Exception e )
				{
					
				}

				new AlertDialog.Builder(diagnotic_profile.this)  
				.setMessage("Have You Got Your Appointment? Help Us Remind You..")  
				.setCancelable(false) 
				.setIcon(R.id.icon)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialog, int which)   
				    {   
				    	Intent addAppointmentIntent = new Intent( getApplicationContext(),AddAppointmentLab.class ) ;
				    	addAppointmentIntent.putExtra("DocName", searchResults.labList.get(position).get("name") );
				    	addAppointmentIntent.putExtra("DocLocation",searchResults.labList.get(position).get("loc") );
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
					isPhoneCalling = false;
				}

			}
		}
	}
}
