package com.example.leftright;

import com.example.leftright.RunTask.Score;

public class StopTask implements Runnable {

	private RunTask runTask;
	public StopTask(RunTask runTask) {
		this.runTask = runTask;
	}

	@Override
	public void run() {
//		runTask.clearQuery();
		runTask.addScore(Score.OVERTIME);
	}

}
