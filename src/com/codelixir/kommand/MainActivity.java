package com.codelixir.kommand;

import javax.xml.datatype.Duration;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText txtIp,txtPort;
	Button btnSave;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*Toast.makeText(getApplicationContext(), "Plugin installed", Toast.LENGTH_SHORT).show();
		getPackageManager().setComponentEnabledSetting(new ComponentName(this,
				getPackageName() + ".MainActivity-Alias"),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);*/
		
		txtIp=(EditText) findViewById(R.id.txtIp);
		txtPort=(EditText) findViewById(R.id.txtPort);
		btnSave=(Button) findViewById(R.id.btnSave);
		
		
		
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


	
	
}
