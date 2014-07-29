package com.codelixir.kommand;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WidgetActivity extends Activity {

	int mAppWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_widget);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			
			final TextView txtCommand=(TextView)findViewById(R.id.txtCommand);

			findViewById(R.id.btnSave).setOnClickListener(
					new View.OnClickListener() {
						public void onClick(View v) {
							String command=txtCommand.getText().toString();
							putSetting("command"+mAppWidgetId, command);
							
							AppWidgetManager appWidgetManager = AppWidgetManager
									.getInstance(WidgetActivity.this);
							
							RemoteViews views=Widget.buildView(WidgetActivity.this, mAppWidgetId, command, 0);
							
							appWidgetManager.updateAppWidget(mAppWidgetId, views);

							Intent resultValue = new Intent();
							resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
									mAppWidgetId);
							setResult(RESULT_OK, resultValue);
							finish();
						}
					});
			

		}

	}
	
    
	public Boolean putSetting(String key, String value){
    	SharedPreferences settings = getSharedPreferences("Widget", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }
	
	public Boolean deleteSetting(String key) {
		SharedPreferences settings = getSharedPreferences("Widget", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		return editor.commit();
	}
}
