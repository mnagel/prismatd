package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.Debug;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

public class MainLoop extends PortableMainLoop implements GLEventListener {
	private static final String SAVEGAME = "savegame";

	private static final long serialVersionUID = 1L;

	final private int EXPECTED_FPS = 60;

	public InputMangler input;
	private AnimatorBase animator;

	public MainLoop(String[] args, int startMission) {
		super();
		// TODO use proper option parser

		if (args.length == 0) {
			game = new Game(eventListener, startMission);
		} else {
			try {
				loadGame(args[0]);
			} catch (Exception e) {
				Util.log("loading failed...\nStacktrace:");
				Util.log(Util.exception2String(e));
				JOptionPane.showMessageDialog(null,
						"loading failed, see terminal for details");
				System.exit(1);
			}
		}

		initWithGame();
	}

	public static int userSelectsAString(String title, String message, String[] strings) {
		int x = JOptionPane.showOptionDialog(
				null,
				message,
				title,
				0,
				JOptionPane.PLAIN_MESSAGE,
				null,
				strings,
				null
		);

		if (x == JOptionPane.CLOSED_OPTION) x = -1;
		return x;
	}

	public static void main(String[] args) {
		String[] arg2 = args;

		if (args.length > 0 && args[0].indexOf("--missioneditor") == 0) {
			Util.log("enabling map editor");
			Debug.mapEditor = true;

			arg2 = new String[args.length - 1];
			System.arraycopy(args, 1, arg2, 0, arg2.length);
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			Util.log("cannot set native look and feel");
		}

		String[] missions = MissionList.getAvailableMissionNames();
		int startMission = userSelectsAString("Load Mission", "Please select a Mission to load:", missions);

		new MainLoop(arg2, startMission);
	}

	@Override
	public void exit() {
		animator.stop();
		System.exit(0);
	}

	@Override
	protected void initWithGame() {
		super.initWithGame();

		ResourceResolverRegistry.setInstance(new FileResourceResolver("gfx"));

		AwtDisplay display = new AwtDisplay(displayEventListener);
		ge = new PortableGraphicsEngine(display, game, mouse, layerHerder, this);
		displayEventListener.add(ge);
		display.canvas.addGLEventListener(this);

		setupInputActors();
		input = new InputMangler(this, inputActor);

		animator = new FPSAnimator(display.canvas, EXPECTED_FPS);
		//animator.setRunAsFastAsPossible(false);
		animator.start();

		input.setupListeners(display);
		this.display = display;
	}

	private void loadGame(final String filename) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		final InputStream is = new FileInputStream(filename);
		final ObjectInputStream ois = new ObjectInputStream(is);

		// Otherwise the parent class would've set up game...
		game = (Game) ois.readObject();
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		performLoop();
	}

	/*
	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// Unused.
	}
	*/

	@Override
	public void init(GLAutoDrawable arg0) {
		// Unused.
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		throw new RuntimeException("added for jogl2. unneeded(?).");
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
						int arg4) {
		// Unused.
	}

	@Override
	public void serialize() {
		try {
			final FileOutputStream fos = new FileOutputStream(SAVEGAME);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game);
			Util.log("game was saved to " + SAVEGAME);
		} catch (IOException e) {
			Util.log("saving failed...\nStacktrace:");
			Util.log(Util.exception2String(e));
		}
	}
}
