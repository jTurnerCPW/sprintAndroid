package com.example.sprint;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ABSFragmentActivity extends SherlockFragmentActivity {

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// initialize actionBar
		actionBar = getSupportActionBar();
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main_sherlock, menu);
		return true;
	}

	

	private void goHome() {
		// TODO Auto-generated method stub
		
	}

}
