package com.example.sprint;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class ABSFragmentActivity extends SherlockFragmentActivity {

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// initialize actionBar
		actionBar = getSupportActionBar();

		actionBar.setTitle(getResources().getString(R.string.app_name));
		actionBar.setIcon(R.drawable.printer_icon);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main_sherlock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			goHome();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void goHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
