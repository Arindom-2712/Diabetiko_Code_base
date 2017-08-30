package com.diabeticsCare.diabetico;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public  class testListAdapter extends BaseAdapter
{
	Activity context;
	String Name[];
	String Location[];
	String mTimings[];
	int mIdentify;
	public testListAdapter(Activity context, String[] Names, String[] Locations, String[] Timings,int identify) {
		super();
		this.context 		= context;
		this.Name 			= Names;
		this.Location 		= Locations;
		this.mTimings 		= Timings;
		this.mIdentify 		= identify;
	}

	public int getCount() {
		return Name.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		TextView txtViewTitle;
		TextView txtViewDescription;
		TextView txtTimings;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		LayoutInflater inflater 	=  context.getLayoutInflater();
		String preciseLoc[] 		= new String[]{};
		preciseLoc 					= Location[position ].split(",");

		if (convertView == null)
		{
			if( 1 == mIdentify )
				convertView = inflater.inflate(R.layout.lab_doc_list, null);
			else if(2 == mIdentify )
				convertView = inflater.inflate(R.layout.lab_test_list, null);
			else if( 3 == mIdentify )
				convertView = inflater.inflate(R.layout.also_practice, null);
			holder 		= new ViewHolder();
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.test_Name);
			holder.txtViewDescription = (TextView) convertView.findViewById(R.id.Test_price);
			holder.txtTimings = (TextView) convertView.findViewById(R.id.Test_Timings );
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		try
		{
			holder.txtViewTitle.setText(Name[position ]);
			holder.txtViewDescription.setText( preciseLoc[ 0 ] );
			holder.txtTimings.setText( mTimings[position ] );
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		return convertView;
	}

}
