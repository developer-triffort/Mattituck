package com.triffortali.mattiluck;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetupNotificationActivity extends Activity implements OnClickListener 
{

	EditText barcode,password;
	boolean resume=true;
	int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_notification);
		barcode=(EditText)findViewById(R.id.notify_edit1);
		password=(EditText)findViewById(R.id.notify_edit2);
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
		Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			findViewById(R.id.setup_back).setVisibility(View.INVISIBLE);
			findViewById(R.id.setup_skip).setVisibility(View.VISIBLE);
			resume=false;
		}
	}
	public void onBack(View v)
	{
		//finish();
		onBackPressed();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(resume)
		{
			super.onBackPressed();
			overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
		}
	}
	public void onSkip(View v)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(SetupNotificationActivity.this);
		builder.setTitle("Are you sure?");
		builder.setMessage("If you would like to validate your information at a later time, you can do so under the 'My Account' menu. If you do not have a library card with us, please visit the library to sign up.");
		builder.setPositiveButton("Cancel", null);
		builder.setNegativeButton("Skip Validation", this);
		AlertDialog d=builder.show();
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		d.getWindow().setAttributes(lp);
		((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
		d.show();
	}
	public void onSubmit(View v)
	{
		if(barcode.getText().toString().equals("") || password.getText().toString().equals(""))
		{
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			//TextView tv=new TextView(this);
			
			if(barcode.getText().toString().equals(""))
			{
				builder.setTitle(R.string.barcode);
				builder.setMessage(R.string.barcode_text);
				//tv.setText(R.string.barcode_text);
			}
			else
			{
				builder.setTitle(R.string.pass);
				builder.setMessage(R.string.pass_text);
				//tv.setText(R.string.pass_text);
			}
			//tv.setGravity(Gravity.CENTER_HORIZONTAL);
			//builder.setView(tv);
			builder.setPositiveButton("Close", null);
			AlertDialog d=builder.show();
			d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
			WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
			lp.copyFrom(d.getWindow().getAttributes());
			lp.width=width-width*20/100;
			d.getWindow().setAttributes(lp);
			((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
			d.show();
		}
		else
		{
			new SetUp().execute();
		}
	}
	
	public void onGlobe(View v)
	{
		resume=false;
		Intent intent=new Intent(this, WebActivity.class);
		intent.putExtra("url","http://www.mattlibrary.org" );
		startActivity(intent);
	}
	public void onDial(View v)
	{		
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY))
		{
			resume=false;
			Intent intent=new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:6312984134"));
			startActivity(intent);
		}
		else
		{
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			//builder.setMessage("");
			builder.setTitle("Cannot Dial Number");
			/*TextView tv=new TextView(this);
			tv.setText("This Android device does not support making phone calls");
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			builder.setView(tv);*/
			builder.setMessage("This Android device does not support making phone calls");
			builder.setPositiveButton("Close", null);
			AlertDialog d=builder.show();
			d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
			WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
			lp.copyFrom(d.getWindow().getAttributes());
			//lp.width=width-width*20/100;
			d.getWindow().setAttributes(lp);
			((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
			d.show();
		}
	}
	public void onEmail(View v)
	{
		resume=false;
		Intent email=new Intent(Intent.ACTION_SEND);
		email.setType("message/rfc822");
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{"mattitucklibrary@gmail.com"});
		startActivity(Intent.createChooser(email, "email sender"));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_setup_notification, menu);
		return true;
	}
	class SetUp extends AsyncTask<Void, Void, Void>
	{

		String result=null;
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd=new ProgressDialog(SetupNotificationActivity.this);
			pd.setMessage("Processing...");
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) 
		{
			try
			{
				JSONObject json = new JSONObject();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 0);
				HttpClient client = new DefaultHttpClient(httpParams);
				String url = "https://epsilon01-us-east-1a.ec2.capiratech.com/mattituckmobile/validate.php?barcode="+barcode.getText().toString()+"&pin="+password.getText().toString();
				//String url="https://epsilon01-us-east-1a.ec2.capiratech.com/mattituckmobile/validate.php?barcode=062711227906&pin=berse";
				HttpPost request = new HttpPost(url);
				request.setEntity(new ByteArrayEntity(json.toString().getBytes(
						"UTF8")));
				request.setHeader("json", json.toString());
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				if(entity!=null)
				{
					InputStream instream = entity.getContent();
					result = RestClient.convertStreamToString(instream);
				}
			}
			catch (Exception e) 
			{
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void res) {
			
			try
			{
				pd.cancel();
				if(result!=null)
				{
					JSONArray jarray = new JSONArray(result);
					JSONObject jobj=jarray.getJSONObject(0);
					String result_code=jobj.getString("result");
					String title=jobj.getString("title");
					
					String msg=jobj.getString("message");
					AlertDialog.Builder b=new AlertDialog.Builder(SetupNotificationActivity.this);
					b.setTitle(title);
					if(result_code.equals("0"))
					{
						b.setIcon(android.R.drawable.ic_dialog_alert);
					}
					else
					{
						CommonData.bc=barcode.getText().toString();
						CommonData.isconfigured=true;
						insert(CommonData.bc);
					}
					b.setMessage(msg);
					if(resume)
					b.setPositiveButton("Close", clk);
					else
					b.setPositiveButton("Close", clk);
					b.setCancelable(false);
					b.show();
				}
				else
				{
					Toast.makeText(SetupNotificationActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			super.onPostExecute(res);
		}
		
	}
	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
		SQLiteDatabase db=new DataManager(this).getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(DataManager.tab_col, 1);
		cv.put(DataManager.tab_col2, "");
		db.insert(DataManager.tab_name, null, cv);
		db.close();
		finish();
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void insert(String bc)
	{
		SQLiteDatabase db=new DataManager(SetupNotificationActivity.this).getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(DataManager.tab_col, 1);
		cv.put(DataManager.tab_col2, bc);
		db.insert(DataManager.tab_name, null, cv);
		db.close();
	}
	OnClickListener clk=new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			SetupNotificationActivity.this.finish();
			SetupNotificationActivity.this.overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
		}
	};
}
