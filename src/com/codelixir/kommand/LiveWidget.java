package com.codelixir.kommand;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class LiveWidget extends AppWidgetProvider implements com.koushikdutta.async.callback.ConnectCallback {
	
	static String command;

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		// TODO Auto-generated method stub
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);

		Log.d("appWidgetId", ":" + appWidgetId);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)){			
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
			SharedPreferences settings = context.getSharedPreferences("Widget", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.remove("command"+appWidgetId);
			editor.commit();
		}
		else if(intent.getAction()!=null && intent.getDataString()!=null && intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
			Log.d("appWidgetId",":"+appWidgetId);
		    SharedPreferences settings = context.getSharedPreferences("Widget", 0);
		    command=settings.getString("command"+appWidgetId,"");

			String ip = settings.getString("ip", "192.168.43.128");
			int port = Integer.parseInt(settings.getString("port", "6969"));

			AsyncServer asyncServer;
			asyncServer = new AsyncServer();
			asyncServer.connectSocket(ip, port, this);
		}
		
		Log.d("intent",intent.toString());
		/*
		try{
		Bundle bundle=intent.getExtras();		
		for (String key : bundle.keySet()) {
		    Object value = bundle.get(key);
		    Log.d(getClass().getName(), String.format("%s %s (%s)", key,  
		        value.toString(), value.getClass().getName()));
		}
		}catch(Exception e){}*/
		
		Toast.makeText(context, "onReceive:"+intent.getAction(), Toast.LENGTH_LONG).show();		
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "onDelete has been called in widgetClass",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		Log.d(getClass().getSimpleName(),"onUpdate");
		
		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			
			Log.d("appWidgetId","@"+appWidgetId);

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, LiveWidget.class);
			//intent.setAction("light on");
			
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		    
		    Uri data = Uri.withAppendedPath(
		    	    Uri.parse("widget://")
		    	    ,String.valueOf(appWidgetId));
		    intent.setData(data);
			
		    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
		            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.livewidget);
			
			views.setOnClickPendingIntent(R.id.button, pendingIntent);

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
	
	@Override
	public void onConnectCompleted(Exception arg0, AsyncSocket arg1) {
		if(command!=null)
			arg1.write(new ByteBufferList(command.getBytes()));		
	}
	

}
