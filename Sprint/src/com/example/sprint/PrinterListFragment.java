package com.example.sprint;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PrinterListFragment extends Fragment implements OnItemClickListener{

	private ListView printerListView;
	private EditText searchText;
	private PrinterListAdapter adapter = null;
	private ArrayList<Printer> printers;
	private LinearLayout view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (LinearLayout) inflater.inflate(R.layout.fragment_printer_list, container, false);
		
		return view;
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {			
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.v("PrinterListActivity", "onTextChanged");
			adapter.getFilter().filter(s);
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Get all printers
		getPrinters();

		searchText = (EditText) view.findViewById(R.id.etSearchPrinter);
		searchText.addTextChangedListener(filterTextWatcher);

		
		printerListView = (ListView) view.findViewById(R.id.lvPrinter);
		printerListView.setAdapter(adapter);
		
		/*  Set up the on-click methods next */
		printerListView.setOnItemClickListener(this);
	}

	private void getPrinters() {
		PrinterListJSONTask task = new PrinterListJSONTask(this);
		task.execute(getActivity());
	}
	
	public void setPrinters(ArrayList<Printer> printers) {
		this.printers = printers;
	}
	
	public void showList() {
		adapter = buildAdapter();
		printerListView.setAdapter(adapter);
	}
	
	public void clearList() {
		if(printerListView != null) {
			printerListView.setAdapter(null);
		}
	}
	
	private PrinterListAdapter buildAdapter() {
		final PrinterListAdapter arrayAdapter =
			new PrinterListAdapter(getActivity(), R.layout.printer_list_row, printers);
		return arrayAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	//pretty much what it says.  explore get jobs for more.  basically refreshes list - removes old items and puts in new ones 
	public void refreshJobs() {
		getPrinters();
	}
	
}
