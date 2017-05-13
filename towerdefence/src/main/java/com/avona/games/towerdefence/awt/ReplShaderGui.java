package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.gfx.ShaderSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReplShaderGui extends JPanel {
	final String cmd = "go";

	public ReplShaderGui() {
		setLayout(new BorderLayout());

		ShaderSource shaderSource = new AwtShader(null, null).getShaderSource("default.frag");
		final JTextArea textArea = new JTextArea(shaderSource.toString());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JButton b1 = new JButton("Activate Shader");
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.LEADING);
		b1.setActionCommand(cmd);
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ShaderSource vertShader = new AwtShader(null, null).getShaderSource("default.vert");
				ShaderSource fragShader = new ShaderSource(textArea.getText());

				AwtReplShader.getInstance().setPrograms(vertShader, fragShader);
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