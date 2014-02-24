package com.triffortali.mattiluck;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LibraryInfoActivity extends Activity implements OnClickListener 
{

	int width;
	AlertDialog.Builder  builder;
	AlertDialog dialog;
	boolean resume=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_info);
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
	public void onGenInfo(View v)
	{
		startActivity(new Intent(this,GeneralInfoActivity.class));
		overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	}
	public void onContact(View v)
	{
		builder=new AlertDialog.Builder(this);
		builder .setTitle("Contact Us");
		builder.setMessage("How would you like to reach us?");
		LinearLayout linear=new LinearLayout(this);
		linear.setGravity(Gravity.CENTER);
		linear.setOrientation(LinearLayout.VERTICAL);
		TextView tv=new TextView(this);
		tv.setText("How would you like to reach us?");
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setVisibility(View.GONE);
		linear.addView(tv);
		Button b1=new Button(this);
		b1.setText("Call Us");
		b1.setTag("1");
		b1.setOnClickListener(this);
		linear.addView(b1);
		
		Button b2=new Button(this);
		b2.setText("Email Us");
		b2.setTag("2");
		b2.setOnClickListener(this);
		linear.addView(b2);
		
		TextView tv1=new TextView(this);
		tv1.setText("How would you like to reach us?");
		tv1.setGravity(Gravity.CENTER_HORIZONTAL);
		linear.addView(tv1);
		
		tv1.setVisibility(View.INVISIBLE);
		
		Button b3=new Button(this);
		b3.setText("Cancel");
		b3.setOnClickListener(this);
		b3.setTag("0");
		linear.addView(b3);
		
		
		builder.setView(linear);
		dialog = builder.show();
		//dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
		((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.show();
	}
	public void onDirction(View v)
	{
		resume=false;
		Intent intent=new Intent(this,WebActivity.class);
		intent.putExtra("url", "http://maps.google.com/maps?daddr=Mattituck-Laurel+Library,+13900+Main+Road,+Mattituck,+NY+11952");
		intent.putExtra("title", getString(R.string.web_map));
		startActivity(intent);
	}
	public void onGetCard(View v)
	{
		resume=false;
		Intent intent=new Intent(this,WebActivity.class);
		intent.putExtra("url", "http://www.mattlibrary.org/library-information/applying-for-card/");
		intent.putExtra("title", R.string.web_card);
		startActivity(intent);
	}
	public void onVisit(View v)
	{
		onGlobe(v);
	}
	public void onFacebook(View v)
	{
		resume=false;
		Intent intent=new Intent(this, WebActivity.class);
		intent.putExtra("url","http://m.facebook.com/MattituckLaurelLibrary?id=205766845132&refsrc=http%3A%2F%2Fwww.facebook.com%2FMattituckLaurelLibrary&_rdr" );
		startActivity(intent);
		
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
		getMenuInflater().inflate(R.menu.activity_library_info, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String tag=v.getTag().toString();
		if(tag.equals("1"))
		{
			dialog.dismiss();
			onDial(v);
		}
		else if(tag.equals("2"))
		{
			dialog.dismiss();
			onEmail(v);
		}
		else  if(tag.equals("0"))
		{
			dialog.dismiss();
		}
	}

}
