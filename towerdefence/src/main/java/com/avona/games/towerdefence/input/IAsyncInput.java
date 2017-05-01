package com.avona.games.towerdefence.input;

public interface IAsyncInput {
	void runnableChooser(String title, String[] options, MyRunnable callback);

	interface MyRunnable {
		void run(int selectedOption);
	}
}
