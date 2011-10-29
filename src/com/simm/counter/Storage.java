package com.simm.counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class Storage extends SQLiteOpenHelper {
	private final static String DB_NAME = "counter";
	private final static int DB_VERSION = 1;
	private static Storage mInstance;
	
	private int mBestTime, mWorstTime;
	
	public static Storage instance(Context context){
		if (mInstance == null)
			mInstance = new Storage(context);
		
		return mInstance;
	}
	
	private Storage(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mBestTime = 205;
		mWorstTime = 620;
	}

	public int getBestTime() {return mBestTime;}
	public int getWorstTime() {return mWorstTime;}
	
	
	private String CREATE_TABLE_TIME_STATEMENT = "CREATE TABLE TIME(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
													"time INTEGER, date INTEGER)";
	private String CREATE_TABLE_EXTRAS_STATEMENT = "CREATE TABLE EXTRAS(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
													"key STRING, value STRING)";
	private String TABLE_TIME_NAME = "TIME";
	private String TABLE_TIME_FIELD_TIME_NAME = "time",
					TABLE_TIME_FIELD_DATE_NAME = "date";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EXTRAS_STATEMENT);
		db.execSQL(CREATE_TABLE_TIME_STATEMENT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS TIME");
		db.execSQL("DROP TABLE IF EXISTS EXTRAS");
		onCreate(db);
	}

	public void storeTime(int time) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLE_TIME_FIELD_DATE_NAME, System.currentTimeMillis());
		values.put(TABLE_TIME_FIELD_TIME_NAME, time);
		db.insert(TABLE_TIME_NAME, null, values);
		db.close();
	}

}
