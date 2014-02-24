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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UniqueServiceActivity extends Activity  implements OnClickListener 
{

	Dialog d;
	ImageButton close;
	boolean resume=true;
	EditText name,telephone,barcode,title,author,format,email;
	String sname,stele,sbar,stitle,sauth,sformat,semai="";
	int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unique_service);
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
	}
	public void onBack(View v)
	{
		//finish();
		onBackPressed();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onAtlantis(View v)
	{
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.atlantis__dialog);
		close=(ImageButton)d.findViewById(R.id.atlantis_close);
		close.setTag("0");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		//lp.width=width-width*10/100;
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
	}
	public void onLocalHistory(View v)
	{
		startActivity(new Intent(this,LocalHistoryActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onMuseum(View v)
	{
		startActivity(new Intent(this, MuseumActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onPAC(View v)
	{
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.pac__dialog);
		close=(ImageButton)d.findViewById(R.id.pac_close);
		close.setTag("0");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		//lp.width=width-width*20/100;
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
	}
	public void onFax(View v)
	{
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.pc_dialog);
		close=(ImageButton)d.findViewById(R.id.pc_close);
		close.setTag("0");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		//lp.width=width-width*20/100;
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
	}
	public void onScanner(View v)
	{
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.scanner_dialog);
		close=(ImageButton)d.findViewById(R.id.scan_close);
		close.setTag("0");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		//lp.width=width-width*20/100;
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
	}
	public void onPrint(View v)
	{
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.print_dialog);
		close=(ImageButton)d.findViewById(R.id.print_close);
		close.setTag("0");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		//lp.width=width-width*20/100;
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
	}
	public void onSuggest(View v)
	{
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.suggestion_dialog);
		close=(ImageButton)d.findViewById(R.id.sug_close);
		close.setTag("0");
		Button submit=(Button)d.findViewById(R.id.sug_subnit);
		Button reset=(Button)d.findViewById(R.id.sug_reset);
		name=(EditText)d.findViewById(R.id.sug_name);
		telephone=(EditText)d.findViewById(R.id.sug_telephone);
		barcode=(EditText)d.findViewById(R.id.sug_barcode);
		title=(EditText)d.findViewById(R.id.sug_title);
		author=(EditText)d.findViewById(R.id.sug_author);
		format=(EditText)d.findViewById(R.id.sug_format);
		email=(EditText)d.findViewById(R.id.sug_email);
		submit.setTag("1");
		reset.setTag("2");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		//lp.width=width-width*10/100;
		lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
		submit.setOnClickListener(this);
		reset.setOnClickListener(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_unique_service, menu);
		return true;
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
	public void onClick(View v) 
	{
		String tag=v.getTag().toString();
		if(tag.equals("0"))
		{
			d.dismiss();
		}
		else if(tag.equals("1"))
		{
			//submit
			if(name.getText().toString().equals("")||telephone.getText().toString().equals("")||barcode.getText().toString().equals("")||title.getText().toString().equals("")|| author.getText().toString().equals("")||format.getText().toString().equals(""))
			{
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setTitle("Required Fields");
				builder.setMessage("You must fill out out your name,telephone and library barcode as well as the item title, item author, and the item format.");
				builder.setPositiveButton("Close", null);
				AlertDialog d=builder.show();
				d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
				WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
				lp.copyFrom(d.getWindow().getAttributes());
				//lp.width=width-width*20/100;
				lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
				d.getWindow().setAttributes(lp);
				((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
				d.show();
			}
			else
			{
				sname=name.getText().toString();
				sname=sname.replaceAll(" ", "%20");
				stele=telephone.getText().toString();
				sbar=barcode.getText().toString();
				stitle=title.getText().toString();
				stitle=stitle.replaceAll(" ", "%20");
				sauth=author.getText().toString();
				sauth=sauth.replaceAll(" ", "%20");
				sformat=format.getText().toString();
				sformat=sformat.replaceAll(" ", "%20");
				semai=email.getText().toString();
				d.cancel();
				new Suggestion().execute();
				
			}
		}
		else if(tag.equals("2"))
		{
			//reset
			name.setText("");
			telephone.setText("");
			barcode.setText("");
			title.setText("");
			author.setText("");
			format.setText("");
			email.setText("");
		}
	}

	class Suggestion extends AsyncTask<Void, Void, Void>
	{

		String result=null;
		ProgressDialog pd;
		String url="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd=new ProgressDialog(UniqueServiceActivity.this);
			pd.setMessage("Proccessing...");
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
				url = "https://epsilon01-us-east-1a.ec2.capiratech.com/mattituckmobile/suggest.php?name="+sname+"&telephone="+stele+"&barcode="+sbar+"&title="+stitle+"&author="+sauth+"&isbn="+CommonData.isbn+"&format="+sformat+"&email="+semai;
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
					AlertDialog.Builder builder=new AlertDialog.Builder(UniqueServiceActivity.this);
					builder.setMessage("Thank you for your submission!");
					
					builder.setPositiveButton("OK", null);
					AlertDialog d=builder.show();
					d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
					WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
					lp.copyFrom(d.getWindow().getAttributes());
					//lp.width=width-width*20/100;
					lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
					d.getWindow().setAttributes(lp);
					((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
					d.show();
				}
				else
				{
					Toast.makeText(UniqueServiceActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			super.onPostExecute(res);
		}
		
	}
	
}

/*

*/