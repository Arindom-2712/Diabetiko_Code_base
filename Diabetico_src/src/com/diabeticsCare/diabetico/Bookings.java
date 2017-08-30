/* Fragment that Shows booking inf Future and Past corresponding to User Identification ******
 * @Arindam****/
package com.diabeticsCare.diabetico;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Bookings extends Fragment{
	public ListView list ;
	public ListView listLab;
	static String bookTimings = "";
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		MainActivity objMActivity = ( MainActivity ) getActivity();
		String sDataFromActivity = objMActivity.getMyData();

		View view = inflater.inflate(R.layout.wish, container, false);

		list = (ListView)view.findViewById(R.id.wishes);
		listLab = (ListView)view.findViewById(R.id.wishesLab);
		
		list.setAdapter( new WishListAdapter(this.getActivity().getBaseContext(),docProfile.docWishList) );


		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if( !bookTimings.isEmpty() )
				{
					Intent appInfo = new Intent( getActivity(), history.class);
					appInfo.putExtra("position", position );
					startActivity(appInfo);
				}
				else
				{
					Intent appInfo = new Intent( getActivity(), booking_profile.class);
					appInfo.putExtra("position", position );
					startActivity(appInfo);
				}
			}
		});
		return view;
	}

	static class WishListAdapter extends BaseAdapter
	{
		private Context context;
		private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
		public final Boolean 		isScrolling 		= true;

		/* ViewHolder Class To Ensure Less Flickering/Hanging... While Scrolling Through Search Results 
		 * Best Practice **************************/
		public static  class ViewHolder
		{
			public TextView 	text;
			public ImageView 	image;
			public ImageView 	button;
		}



		public WishListAdapter(Context c, ArrayList<HashMap<String, String>> list)
		{
			context 	= c;
			MyArr 		= list;
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

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View 			rowView 	= convertView;
			ViewHolder 		viewHolder;
			int ImageId = 0;
			LayoutInflater inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (rowView == null) {

				rowView 			= inflater.inflate(R.layout.bookings, null);
				viewHolder 		= new ViewHolder();
				viewHolder.text 	= (TextView) rowView.findViewById(R.id.firstLine);
				viewHolder.image 	= (ImageView) rowView.findViewById(R.id.icon);
				viewHolder.button  = ( ImageView)rowView.findViewById(R.id.status);
				String location ="";
				location = MyArr.get(position).get("location");
				/*if( null == MyArr.get(position).get("location") )
				{
					location = MyArr.get(position).get("loc");
				}*/
				if( "wish" == MyArr.get(position).get("status"))
				{
					
					viewHolder.button.setImageResource(R.drawable.wish1);
					viewHolder.text.setText( MyArr.get(position).get("name")+"\n\n"+
							location
							);	
					bookTimings = "";
				}
				else if( "booked" == MyArr.get(position).get("status") )
				{
					viewHolder.button.setImageResource(R.drawable.chked);
					viewHolder.text.setText( MyArr.get(position).get("name") + "\n\n"+
							location+"" 
							+"\n\n"+ MyArr.get(position).get("BookTime")
							);
					bookTimings = MyArr.get(position).get("BookTime");
				}

				rowView.setTag(viewHolder);

				ImageId = Integer.parseInt( MyArr.get(position).get("pos") );
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();

			}
			try
			{
				viewHolder.image.setImageBitmap( searchResults.bitMaps.get( ImageId ) );
				if( null == searchResults.bitMaps.get( ImageId ) )
					viewHolder.image.setImageResource(R.drawable.blank_pp);

			} catch (Exception e) {
				viewHolder.image.setImageResource(R.drawable.blank_pp);
			}
			return rowView;
		}	
	}
}
