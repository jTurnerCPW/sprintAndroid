package com.example.sprint;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.util.Log;

public class PrintJobTask extends AsyncTask<Context, Void, String> {
	private JobListFragment jobListFragment;
	private String jobId;
	ProgressDialog pd;
	
	public PrintJobTask(JobListFragment jobListFragment, String jobId) {
		this.jobListFragment = jobListFragment;
		this.jobId = jobId;
	}
	
	@Override
	protected String doInBackground(Context... params) {
				
		try{
		    // Create a new HTTP Client and setup the Post
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    HttpPost httpPostRequest = new HttpPost("http://10.24.16.122/release_print_job.php");;
		    		
		    // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
	        httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
		    // Execute the request in the client
		    HttpResponse httpResponse = defaultClient.execute(httpPostRequest);
		    
		    // Check if the response was OK
		    if(httpResponse.getStatusLine().getStatusCode() == 200)
		    {
		    	// Convert the httpResponse into a string
		    	HttpEntity entity = httpResponse.getEntity();
			    InputStream ist = entity.getContent();
			    ByteArrayOutputStream content = new ByteArrayOutputStream();
			    int readCount = 0;
			    byte[] buffer = new byte[1024];
			    while ((readCount = ist.read(buffer)) != -1)
			    {
			    	content.write(buffer, 0, readCount);
			    }
			    String results = new String(content.toByteArray());
			    
		    	if(results.equals("0"))
		    	{
		    		return "Success";
		    	}
		    	else
		    	{
		    		return "Failed";
		    	}
		    }
		    else
		    {
		    	return "Failed";
		    }

		} catch(Exception e){
		    // In your production code handle any errors and catch the individual exceptions
		    e.printStackTrace();
		}
		
        return "Failed";
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		// Log the result of the print
		Log.v("PRINT RESULT = ", result);
		
		// If the print was successful then remove from the list
		if(result.equals("Success"))
		{
			jobListFragment.removeJob(jobId);
		}
		else
		{
			jobListFragment.getJobs();
		}

		pd.dismiss();
	}
	
	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(jobListFragment.getActivity());
		pd.setMessage("Sending Print Job...");
		pd.show();
		final AsyncTask<Context, Void, String> task = this;
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				task.cancel(true);
			}
		});
	}	
}
