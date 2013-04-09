package com.example.sprint;

import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class DashboardActivity  extends ABSFragmentActivity {
	private static final int EDIT_ID = Menu.FIRST+2;
	private final static int BARCODE_SCAN_REQUEST = 2345;
	private int dialogsShowing = 0;
	String user_name;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.dashboard); 
    }
    
    

	@Override
	protected void onResume() {
		super.onResume();
		
		//TODO: insert some checks to see if there are dialogs up already
		Log.v("dialogCountResume", "" + dialogsShowing);
		
		//check the username and password to see if they have been set correctly
		checkUsrname();
		checkWifi();
	}

	private void checkUsrname() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		user_name = prefs.getString(getResources().getString(R.string.pref_sprintUsername_key), "");
		
		if (user_name.length()==0 && dialogsShowing == 0){

			displayUserNameAlert();
		}	
	}
	
	private void checkWifi() {
		
		if( isConnectedViaWifi() == false && dialogsShowing == 0) {

			createNetworkDisabledAlert();
		}
	}
	
	private boolean isConnectedViaWifi() {

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		//check and see first if the network has connectivity at all.  
		if (networkInfo != null && networkInfo.isConnected()) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			
			//next if the phone has connectivity, make sure it is connected via wi-fi
			if( ! cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() ) {

				//there is a connection but it is not wifi...
				return false;
			}	

			//connection exists and itsa-da wifi
			return true;
		} else {

			//no connection
			return false;
		}
	}

	private void createNetworkDisabledAlert(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your wi-fi connection is not active!  You will need to connect to the Compuware network to use Sprint.  ")
				.setCancelable(true)
				.setPositiveButton("Enable Wi-Fi",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						showNetworkOptions();
						dialog.cancel();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//Canceled.
				
				//TODO: FOLLOW HAN INTO THE TRASH COMPACTOR.  
				//really, chewy, cancel calls dismiss.  its okay. 
				//dialogsShowing--;
				//Log.v("dialogCountNetCan", "" + dialogsShowing);
				
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		
		//update the count of displaying dialogs back down to 0 when this dialog is dismissed or canceled.  
		alert.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(final DialogInterface dialog){
				
				dialogsShowing--;
				Log.v("dialogCountNetDis", "" + dialogsShowing);
				checkUsrname();	
			}
		});
		
		dialogsShowing++;
		Log.v("dialogCountNetShow", "" + dialogsShowing);
		alert.show();
	}

	private void showNetworkOptions() {

		Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
		startActivityForResult(i, 5);
	}
	
	

	private void displayUserNameAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.pref_sprintUsername_Title));
		builder.setMessage(getResources().getString(R.string.pref_sprintUsername_summary));

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setHint(R.string.sprintUsername_hint);
		builder.setView(input);

		builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Editor editor = prefs.edit();
				editor.putString(getResources().getString(R.string.pref_sprintUsername_key), value);
				editor.commit();
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				
				//TODO: FOLLOW HAN INTO THE TRASH COMPACTOR.  
				//really, chewy, cancel calls dismiss.  its okay. 
				//dialogsShowing--;
				//Log.v("dialogCountUsrCan", "" + dialogsShowing);
				
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		
		alert.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(final DialogInterface dialog){
				
				dialogsShowing--;
				Log.v("dialogCountUsrDis", "" + dialogsShowing);
				checkWifi();
			}
		});
		
		dialogsShowing++;
		Log.v("dialogCountUsrShow", "" + dialogsShowing);
		alert.show();
	}

	public void startScanner(View view) {

		Intent i = new Intent();	
		/* setting the action string.  No other apps should respond to this request
		 * because of the unique action string
		 */
		i.setAction("com.compuware.pdp.sprint");
		i.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(i, BARCODE_SCAN_REQUEST);
		
		//dynaTrace Metric for scanning
		CompuwareUEM.enterAction("scannerActivity");
	}


	public boolean startPreferences(View view){
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

	public void startPrinterListActivity(View view){
		Intent intent = new Intent(this, PrinterListActivity.class);
		startActivity(intent);
	}

	public void startJobListActivity(View view){
		Intent intent = new Intent(this, JobListActivity.class);
		startActivity(intent);
	}

	@Override
	protected void goHome(){

	}
}
