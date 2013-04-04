package com.example.sprint;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

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
import android.util.Log;

public class PrinterListJSONTask extends AsyncTask<Context, Void, ArrayList<Printer>> {
	private PrinterListFragment printerListFragment;
	ProgressDialog pd;
	
	public PrinterListJSONTask(PrinterListFragment printerListFragment) {
		Log.v("PrinterListJSONTask", "constructor");
		this.printerListFragment = printerListFragment;
	}
	
	@Override
	protected ArrayList<Printer> doInBackground(Context... params) {
		Log.v("PrinterListJSONTask", "background");
		try{
		    // Create a new HTTP Client and setup the GET
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    HttpGet httpGetRequest = new HttpGet("http://10.24.16.122/show_printers.php");

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

			    // List of printers
			    ArrayList<Printer> printerList = new ArrayList<Printer>();
			    
			    // Loop through the jsonArray and get the name and port
			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject row = jsonArray.getJSONObject(i);
			        String name = row.getString("printer_name");
			        String port = row.getString("port_name");
			        
			        printerList.add(new Printer(name,
			        		port, false));
			    }
	      
		        return printerList;
		    }

	        /* Dummy printer info */
		    ArrayList<Printer> printerList = new ArrayList<Printer>();
			printerList.add(new Printer("Rich's printer",
					"location of printer 1", false));
			printerList.add(new Printer("Josh's printer",
					"location of printer 2", false));
			printerList.add(new Printer("Kim's printer",
					"location of printer 3", false));			
			printerList.add(new Printer("Vinny's printer",
					"location of printer 4", false));
			printerList.add(new Printer("Mike's printer",
					"location of printer 5", false));
			printerList.add(new Printer("Jackson's printer",
					"location of printer 6", false));
      
			// Return dummy info if not status 200
			Log.v("PrinterListJSONTask", "dummy list");
	        return printerList;

		} catch(Exception e){
		    // In your production code handle any errors and catch the individual exceptions
			Log.v("PrinterListJSONTask", "catching errors");
		    e.printStackTrace();
		}
		
        return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Printer> printers) {
		printerListFragment.setPrinters(printers);
		printerListFragment.showList();
		pd.dismiss();
	}
	
	@Override
	protected void onPreExecute() {
		printerListFragment.clearList();
		pd = new ProgressDialog(printerListFragment.getActivity());
		pd.setMessage("Loading Printers ...");
		pd.show();
		final AsyncTask<Context, Void, ArrayList<Printer>> task = this;
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				task.cancel(true);
			}
		});
	}	
}
