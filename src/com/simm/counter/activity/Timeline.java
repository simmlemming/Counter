package com.simm.counter.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.simm.counter.R;

public class Timeline extends Activity {

	
	private Gallery mGallery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_timeline);
		mGallery = (Gallery) findViewById(R.id.activity_timeline_gallery);
		
		mGallery.setAdapter(new TimesAdapter());
	
	}

	private static class TimelineItem{
		public long date;
		public long time;
		public TimelineItem(long date, long time){
			this.date = date;
			this.time = time;
		}
	}
	
	public static class TimesAdapter extends BaseAdapter{
		private List<TimelineItem> mItems;
		
		public TimesAdapter(){
			mItems = new ArrayList<TimelineItem>();
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			mItems.add(new TimelineItem(0, 599));
			}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}	
		
	}
}
