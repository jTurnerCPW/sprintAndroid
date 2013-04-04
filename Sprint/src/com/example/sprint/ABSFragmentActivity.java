package com.example.sprint;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class ABSFragmentActivity extends SherlockFragmentActivity {

	private ActionBar actionBar;
	private final static int BARCODE_SCAN_REQUEST = 2345;

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
			
		case R.id.menu_settings:
			startPreferences();
			return true;
		case R.id.menu_scan:
			startScanner();
			return true;


		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void startScanner() {
		Intent i = new Intent("com.google.zxing.client.android.SCAN");
		i.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(i, BARCODE_SCAN_REQUEST);	
	}

	private boolean startPreferences() {
		if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
			startActivity(new Intent(this, EditPreferences.class));
			return true;
		}
		else {
			Intent intent = new Intent( this, EditPreferencesHC.class );
			/* Adding extras to skip showing headers in the preference activity */
			intent.putExtra( EditPreferencesHC.EXTRA_SHOW_FRAGMENT, StockPreferenceFragment.class.getName() );
			intent.putExtra( EditPreferencesHC.EXTRA_NO_HEADERS, true );
			startActivity(intent);
			return true;
		}
		
	}

	private void goHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
