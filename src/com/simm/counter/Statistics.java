package com.simm.counter;

public class Statistics {
	private static Statistics sInstance;
	public static Statistics instance(){
		if (sInstance == null)
			sInstance = new Statistics();
		return sInstance;
	}
	
	private int mFails, mSuccesses;
	
	public int success() { return mSuccesses++; }
	public int fail() { return mFails++; }
	public int getFails() { return mFails; }
	public int getSuccesses() { return mSuccesses; }
	
	public void reset(){
		mFails = 0;
		mSuccesses = 0;
	}
	
	public int getSuccessPersent() {
		int sum = mFails + mSuccesses;
		if (sum == 0)
			return 0;
		
		int result = mSuccesses * 100 / sum;
		return result;
	}
}
