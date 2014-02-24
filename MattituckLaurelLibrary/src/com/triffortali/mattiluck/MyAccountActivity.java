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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccountActivity extends Activity implements  OnClickListener
{

	boolean resume=true;
	int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
	}
	public void onMyRecords(View v)
	{
		if(CommonData.rec==0)
		{
			/*CommonData.rec++;
			SharedPreferences sp=getSharedPreferences("mattituck",Context.MODE_PRIVATE);
			Editor edit=sp.edit();
			edit.putInt(CommonData.my, 1);
			edit.commit();*/
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("PIN Creation");
			builder.setMessage("Users who have not accessed their account online before must configure a PIN. Would you like to do that now?");
			builder.setPositiveButton("Skip", this);
			builder.setNegativeButton("Create PIN", this);
			AlertDialog d=builder.show();
			d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
			WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
			lp.copyFrom(d.getWindow().getAttributes());
			lp.width=width-width*20/100;
			d.getWindow().setAttributes(lp);
			((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
			d.show();
			CommonData.rec=1;
			SharedPreferences sp=getApplicationContext().getSharedPreferences("mattituck",Context.MODE_PRIVATE);
			Editor edit=sp.edit();
			edit.putInt("key", 1);
			edit.commit();
		}
		else
		{
			skip();
		}
	}
	void skip()
	{
		Intent intent=new Intent(this, WebActivity.class);
		//intent.putExtra("url","https://encore.suffolk.lib.ny.us/iii/cas/login?service=https%3A%2F%2Falpha2.suffolk.lib.ny.us%3A443%2Fpatroninfo~S84%2FIIITICKET&scope=84" );
		intent.putExtra("url", "http://alpha2.suffolk.lib.ny.us/patroninfo");
		startActivity(intent);
	}
	public void onBack(View v)
	{
	//	finish();
		onBackPressed();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onSetup(View v)
	{
		startActivity(new Intent(this, SetupNotificationActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
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
		getMenuInflater().inflate(R.menu.activity_my_account, menu);
		return true;
	}
	public void onDigital(View v)
	{
		if(!CommonData.isconfigured)
		{
			AlertDialog.Builder b=new AlertDialog.Builder(MyAccountActivity.this);
			b.setTitle("Account Not Configured");
			b.setMessage("Before you can use this feature, you must set up notifications on this device.");
			b.setPositiveButton("Close", null);
			b.show();
		}
		else
		{
			startActivity(new Intent(this, DigitalLibraryActivity.class));
			overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
		}
	}
	public void onCheckNotifi(View v)
	{
		if(!CommonData.isconfigured)
		{
			AlertDialog.Builder b=new AlertDialog.Builder(MyAccountActivity.this);
			b.setTitle("Account Not Configured");
			b.setIcon(android.R.drawable.ic_dialog_alert);
			b.setMessage("Before you can use this feature, you must set up notifications on this device.");
			b.setPositiveButton("Close", null);
			b.show();
		}
		else
		{
			new Check().execute();
		}
	}
	class Check extends AsyncTask<Void, Void, Void>
	{

		String result=null;
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd=new ProgressDialog(MyAccountActivity.this);
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
				//String url = "https://emmaclark-mobile.dynalib.com/v2/gateway/notifications.xqy?barcode=berse";
				String url="http://emmaclark-mobile.dynalib.com/v2/gateway/notifications.xqy?barcode="+CommonData.bc;
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
					
					String msg1=jobj.getString("message");
					char []ch=msg1.toCharArray();
					String msg="";
					for(int i=0;i<ch.length;i++)
					{
						if(ch[i]!='\\')
						{
							msg+=ch[i];
						}
						else
						{
							msg+='\n';
							i++;
						}
					}
					
					AlertDialog.Builder b=new AlertDialog.Builder(MyAccountActivity.this);
					b.setTitle(title);
					if(result_code.equals("0"))
					{
						b.setIcon(android.R.drawable.ic_dialog_alert);
					}
					b.setMessage(msg);
					b.setPositiveButton("Close", null);
					b.show();
				}
				else
				{
					Toast.makeText(MyAccountActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
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
		//Toast.makeText(this, "which "+which, 1).show();
		
		//skip -1
		//create in -2
		if(which==-1)
		{
			skip();
		}//hjkb
		if(which==-2)
		{
			Intent intent=new Intent(this, WebActivity.class);
			//intent.putExtra("url","https://encore.suffolk.lib.ny.us/iii/cas/login?service=https%3A%2F%2Falpha2.suffolk.lib.ny.us%3A443%2Fpatroninfo~S84%2FIIITICKET&scope=84" );
			intent.putExtra("url", "https://alpha1.suffolk.lib.ny.us/pinreset~S84");
			startActivity(intent);
		}
	}
}
