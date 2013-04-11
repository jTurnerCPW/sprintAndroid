package com.example.sprint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class JobListActivity extends ABSFragmentActivity{
	
	private String printerName;
	private String jobName;
	private String jobId;
	private String source;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the printer name
		Intent intent = getIntent();
		printerName = intent.getStringExtra("printer_name");
		source = intent.getStringExtra("source");
		// Set the view to the fragment
		setContentView(R.layout.activity_job_list);	
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.jobs_refreshable_sherlock, menu);
		return true;
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
	
	//
	@Override
	public void refreshList(){
		JobListFragment jlfrag = (JobListFragment)getSupportFragmentManager().findFragmentById(R.id.jl_fragment);
		jlfrag.refreshJobs();
	}
	
	@Override  
	public void onBackPressed() {
		//Printer defined - either coming from scanner or select printer
		if(printerName!=null && source==null)
		{
			//Comes from scanner
			Intent intent = new Intent(this, DashboardActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		else
		{
			super.onBackPressed();
		}
	}
}
