package com.example.sprint;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
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
            	
            	// If we don't have the printer yet go to the printer selection
            	if(((JobListActivity)context).getPrinterName() == null)
            	{
            		Intent intent = new Intent(context, PrinterListActivity.class);
            		intent.putExtra("job_id", jobListFiltered.get(position).getId());
            		intent.putExtra("job_name", jobListFiltered.get(position).getName());
            		intent.putExtra("job_type", jobListFiltered.get(position).getType());
            		intent.putExtra("document_owner", jobListFiltered.get(position).getUser());
            		context.startActivity(intent);
            	}
            	else
            	{
            		// Start the confirmation activity            	
                	Intent intent = new Intent(context, PrintConfirmationActivity.class);
            		intent.putExtra("printer_name", ((JobListActivity)context).getPrinterName());
            		intent.putExtra("job_id", jobListFiltered.get(position).getId());
            		intent.putExtra("job_name", jobListFiltered.get(position).getName());
            		intent.putExtra("job_type", jobListFiltered.get(position).getType());
            		intent.putExtra("document_owner", jobListFiltered.get(position).getUser());
            		context.startActivity(intent);
            	}	
            }
        });
		
		ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.btnDeleteJob);
		imageButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) { 
				cancelJobAlert(jobListFiltered.get(position).getId());
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
		
		//this will happen if CUPS is not identifying the filetype
		if(jobType.equals("unknown")) {
			uri = "drawable/text";
		}
		
		//text files
		else if(jobType.equals("doc") || jobType.equals("docx") || jobType.equals("txt") || jobType.equals("rtf") || jobType.equals("odf")  || jobType.equals("gdoc")) {
			uri = "drawable/text";
		}
		//pdf
		else if(jobType.equals("pdf")) {
			uri = "drawable/pdfdoc";
		}
		//spreadsheets
		else if(jobType.equals("xls")  || jobType.equals("xlsx") || jobType.equals("csv") || jobType.equals("numbers") || jobType.equals("ods") || jobType.equals("gsheet")) {
			uri = "drawable/spreadsheet";
		}
		//presentations
		else if(jobType.equals("ppt") || jobType.equals("pptx") || jobType.equals("pps") || jobType.equals("key") || jobType.equals("keynote") || jobType.equals("gslides")) {
			uri = "drawable/presentation";
		}
		//web docs
		else if(jobType.equals("asp") || jobType.equals("aspx") || jobType.equals("htm") || jobType.equals("html") || jobType.equals("js") || jobType.equals("jsp") ||
				jobType.equals("php") || jobType.equals("rss") || jobType.equals("xml") || jobType.equals("xhtml") || jobType.equals("xaml")) {
			uri = "drawable/web";
		}
		//code files >:(
		else if(jobType.equals("c") || jobType.equals("class") || jobType.equals("cpp") || jobType.equals("cs") || jobType.equals("h") || jobType.equals("java") ||
				jobType.equals("m") || jobType.equals("pl") || jobType.equals("py") || jobType.equals("sh")) {
			uri = "drawable/code";
		}
		//some images...
		else if(jobType.equals("ai") || jobType.equals("svg") || jobType.equals("tiff") || jobType.equals("tif") || jobType.equals("tga") || jobType.equals("pspimage") ||
				jobType.equals("psd") || jobType.equals("png") || jobType.equals("jpg") || jobType.equals("gif") || jobType.equals("bmp") || jobType.equals("jpg") ||
				jobType.equals("jpeg") || jobType.equals("xcf")) {
			uri = "drawable/image";
			
		}
		//fall back on generic text icon
		else {
			uri = "drawable/text";
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
							.contains(constraint.toString().toUpperCase(Locale.getDefault()))) {
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

	// Cancel the job id given
	private void cancelJob(String jobId) {
		CancelJobTask task = new CancelJobTask(jobListFragment, jobId);
		task.execute(this.getContext());
	}
	
	private void cancelJobAlert(String job_id) {
		final String jobId = job_id;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Job");
        builder.setMessage("Are you sure you want to cancel this job?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	cancelJob(jobId);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
	}
}
