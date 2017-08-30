package com.diabeticsCare.diabetico;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddAppointmentAlternative extends Activity implements OnClickListener{
		
	private TextView 		nameOfDoctor;
	private TextView 		locOfDoctor;
	private ImageView		profile;
	private EditText 		day;
	private EditText 		time;
	private Button   		addAppointment;
	private int 			mYear,
	mMonth,
	mDay,
	mHour,
	mMinute;
	private AlarmManager 	alarmMgr;
	private int				position;		
	private PendingIntent 	alarmIntent;
	private final int  		ALARM_NOTIFICATION_APPOINTMENT_ID = 89;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		setContentView(R.layout.addappointment);

		getActionBar().setTitle("	Add Appointment");

		Intent appointmentIntent = getIntent();

		day		     		= (EditText)findViewById(R.id.editText1);
		time 		 		= ( EditText)findViewById(R.id.editText2);
		addAppointment		= ( Button )findViewById(R.id.Add);

		day.setOnClickListener(this);
		time.setOnClickListener(this);
		addAppointment.setOnClickListener(this);

		CreateClassAppointment( appointmentIntent );
	}

	public void CreateClassAppointment( Intent intent)
	{
		nameOfDoctor = (TextView)findViewById(R.id.doc_Location);
		locOfDoctor  = (TextView)findViewById(R.id.docName);
		profile      = (ImageView)findViewById(R.id.imageView1);

		String doc 		= intent.getExtras().getString("DocName");
		String loc 		= intent.getExtras().getString("DocLocation");
		position 		= intent.getExtras().getInt("position");

		nameOfDoctor.setText( doc );
		locOfDoctor.setText(loc);
		if( null == Alternative_Search.bitMapsOfAlternatives.get( position ) )
			profile.setImageResource(R.drawable.blank_pp);
		else
			profile.setImageBitmap( Alternative_Search.bitMapsOfAlternatives.get( position ) );

		return;
	}
	@Override
	public void onClick(View v) {
		if( day == v )
		{
			final Calendar c = Calendar.getInstance();
			mYear 	= c.get(Calendar.YEAR);
			mMonth 	= c.get(Calendar.MONTH);
			mDay 	= c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dpd = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					day.setText(dayOfMonth + "-"
							+ (monthOfYear + 1) + "-" + year);

				}
			}, mYear, mMonth, mDay);
			dpd.show();
		}
		else if( time == v)
		{
			final Calendar c = Calendar.getInstance();
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);

			TimePickerDialog tpd = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay,
						int minute) {
					time.setText(hourOfDay + ":" + minute);
				}
			}, mHour, mMinute, false);
			tpd.show();
		}
		else if( addAppointment == v )
		{
			String sDayBook 	= day.getText().toString();
			String sTimeBook	= time.getText().toString();

			String tdayBreakup[] = sDayBook.split("-");
			String timeBreakUp[] = sTimeBook.split(":");

			mDay 		= Integer.parseInt( tdayBreakup[ 0 ] );
			mMonth		= Integer.parseInt( tdayBreakup[ 1 ] );
			mYear   	= Integer.parseInt( tdayBreakup[ 2 ] );
			mHour   	= Integer.parseInt( timeBreakUp[ 0 ] );
			mMinute   	= Integer.parseInt( timeBreakUp[ 1 ] );

			alarmMgr = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(getBaseContext(), Reminder.class);

			intent.putExtra("Title","Booking Reminder");
			intent.putExtra("Msg", "You are Booked With "+Alternative_Search.alternativeList.get(position).get("name")+"\n At:"
					+sTimeBook);
			intent.putExtra("notifyId", ALARM_NOTIFICATION_APPOINTMENT_ID );

			alarmIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());

			calendar.set(Calendar.YEAR, mYear );
			calendar.set(Calendar.MONTH,mMonth );
			calendar.set(Calendar.DATE, mDay);
			calendar.set(Calendar.HOUR_OF_DAY, mHour - 1 );
			calendar.set(Calendar.MINUTE, mMinute - 1 );



			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

			HashMap<String, String> AlteranteWish = new HashMap<String, String>();


			AlteranteWish.put("status", "booked");
			AlteranteWish.put("BookTime", "Booked On: \n"+sDayBook+" At "+sTimeBook);

			AlteranteWish.put("name",Alternative_Search.alternativeList.get(position).get("name") ) ;
			AlteranteWish.put("phone", Alternative_Search.alternativeList.get(position).get("phone") );
			AlteranteWish.put("Type", Alternative_Search.alternativeList.get(position).get("Type") );
			AlteranteWish.put("location", Alternative_Search.alternativeList.get(position).get("loc") );
			AlteranteWish.put("Distance", Alternative_Search.alternativeList.get(position).get("distance") );
			AlteranteWish.put("clinic", Alternative_Search.alternativeList.get(position).get("clinic") );
			AlteranteWish.put("number", Alternative_Search.alternativeList.get(position).get("number") );
			AlteranteWish.put("qual", Alternative_Search.alternativeList.get(position).get("qual") );
			AlteranteWish.put("docs", Alternative_Search.alternativeList.get(position).get("Docs") );
			AlteranteWish.put("services", Alternative_Search.alternativeList.get(position).get("services") );
			AlteranteWish.put("Fees", Alternative_Search.alternativeList.get(position).get("Fees") );
			AlteranteWish.put("Exp", Alternative_Search.alternativeList.get(position).get("Exp") ); 
			AlteranteWish.put("Timings",Alternative_Search.alternativeList.get(position).get("Timings") );
			AlteranteWish.put("Confirmation", Alternative_Search.alternativeList.get(position).get("Confirmation") );
			AlteranteWish.put("Availablity",Alternative_Search.alternativeList.get(position).get("Day") );
			AlteranteWish.put("Time",Alternative_Search.alternativeList.get(position).get("Time") );
			
			AlteranteWish.put("pos", 	""+position);
			AlteranteWish.put("type", "alternative");

			if( !docProfile.docWishList.contains(AlteranteWish))
			{
				docProfile.docWishList.add(AlteranteWish);	
				SQLiteUtility objSQLite = new SQLiteUtility(this, AlteranteWish );
				objSQLite.addDataAlternate();
				Toast.makeText(getApplicationContext(), "Added To Wish List", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "AlreadyAdded", Toast.LENGTH_SHORT).show();
			}	

			Toast.makeText(getApplicationContext(),"You will be reminded an Hour "
					+ "Prior to Your Booking", Toast.LENGTH_LONG ).show();
		}
	}

}
