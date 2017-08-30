/* Register Labs ************
 * @Arindam *****/
package com.diabeticsCare.diabetico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

public class diagnosticsignup extends Activity implements OnClickListener{
	
	private Button 		lab_reg;
	public  EditText 	lab_name;
	public  EditText	lab_email;
	public  EditText    lab_phone;
	public  EditText    lab_location;
	public  EditText    lab_password;
	public  EditText    lab_specialization;
	public  EditText	lab_regNum;
	public  EditText    lab_regFrom;

	private ProgressDialog pDialog;
	
	
	JSONParser						jsonParser 				= new JSONParser();
	private static String 			url_create_product 		= "http://www.diabetiko.com/create_lab.php";
	
	public void onCreate( Bundle savedInstanceate )
	{
		super.onCreate(savedInstanceate);
		setContentView(R.layout.diagnostic);
	
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setTitle("	Register Diagnostic");

		TextView cat = (TextView) findViewById(R.id.desc);
		cat.setText(" Fill up the below form ");
		
		lab_reg 			= (Button)findViewById(R.id.submit_button_lab );
		lab_name 			= (EditText)findViewById(R.id.lab_name );
		lab_email    		= (EditText)findViewById(R.id.lab_email );
		lab_phone    		= (EditText)findViewById(R.id.lab_phone );
		lab_location 		= (EditText)findViewById(R.id.lab_location );
		lab_password 		= (EditText)findViewById(R.id.lab_pass );
		lab_specialization 	= (EditText)findViewById(R.id.lab_specialization);
		lab_regNum 			= (EditText)findViewById(R.id.lab_regNum);
		lab_regFrom 		= (EditText)findViewById(R.id.lab_regFrom );
		
		lab_reg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				new SubmitDiagData().execute();
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
	public class SubmitDiagData  extends AsyncTask<String, String, String>  {

		boolean  bSuccess = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(diagnosticsignup.this);
			pDialog.setMessage("Registering..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String 			labName;
			String 			labEmail;
			String 			labPhone;
			String 			labSpecialization;
			String 			labLocation;
			String 			labPass;
			String          labRegNum;
			String			labRegFrom;

			HttpClient 		myClient;
			HttpPost  		myPost;
			HttpResponse 	myResponse;

			labName 			= lab_name.getText().toString();
			labEmail 			= lab_email.getText().toString();
			labPhone 			= lab_phone.getText().toString();
			labSpecialization   = lab_specialization.getText().toString();
			labLocation 		= lab_location.getText().toString();			
			labRegNum           = lab_regNum.getText().toString();
			labRegFrom    		= lab_regFrom.getText().toString();
			labPass   			= lab_password.getText().toString();

			List<NameValuePair> params = new ArrayList< NameValuePair >();

			params.add(new BasicNameValuePair("labName", labName ) ) ;
			params.add(new BasicNameValuePair("labEmail", labEmail ) ) ;
			params.add(new BasicNameValuePair("labPhone", labPhone ) ) ;
			params.add(new BasicNameValuePair("labSpecialization", labSpecialization ) ) ;
			params.add(new BasicNameValuePair("labLocation", labLocation ) ) ;
			params.add(new BasicNameValuePair("labRegNum", labRegNum ) ) ;			
			params.add(new BasicNameValuePair("labRegFrom", labRegFrom ) ) ;
			params.add(new BasicNameValuePair("labPass", labPass ) ) ;



			myClient 		= new DefaultHttpClient();
			myPost   		= new HttpPost( url_create_product );	


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
	public void onClick(View v) {
		// TODO Auto-generated method stub	
	}
}
