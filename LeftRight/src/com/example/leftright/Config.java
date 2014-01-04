package com.example.leftright;

public class Config {
	
	/** A tag for Logcat to define a log as written explicitly in the non-generated files */
	public final static String TAG_USER = "User";
	
	public static final String EXTRA_SCORE = "com.example.leftright.MainActivity.SCORE";
	
	/** time the user has to respond in ms */
	public static final int TIME_TO_RESPOND = 1000;
	
	public static final int NUM_OF_TRIALS = 10;
	/** Time before the first query is started and the trials begin */
	public static final int DELAY_BEFORE_START = 100;
	/** Time before a new query is posted (after one has been answered) */
	public static final int DELAY_BEFORE_REPEAT = 500;
	
	/** Threashold for counting a tilt as a hit */
	public static final float SENSOR_TOLERANCE = 4.0f;
	
	/** Speed at which audio will be played */
	public static final float SOUND_RATE = 1.2f;
	
	/** Enumeration for right vs left side */
	public enum Side{
		LEFT, RIGHT
	}
	
	/** Enumeration for whether the score is for correct or incorrect */
	public enum Score{
		CORRECT, INCORRECT
	}
	
}
