package com.example.leftright;

//import com.example.leftright.RunTask.Side;

import com.example.leftright.RunTask.Side;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
	
	public static final String EXTRA_SCORE = "com.example.leftright.MainActivity.SCORE";

	TextView queryText;
	RunTask runTask;
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		
		System.out.println("Starting!!!!!!");
		
		Log.i("Sensor Values", "Init Sensor");
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (accelerometer == null){
			Log.w("Missing Accelerometer", "The device does not have a capable accelerometer.");
		}
		
		Log.i("Sensor Values", "Sensor Init");
		
        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix    = new float[9];
        final float[] mRotationMatrix2   = new float[9];
        
        SensorManager.getRotationMatrix(mRotationMatrix, mValuesOrientation, mValuesAccel, mValuesMagnet);
		
		int rotation = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
		if(rotation ==  Surface.ROTATION_0) // Default display rotation is portrait
		    SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_Y, mRotationMatrix2);
		else   // Default display rotation is landscape
		    SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, mRotationMatrix2);
		
		Log.i("Sensor Values", "Remapped Coords?");
		
		queryText = (TextView)findViewById(R.id.queryText);
		runTask = new RunTask(this, queryText);
		runTask.startTask();
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
	  sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	  Log.i("Sensor Values", "Sensor Registered");
	}
	  
	@Override
	protected void onPause() {
	  super.onPause();
	  sensorManager.unregisterListener(this);
	  Log.i("Sensor Values", "Sensor Unregistered");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void leftClick(View view){
		view.setClickable(false);
		runTask.checkAnswer(Side.LEFT);
	}

	public void rightClick(View view){
		view.setClickable(false);
		runTask.checkAnswer(Side.RIGHT);
	}
	


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// Do Nothing
	}
	
	public void registerSensor(){
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;
		Log.i("Sensor Values", "sValues: " + values[0] + "  " + values[1] + "  " + values[2]);
		float y = event.values[1];
		
		if (y < -5){
			sensorManager.unregisterListener(this);
			leftClick((Button) findViewById(R.id.leftButton));
		}
		else if (y > 5){
			sensorManager.unregisterListener(this);
			rightClick((Button) findViewById(R.id.rightButton));
		}
	}

}
