package com.example.sprint;


import android.content.Intent;
import android.os.Bundle;

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
