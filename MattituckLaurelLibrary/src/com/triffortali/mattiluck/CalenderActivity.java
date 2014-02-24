package com.triffortali.mattiluck;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
/*import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;*/
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CalenderActivity extends Activity implements AnimationListener,OnItemClickListener,OnClickListener//,OnQueryTextListener
{

	boolean resume=true;
	ListView event_list;
	TextView tv;
	Dialog d;
	//SearchView  sv;
	String search="";
	AlertDialog dialog;
	Button all,td,ad,cd,tn,cr;
	String res=null;
	String m="Loding Events...";
	int red=-1;
	int width;
	EditText sEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);
		event_list=(ListView)findViewById(R.id.event_list);
		tv=(TextView)findViewById(R.id.event_demo);
		Animation am=AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right );
		am.setAnimationListener(this);
		tv.startAnimation(am);

		sEditText=(EditText)findViewById(R.id.event_search);
		event_list.setOnItemClickListener(this);
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
	}
	public void onBack(View v)
	{
		//finish();
		onBackPressed();
	}
	public void onLine(View v)
	{
		resume=false;
		Intent intent=new Intent(this, WebActivity.class);
		intent.putExtra("url","http://ny.evanced.info/emmaclark/lib/eventcalendar.asp?dt=mo&mo=&Lib=ALL&et=&ag" );
		startActivity(intent);
	}
	public void onLimits(View v)
	{
		/*d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.filter_menu);
		Button close=(Button)d.findViewById(R.id.filter_child);
		close.setTag("0");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);*/
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		b.setView(LayoutInflater.from(this).inflate(R.layout.filter_menu, null));
		dialog= b.show();
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		ForClick f=new ForClick();
		all=(Button)dialog.findViewById(R.id.filter_allp);
		td=(Button)dialog.findViewById(R.id.filter_today);
		ad=(Button)dialog.findViewById(R.id.filter_adult);
		cd=(Button)dialog.findViewById(R.id.filter_child);
		tn=(Button)dialog.findViewById(R.id.filter_teen);
		cr=(Button)dialog.findViewById(R.id.filter_com);
		if(red==1)
		{
			all.setBackgroundResource(R.drawable.btn_filter);
			all.setTextColor(Color.BLACK);
			td.setBackgroundResource(R.drawable.tag_btn);
			td.setTextColor(Color.WHITE);
		}
		else if(red==2)
		{
			all.setBackgroundResource(R.drawable.btn_filter);
			all.setTextColor(Color.BLACK);
			ad.setBackgroundResource(R.drawable.tag_btn);
			ad.setTextColor(Color.WHITE);
		}
		else if(red==3)
		{
			all.setBackgroundResource(R.drawable.btn_filter);
			all.setTextColor(Color.BLACK);
			cd.setBackgroundResource(R.drawable.tag_btn);
			cd.setTextColor(Color.WHITE);
		}
		else if(red==4)
		{
			all.setBackgroundResource(R.drawable.btn_filter);
			all.setTextColor(Color.BLACK);
			tn.setBackgroundResource(R.drawable.tag_btn);
			tn.setTextColor(Color.WHITE);
		}
		else if(red==5)
		{
			all.setBackgroundResource(R.drawable.btn_filter);
			all.setTextColor(Color.BLACK);
			cr.setBackgroundResource(R.drawable.tag_btn);
			cr.setTextColor(Color.WHITE);
		}
		all.setOnClickListener(f);
		td.setOnClickListener(f);
		ad.setOnClickListener(f);
		cd.setOnClickListener(f);
		tn.setOnClickListener(f);
		cr.setOnClickListener(f);
		lp.copyFrom(dialog.getWindow().getAttributes());
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		dialog.show();
		
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
		getMenuInflater().inflate(R.menu.activity_calender, menu);
		return true;
	}
	@Override
	public void onAnimationEnd(Animation animation) 
	{
		new LoadEvents().execute();
	}
	@Override
	public void onAnimationRepeat(Animation animation) 
	{
		
	}
	@Override
	public void onAnimationStart(Animation animation) 
	{
		
	}
	class LoadEvents extends AsyncTask<Void, Void, Void>
	{

		String result=null;
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd=new ProgressDialog(CalenderActivity.this);
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
				String url = "https://epsilon01-us-east-1a.ec2.capiratech.com/mattituckmobile/events.php?query="+search;
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
				Log.w("space", ""+e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void res1) {
			
			try
			{
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
							map.put("date", jobj.getString("date"));
							map.put("time", jobj.getString("time"));
							map.put("location", jobj.getString("location"));
							map.put("id", jobj.getString("id"));
							map.put("decp", jobj.getString("description"));
							map.put("cate", jobj.getString("category"));
							list.add(map);
						}
						String [] from={"title","date","time","location","id","decp","cate"};
						int [] to={R.id.list_title,R.id.list_when,R.id.list_time,R.id.list_location,R.id.list_id,R.id.list_decp,R.id.list_cate};
						SimpleAdapter sa=new SimpleAdapter(CalenderActivity.this, list, R.layout.list, from, to);			
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
							map.put("date", jobj.getString("date"));
							map.put("time", jobj.getString("time"));
							map.put("location", jobj.getString("location"));
							map.put("id", jobj.getString("id"));
							map.put("decp", jobj.getString("description"));
							map.put("cate", jobj.getString("category"));
							list.add(map);
						}
						String [] from={"title","date","time","location","id","decp","cate"};
						int [] to={R.id.list_title,R.id.list_when,R.id.list_time,R.id.list_location,R.id.list_id,R.id.list_decp,R.id.list_cate};
						SimpleAdapter sa=new SimpleAdapter(CalenderActivity.this, list, R.layout.list, from, to);			
						event_list.setAdapter(null);
						event_list.setAdapter(sa);
						
						AlertDialog.Builder builder=new AlertDialog.Builder(CalenderActivity.this);
						builder.setMessage("No Events");
						TextView tv=new TextView(CalenderActivity.this);
						tv.setText("Your Search returned 0 events.\nPlease change your search criteria and try again.");
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
					Toast.makeText(CalenderActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
				}
				
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			pd.cancel();
			super.onPostExecute(res1);
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) 
	{
		RelativeLayout rl=(RelativeLayout)v	;
		String title=((TextView)rl.getChildAt(0)).getText().toString();
		String when=((TextView)rl.getChildAt(1)).getText().toString();
		String time=((TextView)rl.getChildAt(2)).getText().toString();
		String location=((TextView)rl.getChildAt(3)).getText().toString();
		String id=((TextView)rl.getChildAt(4)).getText().toString();
		String decp=((TextView)rl.getChildAt(5)).getText().toString();
		String cat=((TextView)rl.getChildAt(6)).getText().toString();
		
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.list_dialog);
		Button close=(Button)d.findViewById(R.id.ld_close);
		Button reg=(Button)d.findViewById(R.id.ld_reset);
		reg.setTag(id);
		((TextView)d.findViewById(R.id.ld_event)).setText(title);
		((TextView)d.findViewById(R.id.ld_decp)).setText(decp);
		((TextView)d.findViewById(R.id.ld_when)).setText(when);
		((TextView)d.findViewById(R.id.ld_time)).setText(time);
		((TextView)d.findViewById(R.id.ld_loc)).setText(location);
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width=width-width*10/100;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
		reg.setOnClickListener(this);		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		d.cancel();
		String tag=v.getTag().toString();
		if(!v.getTag().equals("0"))
		{
			Intent intent=new Intent(this, WebActivity.class);
			intent.putExtra("url","http://ny.evanced.info/emmaclark/lib/eventsignup.asp?id="+tag );
			startActivity(intent);
		}
	}
	public void onSearch(View v)
	{
		String query=sEditText.getText().toString();
		
		search=query;
		if(query.indexOf(' ')!=-1)
		{
			search=query.replaceAll(" ", "%20");
			
		}
		
		m="Searching event for '"+query+"'";
		new LoadEvents().execute();
	}
	class ForClick implements OnClickListener
	{
		String cat=null;
		ProgressDialog pd;
		List<HashMap<String, String>> list;
		@Override
		public void onClick(View v) 
		{
			dialog.dismiss();
			cat=v.getTag().toString();
			new demo().execute();
			
		}
		class demo extends AsyncTask<Void, Void, Void>
		{

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd=new ProgressDialog(CalenderActivity.this);
				pd.setMessage("Loading..");
				pd.show();
			}
			@Override
			protected Void doInBackground(Void... params) {
				
				String [] month={"January","February","March","April","May","June","July","August","September","October","November","December"};
				if(cat.equalsIgnoreCase("All"))
				{
					red=0;
				}
				else if(cat.equalsIgnoreCase("Todays"))
				{
					red=1;
					Calendar cl=Calendar.getInstance();
					String str=month[cl.get(Calendar.MONTH)]+" "+cl.get(Calendar.DAY_OF_MONTH)+" "+cl.get(Calendar.YEAR);
					cat=str;
				}
				else if(cat.equalsIgnoreCase("Adult"))
				{
					red=2;
				}
				else if(cat.equalsIgnoreCase("Children's"))
				{
					red=3;
				}
				else if(cat.equalsIgnoreCase("Teen"))
				{
					red=4;
				}
				else if(cat.equalsIgnoreCase("Computer"))
				{
					red=5;
				}
				try
				{
					if(res!=null)
					{
						JSONArray jarray = new JSONArray(res);
						list=new ArrayList<HashMap<String,String>>();
						for(int i=0;i<jarray.length();i++)
						{
							HashMap<String, String> map=new HashMap<String, String>();
							JSONObject jobj=jarray.getJSONObject(i);
							map.put("title", jobj.getString("title"));
							map.put("date", jobj.getString("date"));
							map.put("time", jobj.getString("time"));
							map.put("location", jobj.getString("location"));
							map.put("id", jobj.getString("id"));
							map.put("decp", jobj.getString("description"));
							map.put("cate", jobj.getString("category"));
							if(jobj.getString("category").equals(cat) || cat.equals("all") ||jobj.getString("date").indexOf(cat)>=0)
							{
								list.add(map);
							}
						}
					}
					else
					{
					}
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				String [] from={"title","date","time","location","id","decp","cate"};
				int [] to={R.id.list_title,R.id.list_when,R.id.list_time,R.id.list_location,R.id.list_id,R.id.list_decp,R.id.list_cate};
				SimpleAdapter sa=new SimpleAdapter(CalenderActivity.this, list, R.layout.list, from, to);			
				event_list.setAdapter(null);
				event_list.setAdapter(sa);
				pd.dismiss();
				super.onPostExecute(result);
			}
		}
	}
}
