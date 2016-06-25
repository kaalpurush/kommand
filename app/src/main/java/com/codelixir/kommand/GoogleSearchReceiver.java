package com.codelixir.kommand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GoogleSearchReceiver extends BroadcastReceiver {
    String command;

    @Override
    public void onReceive(Context context, Intent intent) {
        command = intent.getStringExtra(GoogleSearchApi.KEY_QUERY_TEXT);
        new Kommander(context).sendCommand(command);
    }

}
