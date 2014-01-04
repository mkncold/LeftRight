package com.example.leftright;

import com.example.leftright.Config;

public class StopTask implements Runnable {

	private TaskActivity taskActivity;
	public StopTask(TaskActivity taskActivity) {
		this.taskActivity = taskActivity;
	}

	@Override
	public void run() {
		taskActivity.disableTaskUI(); //Disable the UI so that clicks may not occur
		taskActivity.addScore(Config.Score.INCORRECT); // Add an incorrect score because the user did not answer in time
	}

}
