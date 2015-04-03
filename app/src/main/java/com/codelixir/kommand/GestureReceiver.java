package com.codelixir.kommand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class GestureReceiver extends BroadcastReceiver  {
	String command;

	@Override
	public void onReceive(Context context, Intent intent) {
		String mGesture = intent.getStringExtra("Gesture");
		Log.d("Gesture", mGesture);

		SharedPreferences settings = context.getSharedPreferences("Widgets", 0);
		command = settings.getString(mGesture, null);
        new Kommander(context).sendCommand(command);
	}

}
