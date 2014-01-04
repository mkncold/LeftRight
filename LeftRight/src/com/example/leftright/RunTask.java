//package com.example.leftright;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class RunTask implements Runnable{
//
//	private static TextView queryText;
//	
//	private StopTask stopTask = new StopTask(this);
//	
//	private static Handler rHandler = new Handler();
//	
//	private int total = 0;
//	private int correct = 0;
//	private int incorrect = 0;
//	private int overtime = 0;
//	
//	private Side answer;
//	
//	final int TASK_TIME = 1000; // time the user has to respond in ms
//	final int NUM_OF_TRIALS = 10;
//	
//	private MainActivity mainActivity;
//	
//	private static Button rightButton;
//	private static Button leftButton;
//	
//	public RunTask(MainActivity mainActivity, TextView queryText) {
//		this.queryText = queryText;
//		this.mainActivity = mainActivity;
//		rightButton = (Button) mainActivity.findViewById(R.id.rightButton);
//		leftButton = (Button) mainActivity.findViewById(R.id.leftButton);
//	}
//
//	public void startTask(){
//		rightButton.setClickable(true); leftButton.setClickable(true);
//		mainActivity.registerSensor();
//		queryUser();
//		rHandler.postDelayed(stopTask, TASK_TIME);
//	}
//	
//	
//	public enum Side{
//		LEFT, RIGHT
//	}
//	
//	public enum Score{
//		CORRECT, INCORRECT, OVERTIME
//	}
//	
//
//	public void queryUser(){
//
//		queryText.setTextColor(Color.BLUE);
//		double rnum = Math.random();
//		//queryText.setText("RIGHT");
//		if (rnum < 0.5){
//			queryText.setText("LEFT");
//			answer = Side.LEFT;
//		}
//		else if (rnum >= 0.5){
//			queryText.setText("RIGHT");
//			answer = Side.RIGHT;
//		}
//	}
//	
////	public void clearQuery(){
////		mainActivity.runOnUiThread(new Runnable() {
////			@Override
////	        public void run() {
////				queryText.setText("");
////			}
////		});
////	}
//	
//	
//	/*Removes the delayed handler from the queue*/
//	public void clearQueue(){
//		rHandler.removeCallbacks(stopTask);
//	}
//
//	public void addScore(Score s){
//		switch (s){
//		case CORRECT:
//			++correct; queryText.setTextColor(Color.GREEN); break;
//		case INCORRECT:
//			++incorrect; queryText.setTextColor(Color.RED); break;
//		case OVERTIME:
//			++overtime; queryText.setTextColor(Color.RED); break;
//		default:
//			Log.e("RunTask", "Unknown score value");
//			mainActivity.finish();
//		}
//		
//		++total;
//		
//		if (total > NUM_OF_TRIALS){
//			Intent intent = new Intent(mainActivity, ResultsActivity.class);
//			intent.putExtra(MainActivity.EXTRA_SCORE, new int[] {correct, incorrect, total});
//			mainActivity.startActivity(intent);
//		}
//		else{
//			rHandler.postDelayed(this, 500);
//		}
//
//	}
//	
//
//	public int getTotal() {
//		return total;
//	}
//
//	public int getCorrect() {
//		return correct;
//	}
//
//	public int getIncorrect() {
//		return incorrect;
//	}
//
//	public int getOvertime() {
//		return overtime;
//	}
//
//	public void checkAnswer(Side a) {
//		clearQueue();
//
//		if (a == answer)
//			addScore(Score.CORRECT);
//		else
//			addScore(Score.INCORRECT);
//	}
//
//	@Override
//	public void run() {
//		startTask();
//	}
//}
