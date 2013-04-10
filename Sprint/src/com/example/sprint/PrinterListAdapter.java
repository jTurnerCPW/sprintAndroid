package com.example.sprint;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PrinterListAdapter extends ArrayAdapter<Printer> {

	// Variables
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	private Filter filter;
	private int recentPrinterCount=0;
	private int listCounter=0;
	private ArrayList<Printer> recentPrinters;
	private ArrayList<Printer> printerListOriginal;
	private ArrayList<Printer> printerListFiltered;

	// Constructor
	public PrinterListAdapter(Context ctx, int resourceId,
			ArrayList<Printer> printers) {
		super(ctx, resourceId, printers);
		
		//get Recent Printers
		printerListOriginal= new ArrayList<Printer>();
		printerListFiltered = new ArrayList<Printer>();
		recentPrinters= new ArrayList<Printer>();
		recentPrinters.add(new Printer("Kim","Kim",false));
		recentPrinterCount = recentPrinters.size();
		
		printerListOriginal.addAll(recentPrinters);
		printerListOriginal.addAll(printers);
		printerListFiltered.addAll(recentPrinters);
		printerListFiltered.addAll(printers);
		
		//printerListFiltered = new ArrayList<Printer>(printers);
		resource = resourceId;
		inflater = LayoutInflater.from(ctx);
		context = ctx;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		/* create a new view of my layout and inflate it in the row */
		convertView = (RelativeLayout) inflater.inflate(resource, null);
		convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            	// If we don't have the job yet go to the job selection
            	if(((PrinterListActivity)context).getJobId() == null)
            	{
            		// When a printer is clicked show the job list and send the printer selected
                    Intent intent = new Intent(context, JobListActivity.class);
            		intent.putExtra("printer_name", printerListFiltered.get(position).getName());
            		context.startActivity(intent);
            	}
            	else
            	{
            		// Start the confirmation activity            	
                	Intent intent = new Intent(context, PrintConfirmationActivity.class);
                	intent.putExtra("printer_name", printerListFiltered.get(position).getName());
            		intent.putExtra("job_id", ((PrinterListActivity)context).getJobId());
            		intent.putExtra("job_name", ((PrinterListActivity)context).getJobName());
            		context.startActivity(intent);
            	}
            	
            }
        });
		
		
		TextView separator = (TextView) convertView.findViewById(R.id.separator);
		
		separator.setVisibility(View.GONE);
		
		//Log.e("APP", ""+listCounter+" "+recentPrinterCount);
		//separator.setVisibility(View.GONE);
		/*
		if(listCounter==0)
		{
			//Log.v("APP", "=0");
			TextView separator = (TextView) convertView.findViewById(R.id.separator);
			
			separator.setText("Recent Printers");
			separator.setVisibility(View.VISIBLE);
		}
		else if(listCounter == recentPrinterCount)
		{

			Log.v("APP", "=recent printers");
			TextView separator = (TextView) convertView.findViewById(R.id.separator);
			
			separator.setText("All Printers");
			separator.setVisibility(View.VISIBLE);
		}
		listCounter++;
		*/
		/* Get the printer object from the filtered list position*/
		Printer printer = printerListFiltered.get(position);

		/* Set the printer's name*/
		TextView title = (TextView) convertView
				.findViewById(R.id.tvPrinterTitle);
		title.setText(printer.getName());

		/* Set the printer's location */
		TextView location = (TextView) convertView
				.findViewById(R.id.tvPrinterLocation);
		location.setText(printer.getLocation());

		/* Set the printer image based on the type of printer */
		ImageView imagePrinter = (ImageView) convertView
				.findViewById(R.id.imgPrinter);
		
		String uri = "";
		if(printer.getColor())
			uri = "drawable/color_printer";
		else
			uri = "drawable/blackwhite_printer";

		int imageResource = context.getResources().getIdentifier(uri, null,
				context.getPackageName());
		Drawable image = context.getResources().getDrawable(imageResource);
		imagePrinter.setImageDrawable(image);

		return convertView;
	}

	@Override
	public Filter getFilter() {
		/* Get a custom filter for the adapter */
		if (filter == null)
			filter = new PrinterListFilter();

		return filter;
	}
	
	@Override
	public int getCount(){
		/* Get the count based on the filtered printer list size*/
		return printerListFiltered.size();
	}

	/*
	 * class: PrinterListFilter
	 * description: Custom Filter for printers.  The filter is implemented
	 * 				to allow filtering on different fields of the printer
	 * 				object such as 'name' and 'location'
	 */
	private class PrinterListFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			FilterResults results = new FilterResults();

			if (constraint == null || constraint.length() == 0) {
				/* No filter. Return the whole list*/
				results.values = printerListOriginal;
				results.count = printerListOriginal.size();
				
			} else {
				/* Filtering*/
				ArrayList<Printer> nPrinterList = new ArrayList<Printer>();
				for (Printer p : printerListOriginal) {
					/* Compare the upper-case of the printer name with the text input */
					if (p.getName().toUpperCase(Locale.getDefault())
							.contains(constraint.toString().toUpperCase(Locale.getDefault()))) {
						/* Add to the new filtered printer list */
						nPrinterList.add(p);
					}
				}
				results.values = nPrinterList;
				results.count = nPrinterList.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count == 0) {
				/* No results matched the text input, so show the whole list */
				printerListFiltered = printerListOriginal;
				notifyDataSetChanged();
			} else {
				/* There are matching results.  Show only those which match */
				printerListFiltered = (ArrayList<Printer>) results.values;
				notifyDataSetChanged();
			}
		}
	}
}