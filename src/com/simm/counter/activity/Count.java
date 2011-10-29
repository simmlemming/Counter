package com.simm.counter.activity;

import com.simm.counter.R;
import com.simm.counter.Statistics;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Count extends Activity implements OnClickListener{
	
	private TextView mPersents;
	private Button mSuccess, mFail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		
		mPersents = (TextView) findViewById(R.id.header_text_right);
		mSuccess = (Button) findViewById(R.id.count_button_success);
		mFail = (Button) findViewById(R.id.count_button_fail);
		
		mSuccess.setOnClickListener(this);
		mFail.setOnClickListener(this);
		
		
		Statistics.instance().reset();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.count_button_success:
			int s = Statistics.instance().success();
			mSuccess.setText(String.valueOf(s));
			break;
		case R.id.count_button_fail:
			int f = Statistics.instance().fail();
			mFail.setText(String.valueOf(f));
			break;
		}
		mPersents.setText(String.format("%d%%", Statistics.instance().getSuccessPersent()));
		
	}

}
