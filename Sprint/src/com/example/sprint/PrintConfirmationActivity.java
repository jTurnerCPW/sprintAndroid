package com.example.sprint;

import android.content.Intent;
import android.os.Bundle;

public class PrintConfirmationActivity extends ABSFragmentActivity{

	private String printerName;
	private String jobId;
	private String jobName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		printerName = intent.getStringExtra("printer_name");
		jobId = intent.getStringExtra("job_id");
		jobName = intent.getStringExtra("job_name");
		
		setContentView(R.layout.activity_print_confirmation);		
	}
	
	public String getPrinterName() {
		return printerName;
	}
	
	public String getJobId() {
		return jobId;
	}
	
	public String getJobName() {
		return jobName;
	}

}
