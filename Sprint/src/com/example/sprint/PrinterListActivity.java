package com.example.sprint;


import android.os.Bundle;

public class PrinterListActivity extends ABSFragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer_list);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.refreshable_sherlock, menu);
		return true;
	}
	
	@Override
	public void refreshList(){
		PrinterListFragment plfrag = (PrinterListFragment)getSupportFragmentManager().findFragmentById(R.id.pl_fragment);
		plfrag.refreshJobs();
	}

}
