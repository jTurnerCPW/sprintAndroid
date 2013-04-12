package com.example.sprint;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class JobListFragment extends Fragment{
	
	private PullToRefreshListView jobListView;
	private TextView jobListEmptyTextView;
	private EditText searchText;
	private LinearLayout view;
	private JobListAdapter adapter = null;
	private ArrayList<Job> jobs;
	private Handler mHandler = new Handler();
	JobListJSONTask jobTask;

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
		
		jobListView = (PullToRefreshListView) view.findViewById(R.id.lvJob);
		//add the empty default view
		jobListEmptyTextView = (TextView) view.findViewById(android.R.id.empty);
		jobListView.setEmptyView(jobListEmptyTextView);
		
		jobListView.setAdapter(adapter);
		
		jobListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(final PullToRefreshBase<ListView> lv) {

				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						refreshJobs();
					}
				},0);
			}
		});
	}
	
	public void getJobs() {	
		jobTask = new JobListJSONTask(this);
		jobTask.execute(getActivity());
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
	
	//pretty much what it says.  explore get jobs for more.  basically refreshes list - removes old items and puts in new ones 
	public void refreshJobs() {
		
		//TODO: test this cookies. (clear)
		searchText.setText("");
		getJobs();
	}

	public void notifyJobLoadComplete() {
		
		jobListView.onRefreshComplete();
	}
	
	public void cancelJobTask() {
		 if (jobTask != null && jobTask.getStatus() != Status.FINISHED)
			 jobTask.cancel(true);
	}
	
	@Override 
	public void onDestroy() {
		
		cancelJobTask();
		super.onDestroy();
	}
	
}
