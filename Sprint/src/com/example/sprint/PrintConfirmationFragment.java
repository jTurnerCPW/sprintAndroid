package com.example.sprint;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PrintConfirmationFragment extends Fragment implements OnClickListener{

	private RelativeLayout view;
	private Button bPrint;
	ImageView ivJobIcon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = (RelativeLayout) inflater.inflate(R.layout.fragment_print_confirmation, container, false);
		bPrint = (Button) view.findViewById(R.id.bPrint);
		bPrint.setOnClickListener(this);

		// Get the views
		TextView tvJobName = (TextView)view.findViewById(R.id.tvJobName);
		TextView tvPrinterName = (TextView)view.findViewById(R.id.tvPrinterName);
		TextView tvHRPrinterName = (TextView)view.findViewById(R.id.tvHRPrinterName);
		TextView tvUserName = (TextView)view.findViewById(R.id.tvUserName);

		getResources().getConfiguration();
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			ivJobIcon = (ImageView)view.findViewById(R.id.job_icon);
		}

		// String values
		String jobName = ((PrintConfirmationActivity)this.getActivity()).getJobName();
		String printerName = ((PrintConfirmationActivity)this.getActivity()).getPrinterName();
		String hrPrinterName = makePrinterNameHumanReadable(printerName);
		String jobType = ((PrintConfirmationActivity)this.getActivity()).getJobType();
		String docOwner = ((PrintConfirmationActivity)this.getActivity()).getDocumentOwner();

		// Set the values
		tvJobName.setText(jobName);
		tvPrinterName.setText(printerName);
		tvHRPrinterName.setText(hrPrinterName);
		tvUserName.setText(docOwner);

		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			//this will happen if CUPS is not identifying the filetype
			if(jobType.equals("unknown")) {
				ivJobIcon.setImageResource(R.drawable.text);
			}

			//text files
			else if(jobType.equals("doc") || jobType.equals("docx") || jobType.equals("txt") || jobType.equals("rtf") || jobType.equals("odf")  || jobType.equals("gdoc")) {
				ivJobIcon.setImageResource(R.drawable.text);
			}
			//pdf
			else if(jobType.equals("pdf")) {
				ivJobIcon.setImageResource(R.drawable.pdfdoc);
			}
			//spreadsheets
			else if(jobType.equals("xls")  || jobType.equals("xlsx") || jobType.equals("csv") || jobType.equals("numbers") || jobType.equals("ods") || jobType.equals("gsheet")) {
				ivJobIcon.setImageResource(R.drawable.spreadsheet);
			}
			//presentations
			else if(jobType.equals("ppt") || jobType.equals("pptx") || jobType.equals("pps") || jobType.equals("key") || jobType.equals("keynote") || jobType.equals("gslides")) {
				ivJobIcon.setImageResource(R.drawable.presentation);
			}
			//web docs
			else if(jobType.equals("asp") || jobType.equals("aspx") || jobType.equals("htm") || jobType.equals("html") || jobType.equals("js") || jobType.equals("jsp") ||
					jobType.equals("php") || jobType.equals("rss") || jobType.equals("xml") || jobType.equals("xhtml") || jobType.equals("xaml")) {
				ivJobIcon.setImageResource(R.drawable.web);
			}
			//code files >:(
			else if(jobType.equals("c") || jobType.equals("class") || jobType.equals("cpp") || jobType.equals("cs") || jobType.equals("h") || jobType.equals("java") ||
					jobType.equals("m") || jobType.equals("pl") || jobType.equals("py") || jobType.equals("sh")) {
				ivJobIcon.setImageResource(R.drawable.code);
			}
			//some images...
			else if(jobType.equals("ai") || jobType.equals("svg") || jobType.equals("tiff") || jobType.equals("tif") || jobType.equals("tga") || jobType.equals("pspimage") ||
					jobType.equals("psd") || jobType.equals("png") || jobType.equals("jpg") || jobType.equals("gif") || jobType.equals("bmp") || jobType.equals("jpg") ||
					jobType.equals("jpeg") || jobType.equals("xcf")) {
				ivJobIcon.setImageResource(R.drawable.image);

			}
			//fall back on generic text icon
			else {
				ivJobIcon.setImageResource(R.drawable.text);
			}
		}

		return view;
	}

	private String makePrinterNameHumanReadable(String codedName) {

		//if the scan was not successful, or returned a string longer or shorter than expected
		if(codedName == null || codedName.length() < 6) {
			return "";
		}

		String humanReadableName = "";

		//Set the building wing
		if( codedName.substring(0, 1).toLowerCase().contentEquals("w") ) {
			//woodward
			humanReadableName = "Woodward, ";
		} else {
			if( codedName.substring(0, 1).contentEquals("m") ) {
				//monroe
				humanReadableName = "Monroe, ";
			} else {
				if( codedName.substring(0, 1).contentEquals("c") ) {
					//center
					humanReadableName = "Center, ";
				} else { return "";}
			}
		}

		//set the floor
		String sFloor = codedName.substring(1, 3);
		int floor = 0;
		try {
			floor = Integer.parseInt(sFloor);
		} catch(Exception e) {
			Log.v("makePrinterNameHumanReadable-floor", e.getLocalizedMessage());
		}

		if(floor == 0) {
			//no-op.  keep human readable floor without adding a value since Integer.parse failed. 
		} else {
			humanReadableName = floor + " " + humanReadableName;
		}

		//set the printer number

		//set the floor
		String sPrinter = codedName.substring(4, 6);
		int printer = 0;
		try {
			printer = Integer.parseInt(sPrinter);
		} catch(Exception e) {
			Log.v("makePrinterNameHumanReadable-printer", e.getLocalizedMessage());
		}

		if(printer == 0) {
			//no-op.  keep human readable floor without adding a value since Integer.parse failed. 
		} else {
			humanReadableName = humanReadableName + "Printer " + printer;
		}


		return humanReadableName;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){

		case R.id.bPrint:
			// Print the job.
			String jobId = ((PrintConfirmationActivity)this.getActivity()).getJobId();
			printJob(jobId);
			break;

		}

	}

	// Move and then Print the job id given
	private void printJob(String jobId) {
		MoveJobTask task = new MoveJobTask(this, jobId);
		task.execute(getActivity());
	}

	// Go back to the jobs page
	public void goBackToJobs() {
		// Start the confirmation activity            	
		Intent intent = new Intent(getActivity(), JobListActivity.class);
		intent.putExtra("printer_name", ((PrintConfirmationActivity)this.getActivity()).getPrinterName());
		getActivity().startActivity(intent);
	}
}
