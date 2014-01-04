package com.example.leftright;

import com.example.leftright.Config;
import com.example.leftright.Config.Side;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TaskActivity extends Activity implements SensorEventListener, Runnable {
	
	private static TextView queryText;
	private static Button rightButton;
	private static Button leftButton;
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	private int total = 0;
	private int correct = 0;
	private int incorrect = 0;
	
	private Config.Side answer; //The answer to the query
	
	private static SoundPool soundPool;
	private static int leftSound;
	private static int rightSound;
	
	/** Indicates if a query is an audio query and not text */
	private static boolean isAudioQuery;
	
	private StopTask stopTask = new StopTask(this);
	
	private static Handler rHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(Config.TAG_USER, "Creating TaskActivity");
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_task);
		
		
		/* Initialize the accelerometer sensor*/
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (accelerometer == null){
			Log.w(Config.TAG_USER, "The device does not have a capable accelerometer.");
			Log.v(Config.TAG_USER, "Sensor Not Initialized");
		}else{
			Log.v(Config.TAG_USER, "Sensor Initialized");
		}
		
		/* Arrays used for getRotationMatrix */
        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix    = new float[9];
        
        final float[] mRotationMatrix2   = new float[9]; //New matrix after remap
        
        
        SensorManager.getRotationMatrix(mRotationMatrix, mValuesOrientation, mValuesAccel, mValuesMagnet);
		
        
        /*Check that the default orientation is taken into account */
		int rotation = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
		if(rotation ==  Surface.ROTATION_0) // Default display rotation is portrait
		    SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_Y, mRotationMatrix2);
		else   // Default display rotation is landscape
		    SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, mRotationMatrix2);
		
		
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
		leftSound = soundPool.load(getApplicationContext(), R.raw.left, 1);
		rightSound = soundPool.load(getApplicationContext(), R.raw.right, 1);
		
		/* Initialize field View widgets */
		queryText = (TextView)findViewById(R.id.queryText);
		rightButton = (Button) findViewById(R.id.rightButton);
		leftButton = (Button) findViewById(R.id.leftButton);
		
		
	}
	
	
	@Override
	protected void onStart() {
		Log.v(Config.TAG_USER, "Starting TaskActivity");
		super.onStart();
	  
		//Make sure the buttons are not clickable and the accelerometer is unregistered before a task begins
		disableTaskUI();
		
    	//Clear the queryText view
    	queryText.setText("");
    	
    	//Set the audio query flag to it's default value
    	isAudioQuery = false;
    	
    	//Reset the score
    	resetScore();
	    
		//Wait for a set amount of time before beginning the queries
		rHandler.postDelayed(this, Config.DELAY_BEFORE_START);
	}
	
	
	@Override
	protected void onResume() {
		Log.v(Config.TAG_USER, "Resuming TaskActivity");
		super.onResume();
		//TODO If a task is being performed, register the accelerometer
	}
	
	
	@Override
	protected void onPause() {
		Log.v(Config.TAG_USER, "Pausing TaskActivity");
		super.onPause();
		unregisterAccelerometer(); // Unregister the accelerometer so that it does not drain battery life
	}
	
	
	/** Run when the left button has been clicked or the device has been tilted to the left */
	public void leftClick(View view){
		disableTaskUI(); //disallow further button clicks
		checkAnswer(Config.Side.LEFT);
	}

	
	/** Run when the right button has been clicked or the device has been tilted to the right */
	public void rightClick(View view){
		disableTaskUI(); //disallow further button clicks
		checkAnswer(Config.Side.RIGHT);
	}
	
	
	/** Used every time a new query is given to the user. Activates components necessary to respond as well as formulates the query */
	public void startTask(){
		//Set the buttons as clickable and register the sensor to read
		enableTaskUI();
		
		//Post the query to the user
		queryUser();
		
		//Post the Handler that will stop accepting an answer and mark it as incorrect
		rHandler.postDelayed(stopTask, Config.TIME_TO_RESPOND);
	}
	
	
	/** Formulate a query to be placed in the queryText view */
	public void queryUser(){

		queryText.setTextColor(Color.BLUE);
		double rnum = Math.random();
		if (rnum < 0.25){
			queryText.setText("LEFT");
			answer = Config.Side.LEFT;
		}
		else if (rnum < 0.5){
			queryText.setText("RIGHT");
			answer = Config.Side.RIGHT;
		}
		else if (rnum < 0.75){
			queryText.setText("");
			soundPool.play(leftSound, 1.0f, 1.0f, 1, 0, Config.SOUND_RATE);
			answer = Config.Side.LEFT;
			isAudioQuery = true;
		}
		else if (rnum < 1){
			queryText.setText("");
			soundPool.play(rightSound, 1.0f, 1.0f, 1, 0, Config.SOUND_RATE);
			answer = Config.Side.RIGHT;
			isAudioQuery = true;
		}
	}
	
	
	public void checkAnswer(Config.Side s) {
		clearQueue(); //Remove the StopTask handler from the queue 

		if (s == answer)
			addScore(Config.Score.CORRECT);
		else
			addScore(Config.Score.INCORRECT);
	}
	
	
	/** Adds either correct or incorrect points to the total score */
	public void addScore(Config.Score s){
		
		//If it is an audio query, set the text to show the query text
		if(isAudioQuery){
			if (answer == Side.LEFT)
				queryText.setText("LEFT");
			else if (answer == Side.RIGHT)
				queryText.setText("RIGHT");
		}
		
		switch (s){
		case CORRECT:
			++correct; queryText.setTextColor(Color.GREEN); break;
		case INCORRECT:
			++incorrect; queryText.setTextColor(Color.RED); break;
		default:
			Log.e("RunTask", "Unknown score value");
			TaskActivity.this.finish();
		}
		
		++total;
		
		if (total >= Config.NUM_OF_TRIALS){
			//Show the results
			Intent intent = new Intent(this, ResultsActivity.class);
			intent.putExtra(Config.EXTRA_SCORE, new int[] {correct, incorrect, total});
			startActivity(intent);
		}
		else{
			//Wait for a set amount of time before running another query
			rHandler.postDelayed(this, Config.DELAY_BEFORE_REPEAT);
		}

	}
	
	
	/** Resets the score values to 0 */
	private void resetScore(){
		correct = 0;
		incorrect = 0;
		total = 0;
	}
	
	
	/** Removes the delayed handler from the queue*/
	public void clearQueue(){
		rHandler.removeCallbacks(stopTask);
	}
	
	
	/** Enables the task UI to provide a response to a query */
	public void enableTaskUI(){
		//Make the buttons clickable and register the accelerometer
		rightButton.setClickable(true); leftButton.setClickable(true);
		registerAccelerometer();
	}
	
	
	/** Disables the task UI so that queries may not be answered */
	public void disableTaskUI(){
		//Make the buttons not clickable and unregister the accelerometer
		rightButton.setClickable(false); leftButton.setClickable(false);
		unregisterAccelerometer();
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// Do Nothing
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		//float[] values = event.values;
		//Log.v("Values", "sValues: " + values[0] + "  " + values[1] + "  " + values[2]);
		float y = event.values[1];
		
		if (y < -Config.SENSOR_TOLERANCE){
			leftClick(leftButton);
		}
		else if (y > Config.SENSOR_TOLERANCE){
			rightClick(rightButton);
		}
	}
	
	
	/** Specific function used to Register the accelerometer sensor. */
	private boolean registerAccelerometer(){
		if (sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)){
			Log.v(Config.TAG_USER, "Accelerometer Registered");
			return true;
		}
		else{
			Log.w(Config.TAG_USER, "Could not Register Accelerometer");
			return false;
		}
	}
	
	
	/** Specific function used to Unregister the accelerometer sensor. */
	private void unregisterAccelerometer(){
		sensorManager.unregisterListener(this, accelerometer);
		Log.v(Config.TAG_USER, "Accelerometer Unregistered");
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}


	@Override
	public void run() {
		startTask();
	}

}
