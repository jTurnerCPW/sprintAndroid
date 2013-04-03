package com.example.sprint;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class PrinterListActivity extends Activity implements OnItemClickListener{

	private ListView printerListView;
	private EditText searchText;
	private PrinterListAdapter adapter = null;
	private ArrayList<Printer> printers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer_list);

		// Get all printers
		getPrinters();

		searchText = (EditText) findViewById(R.id.etSearchPrinter);
		searchText.addTextChangedListener(filterTextWatcher);

		
		printerListView = (ListView) findViewById(R.id.lvPrinter);
		printerListView.setAdapter(adapter);
		
		/*  Set up the on-click methods next */
		printerListView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	private void getPrinters() {	
		PrinterListJSONTask task = new PrinterListJSONTask(this);
		task.execute(this);
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
			new PrinterListAdapter(this, R.layout.printer_list_row, printers);
		return arrayAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
