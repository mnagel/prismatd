package com.avona.games.towerdefence.input;

import java.util.LinkedHashMap;

public class AsyncInput {
	private static IAsyncInput instance;

	public static void setInstance(IAsyncInput iAsyncInput) {
		instance = iAsyncInput;
	}

	static void chooseRunnable(String title, String[] options, boolean isCancelAllowed, IAsyncInput.MyRunnable callback) {
		instance.chooseRunnable(title, options, isCancelAllowed, callback);
	}

	// use LinkedHashMap because it is the most reasonable implementation of List of Tuples that plain old Java offers
	static void chooseNamedRunnable(String title, boolean isCancelAllowed, final LinkedHashMap<String, Runnable> namedRunnables) {
		chooseRunnable(title, namedRunnables.keySet().toArray(new String[]{}), isCancelAllowed, new IAsyncInput.MyRunnable() {
			@Override
			public void run(int selectedOption) {
				if (selectedOption != -1) {
					namedRunnables.values().toArray(new Runnable[]{})[selectedOption].run();
				}
			}
		});
	}
}
