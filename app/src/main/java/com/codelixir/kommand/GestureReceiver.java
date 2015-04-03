package com.codelixir.kommand;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class GestureReceiver extends BroadcastReceiver implements
		com.koushikdutta.async.callback.ConnectCallback {
	String command;

	@Override
	public void onReceive(Context context, Intent intent) {
		String mGesture = intent.getStringExtra("Gesture");
		Log.d("Gesture", mGesture);

		SharedPreferences settings = context.getSharedPreferences("Widgets", 0);
		command = settings.getString(mGesture, null);

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
		if (arg0 == null && command != null && arg1.isOpen()) {
			Log.d("Kommand", command);
			arg1.write(new ByteBufferList(command.getBytes()));
		}

	}

}
