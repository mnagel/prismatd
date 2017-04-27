package com.avona.games.towerdefence.awt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReplShaderGui extends JPanel {
	final String cmd = "go";

	public ReplShaderGui() {
		setLayout(new BorderLayout());

		String s = new AwtShader(null, null).getShaderProgram("default.frag");
		final JTextArea textArea = new JTextArea(s);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JButton b1 = new JButton("Activate Shader");
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.LEADING);
		b1.setActionCommand(cmd);
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String vertShader = new AwtShader(null, null).getShaderProgram("default.vert");
				String fracShader = textArea.getText();

				AwtReplShader.getInstance().setPrograms(vertShader, fracShader);
			}
		});

		add(textArea, BorderLayout.CENTER);
		add(b1, BorderLayout.SOUTH);
	}

	public static void main2() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Edit Shader");
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new ReplShaderGui());
				frame.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		main2();
	}
}