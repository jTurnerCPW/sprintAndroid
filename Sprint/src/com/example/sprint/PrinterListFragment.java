package com.example.sprint;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PrinterListFragment extends Fragment implements OnItemClickListener{

	private PullToRefreshListView printerListView;
	private EditText searchText;
	private PrinterListAdapter adapter = null;
	private ArrayList<Printer> printers;
	private LinearLayout view;
	private Handler mHandler = new Handler();
	private PrinterListJSONTask printerTask;

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

		
		printerListView = (PullToRefreshListView) view.findViewById(R.id.lvPrinter);
		printerListView.setAdapter(adapter);
		
		printerListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(final PullToRefreshBase<ListView> lv) {
				
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						refreshPrinters();
					}
				},0);
			}
		});
		
		/*  Set up the on-click methods next */
		printerListView.setOnItemClickListener(this);
	}

	private void getPrinters() {
		printerTask = new PrinterListJSONTask(this);
		printerTask.execute(getActivity());
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
	
	//pretty much what it says.  explore getPrinters for more.  basically refreshes list - removes old items and puts in new ones 
	public void refreshPrinters() {
		getPrinters();
	}
	
	public void notifyPrinterLoadComplete() {
		
		printerListView.onRefreshComplete();
	}
	
	public void cancelPrinterTask() {
		 if (printerTask != null && printerTask.getStatus() != Status.FINISHED)
			 printerTask.cancel(true);
	}
	
	@Override
	public void onDestroy() {
		
		cancelPrinterTask();
		super.onDestroy();
	}
}
