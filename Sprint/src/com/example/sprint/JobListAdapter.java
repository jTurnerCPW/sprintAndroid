package com.example.sprint;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JobListAdapter extends ArrayAdapter<Job> {
	
	// Variables
	private int resource;
	private JobListFragment jobListFragment;
	private LayoutInflater inflater;
	private Context context;
	private Filter filter;
	private ArrayList<Job> jobListOriginal;
	private ArrayList<Job> jobListFiltered;

	// Constructor
	public JobListAdapter(Context ctx, int resourceId,
			ArrayList<Job> jobs, JobListFragment jobListFrag) {
		super(ctx, resourceId, jobs);
		jobListOriginal = jobs;
		jobListFiltered = new ArrayList<Job>(jobs);
		resource = resourceId;
		inflater = LayoutInflater.from(ctx);
		context = ctx;
		jobListFragment = jobListFrag;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// create a new view of my layout and inflate it in the row
		convertView = (RelativeLayout) inflater.inflate(resource, null);
		convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Print the job on click
            	//printJob(jobListFiltered.get(position).getId());

                // Start the confirmation activity
            	
            	Intent intent = new Intent(context, PrintConfirmationActivity.class);
        		intent.putExtra("printer_name", ((JobListActivity)jobListFragment.getActivity()).getPrinterName());
        		intent.putExtra("job_id", jobListFiltered.get(position).getId());
        		context.startActivity(intent);
            	
            }
        });
		/* Get the job object from the filtered list position*/
		Job job = jobListFiltered.get(position);

		/* Set the job's name*/
		TextView name = (TextView) convertView
				.findViewById(R.id.tvJobName);
		name.setText(job.getName());

		/* Set the job's user */
		TextView user = (TextView) convertView
				.findViewById(R.id.tvJobUser);
		user.setText(job.getUser());

		/* Set the job image based on the type of job */
		ImageView imageFileType = (ImageView) convertView
				.findViewById(R.id.imgFileType);

		String jobType = job.getType();		
		String uri = "";
		if(jobType.equals("doc") || jobType.equals("txt")) {
			uri = "drawable/worddoc";
		}
		else if(jobType.equals("pdf")) {
			uri = "drawable/pdfdoc";
		}
		else if(jobType.equals("xls")) {
			uri = "drawable/exceldoc";
		}
		else if(jobType.equals("ppt")) {
			uri = "drawable/pptdoc";
		}
		else {
			uri = "drawable/worddoc";
		}
		
		int imageResource = context.getResources().getIdentifier(uri, null,
				context.getPackageName());
		Drawable image = context.getResources().getDrawable(imageResource);
		imageFileType.setImageDrawable(image);

		return convertView;
	}

	@Override
	public Filter getFilter() {
		/* Get a custom filter for the adapter */
		if (filter == null)
			filter = new JobListFilter();

		return filter;
	}
	
	@Override
	public int getCount(){
		/* Get the count based on the filtered printer list size*/
		return jobListFiltered.size();
	}

	/*
	 * class: JobListFilter
	 * description: Custom Filter for jobs.  The filter is implemented
	 * 				to allow filtering on different fields of the job
	 * 				object such as 'name' and 'type'
	 */
	private class JobListFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			FilterResults results = new FilterResults();

			if (constraint == null || constraint.length() == 0) {
				/* No filter. Return the whole list*/
				results.values = jobListOriginal;
				results.count = jobListOriginal.size();
				
			} else {
				/* Filtering*/
				ArrayList<Job> nJobList = new ArrayList<Job>();
				for (Job j : jobListOriginal) {
					/* Compare the upper-case of the printer name with the text input */
					if (j.getName().toUpperCase(Locale.getDefault())
							.startsWith(constraint.toString().toUpperCase(Locale.getDefault()))) {
						/* Add to the new filtered printer list */
						nJobList.add(j);
					}
				}
				results.values = nJobList;
				results.count = nJobList.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count == 0) {
				/* No results matched the text input, so show the whole list */
				jobListFiltered = jobListOriginal;
				notifyDataSetChanged();
			} else {
				/* There are matching results.  Show only those which match */
				jobListFiltered = (ArrayList<Job>) results.values;
				notifyDataSetChanged();
			}
		}
	}
	
	// Move and then Print the job id given
	private void printJob(String jobId) {
		MoveJobTask task = new MoveJobTask(jobListFragment, jobId);
		task.execute(this.getContext());
	}
	
	// Cancel the job id given
	private void cancelJob(String jobId) {
		CancelJobTask task = new CancelJobTask(jobListFragment, jobId);
		task.execute(this.getContext());
	}
}
