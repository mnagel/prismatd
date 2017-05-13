package com.avona.games.towerdefence.input;

public interface IAsyncInput {
	void chooseRunnable(String title, String[] options, boolean isCancelAllowed, MyRunnable callback);

	interface MyRunnable {
		void run(int selectedOption);
	}
}
