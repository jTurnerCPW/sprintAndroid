package com.example.sprint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PrintConfirmationActivity extends ABSFragmentActivity{
	private String storageFileName = "sprintapp.txt";
	private String printerName;
	private String jobId;
	private String jobName;
	private String jobType;
	private String docOwner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		printerName = intent.getStringExtra("printer_name");
		jobId = intent.getStringExtra("job_id");
		jobName = intent.getStringExtra("job_name");
		jobType = intent.getStringExtra("job_type");
		docOwner = intent.getStringExtra("document_owner");
		
		setContentView(R.layout.activity_print_confirmation);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.confirmation_menu, menu);
		return true;
	}
	
	public String getPrinterName() {
		return printerName;
	}
	
	public String getJobId() {
		return jobId;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public String getDocumentOwner() {
		return docOwner;
	}
	
	//Return concatenated list of recent printers. Separated by ';'
	public String getRecentPrinters()
	{
		FileInputStream fis;
		String content = "";
		 
		try 
		{
			//Create file & if it exists return the recent printers
			File file = getFileStreamPath(storageFileName);
			if(file.exists())
			{
				fis = openFileInput(storageFileName);
				byte[] input = new byte[fis.available()];
				while (fis.read(input) != -1 && fis.read(input)!=0) 
				{
					content += new String(input);
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace(); 
		}
		return content;
	}
	
	/*Add printer name to internal storage file*/	
	public void saveToRecentPrinter(String name)
	{
		 FileOutputStream fos;
		 String printerList = getRecentPrinters();
		 
		 //Get Current recent list
		 String[] printers = printerList.split(";");
		 ArrayList<String> printersArray = new ArrayList<String>(Arrays.asList(printers));
		 
		 //printer not already a recent, so add.
		 if(!printerList.contains(name))
		 {
			 String content=name+";";
			 //iterate through 4 or less printers then add new one to make 5.
			 int length = 4;
			 if(printers.length<4)
			 {
				 length = printers.length;
			 }	
			 for(int i=0;i<length&&printers[i]!="";i++)
			 {
				 content+=printers[i]+";";
			 }
			 try 
			 {	
				 fos = openFileOutput(storageFileName, Context.MODE_PRIVATE);
				 fos.write(content.getBytes());
				 fos.close();
			 }
			 catch (FileNotFoundException e) 
			 {
			     e.printStackTrace();
			 }
			 catch (IOException e) 
			 {
			    e.printStackTrace();
			 }
		 }
		 else
		 {
			 // Find the printer in the list and remove it
			 for(int i=0;printersArray.size() > i; i++) 
			 {
				 if(printersArray.get(i).equals(name))
				 {
					 printersArray.remove(i);
				 }
			 }
			 
			 String content=name+";";
			 // iterate through 4 or less printers then add new one to make 5.
			 int length = 4;
			 if(printersArray.size()<4)
			 {
				 length = printersArray.size();
			 }	
			 for(int i=0;i<length&&printersArray.get(i)!="";i++)
			 {
				 content+=printersArray.get(i)+";";
			 }
			 
			 try 
			 {	
				 fos = openFileOutput(storageFileName, Context.MODE_PRIVATE);
				 fos.write(content.getBytes());
				 fos.close();
			 }
			 catch (FileNotFoundException e) 
			 {
			     e.printStackTrace();
			 }
			 catch (IOException e) 
			 {
			    e.printStackTrace();
			 }
		 }
	}

}
