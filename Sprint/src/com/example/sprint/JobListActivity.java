package com.example.sprint;

import android.content.Intent;
import android.os.Bundle;

public class JobListActivity extends ABSFragmentActivity{
	
	private String printerName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_list);
		
		Intent intent = getIntent();
		printerName = intent.getStringExtra("printer_name");
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		
		// !!!
		//getSupportMenuInflater().inflate(R.menu.refreshable_sherlock, menu);
		return true;
	}
	
	public String getPrinterName() {
		return printerName;
	}

}
