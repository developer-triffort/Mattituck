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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SearchCollectionActivity extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener
{

	boolean resume=true;
	SegmentedRadioGroup group;
	EditText searchText;
	ToggleButton tButton;
	int flag=78;
	String keyword="Y";
	String author="a";
	String title="t";
	String urlPart1="http://alpha2.suffolk.lib.ny.us/search/";
	String urlPart2="?SEARCH=(";
	String urlPart3=")&SORT=D&m=&searchscope=";
	String key=keyword;
	String url="";
	int width;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_collection);
		group=(SegmentedRadioGroup)findViewById(R.id.segment_text);
		group.setOnCheckedChangeListener(check);
		searchText=(EditText)findViewById(R.id.ser_col_edit_search);
		tButton=(ToggleButton)findViewById(R.id.ser_col1_toggleButton1);
		tButton.setOnCheckedChangeListener(this);
		Display mDisplay= getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
		//-------------------------
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
	public void onSearch(View v)
	{
		if(searchText.getText().toString().equals(""))
		{
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(R.string.no_input);
			builder.setMessage(R.string.you_must);
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
		else
		{
			resume=false;
			url=urlPart1+key+urlPart2+searchText.getText().toString()+urlPart3+flag;
			Intent intent=new Intent(this,WebActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("title", "/MA");
			startActivity(intent);
		}		
	}
	
	public void onReset(View v)
	{
		searchText.setText("");
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
		getMenuInflater().inflate(R.menu.activity_search_collection, menu);
		return true;
	}
	OnCheckedChangeListener check=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) 
		{
			if(checkedId==R.id.button_one)
			{
				key=keyword;
			}
			else if(checkedId==R.id.button_two)
			{
				key=author;
			}
			else if(checkedId==R.id.button_three)
			{
				key=title;
			}
		}
	};
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		if(isChecked)
		{
			flag=84;
		}
		else
		{
			flag=78;
		}
	}
}

/*
 * changes to make 
*/
