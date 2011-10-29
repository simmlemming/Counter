package com.simm.counter.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.simm.counter.R;
import com.simm.counter.Storage;

public class TimeList extends ListActivity {
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_timelist);
		
		mListView = getListView();
		final CursorAdapter adapter = Storage.instance(this).getTimeListAdapter(this, new int[] {R.id.timelist_item_time, R.id.timelist_item_date});
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, final long arg3) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(TimeList.this);
//				String date = ((TextView)arg1.findViewById(R.id.timelist_item_date)).getText().toString();
				String time = ((TextView)arg1.findViewById(R.id.timelist_item_time)).getText().toString();
				dialog.setTitle("Confirm").setMessage("Delete time: " + time + "?");
				dialog.setPositiveButton("Yes", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Storage.instance(TimeList.this).deleteTime(arg3);
						mListView.setAdapter(Storage.instance(TimeList.this).getTimeListAdapter(TimeList.this, new int[] {R.id.timelist_item_time, R.id.timelist_item_date}));
						dialog.dismiss();
					}
					
				});
				
				dialog.setNegativeButton("No", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
					
				});
				dialog.create().show();
				return true;
			}
		});
	}
	
}
