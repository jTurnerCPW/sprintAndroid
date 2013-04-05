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
import android.widget.Toast;

public class MoveJobTask extends AsyncTask<Context, Void, String> {
	private JobListFragment jobListFragment;
	private String jobId;
	private String printerName;
	ProgressDialog pd;
	
	// Constructor for the move job task
	public MoveJobTask(JobListFragment jobListFrag, String jobId) {
		this.jobListFragment = jobListFrag;
		this.jobId = jobId;
		this.printerName = ((JobListActivity) jobListFragment.getActivity()).getPrinterName();
	}
	
	@Override
	protected String doInBackground(Context... params) {
				
		try{
		    // Create a new HTTP Client and setup the Post
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    HttpPost httpPostRequest = new HttpPost("http://10.24.16.122/move_print_job.php");;
		    		
		    // Add the job id and printer name to the post
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
	        nameValuePairs.add(new BasicNameValuePair("printer_name", printerName));
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
			    
			    // Check if the moving was successful or not
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
		    e.printStackTrace();
		}
		
        return "Failed";
	}
	
	@Override
	protected void onPostExecute(String result) {
		// Check if the moving of the print job was successful
		if(result.equals("Success"))
		{
			// Print the Job
			PrintJobTask task = new PrintJobTask(jobListFragment, jobId);
			task.execute(jobListFragment.getActivity());
		}
		else
		{
			// Error - Job couldn't be moved
			Toast.makeText(jobListFragment.getActivity(), 
                    "Error - Couldn't move job to printer!", Toast.LENGTH_LONG).show();
		}
		
		// Dismiss the progress dialog
		pd.dismiss();
	}
	
	@Override
	protected void onPreExecute() {
		// Create a progress dialog for moving the job
		pd = new ProgressDialog(jobListFragment.getActivity());
		pd.setMessage("Moving Print Job...");
		pd.show();
		final AsyncTask<Context, Void, String> task = this;
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// Allow the user to cancel the loading
				task.cancel(true);
			}
		});
	}	
}
