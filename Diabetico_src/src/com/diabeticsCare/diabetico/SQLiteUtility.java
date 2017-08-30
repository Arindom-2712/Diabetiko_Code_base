package com.diabeticsCare.diabetico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteUtility extends SQLiteOpenHelper {

	private static final int 	DATABASE_VERSION 	= 1;
	private static final String DATABASE_NAME 		= "BookingDiabetiko";
	private static final String TABLE_BOOKING 		= "Bookings";

	private static final String KEY_ID 			= "id";
	private static final String NAME 			= "name";
	private static final String PH_NO 			= "Phone";
	private static final String AVL				= "Availablity";
	private static final String SPECIALIZATION 	= "specialization";
	private static final String FEES 			= "Fees";
	private static final String CLINICS 		= "clinic";
	private static final String CLINICLOC		= "loc";
	private static final String STATUS 			= "Status";
	private static final String BOOKTIME 		= "BookTime";
	private static final String DISTANCE		= "Distance";
	private static final String TYPE			= "Type";
	private static final String CLINICTIME		= "clinicTime";
	private static final String EXPERIENCE		= "clinicTime";
	private static final String LOCATION		= "Location";
	

	private HashMap<String, String> mhashMap = new HashMap<String, String>();

	public SQLiteUtility( Context context, HashMap<String, String> hashMap ) {
		super(context,DATABASE_NAME , null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.mhashMap = hashMap;
	}

	public SQLiteUtility( Context context) {
		super(context,DATABASE_NAME , null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_BOOKING_TABLE = "CREATE TABLE " + TABLE_BOOKING + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
				+ PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_BOOKING_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);

		// Create tables again
		onCreate(db);
	}
	
	int randomId()
	{
		Random r = new Random();
		int range = 999999999 - 1 + 1;
		int randomNum =  r.nextInt(range) + 1;
		return randomNum;
	}
	// Adding new contact
	void addDataDoc() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, ""+randomId() );
		values.put(NAME, mhashMap.get("name").toString() ); 
		values.put(PH_NO, mhashMap.get("Phone").toString()); 
		values.put(AVL, ""+mhashMap.get("Availablity").toString() );
		values.put(SPECIALIZATION, mhashMap.get("qual").toString() ); 
		values.put(FEES, mhashMap.get("Fees").toString()); 
		values.put(CLINICS, mhashMap.get("clinics").toString()); 
		values.put(CLINICLOC,mhashMap.get("clinicLoc").toString() );
		values.put(STATUS, mhashMap.get("status").toString() ); 
		values.put(BOOKTIME, mhashMap.get("BookTime").toString()); 
		values.put(DISTANCE, mhashMap.get("Distance").toString() ); 
		values.put(TYPE, mhashMap.get("type").toString()); 
		values.put(CLINICTIME, mhashMap.get("clinicTime").toString()); 
		values.put(EXPERIENCE, mhashMap.get("Experience").toString()); 
		values.put(LOCATION, mhashMap.get("loc").toString()); 
		
		db.insert(TABLE_BOOKING, null, values);
		db.close(); 
	}
	
	void addDataLab() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, ""+randomId() );
		values.put("name",mhashMap.get("name").toString() ) ;
		values.put("number", mhashMap.get("number").toString() );
		values.put("spel", mhashMap.get("spel").toString() );
		values.put("price", mhashMap.get("price").toString());
		values.put("Experience",mhashMap.get("Experience").toString());
		values.put("location", mhashMap.get("location").toString() );
		values.put("Day",mhashMap.get("Day").toString() );
		values.put("Time",mhashMap.get("Time") );
		values.put("homecollection",mhashMap.get("homecollection").toString() );
		values.put("labDos",mhashMap.get("labDos").toString());
		values.put("Distance", mhashMap.get("Distance").toString() );
		values.put("Availablity", mhashMap.get("Availablity").toString() );
		values.put("labDocTimings",mhashMap.get("labDocTimings").toString());
		values.put("labDocFees",mhashMap.get("labDocFees").toString());
		values.put("status", mhashMap.get("status").toString());
		values.put("type", mhashMap.get("type").toString()); 
		values.put("BookTime", mhashMap.get("BookTime").toString());
		
		db.insert(TABLE_BOOKING, null, values);
		db.close(); 
	}
	
	void addDataAlternate() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();


		values.put("status", mhashMap.get("status").toString());
		values.put("BookTime", mhashMap.get("BookTime").toString());

		values.put("name",mhashMap.get("name").toString() ) ;
		values.put("phone", mhashMap.get("phone").toString() );
		values.put("Type", mhashMap.get("Type").toString() );
		values.put("location", mhashMap.get("location").toString() );
		values.put("Distance", mhashMap.get("Distance").toString() );
		values.put("clinic", mhashMap.get("clinic") .toString());
		values.put("number", mhashMap.get("number").toString() );
		values.put("qual", mhashMap.get("qual") .toString());
		values.put("docs", mhashMap.get("docs").toString() );
		values.put("services", mhashMap.get("services").toString() );
		values.put("Fees", mhashMap.get("Fees") .toString());
		values.put("Exp", mhashMap.get("Exp").toString() ); 
		values.put("Timings",mhashMap.get("Timings").toString() );
		values.put("Confirmation", mhashMap.get("Confirmation").toString() );
		values.put("Availablity",mhashMap.get("Availablity").toString() );
		values.put("Time",mhashMap.get("Time").toString() );

		values.put("type", mhashMap.get("type").toString());
		
		db.insert(TABLE_BOOKING, null, values);
		db.close(); 
	}
	
	void addDataDietician() {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("status", mhashMap.get("status").toString());
		values.put("BookTime", mhashMap.get("status").toString());

		values.put("name",		mhashMap.get("name").toString() ) ;
		values.put("location", 	mhashMap.get("loc") .toString());
		values.put("Availablity", mhashMap.get("Day").toString() );
		values.put("qual", mhashMap.get("qual").toString() );
		values.put("clinics", mhashMap.get("clinic") .toString());
		values.put("clinicLoc", mhashMap.get("loc").toString() );
		values.put("clinicTime", mhashMap.get("Timings").toString() );
		values.put("Distance", mhashMap.get("distance").toString() );
		values.put("Experience", mhashMap.get("Exp").toString() );
		values.put("Fees", mhashMap.get("Fees").toString() );
		

		values.put("type",mhashMap.get("type").toString());
		
		db.insert(TABLE_BOOKING, null, values);
		db.close(); 
	}
	ArrayList<HashMap<String, String>> getAllData(){
		ArrayList< HashMap< String, String > > WishList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_BOOKING;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int i = 0;
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap< String, String > HMap = new HashMap<String, String>();
				HMap.put("id",cursor.getString(0) );
				HMap.put("name",cursor.getString(1));
				HMap.put("location",cursor.getString(2));
				// Adding contact to list

				WishList.add(HMap);
			} while (cursor.moveToNext());
		}
		i = 0;
		return WishList; 	
	}
}
