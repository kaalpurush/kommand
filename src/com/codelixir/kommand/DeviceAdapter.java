package com.codelixir.kommand;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

public class DeviceAdapter extends ArrayAdapter<Device> implements Filterable {

    private ArrayList<Device> items;

    private final Context context;

    public DeviceAdapter(Context context, int textViewResourceId, ArrayList<Device> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            this.context=context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listview_discover, null);
            }
            
            Device o = items.get(position);
            
            if (o != null) {
                    TextView tt = (TextView) v.findViewById(R.id.toptext);                    
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    
                    if (tt != null) 
                          tt.setText(o.host_name);                            
                    
                    if(bt != null)
                          bt.setText(o.host_ip+":"+o.host_port);                 
            }
            return v;
    }
    
    @Override
    public int getCount() {
    	// TODO Auto-generated method stub
    	return items.size();
    }
    
}    