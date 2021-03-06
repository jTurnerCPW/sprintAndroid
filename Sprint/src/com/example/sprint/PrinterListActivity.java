package com.example.sprint;


import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PrinterListActivity extends ABSFragmentActivity{
	private String jobName;
	private String jobId;
	private String jobType;
	private String jobOwner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the printer name
		Intent intent = getIntent();
		jobName = intent.getStringExtra("job_name");
		jobId = intent.getStringExtra("job_id");
		jobType = intent.getStringExtra("job_type");
		jobOwner = intent.getStringExtra("document_owner");
		
		// Set the view to the fragment
		setContentView(R.layout.activity_printer_list);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		getSupportMenuInflater().inflate(R.menu.refreshable_sherlock, menu);
		return true;
	}
	
	@Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("ABS", "request code: " + requestCode);
    	if(resultCode == RESULT_OK) {
    		String contents = data.getStringExtra("SCAN_RESULT");
    		
    		// Dynatrace Scanner Time Leave
    		CompuwareUEM.leaveAction("Scan Time");
    		
    		// If we have the job name and id already just go to the confirmation
    		if(jobName != null && jobId != null)
    		{
    			// Start the confirmation activity            	
            	Intent intent = new Intent(this, PrintConfirmationActivity.class);
        		intent.putExtra("printer_name", contents);
        		intent.putExtra("job_id", jobId);
        		intent.putExtra("job_name", jobName);
        		intent.putExtra("job_type", jobType);
        		intent.putExtra("document_owner", jobOwner);
        		startActivity(intent);
    		}
    		else
    		{
    			startJobListActivity(contents);
    		}  			
    	}
	}
	
	
	
	public String getJobName() {
		return jobName;
	}
	
	public String getJobId() {
		return jobId;
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public String getDocumentOwner() {
		return jobOwner;
	}
	
	@Override
	public void refreshList(){
		PrinterListFragment plfrag = (PrinterListFragment)getSupportFragmentManager().findFragmentById(R.id.pl_fragment);
		plfrag.refreshPrinters();
	}

}
