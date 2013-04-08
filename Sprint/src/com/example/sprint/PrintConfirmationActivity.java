package com.example.sprint;

import android.content.Intent;
import android.os.Bundle;

public class PrintConfirmationActivity extends ABSFragmentActivity{

	private String printerName;
	private String job_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_confirmation);
		Intent intent = getIntent();
		printerName = intent.getStringExtra("printer_name");
		job_id = intent.getStringExtra("job_id");
	}

}
