package com.simm.counter.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.simm.counter.R;
import com.simm.counter.Storage;

public class Timer extends Activity implements OnClickListener{
	
	private Button mStart, mStore;
	private TextView mTimeView;
	private TextView mRecordsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_timer);
		mStart = (Button) findViewById(R.id.activity_timer_button_start);
		mStore = (Button) findViewById(R.id.activity_timer_button_store);
		mStart.setOnClickListener(this);
		mStore.setOnClickListener(this);
		mTimeView = (TextView) findViewById(R.id.activity_timer_text_time);
		mRecordsView = (TextView) findViewById(R.id.header_text_right);
		mRecordsView.setOnClickListener(this);
		updateTimeInUi();
		updateRecordsInUi();
		
	}

	
	private boolean mIsGoing = false; 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_timer_button_start:
			if (mIsGoing)
				onStopGoing();
			else
				onStartGoing();
			break;
		case R.id.activity_timer_button_store:
			onStore();
			break;
		case R.id.header_text_right:
			break;
		}
	}
	
	private Handler mTicker = new Handler();
	private int mCurrentTimeSeconds;
	private Runnable mTick = new Runnable() {
		
		@Override
		public void run() {
			if (!mIsGoing) return;
			mCurrentTimeSeconds++;
			updateTimeInUi();
			mTicker.postDelayed(mTick, 1000);
		}
	};
	
	
//	DateFormat mFormat = new DateFormat();
	private void updateTimeInUi(){
		mTimeView.setText(DateFormat.format("mm:ss", mCurrentTimeSeconds * 1000));
	}
	
	private void updateRecordsInUi(){
		String best = DateFormat.format("mm:ss",Storage.instance(this).getBestTime() * 1000).toString();
		String worst = DateFormat.format("mm:ss",Storage.instance(this).getWorstTime() * 1000).toString();
		mRecordsView.setText(best +"/" + worst);
	}
	
	private void onStartGoing(){
		mIsGoing = true;
		mStart.setText("Stop");
		mStore.setEnabled(false);
		mTicker.postDelayed(mTick, 1000);
	}

	private void onStopGoing(){
		mIsGoing = false;
		mStart.setText("Start");
		mStore.setEnabled(true);
	}
	
	private void onStore(){
		if (mCurrentTimeSeconds == 0)
			return;
		mCurrentTimeSeconds = 0;
		updateTimeInUi();
		Storage.instance(this).storeTime(mCurrentTimeSeconds);
	}
}
