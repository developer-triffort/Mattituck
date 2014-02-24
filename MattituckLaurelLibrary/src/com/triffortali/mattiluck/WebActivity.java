package com.triffortali.mattiluck;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebActivity extends Activity implements android.content.DialogInterface.OnClickListener 
{

	WebView web;
	ProgressDialog pd;
	TextView tv;
	String title="";
	ProgressBar pb;
	Button back,far;
	String openUrl="";
	int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
		pb=(ProgressBar)findViewById(R.id.web_progress);
		tv=(TextView)findViewById(R.id.web_title);
		web=(WebView)findViewById(R.id.web);
		back=(Button)findViewById(R.id.back_btn);
		far=(Button)findViewById(R.id.forward_btn);
		Bundle b=getIntent().getExtras();
		String url=b.getString("url");
		title=b.getString("title");
		if(url!=null)
		{
			loadUrl(url);
		}
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
	}
	public void onOpen(View v)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		TextView tv=new TextView(this);
		tv.setText(openUrl);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		if(!openUrl.equals(""))
		builder.setView(tv);
		builder.setPositiveButton("Open in Browser", this);
		AlertDialog d=builder.show();
		d.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width=width-width*10/100;
		d.getWindow().setAttributes(lp);
		d.show();
	}
	public void onBack(View v)
	{
		finish();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	void loadUrl(String url)
	{
		web.loadUrl(url);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setAllowFileAccess(true);
		web.getSettings().setDefaultZoom(ZoomDensity.FAR);
		web.getSettings().setSupportZoom(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.setWebViewClient(new Demo());
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web, menu);
		return true;
	}
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	public void onRefresh(View v)
	{
		web.reload();
		pb.setVisibility(View.VISIBLE);
	}
	public void onBackPage(View v)
	{
		if(web.canGoBack())
		{
			web.goBack();
			//Toast.makeText(this, "Back",1).show();
		}
	}
	public void onForwardPage(View v)
	{
		if(web.canGoForward())
		{
			web.goForward();
			//Toast.makeText(this, "Forward",1).show();
		}
	}
	class Demo extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) 
		{
			super.onPageStarted(view, url, favicon);
			pb.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) 
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
			Toast.makeText(WebActivity.this, "Error code- "+errorCode+"\nDescription- "+description, 1).show();
		}
		@Override
		public void onPageFinished(WebView view, String url) 
		{
			super.onPageFinished(view, url);
			//if(title!=null && title.equals(""))
			//{
				tv.setText(view.getTitle());
			//}
			tv.setVisibility(View.VISIBLE);
			pb.setVisibility(View.INVISIBLE);
			openUrl=url;
			//process();
		}
	}
	void process()
	{
		if(web.canGoBack())
		{
			back.setEnabled(true);
		}
		else
		{
			back.setEnabled(false);
		}
		if(web.canGoForward())
		{
			far.setEnabled(true);
		}
		else
		{
			back.setEnabled(false);
		}
	}
	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
		if(!openUrl.equals(""))
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(openUrl));
			startActivity(browserIntent);
		}
	}
}
