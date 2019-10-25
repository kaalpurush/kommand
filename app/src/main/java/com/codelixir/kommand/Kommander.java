package com.codelixir.kommand;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Kommander {
    String ip;
    int port;

    public Kommander(Context context) {
        ip = getSetting(context, "ip", "192.168.1.255");
        port = Integer.parseInt(getSetting(context, "port", "5000"));
    }

    public void sendCommand(String command) {
        new CommandTask().execute(command);
    }

    private class CommandTask extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... commands) {
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.setBroadcast(true);
                byte[] sendData = commands[0].getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("ip"), port);
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
}
