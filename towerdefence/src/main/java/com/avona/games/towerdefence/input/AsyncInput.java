package com.avona.games.towerdefence.input;

public class AsyncInput {
	private static IAsyncInput instance;

	public static void setInstance(IAsyncInput iAsyncInput) {
		instance = iAsyncInput;
	}

	static void chooseRunnable(String title, String[] options, boolean isCancelAllowed, IAsyncInput.MyRunnable callback) {
		instance.chooseRunnable(title, options, isCancelAllowed, callback);
	}
}
