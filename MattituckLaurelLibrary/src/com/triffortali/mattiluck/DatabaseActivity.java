package com.triffortali.mattiluck;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.SearchView.OnQueryTextListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
//import android.widget.SearchView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseActivity extends Activity implements AnimationListener,OnItemClickListener//,OnQueryTextListener
{

	boolean resume=true;
	ListView event_list;
	TextView tv;
	Dialog d;
	//SearchView sv;
	String search="";
	String m="Loading Databases...";
	String res;
	int width;
	EditText sEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		event_list=(ListView)findViewById(R.id.database_list);
		tv=(TextView)findViewById(R.id.data_demo);
		Animation am=AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right );
		am.setAnimationListener(this);
		//sv=(SearchView)findViewById(R.id.database_search);
		tv.startAnimation(am);
		event_list.setOnItemClickListener(this);
		sEditText=(EditText)findViewById(R.id.data_Search);
		/* int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	        TextView textView = (TextView)sv.findViewById(id);
	        textView.setTextColor(Color.WHITE);
		sv.setOnQueryTextListener(this);*/
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
	}
	public void onBack(View v)
	{
	//	finish();
		onBackPressed();
	}
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
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
		getMenuInflater().inflate(R.menu.activity_database, menu);
		return true;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) 
	{
		RelativeLayout rl=(RelativeLayout)v;
		TextView tv=(TextView)rl.getChildAt(3);
		resume=false;
		Intent intent=new Intent(this, WebActivity.class);
		intent.putExtra("url",tv.getText().toString() );
		startActivity(intent);
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		new LoadEvents().execute();
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	class LoadEvents extends AsyncTask<Void, Void, Void>
	{

		String result=null;
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd=new ProgressDialog(DatabaseActivity.this);
			pd.setMessage(m);
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
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
				HttpConnectionParams.setSoTimeout(httpParams, 0);
				HttpClient client = new DefaultHttpClient(httpParams);
				String url = "https://epsilon01-us-east-1a.ec2.capiratech.com/mattituckmobile/databases.php?query="+search;
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
		protected void onPostExecute(Void res1) {
			
			try
			{
				pd.cancel();
				if(result!=null)
				{
					if(result.length()>3)
					{
						res=result;
						JSONArray jarray = new JSONArray(res);
						List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
						for(int i=0;i<jarray.length();i++)
						{
							HashMap<String, String> map=new HashMap<String, String>();
							JSONObject jobj=jarray.getJSONObject(i);
							map.put("title", jobj.getString("title"));
							map.put("link", jobj.getString("link"));
							map.put("sub", jobj.getString("subjects"));
							map.put("decp", jobj.getString("description"));
							list.add(map);
						}
						String [] from={"title","link","sub","decp"};
						int [] to={R.id.list1_title,R.id.list1_link,R.id.list1_sub,R.id.list1_decp};
						SimpleAdapter sa=new SimpleAdapter(DatabaseActivity.this, list, R.layout.list1, from, to);			
						event_list.setAdapter(null);
						event_list.setAdapter(sa);
					}
					else
					{
						JSONArray jarray = new JSONArray(res);
						List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
						for(int i=0;i<jarray.length();i++)
						{
							HashMap<String, String> map=new HashMap<String, String>();
							JSONObject jobj=jarray.getJSONObject(i);
							map.put("title", jobj.getString("title"));
							map.put("link", jobj.getString("link"));
							map.put("sub", jobj.getString("subjects"));
							map.put("decp", jobj.getString("description"));
							list.add(map);
						}
						String [] from={"title","link","sub","decp"};
						int [] to={R.id.list1_title,R.id.list1_link,R.id.list1_sub,R.id.list1_decp};
						SimpleAdapter sa=new SimpleAdapter(DatabaseActivity.this, list, R.layout.list1, from, to);			
						event_list.setAdapter(null);
						event_list.setAdapter(sa);
						AlertDialog.Builder builder=new AlertDialog.Builder(DatabaseActivity.this);
						builder.setMessage("No Databas Found");
						TextView tv=new TextView(DatabaseActivity.this);
						tv.setText("Your Search returned 0 Databases.\nPlease change your search criteria and try again.");
						tv.setGravity(Gravity.CENTER_HORIZONTAL);
						builder.setView(tv);
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
				}
				else
				{
					Toast.makeText(DatabaseActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			super.onPostExecute(res1);
		}	
	}
	/*@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		search=query;
		if(query.indexOf(' ')!=-1)
		{
			search=query.replaceAll(" ", "%20");
			
		}
		search=query;
		if(query.indexOf(' ')!=-1)
		{
			search=query.replaceAll(" ", "&nsbp;");
			
		}
		m="Searching database for '"+query+"'";
		new LoadEvents().execute();
		return false;
	}*/
	public void onSearch(View v)
	{
		String query=sEditText.getText().toString();
		search=query;
		if(query.indexOf(' ')!=-1)
		{
			search=query.replaceAll(" ", "%20");
			
		}
		search=query;
		if(query.indexOf(' ')!=-1)
		{
			search=query.replaceAll(" ", "&nsbp;");
			
		}
		m="Searching database for '"+query+"'";
		new LoadEvents().execute();
	}
}
