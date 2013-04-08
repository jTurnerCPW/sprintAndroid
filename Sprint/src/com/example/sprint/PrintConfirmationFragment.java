package com.example.sprint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class PrintConfirmationFragment extends Fragment implements OnClickListener{

	private LinearLayout view;
	private Button bCancel;
	private Button bPrint;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = (LinearLayout) inflater.inflate(R.layout.fragment_print_confirmation, container, false);
		bCancel = (Button) view.findViewById(R.id.bCancel);
		bCancel.setOnClickListener(this);
		bPrint = (Button) view.findViewById(R.id.bPrint);
		bPrint.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.bCancel:
			//  Don't print the job.  Go back
			getActivity().finish();
			break;
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
}
