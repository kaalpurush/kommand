package com.codelixir.kommand;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class GoogleSearchReceiver extends BroadcastReceiver implements com.koushikdutta.async.callback.ConnectCallback {
	String queryText;

	@Override
	public void onReceive(Context context, Intent intent) {
		queryText = intent
				.getStringExtra(GoogleSearchApi.KEY_QUERY_TEXT);
		GoogleSearchApi.speak(context, "Kaal says: " + queryText);

		String ip = getSetting(context, "ip", "192.168.43.128");
		int port = Integer.parseInt(getSetting(context, "port", "6969"));

		AsyncServer asyncServer;
		asyncServer = new AsyncServer();
		asyncServer.connectSocket(ip, port, this);
	}

	private String getSetting(Context context, String key, String def) {
		SharedPreferences settings = context
				.getSharedPreferences("Settings", 0);
		return settings.getString(key, def);
	}

	@Override
	public void onConnectCompleted(Exception arg0, AsyncSocket arg1) {
		if(queryText!=null)
			arg1.write(new ByteBufferList(queryText.getBytes()));		
	}

}
