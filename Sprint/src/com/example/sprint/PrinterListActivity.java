package com.example.sprint;


import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PrinterListActivity extends ABSFragmentActivity{
	private String printerName;
	private String jobName;
	private String jobId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the printer name
		Intent intent = getIntent();
		jobName = intent.getStringExtra("job_name");
		jobId = intent.getStringExtra("job_id");
		
		// Set the view to the fragment
		setContentView(R.layout.activity_printer_list);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.refreshable_sherlock, menu);
		return true;
	}
	
	@Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("ABS", "request code: " + requestCode);
    	if(resultCode == RESULT_OK) {
    		String contents = data.getStringExtra("SCAN_RESULT");
    		
    		//dynaTrace End Scanner
    		CompuwareUEM.leaveAction("scannerActivity");
    		
    		// If we have the job name and id already just go to the confirmation
    		if(jobName != null && jobId != null)
    		{
    			// Start the confirmation activity            	
            	Intent intent = new Intent(this, PrintConfirmationActivity.class);
        		intent.putExtra("printer_name", contents);
        		intent.putExtra("job_id", jobId);
        		intent.putExtra("job_name", jobName);
        		startActivity(intent);
    		}
    		else
    		{
    			startJobListActivity(contents);
    		}  			
    	}
	}
	
	public String getPrinterName() {
		return printerName;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public String getJobId() {
		return jobId;
	}
	
	@Override
	public void refreshList(){
		PrinterListFragment plfrag = (PrinterListFragment)getSupportFragmentManager().findFragmentById(R.id.pl_fragment);
		plfrag.refreshPrinters();
	}

}
