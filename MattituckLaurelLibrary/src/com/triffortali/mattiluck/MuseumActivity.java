package com.triffortali.mattiluck;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MuseumActivity extends Activity {

	boolean resume=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_museum);
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
	public void onAmerican(View v)
	{
		start("http://www.folkartmuseum.org/index.php?p=folk&id=517", getString(R.string.web_american));
	}
	public void onSpaceMuseum(View v)
	{
		start("http://www.intrepidmuseum.org/", getString(R.string.web_space));
	}
	public void onNatural(View v)
	{
		start("http://www.amnh.org/", getString(R.string.web_natural));
	}
	public void onBrooklyn(View v)
	{
		start("http://www.brooklynmuseum.org/", getString(R.string.web_brook));
	}
	public void onCMEE(View v)
	{
		start("http://cmee.org/", getString(R.string.web_cmee));
	}
	public void onFrick(View v)
	{
		start("http://www.frick.org/", getString(R.string.web_frick));
	}
	public void onGuggen(View v)
	{
		start("http://www.guggenheim.org/new-york/", getString(R.string.web_g));
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_museum, menu);
		return true;
	}
}
