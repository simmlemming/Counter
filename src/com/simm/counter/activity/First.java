package com.simm.counter.activity;

import com.simm.counter.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class First extends Activity implements OnClickListener{
    
	private Button mCount, mHistory;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        mCount = (Button) findViewById(R.id.first_button_count);
        mCount.setOnClickListener(this);
        
        mHistory = (Button) findViewById(R.id.first_button_history);
        mHistory.setOnClickListener(this);
               
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.first_button_count:
			Intent i = new Intent(this, Count.class);
			startActivity(i);
			break;
		case R.id.first_button_history:
			i = new Intent(this, Timer.class);
			startActivity(i);
			break;

		default:
			break;
		}
		
	}
}