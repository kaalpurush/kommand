package com.codelixir.kommand;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Kommander implements
        com.koushikdutta.async.callback.ConnectCallback {
    String command, ip;
    int port;
    AsyncServer asyncServer;

    public Kommander(Context context) {
        ip = getSetting(context, "ip", "192.168.43.128");
        port = Integer.parseInt(getSetting(context, "port", "6969"));
    }

    public void sendCommand(String command) {
        new CommandTask().execute(command);
    }

    public void sendCommand(String command, boolean useTcp) {
        this.command = command;
        asyncServer = new AsyncServer();
        asyncServer.connectSocket(ip, port, this);
    }

    private class CommandTask extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... commands) {
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.setBroadcast(true);
                byte[] sendData = commands[0].getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 6969);
                socket.send(sendPacket);
            } catch (IOException e) {
                Log.e("UDP", "IOException: " + e.getMessage());
            }
            return 1;
        }

        protected void onPostExecute(Integer ret) {

        }
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
