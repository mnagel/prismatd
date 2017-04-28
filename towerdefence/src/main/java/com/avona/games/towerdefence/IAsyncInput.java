package com.avona.games.towerdefence;

public interface IAsyncInput {
	interface MyRunnable {
		void run(int selectedOption);
	}

	void runnableChooser(String title, String[] options, MyRunnable callback);
}
