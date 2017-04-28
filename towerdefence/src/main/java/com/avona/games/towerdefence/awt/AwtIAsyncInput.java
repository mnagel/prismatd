package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.IAsyncInput;

import javax.swing.*;
import java.util.Arrays;

public class AwtIAsyncInput implements IAsyncInput {
	@Override
	public void runnableChooser(String title, String[] options, MyRunnable callback) {
		int index = userSelectsAString(title, title, options);
		callback.run(index);
	}

	public static int userSelectsAString(String title, String message, String[] strings) {
		String s = (String) JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, strings, strings[0]);
		if (s == null) {
			return -1;
		}

		// Returns -1 if the list does not contain the element
		return Arrays.asList(strings).indexOf(s);
	}
}
