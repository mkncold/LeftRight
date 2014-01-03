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
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_results);
		
		// Get the Score from the intent
	    Intent intent = getIntent();
	    int[] score = intent.getIntArrayExtra(MainActivity.EXTRA_SCORE);
		
		TextView correctText = (TextView)findViewById(R.id.correctVal);
		TextView incorrectText = (TextView)findViewById(R.id.incorrectVal);
		TextView totalText = (TextView)findViewById(R.id.totalVal);
		
		if (correctText == null){
			Log.d("NULL P", "correctText is NULL");
		}
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
