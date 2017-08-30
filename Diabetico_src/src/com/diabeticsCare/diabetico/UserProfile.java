package com.diabeticsCare.diabetico;


import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserProfile extends Fragment implements OnClickListener{
	private Button 			SetReminder;
	private EditText 		Hours;
	private EditText 		Mins;
	private EditText 		TimeOfDay;
	private AlarmManager 	alarmMgr;
	private PendingIntent 	alarmIntent;
	private final int  		ALARM_NOTIFICATION_REMINDER_ID = 43;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        View view =  inflater.inflate(R.layout.userprofile, container, false);
	        
	        SetReminder = (Button)view.findViewById(R.id.Reminder);
	        Hours		= (EditText)view.findViewById(R.id.hrs);
	        Mins		= (EditText)view.findViewById(R.id.mns);
	        TimeOfDay   = (EditText)view.findViewById(R.id.ampm);
	        
	        SetReminder.setOnClickListener(this);
	        return view;
	}

	@Override
	public void onClick(View arg0) {
		if( SetReminder == arg0)
		{

			Intent i = new Intent(getActivity(), CompleteProfile.class);
			
			startActivity(i);
		}

	/*	String 	hrs 	= Hours.getText().toString();
		String 	mins 	= Mins.getText().toString();
		String 	Time 	= TimeOfDay.getText().toString();
		int 	iHrs 	= 0;

		if( Time.contains("PM") )
		{
			if( Integer.parseInt(hrs) > 12 )
			{
				Toast.makeText(getActivity(), " Invalid Time", Toast.LENGTH_LONG).show();
				return;
			}
			else if( 12 == Integer.parseInt(hrs)  )
			{
				iHrs = Integer.parseInt(hrs);
			}
			else
			{
				iHrs = Integer.parseInt(hrs) + 12;
			}
		}
		else if( Time.contains("AM"))
		{
			if( Integer.parseInt(hrs) > 12 )
			{
				Toast.makeText(getActivity(), " Invalid Time", Toast.LENGTH_LONG).show();
				return;
			}
			iHrs = Integer.parseInt(hrs);
		}
		
		alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getActivity(), Reminder.class);
		
		intent.putExtra("Title","Medicine");
		intent.putExtra("Msg", "Take Insulin at "+iHrs+" : "+Integer.parseInt(mins) + Time);
		intent.putExtra("notifyId", ALARM_NOTIFICATION_REMINDER_ID );
		alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, iHrs);
		calendar.set(Calendar.MINUTE, Integer.parseInt(mins) - 1);
		
		
		
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, alarmIntent);*/
	}
}