package com.codelixir.kommand;

import java.net.InetAddress;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final String TAG = "Kommand";
	EditText txtIp,txtPort;
	Button btnSave,btnDiscover;
	
	NsdManager mNsdManager;	
	DiscoveryListener mDiscoveryListener;
	ResolveListener mResolveListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mNsdManager = (NsdManager)getSystemService(NSD_SERVICE);

		/*Toast.makeText(getApplicationContext(), "Plugin installed", Toast.LENGTH_SHORT).show();
		getPackageManager().setComponentEnabledSetting(new ComponentName(this,
				getPackageName() + ".MainActivity-Alias"),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);*/
		
		txtIp=(EditText) findViewById(R.id.txtIp);
		txtPort=(EditText) findViewById(R.id.txtPort);
		btnSave=(Button) findViewById(R.id.btnSave);
		btnDiscover=(Button) findViewById(R.id.btnDiscover);
		
		txtIp.setText(getSetting("ip","192.168.43.128"));
		txtPort.setText(getSetting("port","6969"));
		
		btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                putSetting("ip", txtIp.getText().toString());
                putSetting("port", txtPort.getText().toString());
            	Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
            	finish();
            }
        });	
		
		btnDiscover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initializeResolveListener();
                initializeDiscoveryListener();
                mNsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
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
				if(service.getServiceType().equals("_http._tcp.") && service.getServiceName().contains("Kommand")){
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
	            Log.i(TAG, host.toString()+":"+port);
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						txtIp=(EditText) findViewById(R.id.txtIp);
						txtPort=(EditText) findViewById(R.id.txtPort);						
						txtIp.setText(host.getHostAddress());
						txtPort.setText(Integer.toString(port));
					}
	            });
	        }
	    };
	}


	
	
}
