package com.diabeticsCare.diabetico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class docsignup extends Activity implements OnClickListener {

	private Button 		doc_reg;
	public  EditText 	doc_name;
	public  EditText	email;
	public  EditText    phone;
	public	EditText    qualification;
	public  EditText    location;
	public	EditText    clinic;
	public 	EditText    hospital;
	public  EditText    password;
	public  EditText    _specialization;
	public  EditText	regNum;
	public  EditText    regFrom;
	public  EditText    fees;

	private ProgressDialog pDialog;

	JSONParser						jsonParser 				= new JSONParser();
	private static String 			url_reg_doc 		= "http://www.diabetiko.com/create_doc.php";

	@Override
	public void onCreate( Bundle savedInstanceate )
	{
		super.onCreate(savedInstanceate);
		setContentView(R.layout.doc);
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getActionBar().setTitle("	Doc Registration");
		
		TextView cat = (TextView) findViewById(R.id.desc);
		cat.setText(" Fill up the below form ");

		doc_reg 		= (Button)findViewById(R.id.submit_button );
		doc_name 		= (EditText)findViewById(R.id.name );
		email    		= (EditText)findViewById(R.id.email );
		phone    		= (EditText)findViewById(R.id.phone );
		qualification 	= (EditText)findViewById(R.id.qualification );
		_specialization = (EditText)findViewById(R.id.specialization );
		location 		= (EditText)findViewById(R.id.location );
		clinic 			= (EditText)findViewById(R.id.clinic );
		hospital 		= (EditText)findViewById(R.id.hospital );
		password		= (EditText)findViewById(R.id.pass );
		regNum			= (EditText)findViewById(R.id.regNum );
		regFrom			= (EditText)findViewById(R.id.regFrom );
		fees			= (EditText)findViewById(R.id.fees );

		doc_reg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new SubmitDocData().execute();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected( MenuItem menu )
	{
		switch ( menu.getItemId() ) 
		{
		case R.id.item1:
			startActivity(new Intent(getApplicationContext(), login.class));
			return true;
		case R.id.item4:
			startActivity(new Intent(getApplicationContext(), diagnosticsignup.class));
			return true;
		case R.id.item3:
			startActivity(new Intent(getApplicationContext(), docsignup.class));
			return true;
		case R.id.action_settings:
			Toast.makeText(getApplicationContext(),"Under Construction", 
					Toast.LENGTH_SHORT).show();	
			return true;
		default:
			return super.onOptionsItemSelected( menu );

		}
	}

	public class SubmitDocData  extends AsyncTask<String, String, String>  {

		boolean  bSuccess = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(docsignup.this);
			pDialog.setMessage("Registering..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String 			docName;
			String 			docEmail;
			String 			docPhone;
			String 			docQualification;
			String 			docLocation;
			String 			docClinic;
			String 			docHospital;
			String 			docPass;
			String 			__specialization;
			String          docRegNum;
			String			docRegFrom;
			String			docFees;

			HttpClient 		myClient;
			HttpPost  		myPost;
			HttpResponse 	myResponse;

			docName 			= doc_name.getText().toString();
			docEmail 			= email.getText().toString();
			docPhone 			= phone.getText().toString();
			docQualification 	= qualification.getText().toString();
			docLocation 		= location.getText().toString();
			docClinic  			= clinic.getText().toString();
			docHospital 		= hospital.getText().toString();
			docPass   			= password.getText().toString();
			__specialization    = _specialization.getText().toString();
			docRegNum           = regNum.getText().toString();
			docRegFrom    		= regFrom.getText().toString();
			docFees				= fees.getText().toString();

			List<NameValuePair> params = new ArrayList< NameValuePair >();

			params.add(new BasicNameValuePair("docName", docName ) ) ;
			params.add(new BasicNameValuePair("docEmail", docEmail ) ) ;
			params.add(new BasicNameValuePair("docPhone", docPhone ) ) ;
			params.add(new BasicNameValuePair("docRegNum", docRegNum ) ) ;
			params.add(new BasicNameValuePair("docRegFrom", docRegFrom ) ) ;			
			params.add(new BasicNameValuePair("docQualification", docQualification ) ) ;
			params.add(new BasicNameValuePair("__specialization", __specialization ) ) ;
			params.add(new BasicNameValuePair("docClinic", docClinic ) ) ;
			params.add(new BasicNameValuePair("docLocation", docLocation ) ) ;		
			params.add(new BasicNameValuePair("docHospital", docHospital ) ) ;
			params.add(new BasicNameValuePair("docPass", docPass ) ) ;
			params.add(new BasicNameValuePair("docFees", docFees ) ) ;
			params.add(new BasicNameValuePair("docLocation", docLocation ) ) ;



			myClient 		= new DefaultHttpClient();
			myPost   		= new HttpPost( url_reg_doc );	


			try {
				myPost.setEntity(new UrlEncodedFormEntity( params ) );
				myResponse = myClient.execute( myPost );

				BufferedReader br = new BufferedReader( new InputStreamReader(myResponse.getEntity().getContent()));
				String myLine = "";
				while( ( myLine = br.readLine() ) != null )
				{
					Log.d( "conn", myLine );
				}
				
				if( myResponse != null )
					bSuccess = true;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		protected void onPostExecute( String file_url)
		{
			pDialog.dismiss();
			if( true == bSuccess )
				Toast.makeText(getApplicationContext(), "Registered",Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "Registeration failed",Toast.LENGTH_SHORT).show();


		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}