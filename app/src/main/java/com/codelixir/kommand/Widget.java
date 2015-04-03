package com.codelixir.kommand;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {

	static String command;

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		// TODO Auto-generated method stub
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);

		// Log.d("appWidgetId", ":" + appWidgetId);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		Log.d(getClass().getSimpleName(), "onReceive");
		Log.d("intent", intent.toString());

		try {
			Bundle bundle = intent.getExtras();
			for (String key : bundle.keySet()) {
				Object value = bundle.get(key);
				Log.d(getClass().getName(), String.format("%s %s (%s)", key,
						value.toString(), value.getClass().getName()));
			}
		} catch (Exception e) {
		}

		if (intent.getAction()
				.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
			SharedPreferences settings = context.getSharedPreferences(
					"Widgets", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.remove("command" + appWidgetId);
			editor.commit();
		} else if (intent.getAction() != null
				&& intent.getDataString() != null
				&& intent.getAction().equals(
						AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

			SharedPreferences settings = context.getSharedPreferences(
					"Settings", 0);
			String ip = settings.getString("ip", "192.168.43.128");
			int port = Integer.parseInt(settings.getString("port", "6969"));

			settings = context.getSharedPreferences("Widgets", 0);
			String command = settings.getString("command" + appWidgetId, "");

			int state = intent.getIntExtra("STATE", 1);

			if (state > 0)
				Widget.command = command + " on";
			else
				Widget.command = command + " off";

            new Kommander(context).sendCommand(Widget.command);

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);

			RemoteViews views = buildView(context, appWidgetId, command, state);

			appWidgetManager.updateAppWidget(appWidgetId, views);

		}

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.d(getClass().getSimpleName(), "onDeleted");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		Log.d(getClass().getSimpleName(), "onUpdate");

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Log.d("appWidgetId","@"+appWidgetId);

			SharedPreferences settings = context.getSharedPreferences(
					"Widgets", 0);
			String command = settings.getString("command" + appWidgetId, "");

			RemoteViews views = buildView(context, appWidgetId, command, 0);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	public static RemoteViews buildView(Context context, int appWidgetId,
			String command, int state) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		views.setTextViewText(R.id.txtCommand, command);
		if (state > 0)
			views.setTextColor(R.id.txtCommand, Color.YELLOW);
		else
			views.setTextColor(R.id.txtCommand, Color.GRAY);

		// Create an Intent to launch ExampleActivity
		Intent intent = new Intent(context, Widget.class);
		// intent.setAction("light on");

		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.putExtra("STATE", 1);

		Uri data = Uri.withAppendedPath(Uri.parse("widget://"),
				String.valueOf(appWidgetId) + "/1");
		intent.setData(data);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		views.setOnClickPendingIntent(R.id.btnOn, pendingIntent);

		// Create an Intent to launch ExampleActivity
		intent = new Intent(context, Widget.class);
		// intent.setAction("light on");

		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.putExtra("STATE", 0);

		data = Uri.withAppendedPath(Uri.parse("widget://"),
				String.valueOf(appWidgetId) + "/0");
		intent.setData(data);

		pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		views.setOnClickPendingIntent(R.id.btnOff, pendingIntent);

		/*
		 * if(state<0) views.setTextColor(R.id.button, Color.BLACK); else
		 * views.setTextColor(R.id.button, Color.YELLOW);
		 */

		return views;

		// Tell the AppWidgetManager to perform an update on the current app
		// widget

	}
}
