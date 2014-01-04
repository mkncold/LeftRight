package com.example.leftright;

//import com.example.leftright.RunTask.Side;

import com.example.leftright.Config;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(Config.TAG_USER, "Creating MainActivity");
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
			
	}
	
	@Override
	protected void onResume() {
	  Log.v(Config.TAG_USER, "Resuming MainActivity");
	  super.onResume();
	}
	  
	@Override
	protected void onPause() {
	  Log.v(Config.TAG_USER, "Pausing MainActivity");
	  super.onPause();
	}
	
	
	public void runTaskActivity(View view){
		Intent intent = new Intent(this, TaskActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
