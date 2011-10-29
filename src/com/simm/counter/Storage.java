package com.simm.counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
		mBestTime = calculateBestTime();
		mWorstTime = calculateWorstTime();
	}

	public int getBestTime() {return mBestTime;}
	public int getWorstTime() {return mWorstTime;}
	
	private String KEY_BEST_TIME = "best_time",
					KEY_WORST_TIME = "worst_time";
	
	private int calculateBestTime(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("SELECT MIN(" + TABLE_TIME_FIELD_TIME_NAME + ") AS best_time FROM " + TABLE_TIME_NAME, null);
		int result = Integer.MAX_VALUE;
		if (c.moveToFirst()){
			result = c.getInt(c.getColumnIndex("best_time"));
		}
		c.close();
		db.close();
		return result == 0 ? Integer.MAX_VALUE : result;
	}

	private int calculateWorstTime(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("SELECT MAX(" + TABLE_TIME_FIELD_TIME_NAME + ") AS worst_time FROM " + TABLE_TIME_NAME, null);
		int result = Integer.MIN_VALUE;
		if (c.moveToFirst()){
			result = c.getInt(c.getColumnIndex("worst_time"));
		}
		c.close();
		db.close();
		return result == 0 ? Integer.MIN_VALUE : result;
	}

	
	private void insertOrUpdate(String key, String value){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLE_EXTRAS_FIELD_KEY_NAME, key);
		values.put(TABLE_EXTRAS_FIELD_VALUE_NAME, value);
		if (containsExtra(db, key)){
			db.update(TABLE_EXTRAS_NAME, values, TABLE_EXTRAS_FIELD_KEY_NAME + "=?", new String[] {key});
		} else {
			db.insert(TABLE_EXTRAS_NAME, null, values);
		}
		db.close();
	}

	private String getExtraString(String key, String fallback){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_EXTRAS_NAME, new String[] {TABLE_EXTRAS_FIELD_VALUE_NAME},
				TABLE_EXTRAS_FIELD_KEY_NAME + " = ?", new String[] {key}, null, null, null);
		String result = fallback;
		if (c.moveToFirst()){
			result = c.getString(c.getColumnIndex(TABLE_EXTRAS_FIELD_VALUE_NAME));
		}
		c.close();
		db.close();
		return result;
	}
	
	public boolean containsExtra(SQLiteDatabase db, String key){

		Cursor c = db.query(TABLE_EXTRAS_NAME, new String[] {TABLE_EXTRAS_FIELD_KEY_NAME},
				TABLE_EXTRAS_FIELD_KEY_NAME + "=?", new String[] {key}, null, null, null);
		boolean result = c.getCount() > 0;
		c.close();
		return result;
	}
	
	private String CREATE_TABLE_TIME_STATEMENT = "CREATE TABLE TIME(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
													"time INTEGER, date INTEGER)";
	private String CREATE_TABLE_EXTRAS_STATEMENT = "CREATE TABLE EXTRAS(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
													"key STRING, value STRING)";
	private String TABLE_TIME_NAME = "TIME", TABLE_EXTRAS_NAME = "EXTRAS";
	private String TABLE_TIME_FIELD_TIME_NAME = "time",
					TABLE_TIME_FIELD_DATE_NAME = "date",
					TABLE_EXTRAS_FIELD_KEY_NAME = "key",
					TABLE_EXTRAS_FIELD_VALUE_NAME = "value"
					;
	
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

	public void deleteTime(long timeId){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_TIME_NAME, "_id = ?", new String[] {String.valueOf(timeId)});
		db.close();
		mBestTime = calculateBestTime();
		mWorstTime = calculateWorstTime();
//		mAdapterForTimeList.changeCursor(getCursorForTimeList());
//		mAdapterForTimeList.notifyDataSetChanged();
	}
	
	
	
	public boolean storeTime(int time) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLE_TIME_FIELD_DATE_NAME, System.currentTimeMillis());
		values.put(TABLE_TIME_FIELD_TIME_NAME, time);
		db.insert(TABLE_TIME_NAME, null, values);
		db.close();
		
		boolean result = false;
		if (time > mWorstTime){
			result = true;
			mWorstTime = time;
		}
		
		if (time < mBestTime){
			result = true;
			mBestTime = time;
		}
		return result;	
	}

	private Cursor getCursorForTimeList(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_TIME_NAME,
				new String[] {"_id", TABLE_TIME_FIELD_TIME_NAME, TABLE_TIME_FIELD_DATE_NAME},
				null,
				null,
				null,
				null,
				null);
		return c;
	}
	
	private CursorAdapter mAdapterForTimeList;
	public CursorAdapter getTimeListAdapter(Context context, int[] to){
		
		CursorAdapter mAdapterForTimeList =  new CursorAdapter(context, getCursorForTimeList()) {
			
			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent) {
				LayoutInflater inflater = LayoutInflater.from(context);
				View view = inflater.inflate(R.layout.timelist_item, null);
				
				fillView(view, cursor);
				
				return view;
			}
			
			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				fillView(view, cursor);
				
			}
			
			private View fillView(View view, Cursor cursor){
				int time = cursor.getInt(cursor.getColumnIndex(TABLE_TIME_FIELD_TIME_NAME));
				long date = cursor.getLong(cursor.getColumnIndex(TABLE_TIME_FIELD_DATE_NAME));
				
				TextView timeView = (TextView) view.findViewById(R.id.timelist_item_time);
				TextView dateView = (TextView) view.findViewById(R.id.timelist_item_date);
				
				timeView.setText(DateFormat.format("mm:ss", time * 1000));
				dateView.setText(DateFormat.format("dd/MM/yyyy", date));

				return view;
			}
		};
		
		return mAdapterForTimeList;
	}
}
