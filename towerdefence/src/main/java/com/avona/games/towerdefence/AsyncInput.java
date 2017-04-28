package com.avona.games.towerdefence;

public class AsyncInput {
	private static IAsyncInput instance;
	public static void setInstance(IAsyncInput iAsyncInput) {
		instance = iAsyncInput;
	}

	public static void runnableChooser(String title, String[] options, IAsyncInput.MyRunnable callback) {
		instance.runnableChooser(title, options, callback);
	}
}
