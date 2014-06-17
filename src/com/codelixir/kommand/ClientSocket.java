package com.codelixir.kommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;


//The Thread Class that handles the socket connection and requests.
	public class ClientSocket{
		Context context;
		public Socket socket = null;
		public BufferedReader br;
		public BufferedWriter bw;
		public String ip,msg;
		public int port;
		public boolean connected = false;
		public Handler handler;
		
		public ClientSocket(Context context) {
			this.context=context;
			ip=getSetting("ip","192.168.43.128");
			port=Integer.parseInt(getSetting("port","6969"));
		}
		
		public void send(String str){
			new SockTask().execute(new String[] { str });
		}
	
		private void connect() {
			try {
				socket = new Socket(ip, port);
				connected = true;
				br = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				bw = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
			} catch (Exception e) {
				connected = false;
				Log.e("ClientSocket", "Connect Error", e);
			}
		}
		
		private void write(String str) {
			if(!connected)
				connect();
			try {
				bw.write(str);
				bw.flush();
			} catch (Exception e) {
				Log.e("ClientSocket", "Write Error", e);
			}
			disconnect();
		}
		
		private void disconnect() {
			try {
				socket.close();
				connected = false;
			} catch (Exception e) {
				Log.e("ClientSocket", "Disconnect Error", e);
			}
		}
		
	    private class SockTask extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... msg) {
	          String response="";
	          write(msg[0]);
	          return response;
	        }
	        
	        @Override
	        protected void onPreExecute() {
	        	
	        }

	        @Override
	        protected void onPostExecute(String result) { 
	        	
	        }
	        
	      }
	    
	    private String getSetting(String key,String def){
	    	SharedPreferences settings = context.getSharedPreferences("Settings", 0);
	    	return settings.getString(key, def); 
	    }
		
	}