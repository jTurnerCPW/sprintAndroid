package com.example.sprint;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class JobListActivity extends Activity implements OnItemClickListener{

	private ListView jobListView;
	private EditText searchText;
	private JobListAdapter adapter = null;
	private ArrayList<Job> jobs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Log.v("JobListActivity", "onTextChanged");
			adapter.getFilter().filter(s);
		}

	};
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Get all jobs
		getJobs();

		searchText = (EditText) findViewById(R.id.etSearchJob);
		searchText.addTextChangedListener(filterTextWatcher);
		
		jobListView = (ListView) findViewById(R.id.lvJob);
		jobListView.setAdapter(adapter);
		
		/*  Set up the on-click methods next */
		jobListView.setOnItemClickListener(this);
	}
	
	private void getJobs() {	
		JobListJSONTask task = new JobListJSONTask(this);
		task.execute(this);
	}
	
	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public void showList() {
		adapter = buildAdapter();
		jobListView.setAdapter(adapter);
	}
	
	public void clearList() {
		if(jobListView != null) {
			jobListView.setAdapter(null);
		}
	}
	
	private JobListAdapter buildAdapter() {
		final JobListAdapter arrayAdapter =
			new JobListAdapter(this, R.layout.job_list_row, jobs);
		return arrayAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
