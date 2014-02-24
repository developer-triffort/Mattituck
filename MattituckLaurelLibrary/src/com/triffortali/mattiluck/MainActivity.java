package com.triffortali.mattiluck;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements AnimationListener
{

	boolean resume=true;
	RelativeLayout rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		SQLiteDatabase db=new DataManager(this).getReadableDatabase();
		SharedPreferences sp=getApplicationContext().getSharedPreferences("mattituck",Context.MODE_PRIVATE );
		CommonData.rec=sp.getInt("key", 0);
		Cursor cr=db.query(DataManager.tab_name, null, null, null, null, null, null);
		if(cr.moveToLast())
		{
			String bar=cr.getString(1);
			if(!bar.equals(""))
			{
				CommonData.isconfigured=true;
				CommonData.bc=bar;
			}
			db.close();
		}
		else
		{
			db.close();
			Intent intent=new Intent(this, SetupNotificationActivity.class);
			intent.putExtra("flag", true);
			startActivity(intent);
		}
		Animation am=AnimationUtils.loadAnimation(this, R.anim.demo_anim);
		am.setAnimationListener(this);
		rl=(RelativeLayout)findViewById(R.id.splash);
		findViewById(R.id.splash_demo).startAnimation(am);		
	}
	public void onSearchCatalog(View v)
	{
		startActivity(new Intent(this, SearchCatalogActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onLibrary(View v)
	{
		startActivity(new Intent(this, LibraryInfoActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onNews(View v)
	{
		resume=false;
		Intent intent=new Intent(this,WebActivity.class);
		intent.putExtra("url", "http://www.mattlibrary.org/images/file/Newsletter/2013%20Spring%20Newletter.pdf");
		intent.putExtra("title", getString(R.string.web_map));
		startActivity(intent);
	}
	public void onAcount(View v)
	{
		startActivity(new Intent(this, MyAccountActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onCalender(View v)
	{
		startActivity(new Intent(this, CalenderActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onDatabase(View v)
	{
		startActivity(new Intent(this, DatabaseActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onService(View v)
	{
		startActivity(new Intent(this,OnlineServiceActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onUnique(View v)
	{
		startActivity(new Intent(this,UniqueServiceActivity.class));
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		rl.setVisibility(View.GONE);
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
