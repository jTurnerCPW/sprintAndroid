package com.example.sprint;

import android.content.Intent;
import android.os.Bundle;

public class JobListActivity extends ABSFragmentActivity{
	
	private String printerName;
	private String jobName;
	private String jobId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the printer name
		Intent intent = getIntent();
		printerName = intent.getStringExtra("printer_name");

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
}
