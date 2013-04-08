package com.example.sprint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PrintConfirmationFragment extends Fragment {

	private LinearLayout view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = (LinearLayout) inflater.inflate(R.layout.fragment_print_confirmation, container, false);
		return view;
	}
	
}
