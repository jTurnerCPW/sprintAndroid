package com.example.sprint;

import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DashboardActivity  extends ABSFragmentActivity {
	private static final int EDIT_ID = Menu.FIRST+2;
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

			//displayUserNameAlert();
			usernameDialog();
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
	
	public void usernameDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    final View layout = inflater.inflate(R.layout.username_confirmation, (ViewGroup) findViewById(R.id.root));
	    final EditText username1 = (EditText) layout.findViewById(R.id.EditTextUsername1);
	    final EditText username2 = (EditText) layout.findViewById(R.id.EditTextUsername2);
	    final TextView error = (TextView) layout.findViewById(R.id.TextViewUsernameProblems);
	      
	    // Check the usernames as they type
	    username2.addTextChangedListener(new TextWatcher() {
	    	   public void afterTextChanged(Editable s) {
	    	      String strPass1 = username1.getText().toString();
	    	      String strPass2 = username2.getText().toString();
	    	      if (strPass1.equals(strPass2)) {
	    	         error.setText("Usernames Match");
	    	      } else {
	    	         error.setText("Usernames Do NOT Match");
	    	      }
	    	   }
	    	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	    	   public void onTextChanged(CharSequence s, int start, int before, int count) {}
	    });
	    
	    // Create the dialog
		final AlertDialog d = new AlertDialog.Builder(this)
        .setView(layout)
        .setTitle(R.string.pref_sprintUsername_Title)
        .setPositiveButton(android.R.string.ok,
                new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        //Do nothing here. We override the onclick
                    }
                })
        .create();
	
		// Listener for once the dialog is shown
		d.setOnShowListener(new DialogInterface.OnShowListener() {
		
		    @Override
		    public void onShow(DialogInterface dialog) {
		
		        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {
		
		            @Override
		            public void onClick(View view) {
		            	// Check the usernames against each other
			    	    String strUsername1 = username1.getText().toString();
			    	    String strUsername2 = username2.getText().toString();
			    	    if (strUsername1.equals(strUsername2)) {
			    	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			  				Editor editor = prefs.edit();
			  				editor.putString(getResources().getString(R.string.pref_sprintUsername_key), strUsername1);
			  				editor.commit();
			  				d.dismiss();
			    	    }  
		            }
		        });
		    }
		});
		
		// Handle the dismiss of the dialog
		d.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(final DialogInterface dialog){
				
				dialogsShowing--;
				checkWifi();
			}
		});
		
		// Show the dialog
		dialogsShowing++;	
		d.show();
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
	
	@Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("ABS", "request code: " + requestCode);
    	if(resultCode == RESULT_OK) {
    		String contents = data.getStringExtra("SCAN_RESULT");
    		
    		// Dynatrace Scanner Time Leave
    		CompuwareUEM.leaveAction("Scan Time");
    		
    		startJobListActivity(contents);
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
	
	// Disable back button
		public void onBackPressed() {
			this.finish();
		}
}
