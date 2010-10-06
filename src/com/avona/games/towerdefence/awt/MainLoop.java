package com.avona.games.towerdefence.awt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.JOptionPane;

import com.avona.games.towerdefence.Debug;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

public class MainLoop extends PortableMainLoop implements GLEventListener {
	private static final String SAVEGAME = "savegame";

	private static final long serialVersionUID = 1L;

	final private int EXPECTED_FPS = 30;

	public InputMangler input;
	private Animator animator;

	@Override
	public void exit() {
		animator.stop();
		System.exit(0);
	}

	public MainLoop(String[] args) {
		super();
		// TODO use proper option parser

		if (args.length == 0) {
			game = new Game(eventListener);
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

	@Override
	protected void initWithGame() {
		super.initWithGame();

		ResourceResolverRegistry.setInstance(new FileResourceResolver("gfx"));

		GraphicsEngine graphicsEngine = new GraphicsEngine(game, mouse,
				layerHerder, this);
		ge = graphicsEngine;
		graphicsEngine.canvas.addGLEventListener(this);

		setupInputActors();
		input = new InputMangler(this, inputActor);

		animator = new FPSAnimator(graphicsEngine.canvas, EXPECTED_FPS);
		animator.setRunAsFastAsPossible(false);
		animator.start();

		input.setupListeners(graphicsEngine);
	}

	private void loadGame(final String filename) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		final InputStream is = new FileInputStream(filename);
		final ObjectInputStream ois = new ObjectInputStream(is);

		// Otherwise the parent class would've set up game...
		game = (Game) ois.readObject();
	}

	public static void main(String[] args) {
		String[] arg2 = args;

		if (args.length > 0 && args[0].indexOf("--leveleditor") == 0) {
			Util.log("enabling map editor");
			Debug.mapEditor = true;

			arg2 = new String[args.length - 1];
			System.arraycopy(args, 1, arg2, 0, arg2.length);
		}

		new MainLoop(arg2);
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		performLoop();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// Unused.
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// Unused.
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
