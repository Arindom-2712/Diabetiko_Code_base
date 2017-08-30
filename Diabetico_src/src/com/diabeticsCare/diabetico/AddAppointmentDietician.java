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

public class AddAppointmentDietician extends Activity implements OnClickListener{
		
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
	private final int  		ALARM_NOTIFICATION_APPOINTMENT_ID = 45;

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
		if( null == SearchDietician.bitMapsOfDieticin.get( position ) )
			profile.setImageResource(R.drawable.blank_pp);
		else
			profile.setImageBitmap( SearchDietician.bitMapsOfDieticin.get( position ) );

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
			intent.putExtra("Msg", "You are Booked With "+SearchDietician.dieticianList.get(position).get("name")+"\n At:"
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

			HashMap<String, String> dieticianWish = new HashMap<String, String>();


			dieticianWish.put("status", "booked");
			dieticianWish.put("BookTime", "Booked On: \n"+sDayBook+" At "+sTimeBook);

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
			dieticianWish.put("type", "dietician");

			if( !docProfile.docWishList.contains(dieticianWish))
			{
				docProfile.docWishList.add(dieticianWish);	
				SQLiteUtility objSQLite = new SQLiteUtility(this, dieticianWish );
				objSQLite.addDataDietician();
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
