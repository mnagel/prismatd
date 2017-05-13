package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.input.IAsyncInput;

import javax.swing.*;
import java.util.Arrays;

public class AwtIAsyncInput implements IAsyncInput {
	private static int userSelectsAString(String title, String message, String[] strings) {
		String s = (String) JOptionPane.showInputDialog(
				null,
				message,
				title,
				JOptionPane.QUESTION_MESSAGE,
				null,
				strings,
				strings[0]
		);
		if (s == null) {
			return -1;
		}

		// Returns -1 if the list does not contain the element
		return Arrays.asList(strings).indexOf(s);
	}

	@Override
	public void chooseRunnable(String title, String[] options, boolean isCancelAllowed, MyRunnable callback) {
		int index;
		do {
			index = userSelectsAString(title, title, options);
		}
		while (!isCancelAllowed && index == -1);

		if (index != -1) {
			callback.run(index);
		}
	}
}
