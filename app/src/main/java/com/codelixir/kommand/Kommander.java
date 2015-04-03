package com.codelixir.kommand;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;

public class Kommander implements
        com.koushikdutta.async.callback.ConnectCallback {
    String command,ip;
    int port;
    AsyncServer asyncServer;

    public Kommander(Context context) {
        ip = getSetting(context, "ip", "192.168.43.128");
        port = Integer.parseInt(getSetting(context, "port", "6969"));
    }

    public void sendCommand(String command) {
        this.command=command;
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
