package com.example.sprint;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
	private JobListAdapter adapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_list);
		context = this;

		// Get all printers
		ArrayList<Job> jobList = getJobs();

		searchText = (EditText) findViewById(R.id.etSearchJob);
		searchText.addTextChangedListener(filterTextWatcher);
		
		jobListView = (ListView) findViewById(R.id.lvJob);
		adapter = new JobListAdapter(context, R.layout.job_list_row,
				jobList);
		jobListView.setAdapter(adapter);
		
		/*  Set up the on-click methods next */
		jobListView.setOnItemClickListener(this);
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

	private ArrayList<Job> getJobs() {
		ArrayList<Job> jobList = new ArrayList<Job>();
		
		/* Dummy job info */
		jobList.add(new Job("1", "Compuware.doc",
				"Josh", "doc"));
		jobList.add(new Job("2", "Detroit.pdf",
				"Mike", "pdf"));
		jobList.add(new Job("3", "Payroll.xls",
				"Kim", "xls"));		
		jobList.add(new Job("4", "Watch.jpeg",
				"Kobby", "jpeg"));
		jobList.add(new Job("5", "Bike.png",
				"Jackson", "png"));
		jobList.add(new Job("6", "Vacation.gif",
				"Rich", "gif"));
		return jobList;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
