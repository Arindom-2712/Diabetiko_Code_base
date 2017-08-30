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
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Alternative_Search extends Activity implements OnClickListener{

	private 	ListView 							listView;
	private 	ImageView 							sorry;
	private 	static final int 					USER_LOCATION				= 1;
	private 	static final int 					CURRENT_LOCATION			= 2;
	private 	String 								jsonResult;
	private 	static String 						TAG 						= "AlternativeSearch"; 
	private 	static final String 				url_display_altrnative 		= "http://www.diabetiko.com/search_alternative.php";
	private 	static final int 					IO_BUFFER_SIZE 				= 4 * 1024; /* 4KB */
	private 	static final String 				url_dispaly_folder 			= "http://www.diabetiko.com/Images_alternative/";
	static 		final ArrayList<String> 			isAvlAlternative			= new ArrayList<String>();
	public 		static String						AvalableTiming				="Closed"; 
	public 		static 	ArrayList< String > 		howFarIslternatve			= new ArrayList<String>();
	public 		ArrayList<HashMap<String, String>> 	listOfAlternatives 			= new ArrayList<HashMap<String,String>>();
	public 		static ArrayList< Bitmap > 			bitMapsOfAlternatives 		= new ArrayList<Bitmap>();	
	static 		final ArrayList< HashMap< String, String > > alternativeList 	= new ArrayList<HashMap<String, String>>();


	public void onCreate( Bundle savedInstanceState ){
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
		listOfAlternatives	= new ArrayList<HashMap<String, String>>();


		sorry.setVisibility(  View.INVISIBLE );



		Intent i = getIntent();
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
		if( "" == myLocation )
		{
			Intent iDietician = new Intent( getApplicationContext(), detailImage.class );
			iDietician.putExtra("SearchFor", 1);
			startActivity( iDietician );
		}
		getActionBar().setTitle("	Alternaive Care : ");
		getActionBar().setSubtitle("	"+myLocation);
		new getAlternativesList( url_display_altrnative, myLocation ).execute();


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

	class getAlternativesList extends AsyncTask<String, String, String> {

		private String location = "";
		private String url      = "";
		private ProgressDialog pDialog;


		public getAlternativesList(String url_type, String myLocation) {
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

			pDialog = new ProgressDialog(Alternative_Search.this);
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
				alternativesListDrwaer( url );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void alternativesListDrwaer( String url_type) throws InterruptedException {
		final ArrayList<String> imageUrls = new ArrayList<String>();

		alternativeList.clear();		
		howFarIslternatve.clear();

		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			/*Toast.makeText(getApplicationContext(),
					""+jsonResponse, Toast.LENGTH_LONG).show();*/

			JSONArray jsonMainNode = jsonResponse.optJSONArray("doc_info");
			if( 0 == jsonMainNode.length() || null == jsonResponse )
			{
				Toast.makeText(getApplicationContext(),
						"Cannot Communicate", Toast.LENGTH_LONG).show();
				return;
			}
			new calculateDistanceForAlternatives( jsonMainNode, url_type ).execute();

			for (int i = 0; i < jsonMainNode.length(); i++) {

				HashMap<String, String> createAlternatives = new HashMap<String, String>();

				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				createAlternatives.put("name",jsonChildNode.getString("Name") ) ;
				createAlternatives.put("phone", jsonChildNode.getString("phone") );
				createAlternatives.put("Type", jsonChildNode.getString("Type") );
				createAlternatives.put("loc", jsonChildNode.getString("Location") );
				createAlternatives.put("clinic", jsonChildNode.getString("Centers") );
				createAlternatives.put("number", jsonChildNode.getString("phone") );
				createAlternatives.put("qual", jsonChildNode.getString("Specalization") );
				createAlternatives.put("docs", jsonChildNode.getString("Docs") );
				createAlternatives.put("services", jsonChildNode.getString("Services") );
				createAlternatives.put("Fees", jsonChildNode.getString("Fees") );
				createAlternatives.put("Exp", jsonChildNode.getString("Exp") ); 
				createAlternatives.put("Timings",jsonChildNode.getString("Timings") );
				createAlternatives.put("Confirmation", jsonChildNode.getString("Confirmation") );
				createAlternatives.put("Day",jsonChildNode.getString("Day") );
				createAlternatives.put("Time",jsonChildNode.getString("Time") );

				while( true )
					try
				{ 
						howFarIslternatve.get( i );
						break;
				}
				catch( Exception e) /* List Out Of Bound Exception *****/
				{
					continue;
				}

				createAlternatives.put("distance", howFarIslternatve.get(i) );
				String imageUrl = url_dispaly_folder+jsonChildNode.getString("alternative_ID")+".jpg";
				imageUrls.add( imageUrl );
				createAlternatives.put("image", imageUrl);
				alternativeList.add( createAlternatives );
			}


			/************** Loading the Images corresponding to the URLs fetched from Database 
			 *  Too Much :-( ****************
			 */
			new getAllImageOfAlternatives( imageUrls ).execute();

		} catch (JSONException e) {
			sorry.setVisibility(  View.VISIBLE );
			Toast.makeText(getApplicationContext(),
					"Oops !! No Response", Toast.LENGTH_LONG).show();
			return;
		}
		if( alternativeList.isEmpty()  )
		{
			sorry.setVisibility(  View.VISIBLE );
			Toast.makeText(getApplicationContext(),
					"Oops !! No results Match your Search", Toast.LENGTH_LONG).show();
		}
		else
		{

			isAvlAlternative.clear();
			listView.setAdapter( new ImageAdapterForalternatives(this,alternativeList,url_type) );	
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent appInfo = new Intent(getApplicationContext(), AlternativeProfile.class);

					appInfo.putExtra("position", position );

					/*** The following while loop is to avoid ArrayOutOfBound Exception ***/
					while( imageUrls.size() != bitMapsOfAlternatives.size( ) && position > bitMapsOfAlternatives.size( ) )
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

	/** Function: Viewholder Adapter For Search List
	 *  @author Arindam*****/
	static class ImageAdapterForalternatives extends BaseAdapter
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
			public ImageView imge1;
		}

		public ImageAdapterForalternatives(Activity context, String[] names) {
			super();
			this.context = context; 
		}

		public ImageAdapterForalternatives(Context c, ArrayList<HashMap<String, String>> list, String url)
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


			int time  = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int Date   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

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
							if( 0 != Integer.parseInt(splitFromTo[0] ) && 
									time <= Integer.parseInt(splitFromTo[1] ) )
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

				rowView 			= inflater.inflate(R.layout.searchresalternative, null);
				viewHolder 			= new ViewHolder();
				viewHolder.text 	= (TextView)rowView.findViewById(R.id.firstLine);
				viewHolder.text2   	= (TextView)rowView.findViewById(R.id.secondLine);
				viewHolder.text3   	= (TextView)rowView.findViewById(R.id.thirdLine);
				viewHolder.text5    = (TextView)rowView.findViewById(R.id.homecollection);
				viewHolder.text4   	= (TextView)rowView.findViewById(R.id.qual);
				viewHolder.image 	= (ImageView)rowView.findViewById(R.id.icon);
				viewHolder.imge1    = (ImageView)rowView.findViewById(R.id.imageCenter);

				rowView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();

			}
			try
			{
				if(  "Doc" == MyArr.get(position).get("Type") )
				{
					viewHolder.imge1.setVisibility(View.GONE);
					viewHolder.image.setVisibility(View.VISIBLE);
					if( null != bitMapsOfAlternatives.get( position ) )
					{
						viewHolder.image.setImageBitmap( bitMapsOfAlternatives.get( position ) );
					}
					else
					{
						viewHolder.image.setImageResource(R.drawable.blank_pp);
					}
				}
				else if( "Center" == MyArr.get(position).get("Type") )
				{
					viewHolder.image.setVisibility(View.GONE);
					viewHolder.imge1.setVisibility(View.VISIBLE);
					if( null != bitMapsOfAlternatives.get( position ) )
					{
						viewHolder.imge1.setImageBitmap( bitMapsOfAlternatives.get( position ) );
					}
					else
					{
						viewHolder.imge1.setImageResource(R.drawable.blank_pp);
					}
				}


			} catch (Exception e) {
				viewHolder.image.setImageResource(R.drawable.blank_pp);
			}
			if( searchUrl.contains("search_alternative") )
			{

				viewHolder.text5.setVisibility(View.GONE);
				viewHolder.text.setText( MyArr.get(position).get("name"));
				viewHolder.text4.setText(MyArr.get(position).get("qual"));

				bAvailable = checkAvailibility( MyArr.get(position).get("Day"),MyArr.get(position).get("Time") );
				if( bAvailable )
				{
					viewHolder.text2.setTextColor( Color.parseColor("#4CAF50") );
					viewHolder.text2.setText( AvalableTiming );
					isAvlAlternative.add( AvalableTiming );

				}
				else
				{
					viewHolder.text2.setTextColor( Color.parseColor("#87CEFA") );
					viewHolder.text2.setText( AvalableTiming );
					isAvlAlternative.add( AvalableTiming );
				}
				viewHolder.text3.setText("\n"+MyArr.get(position).get("loc") +","+
						MyArr.get(position).get("distance")+"\n"+"\n"+"Rs: "+
						MyArr.get(position).get("Fees") );
			}

			Log.v("","***"+ MyArr.get(position).get("spel"));

			return rowView;
		}	
	}

	public  class calculateDistanceForAlternatives extends AsyncTask<String, String, String>
	{
		JSONArray jsonMainNode = null;
		String    mUrl         = "";
		public calculateDistanceForAlternatives( JSONArray jsonNode, String url )
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


					addressStr = jsonChildNode.getString("Location");

					String currenrtAddr = MainActivity.resultLoc;

					Geocoder geoCoder = new Geocoder(Alternative_Search.this, Locale.getDefault());

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


						howFarIslternatve.add( distaneAfterDecimal );

					} catch (IOException e) { 
						howFarIslternatve.add(" Unknown Distance");
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

	public static class getAllImageOfAlternatives extends AsyncTask<String, String, String>
	{
		ArrayList< String > urls = new ArrayList<String>();

		public getAllImageOfAlternatives( ArrayList< String > urlList ) {
			/* Flushing the bitMap Array of Previous records ************************/
			bitMapsOfAlternatives.clear();

			this.urls = urlList;
		}
		@Override
		protected String doInBackground(String... arg0) {
			for( int i = 0 ; i < urls.size() ; i++ )
			{
				bitMapsOfAlternatives.add(loadBitmapOfAlternatives( urls.get( i ) ) );
			}
			return null;
		}
	}

	/*** Loading bitMap Images Stored in dataBases ()***************************************/
	public static Bitmap loadBitmapOfAlternatives(String url) {
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
		} finally {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
