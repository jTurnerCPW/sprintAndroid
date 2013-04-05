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
	
	public String getPrinterName() {
		return printerName;
	}

}
