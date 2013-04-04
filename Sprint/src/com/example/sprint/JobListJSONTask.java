package com.example.sprint;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;

public class JobListJSONTask extends AsyncTask<Context, Void, ArrayList<Job>> {
	private JobListFragment jobListFragment;
	ProgressDialog pd;
	
	public JobListJSONTask(JobListFragment jobListFragment) {
		this.jobListFragment = jobListFragment;
	}
	
	@Override
	protected ArrayList<Job> doInBackground(Context... params) {
				
		try{
		    // Create a new HTTP Client and setup the GET
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    HttpGet httpGetRequest = new HttpGet("http://10.24.16.122/show_active_jobs.php");

		    // Execute the request in the client
		    HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
		    
		    // Check if the response was OK
		    if(httpResponse.getStatusLine().getStatusCode() == 200)
		    {
		    	// Convert the httpResponse into a JSONArray
		    	HttpEntity entity = httpResponse.getEntity();
			    InputStream ist = entity.getContent();
			    ByteArrayOutputStream content = new ByteArrayOutputStream();
			    int readCount = 0;
			    byte[] buffer = new byte[1024];
			    while ((readCount = ist.read(buffer)) != -1)
			    {
			    	content.write(buffer, 0, readCount);
			    }
			    String retVal = new String(content.toByteArray());
			    JSONArray jsonArray = new JSONArray(retVal);

			    // List of jobs
			    ArrayList<Job> jobList = new ArrayList<Job>();
			    
			    // Loop through the jsonArray and get the user,title,id,type
			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject row = jsonArray.getJSONObject(i);
			        String user = row.getString("user_name").trim();
			        String name = row.getString("job_title").trim();
			        String id = row.getString("job_id").trim();
			        String type = row.getString("file_type").trim().toLowerCase(Locale.getDefault());
			        
			        jobList.add(new Job(id, name, user, type));
			    }
	      
		        return jobList;
		    }

	        /* Dummy printer info */
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
      
			// Return dummy info if not status 200
	        return jobList;

		} catch(Exception e){
		    // In your production code handle any errors and catch the individual exceptions
		    e.printStackTrace();
		}
		
        return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Job> jobs) {
		jobListFragment.setJobs(jobs);
		jobListFragment.showList();
		pd.dismiss();
	}
	
	@Override
	protected void onPreExecute() {
		jobListFragment.clearList();
		pd = new ProgressDialog(jobListFragment.getActivity());
		pd.setMessage("Loading Jobs ...");
		pd.show();
		final AsyncTask<Context, Void, ArrayList<Job>> task = this;
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				task.cancel(true);
			}
		});
	}	
}
