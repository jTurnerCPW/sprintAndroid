package com.example.sprint;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SprintSherlockActivity extends SherlockFragmentActivity {

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// initialize actionBar
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		
		/*actionBar.addTab(actionBar.newTab()
				.setText("home"))
				.setTabListener(new TabListener(new TabListener<Fragment>(this, "home", Fragment.class, null)));
		
		actionBar.addTab(actionBar.newTab()
				.setText("Other"));*/
		
		
//		actionBar.setTitle("App Title");
//		actionBar.setIcon(R.drawable.printer_icon);
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(false);
//		/*
//		 * actionBar.setBackgroundDrawable(getResources().getDrawable(
//		 * R.drawable.actionbar_gradient));
//		 */

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
