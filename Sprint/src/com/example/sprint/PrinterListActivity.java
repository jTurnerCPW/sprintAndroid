package com.example.sprint;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

public class PrinterListActivity extends Activity{

	private ListView printerListView;
	private EditText searchText;
	private PrinterListAdapter adapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer_list);
		context = this;

		// Get all printers
		ArrayList<Printer> printerList = getPrinters();

		searchText = (EditText) findViewById(R.id.etSearchPrinter);
		searchText.addTextChangedListener(filterTextWatcher);
		
		printerListView = (ListView) findViewById(R.id.lvPrinter);
		adapter = new PrinterListAdapter(context, R.layout.printer_list_row,
				printerList);
		printerListView.setAdapter(adapter);
		
		/*  Set up the on-click methods next */
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

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Log.v("PrinterListActivity", "onTextChanged");
			adapter.getFilter().filter(s);
		}

	};

	private ArrayList<Printer> getPrinters() {
		ArrayList<Printer> printerList = new ArrayList<Printer>();
		
		/* Dummy printer info */
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
		return printerList;
	}
}
