package com.codelixir.kommand;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;


import android.app.Activity;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final String TAG = "Kommand";
	EditText txtIp,txtMac,txtPort;
	Button btnSave,btnDiscover;
	ListView lstDiscover;
	
	ArrayList<Device> devices = new ArrayList<Device>();
	
	DeviceAdapter d_adapter;
	
	NsdManager mNsdManager;	
	DiscoveryListener mDiscoveryListener;
	ResolveListener mResolveListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mNsdManager = (NsdManager)getSystemService(NSD_SERVICE);
		
		d_adapter=new DeviceAdapter(this, R.layout.listview_discover, devices);

		/*Toast.makeText(getApplicationContext(), "Plugin installed", Toast.LENGTH_SHORT).show();
		getPackageManager().setComponentEnabledSetting(new ComponentName(this,
				getPackageName() + ".MainActivity-Alias"),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);*/
		
		txtIp=(EditText) findViewById(R.id.txtIp);
		//txtMac=(EditText) findViewById(R.id.txtMac);
		txtPort=(EditText) findViewById(R.id.txtPort);
		btnSave=(Button) findViewById(R.id.btnSave);
		btnDiscover=(Button) findViewById(R.id.btnDiscover);
		lstDiscover=(ListView) findViewById(R.id.lstDiscover);
		
		lstDiscover.setAdapter(d_adapter);
		
		txtIp.setText(getSetting("ip","192.168.43.128"));
		txtMac.setText(getSetting("mac",""));
		txtPort.setText(getSetting("port","6969"));
		
		btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                putSetting("ip", txtIp.getText().toString());
                putSetting("mac", txtMac.getText().toString());
                putSetting("port", txtPort.getText().toString());
            	Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
            	finish();
            }
        });	
		
		btnDiscover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	devices.clear();
            	d_adapter.notifyDataSetChanged();
                initializeResolveListener();
                initializeDiscoveryListener();
                mNsdManager.discoverServices("_kommand._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
            }
        });	
		
		lstDiscover.setClickable(true);
		lstDiscover.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		    Device d = (Device)lstDiscover.getItemAtPosition(position);
		    txtIp.setText(d.host_ip);
		    txtPort.setText(Integer.toString(d.host_port));
		  }
		});
	}
	
	
	private String getSetting(String key,String def){
    	SharedPreferences settings = getSharedPreferences("Settings", 0);
    	return settings.getString(key, def); 
    }
    
	private Boolean putSetting(String key, String value){
    	SharedPreferences settings = getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

	
	public void initializeDiscoveryListener() {

	    // Instantiate a new DiscoveryListener
	    mDiscoveryListener = new NsdManager.DiscoveryListener() {

			//  Called as soon as service discovery begins.
	        @Override
	        public void onDiscoveryStarted(String regType) {
	            Log.d(TAG, "Service discovery started");
	        }

	        @Override
	        public void onServiceFound(NsdServiceInfo service) {
	            // A service was found!  Do something with it.
	            Log.d(TAG, "Service discovery success" + service);
				if(service.getServiceType().equals("_kommand._tcp.")){
	            	Log.d(TAG,"Resolving..."+service.getServiceName());
	                mNsdManager.resolveService(service, mResolveListener);
	            }
	        }

	        @Override
	        public void onServiceLost(NsdServiceInfo service) {
	            // When the network service is no longer available.
	            // Internal bookkeeping code goes here.
	            Log.e(TAG, "service lost" + service);
	        }

	        @Override
	        public void onDiscoveryStopped(String serviceType) {
	            Log.i(TAG, "Discovery stopped: " + serviceType);
	        }

	        @Override
	        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
	            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
	            mNsdManager.stopServiceDiscovery(this);
	        }

	        @Override
	        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
	            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
	            mNsdManager.stopServiceDiscovery(this);
	        }
	    };
	}
	
	public void saveDevices(){
		
	}
	
	public static String getMacFromArpCache(String ip) {
	    if (ip == null)
	        return null;
	    BufferedReader br = null;
	    try {
	        br = new BufferedReader(new FileReader("/proc/net/arp"));
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] splitted = line.split(" +");
	            if (splitted != null && splitted.length >= 4 && ip.equals(splitted[0])) {
	                // Basic sanity check
	                String mac = splitted[3];
	                if (mac.matches("..:..:..:..:..:..")) {
	                    return mac;
	                } else {
	                    return null;
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	public void initializeResolveListener() {
	    mResolveListener = new NsdManager.ResolveListener() {

	        @Override
	        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
	            // Called when the resolve fails.  Use the error code to debug.
	            Log.e(TAG, "Resolve failed" + errorCode);
	        }

	        @Override
	        public void onServiceResolved(NsdServiceInfo serviceInfo) {
	            Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
	            final int port = serviceInfo.getPort();
	            final InetAddress host = serviceInfo.getHost();
	            final String host_name=serviceInfo.getServiceName();
	            final String host_ip=host.getHostAddress();
	            final String host_mac=getMacFromArpCache(host.getHostAddress());           
	            Log.i(TAG, host.toString()+":"+port+" "+host_mac);
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Device device=new Device(host_name,host_mac,host_ip,port);
						devices.add(device);
						d_adapter.notifyDataSetChanged();
						//saveDevices();
					}
	            });
	        }
	    };
	}


	
	
}
