package com.example.sprint;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

public class MainActivity extends Activity {

	private static final int EDIT_ID = Menu.FIRST+2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

						//TODO:check with mike re: ABS - do will the menu work ok with sherlock and no inflate?
						//if no, figure out how to change the "settings" menu option to my "edit prefs"
	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.main, menu);
	//		return true;
	//	}
	//	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, EDIT_ID, Menu.NONE, "Edit Prefs")
		.setIcon(R.drawable.misc)
		.setAlphabeticShortcut('e');

		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case EDIT_ID:
			if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
				startActivity(new Intent(this, EditPreferences.class));
			}
			else {
				startActivity(new Intent(this, EditPreferencesHC.class));
			}
			return(true);
		}

		return(super.onOptionsItemSelected(item));
	}

	public void startScanner(View view) {
		Intent intent = new Intent(this, ScannerActivity.class);
		startActivity(intent);
	}

	public void startPrinterListActivity(View view){
		Intent intent = new Intent(this, PrinterListActivity.class);
		startActivity(intent);
	}
	
	public void startJobListActivity(View view){
		Intent intent = new Intent(this, JobListActivity.class);
		startActivity(intent);
	}
	
	/*HELLLO CHANGES*/
	/*Mikes changes*/
	/* Mikes more changes */
	/* Mikes more more changes */

}
