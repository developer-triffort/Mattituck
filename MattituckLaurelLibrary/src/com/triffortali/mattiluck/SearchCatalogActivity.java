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

import com.triffortali.mattiluck.UniqueServiceActivity.Suggestion;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class SearchCatalogActivity extends Activity implements OnClickListener ,android.content.DialogInterface.OnClickListener
{

	boolean resume=true;
	AlertDialog.Builder  builder;
	AlertDialog dialog;
	EditText ISBN;
	String search;
	Dialog d;
	EditText name,telephone,barcode,title,author,format,email;
	String sname,stele,sbar,stitle,sauth,sformat,semai="";
	boolean iscamera=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_catalog);
		PackageManager pm = this.getPackageManager();
		iscamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
	public void onBack(View v)
	{
		//finish();
		onBackPressed();
	}
	@Override
	public void onBackPressed() 
	{
		CommonData.isbn="";
		super.onBackPressed();
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onISBN(View v)
	{
		if(iscamera)
		{
		builder=new AlertDialog.Builder(this);
		builder .setTitle("Scan ISBN");
		builder.setMessage("Would you like to scan an ISBN using your device camera, or enter it manually?");
		LinearLayout linear=new LinearLayout(this);
		linear.setGravity(Gravity.CENTER);
		linear.setOrientation(LinearLayout.VERTICAL);
		
		Button b1=new Button(this);
		b1.setText("Enter Manually");
		b1.setTag("1");
		b1.setOnClickListener(this);
		linear.addView(b1);
		
		Button b2=new Button(this);
		b2.setText("Use Camera");
		b2.setTag("2");
		b2.setOnClickListener(this);
		linear.addView(b2);
		
		TextView tv1=new TextView(this);
		tv1.setText("How would you like?");
		tv1.setGravity(Gravity.CENTER_HORIZONTAL);
		linear.addView(tv1);
		
		tv1.setVisibility(View.INVISIBLE);
		
		Button b3=new Button(this);
		b3.setText("Cancel");
		b3.setOnClickListener(this);
		b3.setTag("10");
		linear.addView(b3);
		
		
		builder.setView(linear);
		dialog = builder.show();
		//dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.show();
		}
		else
		{
		showManualDialog();
		}
	}
	public void onSearchCollection(View v)
	{
		startActivity(new Intent(this, SearchCollectionActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onMovie(View v)
	{
		startActivity(new Intent(this, SearchMovieActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onAudio(View v)
	{
		startActivity(new Intent(this, SearchAudiobookActivity.class));
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
		getMenuInflater().inflate(R.menu.activity_search_catalog, menu);
		return true;
	}
	@Override
	public void onClick(View v) 
	{
	
		String str=v.getTag().toString();
		dialog.cancel();
		if(str.equals("1"))
		{
			//manually
			showManualDialog();
		}
		else if (str.equals("2"))
		{
			//bar code
			Intent intent =new Intent(this, BCActivity.class);
			startActivityForResult(intent, 1);
		}
		else if(str.equals("0"))
		{
			d.dismiss();
		}
		else if(str.equals("3"))
		{
			//submit
			if(name.getText().toString().equals("")||telephone.getText().toString().equals("")||barcode.getText().toString().equals("")||title.getText().toString().equals("")|| author.getText().toString().equals("")||format.getText().toString().equals(""))
			{
				AlertDialog.Builder builder=new AlertDialog.Builder(SearchCatalogActivity.this);
				builder.setTitle("Required Fields");
				builder.setMessage("You must fill out out your name, the item title, and the item format.");
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
			else
			{
				/*sname=name.getText().toString();
				stele=telephone.getText().toString();
				sbar=barcode.getText().toString();
				stitle=title.getText().toString();
				sauth=author.getText().toString();
				sformat=format.getText().toString();
				semai=email.getText().toString();*/
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
		else if(str.equals("4"))
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==RESULT_OK)
		{
			String str=data.getStringExtra("res");
			search=str;
			CommonData.isbn=str;
			/*AlertDialog.Builder b=new AlertDialog.Builder(this);
			b.setMessage(str);
			b.show();*/
			new Searchisbn().execute();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	void showManualDialog()
	{
		builder=new AlertDialog.Builder(this);
		builder .setTitle("Enter ISBN");
		builder.setMessage("Enter the ISBN you want to search for below.");
		LinearLayout linear=new LinearLayout(this);
		linear.setGravity(Gravity.CENTER);
		linear.setOrientation(LinearLayout.VERTICAL);			
		TextView tv1=new TextView(this);
		tv1.setText("How would you like?");
		tv1.setGravity(Gravity.CENTER_HORIZONTAL);
		linear.addView(tv1);
		tv1.setVisibility(View.INVISIBLE);
		
		ISBN=new EditText(this);
		ISBN.setHint("Enter ISBN Here");
		ISBN.setInputType(InputType.TYPE_CLASS_NUMBER);
		linear.addView(ISBN);
		builder.setView(linear);
		builder.setPositiveButton("Cancel", null);
		builder.setNegativeButton("Search", this);
		
		
		dialog = builder.show();
		//dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.show();
	}
	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
		search=ISBN.getText().toString();
		//Toast.makeText(this, ""+search, 1).show();
		new Searchisbn().execute();
	}
	
	void showNotFound(String title,String msg)
	{
		builder=new AlertDialog.Builder(SearchCatalogActivity.this);
		builder .setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("No", null);
		builder.setNegativeButton("Yes", click);
		dialog = builder.show();
		//dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.show();
	}
	android.content.DialogInterface.OnClickListener click =new android.content.DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			showSuggestion();
		}
	};
	void showSuggestion()
	{
		//Toast.makeText(SearchCatalogActivity.this, "show Suggestion", 1).show();
		Display mDisplay= getWindowManager().getDefaultDisplay();
		int width= mDisplay.getWidth();
		d=new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		d.setContentView(R.layout.suggestion_dialog);
		ImageButton close=(ImageButton)d.findViewById(R.id.sug_close);
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
		submit.setTag("3");
		reset.setTag("4");
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width=width-width*10/100;
		lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.windowAnimations=R.style.dailog_animation;
		d.getWindow().setAttributes(lp);
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		d.show();
		close.setOnClickListener(this);
		submit.setOnClickListener(this);
		reset.setOnClickListener(this);
		
	}
	class Searchisbn extends AsyncTask<Void, Void, Void>
	{

		String result=null;
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd=new ProgressDialog(SearchCatalogActivity.this);
			pd.setMessage("Searching for ISBN...");
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
				String url = "https://epsilon01-us-east-1a.ec2.capiratech.com/mattituckmobile/isbnlookup.php?isbn="+search;
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
					JSONArray jarray = new JSONArray(result);
					JSONObject jobj=jarray.getJSONObject(0);
					String result_code=jobj.getString("result");
					if(result_code.equals("1"))
					{
						String url=jobj.getString("message");
						Intent intent=new Intent(SearchCatalogActivity.this, WebActivity.class);
						intent.putExtra("url", url);
						startActivity(intent);
					}
					else
					{
						showNotFound(jobj.getString("title"),jobj.getString("message"));
					}
				}
				else
				{
					Toast.makeText(SearchCatalogActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			super.onPostExecute(res1);
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
			pd=new ProgressDialog(SearchCatalogActivity.this);
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
					AlertDialog.Builder builder=new AlertDialog.Builder(SearchCatalogActivity.this);
					builder.setMessage("Thank you for your submission!");
					
					builder.setPositiveButton("OK", null);
					AlertDialog d=builder.show();
					d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
					WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
					lp.copyFrom(d.getWindow().getAttributes());
					//lp.width=width-width*20/100;
					d.getWindow().setAttributes(lp);
					((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
					d.show();
				}
				else
				{
					Toast.makeText(SearchCatalogActivity.this, "Network connection timeout", Toast.LENGTH_LONG).show();
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
