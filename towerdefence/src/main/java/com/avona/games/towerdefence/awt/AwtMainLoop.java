package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.engine.PortableMainLoop;
import com.avona.games.towerdefence.gfx.DisplayEventListener;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.input.AsyncInput;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.avona.games.towerdefence.util.Util;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.io.*;

public class AwtMainLoop extends PortableMainLoop implements GLEventListener {
	private static final String SAVEGAME = "savegame";

	private static final long serialVersionUID = 1L;

	final static private int EXPECTED_FPS = 60;

	private AnimatorBase animator;

	private AwtMainLoop(String[] args) {
		super();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			Util.log("cannot set native look and feel");
		}

		AsyncInput.setInstance(new AwtIAsyncInput());

		if (args.length == 0) {
			game = new Game(eventDistributor);
		} else {
			try {
				loadGame(args[0]);
			} catch (Exception e) {
				Util.log("loading failed...\nStacktrace:");
				Util.log(Util.exception2String(e));
				JOptionPane.showMessageDialog(null, "loading failed, see terminal for details");
				System.exit(1);
			}
		}

		initWithGame();
	}

	public static void main(String[] args) {
		new AwtMainLoop(args);
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

		final AwtDisplay display = new AwtDisplay(displayEventListener);
		ge = new PortableGraphicsEngine(display, game, mouse, layerHerder, this);
		displayEventListener.add(new DisplayEventListener() {
			@Override
			public void onReshapeScreen() {
				layerHerder.onReshapeScreen(display.getSize());
			}

			@Override
			public void onNewScreenContext() {
				ge.freeMissionVertices();
			}
		});
		display.canvas.addGLEventListener(this);

		InputMangler input = new InputMangler(this, rootInputActor);

		animator = new FPSAnimator(display.canvas, EXPECTED_FPS);
		//animator.setRunAsFastAsPossible(false);
		animator.start();

		input.setupListeners(display);
		this.display = display;
	}

	private void loadGame(final String filename) throws IOException, ClassNotFoundException {
		final InputStream is = new FileInputStream(filename);
		final ObjectInputStream ois = new ObjectInputStream(is);

		// Otherwise the parent class would've set up game...
		game = (Game) ois.readObject();
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		performIteration();
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
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
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
