package com.triffortali.mattiluck;


import com.onbarcode.barcode.android.AndroidColor;
import com.onbarcode.barcode.android.AndroidFont;
import com.onbarcode.barcode.android.Code128;
import com.onbarcode.barcode.android.IBarcode;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DigitalLibraryActivity extends Activity 
{
	ImageView iv;
	boolean resume=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digital_library);
		iv=(ImageView)findViewById(R.id.barcode);
		try
		{
			Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.digitalcard_demo).copy(Config.ARGB_8888, true);
			iv.setImageBitmap(bm);
			Canvas cn=new Canvas(bm);
			Paint p=new Paint();
			p.setColor(Color.YELLOW);
			Code128 barcode = new Code128();
			//barcode.setData("112233445566");
			barcode.setData(CommonData.bc);
			//barcode.setData("062711227906");
			barcode.setProcessTilde(false);
			barcode.setUom(IBarcode.UOM_PIXEL);
			int ht=bm.getHeight();
			RectF rf=null;
			Rect r=null;
			int x1=0,y1=0,x2=0,y2=0;
			if(ht<550)
			{
				barcode.setX(5f);
				barcode.setY(100f);
				rf=new RectF(80,340,725,550);
				r=new Rect(85, 345, 720, 100);
				x1=80;y1=345;y2=345;x2=720;
				p.setStrokeWidth(13);
				p.setColor(Color.WHITE);
				barcode.setTextFont(new AndroidFont("Arial", Typeface.NORMAL, 25));
			}	
			else if (ht<800)
			{
				barcode.setX(7f);
				barcode.setY(150f);
				rf=new RectF(150,520,1080,705);
				r=new Rect(160, 525	,1000 , 180);
				x1=160;y1=525;x2=1000;y2=525;
				p.setStrokeWidth(20);
			//	Toast.makeText(this, "20", 1).show();
				barcode.setTextFont(new AndroidFont("Arial", Typeface.NORMAL, 35));
			}	
			else
			{
				barcode.setX(10f);
				barcode.setY(200f);
				rf=new RectF(160,700,1450,940);
				r=new Rect(170, 710, 1400, 210);
				x1=170;y1=710;x2=1400;y2=710;
				p.setStrokeWidth(40);
				p.setColor(Color.WHITE);
			//	Toast.makeText(this, "30", 1).show();
				barcode.setTextFont(new AndroidFont("Arial", Typeface.NORMAL, 50));
			}		
			
			//barcode.setShowText(true);
			//barcode.setTextMargin(6);
		
		
			barcode.setTextColor(AndroidColor.black);
	
			// barcode bar color and background color in Android device
			barcode.setForeColor(AndroidColor.black);
			//barcode.setResolution(70);
			//barcode.setAutoResize(true);
			barcode.setBackColor(AndroidColor.white);
			
			barcode.drawBarcode(cn, rf);
			//cn.drawRect(r, p);
			cn.drawLine(x1, y1, x2, y2	, p);
			iv.setImageBitmap(bm);
			iv.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rt));
		}
		catch (Exception e)
		{
			// TODO: handle exception
			//Toast.makeText(this, ""+e, 1).show();
		}
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
		getMenuInflater().inflate(R.menu.activity_digital_library, menu);
		return true;
	}

}
