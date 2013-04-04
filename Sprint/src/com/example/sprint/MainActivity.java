package com.example.sprint;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int EDIT_ID = Menu.FIRST+2;
	private final static int BARCODE_SCAN_REQUEST = 2345;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

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

		return(super.onOptionsItemSelected(item));
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String user_name = prefs.getString(getResources().getString(R.string.pref_sprintUsername_key), "");
		
		/* if the username is not "" */
		if (user_name.length()==0){
			
			displayUserNameAlert();
		}
	}

	private void displayUserNameAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getResources().getString(R.string.pref_sprintUsername_Title));
		alert.setMessage(getResources().getString(R.string.pref_sprintUsername_summary));

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = prefs.edit();
			editor.putString(getResources().getString(R.string.pref_sprintUsername_key), value);
			editor.commit();
			}
		});

		alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
		
	}

	public void startScanner(View view) {

		Intent i = new Intent("com.google.zxing.client.android.SCAN");
		i.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(i, BARCODE_SCAN_REQUEST);
		
	}
	
	@Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
    	if(requestCode == BARCODE_SCAN_REQUEST && resultCode == RESULT_OK) {
    		String contents = data.getStringExtra("SCAN_RESULT");
    		
    		Toast toast = Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_LONG);
    		toast.show();
    	}
	}

	public void startPrinterListActivity(View view){
		Intent intent = new Intent(this, PrinterListActivity.class);
		startActivity(intent);
	}
	
	public void startJobListActivity(View view){
		Intent intent = new Intent(this, JobListActivity.class);
		startActivity(intent);
	}

}
