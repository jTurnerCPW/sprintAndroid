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

import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.widget.Toast;

public class PrintJobTask extends AsyncTask<Context, Void, String> {
	private PrintConfirmationFragment printConfirmationFragment;
	private String jobId;
	ProgressDialog pd;
	
	// Constructor for the print job task
	public PrintJobTask(PrintConfirmationFragment printConfirmationFrag, String jobId) {
		this.printConfirmationFragment = printConfirmationFrag;
		this.jobId = jobId;
	}
	
	@Override
	protected String doInBackground(Context... params) {
				
		try{
		    // Create a new HTTP Client and setup the Post
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    HttpPost httpPostRequest = new HttpPost("http://10.24.16.122/release_print_job.php");;
		    		
		    // Add the job id to the post
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
			    
			    // Check if the printing of the job was successful
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
		
		// Check if the HTTP Post was successful
		if(result.equals("Success"))
		{
			// Dynatrace Print Job Leave if Successful
			CompuwareUEM.leaveAction("Print Job");
			
			// TODO: Dynatrace the Printer Name
			
			
			// TODO: Dynatrace the Username
			
			
			// Save the printer used to the recent printers list
			((PrintConfirmationActivity) printConfirmationFragment.getActivity()).saveToRecentPrinter(
					((PrintConfirmationActivity) printConfirmationFragment.getActivity()).getPrinterName());
			
			// Toast that it printed
			Toast.makeText(printConfirmationFragment.getActivity(), 
					"Your job has been sent to the printer!", Toast.LENGTH_LONG).show();
			
			// Go back to the jobs page
			printConfirmationFragment.goBackToJobs();
		}
		else
		{
			// Dynatrace Print Job Leave if Failed
			CompuwareUEM.reportEvent("Print Job - Failed");
			CompuwareUEM.leaveAction("Print Job");			
			
			// Error - Job couldn't be printed
			Toast.makeText(printConfirmationFragment.getActivity(), 
					"Error - Couldn't print job!", Toast.LENGTH_LONG).show();
		}
		
		// Dismiss the progress dialog
		pd.dismiss();
	}
	
	@Override
	protected void onPreExecute() {
		
		// Dynatrace Print Job Enter
		CompuwareUEM.enterAction("Print Job");
				
		// Create a progress dialog for sending the print job
		pd = new ProgressDialog(printConfirmationFragment.getActivity());
		pd.setMessage("Sending Print Job...");
		pd.show();
		final AsyncTask<Context, Void, String> task = this;
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// Allows canceling the task
				task.cancel(true);
			}
		});
	}	
}
