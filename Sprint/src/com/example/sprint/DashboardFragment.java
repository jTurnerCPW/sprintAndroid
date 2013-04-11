package com.example.sprint;

import com.compuware.apm.uem.mobile.android.CompuwareUEM;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DashboardFragment extends Fragment implements OnItemClickListener{
	private static final int EDIT_ID = Menu.FIRST+2;
	private final static int BARCODE_SCAN_REQUEST = 2345;

	RelativeLayout view;
    static final LauncherIcon[] ICONS = {
    	new LauncherIcon(R.drawable.dash_scan, "Scan Printer QR Code", "qrbypdpicon.png"),
    	new LauncherIcon(R.drawable.dash_print,"Select Printer", "color_printer.png"),
        new LauncherIcon(R.drawable.jobs, "Select Job", "jobs.png")
    };
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		// Inflate the view
		view = (RelativeLayout)
				inflater.inflate(R.layout.display_dashboard, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.dashboard_grid);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        gridview.setOnItemClickListener(this);
 
        // Hack to disable GridView scrolling
        gridview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
        
		return view;
	}	
	
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	switch(position)
    	{
    		case(0):
    		{
    			Intent i = new Intent();	
    			/* setting the action string.  No other apps should respond to this request
    			 * because of the unique action string
    			 */
    			i.setAction("com.compuware.pdp.sprint");
    			i.putExtra("SCAN_MODE", "QR_CODE_MODE");
    			startActivityForResult(i, BARCODE_SCAN_REQUEST);
			
    			//dynaTrace Metric for scanning
    			CompuwareUEM.enterAction("scannerActivity");
    			break;
    		}
    		case(1):
    		{
    			Intent intent = new Intent(getActivity(), PrinterListActivity.class);
    			startActivity(intent);
    			break;
    		}   
    		case(2):
    		{
    			Intent intent = new Intent(getActivity(), JobListActivity.class);
    			startActivity(intent);
    			break;
    		}
    		default:
    		{break;}
    	}
    }
 
    static class LauncherIcon {
        final String text;
        final int imgId;
        final String map;
 
        public LauncherIcon(int imgId, String text, String map) {
            super();
            this.imgId = imgId;
            this.text = text;
            this.map = map;
        }
 
    }
 
    static class ImageAdapter extends BaseAdapter {
        private Context mContext;
 
        public ImageAdapter(Context c) {
            mContext = c;
        }
 
        @Override
        public int getCount() {
            return ICONS.length;
        }
 
        @Override
        public LauncherIcon getItem(int position) {
            return null;
        }
 
        @Override
        public long getItemId(int position) {
            return 0;
        }
 
        static class ViewHolder {
            public ImageView icon;
            public TextView text;
        }
 
        // Create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) { 
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
 
                v = vi.inflate(R.layout.dashboard_icon, null);
                holder = new ViewHolder();
                holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
                holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
 
            holder.icon.setImageResource(ICONS[position].imgId);
            holder.text.setText(ICONS[position].text);
 
            return v;
        }
    }
}
