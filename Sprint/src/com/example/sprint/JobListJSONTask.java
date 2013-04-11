package com.example.sprint;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class JobListJSONTask extends AsyncTask<Context, Void, ArrayList<Job>> {
	private JobListFragment jobListFragment;
	ProgressDialog pd;
	
	// Constructor for the JobListJSON Task
	public JobListJSONTask(JobListFragment jobListFragment) {
		this.jobListFragment = jobListFragment;
	}
	
	@Override
	protected ArrayList<Job> doInBackground(Context... params) {
				
		try{
		    // Create a new HTTP Client and setup the POST
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    HttpPost httpPostRequest = new HttpPost("http://10.24.16.122/show_active_jobs.php");
		    
		    // Grab the username from preferences
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(jobListFragment.getActivity());
		    String prefsUserName = preferences.getString("pref_sprintUsername","");
		    		
		    // Add the username to the POST Data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("user_name", prefsUserName));
	        httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
		    // Execute the request in the client
		    HttpResponse httpResponse = defaultClient.execute(httpPostRequest);
		    
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
			    
			    // Attempt to convert the return value to a JSON Array
			    try {
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
			    } catch (Exception e) {
			    	e.printStackTrace();
			    }

			   return new ArrayList<Job>();
		    }

	        return new ArrayList<Job>();

		} catch(Exception e){
		    e.printStackTrace();
		}
		
        return new ArrayList<Job>();
	}
	
	@Override
	protected void onPostExecute(ArrayList<Job> jobs) {
		// Set the jobs and show the list and dismiss the progress dialog
		jobListFragment.setJobs(jobs);
		jobListFragment.showList();
		pd.dismiss();		
		jobListFragment.notifyJobLoadComplete();
		
		// Dynatrace Job List Load Leave
		CompuwareUEM.leaveAction("Job List Load");
	}
	
	@Override
	protected void onPreExecute() {

		// Dynatrace Job List Load Enter
		CompuwareUEM.enterAction("Job List Load");		
		
		// Clear the list and start a progress dialog for loading jobs
		jobListFragment.clearList();
		pd = new ProgressDialog(jobListFragment.getActivity());
		pd.setMessage("Loading Jobs ...");
		pd.show();
		final AsyncTask<Context, Void, ArrayList<Job>> task = this;
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// Allows canceling the task
				task.cancel(true);
			}
		});
	}	
}
