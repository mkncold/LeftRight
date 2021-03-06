package com.example.leftright;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(Config.TAG_USER, "ResultActivity Created");
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_results);
		
		
	}
	
	
	@Override
	protected void onStart(){
		Log.v(Config.TAG_USER, "ResultActivity Started");
		super.onStart();
		
		// Get the Score from the intent
	    Intent intent = getIntent();
	    int[] score = intent.getIntArrayExtra(Config.EXTRA_SCORE);
		
		TextView correctText = (TextView)findViewById(R.id.correctVal);
		TextView incorrectText = (TextView)findViewById(R.id.incorrectVal);
		TextView totalText = (TextView)findViewById(R.id.totalVal);
		
		correctText.setText(String.valueOf(score[0]));
		incorrectText.setText(String.valueOf(score[1]));
		totalText.setText(String.valueOf(score[2]));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

}
