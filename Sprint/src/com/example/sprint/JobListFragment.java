package com.example.sprint;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class JobListFragment extends Fragment{
	
	private ListView jobListView;
	private EditText searchText;
	private LinearLayout view;
	private JobListAdapter adapter = null;
	private ArrayList<Job> jobs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = (LinearLayout) inflater.inflate(R.layout.fragment_job_list, container, false);
		return view;
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

		searchText = (EditText) view.findViewById(R.id.etSearchJob);
		searchText.addTextChangedListener(filterTextWatcher);
		
		jobListView = (ListView) view.findViewById(R.id.lvJob);
		jobListView.setAdapter(adapter);		
	}
	
	public void getJobs() {	
		JobListJSONTask task = new JobListJSONTask(this);
		task.execute(getActivity());
	}
	
	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public void removeJob(String jobId)
	{
		// Loop to find the job and remove it
		for(int i=0;i<jobs.size();i++)
		{
			if(jobs.get(i).getId().equals(jobId))
			{
				jobs.remove(i);
			}
		}
		
		// Refresh the list
		showList();
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
			new JobListAdapter(getActivity(), R.layout.job_list_row, jobs, this);
		return arrayAdapter;
	}
	
	public void refreshJobs() {
		
		onResume();	
	}

}
