package com.codelixir.kommand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GoogleSearchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String queryText = intent.getStringExtra(GoogleSearchApi.KEY_QUERY_TEXT);
		GoogleSearchApi.speak(context, "Kaal says: "+ queryText);
		ClientSocket sock=new ClientSocket(context);		
		sock.send(queryText);
	}

}


