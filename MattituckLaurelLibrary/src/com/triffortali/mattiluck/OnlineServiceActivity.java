package com.triffortali.mattiluck;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.text.style.LineHeightSpan.WithDensity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class OnlineServiceActivity extends Activity implements OnClickListener
{

	boolean resume=true;
	AlertDialog.Builder  builder;
	AlertDialog dialog;
	String main_url="";
	String main_title="";
	String market_id="";
	int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_service);
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
	public void onLive(View v)
	{
		main_url="http://www.live-brary.com/";
		main_title=getString(R.string.web_over);
		start(main_url, main_title);
	}
	public void onOver(View v)
	{
		main_url="http://downloads.live-brary.com/";
		main_title=getString(R.string.web_over);
		market_id="com.overdrive.mobile.android.mediaconsole";
		showDialogs(R.string.sv_over, R.string.overdrive_text,0);
	}
	public void onZinio(View v)
	{
		main_url="http://rbdg.envionsoftware.com/mattitucklaurelny/zinio";
		main_title=getString(R.string.web_zinio);
		market_id="com.zinio.mobile.android.reader";
		showDialogs(R.string.sv_zinio, R.string.zinio_text,0);
	}
	public void onFreegal(View v)
	{
		main_url="https://mattlibrary.freegalmusic.com/users/indlogin";
		main_title=getString(R.string.web_freegal);
		market_id="com.libraryideas.freegalmusic";
		showDialogs(R.string.sv_freegal, R.string.freegal_text,0);
	}
	public void onP4(View v)
	{
		main_url="http://www.p4aantiquesreference.com/library/mattituck.asp";
		main_title=getString(R.string.web_p4);
		start(main_url, main_title);
	}
	public void onEsequels(View v)
	{
		main_url="http://www.esequels.com/portal.asp?id=matti";
		main_title=getString(R.string.web_es);
		start(main_url, main_title);
	}
	public void onRef(View v)
	{
		//main_url="https://alpha1.suffolk.lib.ny.us/patroninfo~S84?/0/redirect=/validae?url=http%3A%2F%2F0-www.referenceusa.com.alpha1.suffolk.lib.ny.us%3A80%2F";
		main_url="http://0-www.referenceusa.com.alpha1.suffolk.lib.ny.us/";
		main_title=getString(R.string.web_ref);
		showDialogs(R.string.sv_rr, R.string.ref_text,1);
	}
	public void onMango(View v)
	{
		main_url="https://alpha1.suffolk.lib.ny.us/patroninfo~S84?/0/redirect=/validae?url=http%3A%2F%2F0-libraries.mangolanguages.com.alpha1.suffolk.lib.ny.us%3A80%2Fsuffolk%2Fstart";
		main_title=getString(R.string.web_ref);
		showDialogs(R.string.sv_m, R.string.mango_text,1);
	}
	void showDialogs(int title,int msg,int f)
	{
		builder=new AlertDialog.Builder(this);
		builder .setTitle(title);
		builder.setMessage(msg);
		if(f==0)
		{
			builder.setPositiveButton(R.string.Later, this);
			builder.setNegativeButton(R.string.download,this);
		}
		else
		{
			builder.setPositiveButton("OK",this);
		}
		dialog = builder.show();
		dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
		WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width=width-width*10/100;
		dialog.getWindow().setAttributes(lp);
		dialog.show();
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
	void start(String url,String title)
	{
		resume=false;
		Intent intent=new Intent(this,WebActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_online_service, menu);
		return true;
	}
	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
		if(which==-1)
		{
			start(main_url, main_title);
		}
		else if(which==-2)
		{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id="+market_id));
			startActivity(intent);
		}
	}
}
