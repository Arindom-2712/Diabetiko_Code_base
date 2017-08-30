package com.diabeticsCare.diabetico;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class searchResults extends Activity {

	JSONParser 				jsonParser 		= new JSONParser();
	private 				String 			jsonResult;
	public 					ImageView 		sorry;
	private static 			ListView 		listView; 
	JSONArray 				users 			= null;
	public 	static String 	resultLoc 		= "";
	public  static Bitmap 	bm 				= null;
	private 				ProgressDialog 	pDialog;
	public 	static			String			AvalableTiming; 


	ArrayList<HashMap< String, String > > 		usersList; /* Wromgly Named ...Should Be GetServerConnectiion */
	static final ArrayList<String> isAvl = new ArrayList<String>();
	static final ArrayList< HashMap< String, String > > docList = new ArrayList<HashMap<String, String>>();
	static final ArrayList< HashMap< String, String > > labList = new ArrayList<HashMap<String, String>>();

	private static final String url_display_doc  	= "http://www.diabetiko.com/search_doc.php";
	private static final String url_display_lab  	= "http://www.diabetiko.com/search_lab.php";
	private static final String url_display_ayu  	= "http://www.diabetiko.com/search_ayu.php";
	private static final String url_display_hos  	= "http://www.diabetiko.com/search_hos.php";
	private static final String url_display_diet 	= "http://www.diabetiko.com/search_diet.php";
	private static final String url_dispaly_folder_doc 	= "http://www.diabetiko.com/Images_doc/";
	private static final String url_dispaly_folder_lab 	= "http://www.diabetiko.com/Images_lab/";
	private static final int 	USER_LOCATION		= 1;
	private static final int 	CURRENT_LOCATION	= 2;
	private static final String TAG 				= "ERROR";
	private static final int 	IO_BUFFER_SIZE 		= 4 * 1024; /* 4KB */
	public static ArrayList< Bitmap > bitMaps 		= new ArrayList<Bitmap>();
	public static ArrayList< String > howFar		= new ArrayList<String>();
	public static String[] distances 				= new String[]{"",};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.res);


		/* Scrict mode for Smooth ListView Scrolling */
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().
				detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}



		listView 			= (ListView) findViewById(android.R.id.list); 
		sorry 				= (ImageView)findViewById(R.id.sorry);
		usersList 			= new ArrayList<HashMap<String, String>>();


		sorry.setVisibility(  View.INVISIBLE );

		Intent i = getIntent();

		int position = i.getExtras().getInt("id");
		int loc      = i.getExtras().getInt("loc");

		String myLocation = "";

		if( USER_LOCATION == loc )
		{
			myLocation = detailImage.myLocation.getText().toString();
		}
		else if( CURRENT_LOCATION == loc )
		{
			try {

				myLocation = MainActivity.resultLoc;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		switch( position )
		{
		case 0:
			if( "" == myLocation )
			{
				Intent iDietician = new Intent( getApplicationContext(), detailImage.class );
				iDietician.putExtra("SearchFor", 0);
				startActivity( iDietician );
			}
			getActionBar().setTitle("	Doctors : ");
			getActionBar().setSubtitle("	"+myLocation);
			new getUserList( url_display_doc, myLocation ).execute();
			break;
		case 1:
			getActionBar().setTitle("	Ayurveda : ");
			getActionBar().setSubtitle("	"+myLocation);
			new getUserList( url_display_ayu, myLocation ).execute();
			break;
		case 2:
			getActionBar().setTitle("	Diet : " );
			getActionBar().setSubtitle("	"+myLocation);
			new getUserList( url_display_diet, myLocation ).execute();
			break;
		case 3:
			if( "" == myLocation )
			{
				Intent iDietician = new Intent( getApplicationContext(), detailImage.class );
				iDietician.putExtra("SearchFor", 3);
				startActivity( iDietician );
			}
			getActionBar().setTitle("	Diagnostics : ");
			getActionBar().setSubtitle("	"+myLocation);
			new getUserList( url_display_lab, myLocation ).execute();
			break;
		case 4:
			getActionBar().setTitle("	Diabetico Genie : " );
			getActionBar().setSubtitle("	"+myLocation);
			new getUserList( url_display_hos, myLocation ).execute();
			break;
		}

		View header = View.inflate( this, R.layout.header, null);	
		listView.addHeaderView(header);

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			private int mLastFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (mLastFirstVisibleItem < firstVisibleItem) {
					if (getActionBar().isShowing()) {
						getActionBar().hide();
					}
				}

				if (mLastFirstVisibleItem > firstVisibleItem) {
					if (!getActionBar().isShowing()) {
						getActionBar().show();
					}
				}
				mLastFirstVisibleItem = firstVisibleItem;
			}
		});
	}

	/********* Establishes Connection asynchronously and Calls List Drawer on Post Execution *
	 * TODO: ListDrawer has to be asynchronous itself.. ******************  :-(
	 * */
	class getUserList extends AsyncTask<String, String, String> {

		private String location = "";
		private String url      = "";

		public getUserList(String url_type, String myLocation) {
			// TODO Auto-generated constructor stub
			this.location = myLocation;
			this.url = url_type;
		}
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(searchResults.this);
			pDialog.setMessage("Populating Your Criteria..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		/* TODO: This Method is Too Kacha Pacha...Need Cleansing *************
		 	SET A timeout Handling */
		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost( url );

			/***************SET A timeout Handling ************/
			HttpParams httpParams = new BasicHttpParams();
			int imeout = (int) (3 * DateUtils.SECOND_IN_MILLIS);
			HttpConnectionParams.setConnectionTimeout(httpParams, imeout);
			HttpConnectionParams.setSoTimeout(httpParams, imeout);
			HttpClient client = new DefaultHttpClient(httpParams);

			try {
				HttpResponse response = httpclient.execute(httppost);

				if( null == response  )
				{
					Toast.makeText(getApplicationContext(),
							"Timed Out !! Please Try Again", Toast.LENGTH_LONG).show();
					return null;
				}
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				Toast.makeText(getApplicationContext(),
						"Oops !! Connection Problem", Toast.LENGTH_LONG).show();
				sorry.setVisibility(  View.VISIBLE );
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						"Oops !! I/O Error", Toast.LENGTH_LONG).show();
				sorry.setVisibility(  View.VISIBLE );
				e.printStackTrace();
				return null;
			}
			catch( Exception e)
			{
				Toast.makeText(getApplicationContext(),
						"Slow Internet ! Please  Try Again", Toast.LENGTH_LONG).show();
				client.getConnectionManager().shutdown();
			}
			return null;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
				return null;
			}
			return answer;
		}
		@Override
		protected void onPostExecute(String result) {

			pDialog.dismiss();
			try {
				ListDrwaer( url );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**** The Primary Function Responsible for Drawing the Search List on the Screen after getting server Data
	 * TODO : Has to be shifted to a thread to lighten Main UI Thread **DONE
	 * @throws InterruptedException
	 */
	public void ListDrwaer( String url_type) throws InterruptedException {
		final ArrayList<String> imageUrls = new ArrayList<String>();

		docList.clear();
		labList.clear();		
		howFar.clear();
		
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			/*Toast.makeText(getApplicationContext(),
					""+jsonResponse, Toast.LENGTH_LONG).show();*/
			if( url_type.contains("search_doc") )
			{
				JSONArray jsonMainNode = jsonResponse.optJSONArray("doc_info");
				if( 0 == jsonMainNode.length() || null == jsonResponse )
				{
					Toast.makeText(getApplicationContext(),
							"Cannot Communicate", Toast.LENGTH_LONG).show();
					return;
				}
				new calculateDistance( jsonMainNode, url_type ).execute();

				for (int i = 0; i < jsonMainNode.length(); i++) {

					HashMap<String, String> createDoc = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					createDoc.put("name",jsonChildNode.getString("Name") ) ;
					createDoc.put("loc", jsonChildNode.getString("location") );
					createDoc.put("clinic", jsonChildNode.getString("clinic") );
					createDoc.put("number", jsonChildNode.getString("phone") );
					createDoc.put("qual", jsonChildNode.getString("qualification") );
					createDoc.put("Fees", jsonChildNode.getString("Fees") );
					createDoc.put("Exp", jsonChildNode.getString("password") ); /* Actually Represents Years Of Experience*/
					createDoc.put("Timings",jsonChildNode.getString("Timings") );
					createDoc.put("Day",jsonChildNode.getString("Day") );
					createDoc.put("Time",jsonChildNode.getString("Time") );
					
					while( true )
						try
					{ 
							howFar.get( i );
							break;
					}
					catch( Exception e) /* List Out Of Bound Exception *****/
					{
						continue;
					}
					
					createDoc.put("distance", howFar.get(i) );
					String imageUrl = url_dispaly_folder_doc+jsonChildNode.getString("DocId")+".jpg";
					imageUrls.add( imageUrl );
					createDoc.put("image", imageUrl);
					docList.add( createDoc );
				}
			}
			else if( url_type.contains("search_lab") )
			{
				JSONArray jsonMainNode = jsonResponse.optJSONArray("lab_info");
				if( 0 == jsonMainNode.length() || null == jsonResponse )
				{
					Toast.makeText(getApplicationContext(),
							"Cannot Communicate", Toast.LENGTH_LONG).show();
					return;
				}
				new calculateDistance( jsonMainNode, url_type ).execute();

				for (int i = 0; i < jsonMainNode.length(); i++) {

					HashMap<String, String> createLab = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					createLab.put("name",jsonChildNode.getString("Name") ) ;
					createLab.put("number", jsonChildNode.getString("phone") );
					createLab.put("spel", jsonChildNode.getString("__specialization") );
					createLab.put("price", jsonChildNode.getString("price"));
					createLab.put("Exp",jsonChildNode.getString("password"));
					createLab.put("loc", jsonChildNode.getString("labLocation") );
					createLab.put("Day",jsonChildNode.getString("Day") );
					createLab.put("Time",jsonChildNode.getString("Time") );
					createLab.put("homecollection",jsonChildNode.getString("homecollection") );
					createLab.put("labDos",jsonChildNode.getString("labDocs"));
					createLab.put("labDocTimings",jsonChildNode.getString("labDocTimings"));
					createLab.put("labDocFees",jsonChildNode.getString("labDocFees"));
					
					/**** To Ensure Do In BackGround Populate the Distance List 
					 * TODO: Should not be a blocking call, Should be handled in the same way as
					 * Images****/
					while( true )
						try
					{ 
							howFar.get( i );
							break;
					}
					catch( Exception e) /* List Out Of Bound Exception *****/
					{
						continue;
					}
					createLab.put("distance", howFar.get(i) );

					String imageUrl = url_dispaly_folder_lab+jsonChildNode.getString("ID")+".jpg";
					imageUrls.add( imageUrl );
					createLab.put("image", imageUrl);
					labList.add( createLab );
				}
			}
			/************** Loading the Images corresponding to the URLs fetched from Database 
			 *  Too Much :-( ****************
			 */
			new getAllImage( imageUrls ).execute();

		} catch (JSONException e) {
			sorry.setVisibility(  View.VISIBLE );
			Toast.makeText(getApplicationContext(),
					"Oops !! No Response", Toast.LENGTH_LONG).show();
			return;
		}
		if( docList.isEmpty() && labList.isEmpty() )
		{
			sorry.setVisibility(  View.VISIBLE );
			Toast.makeText(getApplicationContext(),
					"Oops !! No results Match your Search", Toast.LENGTH_LONG).show();
		}
		else
		{
			if( url_type.contains("search_doc") )
			{
				isAvl.clear();
				listView.setAdapter( new ImageAdapter(this,docList,url_type) );	
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Intent appInfo = new Intent(getApplicationContext(), docProfile.class);

						appInfo.putExtra("position", position );

						/*** The following while loop is to avoid ArrayOutOfBound Exception ***/
						while( imageUrls.size() != bitMaps.size( ) && position > bitMaps.size( ) )
						{
							continue;
						}
						try
						{
							startActivity(appInfo);
						}
						catch( ArrayIndexOutOfBoundsException e)
						{
							Toast.makeText(getApplicationContext(), " Jus A Sec !" , Toast.LENGTH_LONG).show();
						}

					}
				});
			}
			else if( url_type.contains("search_lab") )
			{
				isAvl.clear();
				listView.setAdapter( new ImageAdapter(this,labList,url_type) );	
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Intent appInfo = new Intent(getApplicationContext(), diagnotic_profile.class);

						appInfo.putExtra("position", position );

						while( imageUrls.size() != bitMaps.size( ) && position > bitMaps.size( ) )
						{
							continue;
						}
						try
						{
							startActivity(appInfo);
						}
						catch( ArrayIndexOutOfBoundsException e)
						{
							Toast.makeText(getApplicationContext(), " Jus A Sec !" , Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}
	}	

	public  class calculateDistance extends AsyncTask<String, String, String>
	{
		JSONArray jsonMainNode = null;
		String    mUrl         = "";
		public calculateDistance( JSONArray jsonNode, String url )
		{
			this.jsonMainNode = jsonNode;
			this.mUrl		  = url;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			double lat = 0;
			double lon = 0;
			double latitude = 0;
			double longitude = 0;
			String addressStr="";
			for( int i = 0; i < jsonMainNode.length(); i++)
			{
				try {
					
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
					if( mUrl.contains("search_lab") )
						addressStr = jsonChildNode.getString("labLocation");
					else if( mUrl.contains("search_doc") )
						addressStr = jsonChildNode.getString("location");
					
					String currenrtAddr = MainActivity.resultLoc;

					Geocoder geoCoder = new Geocoder(searchResults.this, Locale.getDefault());

					try {
						List<Address> address =
								geoCoder.getFromLocationName(addressStr, 1); 
						if (address.size() >  0) {
							lat = address.get(0).getLatitude();
							lon = address.get(0).getLongitude();
						}
						List<Address> curAd = geoCoder.getFromLocationName(currenrtAddr, 1);
						if( curAd.size() > 0 )
						{
							latitude = curAd.get(0).getLatitude();
							longitude = curAd.get(0).getLongitude();
						}

						Location loc1 = new Location("");
						loc1.setLatitude(latitude);
						loc1.setLongitude(longitude);

						Location loc2 = new Location("");
						loc2.setLatitude(lat);
						loc2.setLongitude(lon);
						
						float dist = loc1.distanceTo(loc2)/1000;
						
						
						String distance = ""+dist;
						String distancetrimmed[] = new String[]{};
						distancetrimmed = distance.split("\\.");
						String distaneAfterDecimal = " ~ "+distancetrimmed[0]+"."+
						distancetrimmed[1].substring(0, 1)+"kms";
						
						
						howFar.add( distaneAfterDecimal );

					} catch (IOException e) { 
						howFar.add(" Unknown Distance");
						e.printStackTrace(); 
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute( String Result)
		{
			Result = " Address Detected";
			Log.v("RESULT", "Distance Calculated");
		}
	}
	
	/******************Get the Images( Profile Pics Asynchronously)*******************
	 * TODO: Ensuring All theImages Are Loaded before set Adapter is called  ********/
	public static class getAllImage extends AsyncTask<String, String, String>
	{
		ArrayList< String > urls = new ArrayList<String>();

		public getAllImage( ArrayList< String > urlList ) {
			/* Flushing the bitMap Array of Previous records ************************/
			bitMaps.clear();

			this.urls = urlList;
		}
		@Override
		protected String doInBackground(String... arg0) {
			for( int i = 0 ; i < urls.size() ; i++ )
			{
				bitMaps.add(loadBitmap( urls.get( i ) ) );
			}
			return null;
		}
	}

	/** Function: Viewholder Adapter For Search List
	 *  @author Arindam*****/
	static class ImageAdapter extends BaseAdapter
	{
		private Context context;
		private String searchUrl;
		
		private ArrayList<HashMap<String, String>> 	MyArr 				= new ArrayList<HashMap<String, String>>();
		public final Boolean 						isScrolling 		= true;

		/* ViewHolder Class To Ensure Less Flickering/Hanging... While Scrolling Through Search Results 
		 * Best Practice **************************/
		public static  class ViewHolder
		{
			public TextView text;
			public TextView text2;
			public TextView text3;
			public TextView text4;
			public TextView text5;
			public ImageView image;
		}

		public ImageAdapter(Activity context, String[] names) {
			super();
			this.context = context; 
		}

		public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list, String url)
		{
			context 	= c;
			MyArr 		= list;
			searchUrl   = url;
		}
		@Override
		public int getCount() {
			return MyArr.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@SuppressLint("SimpleDateFormat") public boolean checkAvailibility( String Day, String Time )
		{
			int iAllTimings = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			Date d = new Date();
			String dayOfTheWeek = sdf.format(d);
			String subStringDay = "";
			
			
			int hrs  = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int min   = Calendar.getInstance().get(Calendar.MINUTE);
			
			int time = hrs*100 + min;
			
			int Date   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int avlTimings = 0;
			String splitDay[] 		= new String[]{};
			String splitTime[]  	= new String[]{};
			
			
			splitDay			= Day.split(",");
			splitTime			= Time.split(",");
			
			try
			{
				for( iAllTimings = 0; iAllTimings < splitDay.length;++iAllTimings)
				{
					subStringDay = dayOfTheWeek.substring(0, 2 );
					if( splitDay[iAllTimings].contains( subStringDay ) )
					{
						try
						{
							String splitFromTo[] 	= new String[2];
							splitFromTo = splitTime[ iAllTimings ].split("-");
							if( splitFromTo[1].contains(":") )
							{
								String splitHrMin[] = splitFromTo[1].split(":");
								avlTimings = ( Integer.parseInt(splitHrMin[0]) * 100 ) + 
										(Integer.parseInt( splitHrMin[0] ) ) ;
							}
							else
							{
								avlTimings = Integer.parseInt( splitFromTo[1] ) * 100;
							}
							if( 0 != Integer.parseInt(splitFromTo[0] ) && 
									time <= avlTimings )
									{
										
										break;
									}
						}
						catch( Exception e )
						{
							e.printStackTrace();
						}
					}	
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			if( iAllTimings == splitDay.length )
			{
				try
				{
					AvalableTiming = "Closed For Today";
				}
				catch( Exception e){
				
				}
				return false;
			}
			else
			{
				AvalableTiming = "Available Today";
				return true;
			}
		}
		@SuppressLint("InflateParams")/*TODO: new API vs old API*/
		@Override
		public  View getView(int position, View convertView, ViewGroup parent) {

			View 			rowView 	= convertView;
			ViewHolder 		viewHolder;
			boolean 		bAvailable  = false;
			
			LayoutInflater inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (rowView == null) {

				rowView 			= inflater.inflate(R.layout.searchres, null);
				viewHolder 			= new ViewHolder();
				viewHolder.text 	= (TextView)rowView.findViewById(R.id.firstLine);
				viewHolder.text2   	= (TextView)rowView.findViewById(R.id.secondLine);
				viewHolder.text3   	= (TextView)rowView.findViewById(R.id.thirdLine);
				viewHolder.text5    = (TextView)rowView.findViewById(R.id.homecollection);
				viewHolder.text4   	= (TextView)rowView.findViewById(R.id.qual);
				viewHolder.image 	= (ImageView)rowView.findViewById(R.id.icon);

				rowView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();

			}
			try
			{
				if( null != bitMaps.get( position ) )
				{
					viewHolder.image.setImageBitmap( bitMaps.get( position ) );
				}
				else
				{
					viewHolder.image.setImageResource(R.drawable.blank_pp);
				}

			} catch (Exception e) {
				viewHolder.image.setImageResource(R.drawable.blank_pp);
			}
			if( searchUrl.contains("search_doc") )
			{
				viewHolder.text5.setVisibility(View.GONE);
				viewHolder.text.setText( MyArr.get(position).get("name"));
				viewHolder.text4.setText(MyArr.get(position).get("qual"));
				
				bAvailable = checkAvailibility( MyArr.get(position).get("Day"),MyArr.get(position).get("Time") );
				if( bAvailable )
				{
					viewHolder.text2.setTextColor( Color.parseColor("#4CAF50") );
					viewHolder.text2.setText( AvalableTiming );
					isAvl.add( AvalableTiming );
					
				}
				else
				{
					viewHolder.text2.setTextColor( Color.parseColor("#87CEFA") );
					viewHolder.text2.setText( AvalableTiming );
					isAvl.add( AvalableTiming );
				}
				String primaryLoc[] = MyArr.get(position).get("loc").split("/"); /* Showing only The Primary Location */
				
				viewHolder.text3.setText("\n"+ primaryLoc[0] +","+
						MyArr.get(position).get("distance")+"\n"+"\n"+"Rs: "+
						MyArr.get(position).get("Fees") );
			}
			else if ( searchUrl.contains("search_lab"))
			{
				viewHolder.text.setText( MyArr.get(position).get("name"));
				bAvailable = checkAvailibility( MyArr.get(position).get("Day"),MyArr.get(position).get("Time") );
				if( bAvailable )
				{
					AvalableTiming = "Open Today";
					viewHolder.text2.setTextColor( Color.parseColor("#4CAF50") );
					viewHolder.text2.setText( AvalableTiming );
					isAvl.add( AvalableTiming );
					
				}
				else
				{
					viewHolder.text2.setTextColor( Color.parseColor("#87CEFA") );
					viewHolder.text2.setText( AvalableTiming );
					isAvl.add( AvalableTiming );
				}
				viewHolder.text3.setText(MyArr.get(position).get("loc") +",\n"+
						MyArr.get(position).get("distance") );
				if( 1 == Integer.parseInt(MyArr.get(position).get("homecollection")))
				{
					viewHolder.text5.setTextColor( Color.parseColor("#4CAF50") );
					viewHolder.text5.setText( "Home Collection Available" );
				}
				else
				{
					viewHolder.text5.setTextColor( Color.parseColor("#FF0000") );
					viewHolder.text5.setText( "Home Collection Not Available" );
				}
			}
			Log.v("","***"+ MyArr.get(position).get("spel"));

			return rowView;
		}	
	}

	/*** Loading bitMap Images Stored in dataBases ()***************************************/
	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();
			final byte[] data = dataStream.toByteArray();
			BitmapFactory.Options options = new BitmapFactory.Options(); 
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
		} catch (IOException e) {

			Log.e(TAG, "Could not load Bitmap from: " + url);
			closeStream(in);
			closeStream(out);
			return null;
		} 
		finally 
		{
			closeStream(in);
			closeStream(out);
		}
		return bitmap;
	}

	public static void closeStream( Closeable stream )
	{
		if( null != stream )
			try {
				stream.close();
			} catch (IOException e) {
				android.util.Log.e(TAG, "Could not close stream", e);
			}
	}

	public static void copy( InputStream in, OutputStream o) throws IOException
	{
		byte[] b = new byte[ IO_BUFFER_SIZE ];
		int read;
		while((read = in.read(b)) != -1 )
		{
			o.write( b, 0, read );
		}
	}
}


