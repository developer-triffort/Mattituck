package com.triffortali.mattiluck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper 
{

	public static final String name="Data";
	public static final String tab_name="use_data";
	public static final String tab_col="use_data_col";
	public static final String tab_col2="barcode";
	
	public DataManager(Context context) {
		super(context, name, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table use_data (use_data_col integer,barcode text)");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.w(DataManager.class.getName(), "Upgrading database from version "+oldVersion+" to"+newVersion+", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+tab_name);
		onCreate(db);
	}

}
